/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.wip;

import com.google.common.base.Throwables;
import com.google.gson.JsonObject;
import com.mojang.util.UUIDTypeAdapter;
import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.presistanceDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
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

public class SessionLogIn extends basicModule {
    public static Session lastsession;
    static LinkedList<String> histroy = new LinkedList<>();
    static long lastLoginClick;

    public SessionLogIn() {
        super("SessionLogIn", "login using tokens", moduleManager.Category.WIP, new presistanceDecorator());
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
        final Session t = new Session(name, UUIDTypeAdapter.fromUUID(UUID.fromString(uuid)), token, "mojang");

        lastLoginClick = System.currentTimeMillis();

        return t;
    }

    public static Session generateValidSession(String nick, String uuid, String token) throws NoSuchFieldException, IllegalAccessException, IOException {
        // check rate limit
        if (System.currentTimeMillis() - lastLoginClick <= 1000)
            throw new RuntimeException("Rate limit, wait a second");

        // validate credentials

        // validate uuid
        UUID u = UUID.fromString(uuid);
        // validate nick
        if (!APIHandler.getUUID(nick).equalsIgnoreCase(uuid.replaceAll("-", ""))) {
            throw new IllegalArgumentException("Uuid does not belong to nickname");
        }
        // check token valididty
        if (!APIHandler.tokentouuid(token).equals(uuid)) {
            throw new IllegalArgumentException("Uuid does not belong to token");
        }

        // create session
        final Session t = new Session(nick, UUIDTypeAdapter.fromUUID(u), token, "mojang");

        lastLoginClick = System.currentTimeMillis();

        return t;
    }

    @SubscribeEvent
    public void DaDSAdisint(GuiScreenEvent.InitGuiEvent.Post event) {
        if (!(event.gui instanceof GuiMainMenu)) return;

        event.buttonList.add(new GuiButton(1000,
                mc.currentScreen.width - (mc.fontRendererObj.getStringWidth("kokomod fake login") + 5),
                mc.currentScreen.height - 14 - mc.fontRendererObj.FONT_HEIGHT,
                mc.fontRendererObj.getStringWidth("kokomod fake login") + 5,
                12,
                "kokomod fake login"));

    }

    @SubscribeEvent
    public void GuiOpenEvent(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if (mc.currentScreen instanceof GuiMainMenu) {
            if (event.button.id == 1000) {
                mc.displayGuiScreen(new SessionLogInGui(event.gui));
            }
        }
    }

    static final class SessionLogInGui extends GuiScreen {

        private final GuiScreen parent;

        private GuiTextField uuid;
        private GuiTextField token;
        private GuiTextField nickname;

        private String rendertext = " ";

        public SessionLogInGui(GuiScreen parent) {
            this.parent = parent;
        }

        @Override
        public void initGui() {
            this.buttonList.add(new GuiButton(106, this.width / 2 - 155, this.height / 6 + 72 - 6, this.fontRendererObj.getStringWidth("login") + 5, this.fontRendererObj.FONT_HEIGHT + 2, "login"));
            this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, I18n.format("gui.done", new Object[0])));
            this.buttonList.add(new GuiButton(201, this.width / 2 - 155 - (this.fontRendererObj.getStringWidth("login to last accout") / 2), this.height / 6 + 72 + 7, this.fontRendererObj.getStringWidth("login to last accout") + 8, this.fontRendererObj.FONT_HEIGHT + 2, "login to last accout"));

            this.uuid = new GuiTextField(1, this.fontRendererObj, this.width / 2 - 68, this.height / 2 - 30, 137, 20);
            uuid.setMaxStringLength(36);
            uuid.setCanLoseFocus(true);
            uuid.setText("uuid (optional)");
            uuid.setEnabled(false);

