/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.wip;

import com.google.common.base.Throwables;
import com.google.gson.JsonObject;
import com.mojang.util.UUIDTypeAdapter;
import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.presistanceDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.util.handelers.APIHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Session;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SessionLogIn extends BasicModule {
    static LinkedList<String> histroy = new LinkedList<>();
    static long lastLoginClick;
    private static Session lastsession;

    public SessionLogIn() {
        super("SessionLogIn", "login using tokens", ModuleManager.Category.WIP, new presistanceDecorator());
    }

    static void addHistory(String s) {
        histroy.push(s);
        if (histroy.size() == 11) {
            histroy.remove(10);
        }
    }

    public static void setSesion(Session session) throws NoSuchFieldException, IllegalAccessException {
        Field nameField;
        nameField = Minecraft.getMinecraft().getClass().getDeclaredField("session");
        nameField.setAccessible(true);
        nameField.set(Minecraft.getMinecraft(), session);
    }

    public static Session generateValidSession(String token) throws IOException {
        // check rate limit
        if (System.currentTimeMillis() - lastLoginClick <= 1000)
            throw new RuntimeException("Rate limit, wait a second");

        final JsonObject profile = APIHandler.tokentoprofile(token);

        final String uuid = profile.get("id").getAsString().replaceFirst(
                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                "$1-$2-$3-$4-$5"
        );

        final String name = profile.get("name").getAsString();


        // create session
        final Session s = new Session(name, UUIDTypeAdapter.fromUUID(UUID.fromString(uuid)), token, "mojang");

        lastLoginClick = System.currentTimeMillis();

        return s;
    }

    @SubscribeEvent
    public void OnMainGuiOpen(GuiScreenEvent.InitGuiEvent.Post event) {
        if (!(event.gui instanceof GuiMainMenu)) return;

        String buttonText = "kokomod fake login";
        int buttonTextWidth = mc.fontRendererObj.getStringWidth(buttonText);

        event.buttonList.add(new GuiButton(1000,
                mc.currentScreen.width - (buttonTextWidth + 5),
                mc.currentScreen.height - 14 - mc.fontRendererObj.FONT_HEIGHT,
                buttonTextWidth + 5,
                12,
                buttonText));

    }

    @SubscribeEvent
    public void GuiOpenEvent(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if (mc.currentScreen instanceof GuiMainMenu && event.button.id == 1000) {
            mc.displayGuiScreen(new SessionLogInGui(event.gui));
        }
    }

    static final class SessionLogInGui extends GuiScreen {

        private final GuiScreen parent;

        private GuiTextField token;
        private GuiTextField offlinename;
        private String rendertext = " ";

        public SessionLogInGui(GuiScreen parent) {
            this.parent = parent;
        }

        @Override
        public void initGui() {
            this.buttonList.add(
                    new GuiButton(
                            106,
                            this.width / 2 - 155,
                            this.height / 6 + 72 - 6,
                            this.fontRendererObj.getStringWidth("login") + 5,
                            this.fontRendererObj.FONT_HEIGHT + 2,
                            "login"));
            this.buttonList.add(
                    new GuiButton(
                            107,
                            this.width / 2 - 155,
                            this.height / 6 + 72 - 6,
                            this.fontRendererObj.getStringWidth("loginOffline") + 5,
                            this.fontRendererObj.FONT_HEIGHT + 2,
                            "loginOffline"));
            this.buttonList.add(
                    new GuiButton(
                            200,
                            this.width / 2 - 100,
                            this.height / 6 + 168,
                            I18n.format("gui.done")));
            this.buttonList.add(
                    new GuiButton(
                            201,
                            this.width / 2 - 155 - (this.fontRendererObj.getStringWidth("login to last accout") / 2),
                            this.height / 6 + 72 + 7,
                            this.fontRendererObj.getStringWidth("login to last accout") + 8,
                            this.fontRendererObj.FONT_HEIGHT + 2,
                            "login to last accout"));


            this.token = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 68, this.height / 2 - 50, 137, 20);
            token.setMaxStringLength(500);
            token.setCanLoseFocus(true);
            token.setText("token");
            token.setEnabled(false);
            this.offlinename = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 68, this.height / 2 - 40, 137, 20);
            token.setMaxStringLength(500);
            token.setCanLoseFocus(true);
            token.setText("username");
            token.setEnabled(false);


            super.initGui();
        }

        @Override
        protected void actionPerformed(GuiButton button) throws IOException {
            if (button.id == 200) {
                this.mc.displayGuiScreen(this.parent);
            }
            if (button.id == 201 && lastsession != null) {
                Session s = mc.getSession();
                try {
                    setSesion(lastsession);
                    rendertext = "Succes: current nick " + mc.getSession().getUsername();
                    addHistory(mc.getSession().getUsername());
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    rendertext = "Fail: " + Throwables.getRootCause(e).getMessage();
                    e.printStackTrace();
                }
                lastsession = s;
            }

            // Called when user clicks on "login"
            if (button.id == 106) {
                handleLoginButton();
            }
            if (button.id == 107) {
                handleOfflineLoginBtn();
            }
            super.actionPerformed(button);
        }

        private void handleOfflineLoginBtn() {
            String name = this.offlinename.getText();

            if (name == null) {
                rendertext = "Fail: Please enter a name";
                return;
            }

            if (name.length() > 16 || name.length() < 4) {
                rendertext = "Fail: Enter a valid name";
            }

            try {
                String uuid = null;
                try {
                    uuid = APIHandler.getUUID(name);
                } catch (Exception ignored) {

                }

                uuid = uuid == null ? mc.getSession().getPlayerID() : "b876ec32e396476ba1158438d83c67d4";
                final Session sesz = new Session(name, uuid, mc.getSession().getToken(), "mojang");
                lastsession = mc.getSession();
                // set the session
                setSesion(sesz);
                rendertext = "Succes: current nick " + mc.getSession().getUsername();
                addHistory(mc.getSession().getUsername());
            } catch (Exception e) {
                rendertext = "Fail: " + Throwables.getRootCause(e).getMessage();
                e.printStackTrace();
            }


        }


        void handleLoginButton() {
            String tokenz = this.token.getText();

            if (tokenz == null) {
                rendertext = "Fail: Please enter a token";
                return;
            }

            // JWT regex
            final String regex = "(^[\\w-]*\\.[\\w-]*\\.[\\w-]*$)";

            final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
            final Matcher matcher = pattern.matcher(tokenz);

            String match = null;

            while (matcher.find()) {
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    match = matcher.group(i);
                }
            }

            if (match == null) {
                rendertext = "Fail: Please enter a Valid token";
                return;
            }

            try {
                final Session sesz = generateValidSession(match);
                lastsession = mc.getSession();
                // set the session
                setSesion(sesz);
                rendertext = "Succes: current nick " + mc.getSession().getUsername();
                addHistory(mc.getSession().getUsername());
            } catch (Exception e) {
                rendertext = "Fail: " + Throwables.getRootCause(e).getMessage();
                e.printStackTrace();
            }

        }

        @Override
        protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

            this.token.mouseClicked(mouseX, mouseY, mouseButton);
            this.offlinename.mouseClicked(mouseX, mouseY, mouseButton);

            token.setEnabled(token.isFocused());
            offlinename.setEnabled(offlinename.isFocused());


            super.mouseClicked(mouseX, mouseY, mouseButton);
        }

        @Override
        protected void keyTyped(char typedChar, int keyCode) throws IOException {

            this.token.textboxKeyTyped(typedChar, keyCode);
            this.offlinename.textboxKeyTyped(typedChar, keyCode);

            token.setEnabled(token.isFocused());
            offlinename.setCanLoseFocus(offlinename.isFocused());


            super.keyTyped(typedChar, keyCode);
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            this.drawDefaultBackground();

            this.drawCenteredString(this.fontRendererObj,
                    "History",
                    this.width / 2 + (this.width / 4) + fontRendererObj.getStringWidth("History"),
                    16,
                    0xFFFFFF);

            int offset = 4 + fontRendererObj.FONT_HEIGHT;
            for (String s : histroy) {
                this.drawCenteredString(this.fontRendererObj,
                        s,
                        this.width / 2 + (this.width / 4) + fontRendererObj.getStringWidth("History"),
                        16 + offset,
                        0xFFFFFF);
                offset += this.fontRendererObj.FONT_HEIGHT + 1;
            }

            this.token.drawTextBox();
            this.offlinename.drawTextBox();

            this.drawCenteredString(this.fontRendererObj,
                    "fake log in to the moon",
                    this.width / 2, 15,
                    0xFFFFFF);

            int textColor;
            if (Objects.equals(rendertext, " ")) {
                textColor = 0xFFFFFF;
            } else if (rendertext.startsWith("S")) {
                textColor = 0x32A852;
            } else {
                textColor = 0xFF8C8C;
            }

            this.drawCenteredString(this.fontRendererObj,
                    rendertext,
                    this.width / 2,
                    (this.height / 2) + this.fontRendererObj.FONT_HEIGHT - 70 - 20,
                    textColor);

            super.drawScreen(mouseX, mouseY, partialTicks);
        }

    }

}