            this.token = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 68, this.height / 2 - 50, 137, 20);
            token.setMaxStringLength(500);
            token.setCanLoseFocus(true);
            token.setText("token");
            token.setEnabled(false);

            this.nickname = new GuiTextField(3, this.fontRendererObj, this.width / 2 - 68, this.height / 2 - 70, 137, 20);
            nickname.setMaxStringLength(16);
            nickname.setCanLoseFocus(true);
            nickname.setText("nickname (optional)");
            nickname.setEnabled(false);


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

            if (button.id == 106) {
                String nicknames = this.nickname.getText(), tokens = this.token.getText(), uuids = this.uuid.getText();
                if (nicknames != null && tokens != null && uuids != null) {
                    try {
                        final Session a = generateValidSession(nicknames, uuids, tokens);
                        lastsession = mc.getSession();
                        // set the session
                        setSesion(a);
                        rendertext = "Succes: current nick " + mc.getSession().getUsername();
                        addHistory(mc.getSession().getUsername());
                    } catch (Exception e) {
                        rendertext = "Fail: " + Throwables.getRootCause(e).getMessage();
                        e.printStackTrace();
                    }
                }
                if (nicknames != null && tokens != null && uuids != null) {
                    if (nicknames.contains("(") && !tokens.contains(" ") && uuids.contains("(")) {
                        try {
                            final Session a = generateValidSession(tokens);
                            lastsession = mc.getSession();
                            // set the session
                            setSesion(a);
                            rendertext = "Succes: current nick " + mc.getSession().getUsername();
                            addHistory(mc.getSession().getUsername());
                        } catch (Exception e) {
                            rendertext = "Fail: " + Throwables.getRootCause(e).getMessage();
                            e.printStackTrace();
                        }
                    }
                }

            }
            super.actionPerformed(button);
        }

        @Override
        protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

            this.uuid.mouseClicked(mouseX, mouseY, mouseButton);
            this.nickname.mouseClicked(mouseX, mouseY, mouseButton);
            this.token.mouseClicked(mouseX, mouseY, mouseButton);

            uuid.setEnabled(uuid.isFocused());
            nickname.setEnabled(nickname.isFocused());
            token.setEnabled(token.isFocused());


            super.mouseClicked(mouseX, mouseY, mouseButton);
        }

        @Override
        protected void keyTyped(char typedChar, int keyCode) throws IOException {

            this.token.textboxKeyTyped(typedChar, keyCode);
            this.uuid.textboxKeyTyped(typedChar, keyCode);
            this.nickname.textboxKeyTyped(typedChar, keyCode);

            uuid.setEnabled(uuid.isFocused());
            nickname.setEnabled(nickname.isFocused());
            token.setEnabled(token.isFocused());

            super.keyTyped(typedChar, keyCode);
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            this.drawDefaultBackground();

            this.drawCenteredString(this.fontRendererObj, "History", this.width / 2 + (this.width / 4) + fontRendererObj.getStringWidth("History"), 16, 16777215);
            int offset = 4 + fontRendererObj.FONT_HEIGHT;
            for (String s : histroy) {
                this.drawCenteredString(this.fontRendererObj, s, this.width / 2 + (this.width / 4) + fontRendererObj.getStringWidth("History"), 16 + offset, 16777215);
                offset += this.fontRendererObj.FONT_HEIGHT + 1;
            }

            this.uuid.drawTextBox();
            this.token.drawTextBox();
            this.nickname.drawTextBox();
            this.drawCenteredString(this.fontRendererObj, "fake log in to the moon", this.width / 2, 15, 16777215);
            this.drawCenteredString(this.fontRendererObj, rendertext, this.width / 2, (this.height / 2) + this.fontRendererObj.FONT_HEIGHT - 70 - 20, Objects.equals(rendertext, " ") ? 16777215 : rendertext.startsWith("S") ? 0x32a852 : 0xff8c8c);
            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        public boolean isWithinHeader(int x, int y, int myx, int myy) {
            return x >= myx && x <= myx + this.width && y >= myy && y <= myy + this.height;
        }

    }

}
