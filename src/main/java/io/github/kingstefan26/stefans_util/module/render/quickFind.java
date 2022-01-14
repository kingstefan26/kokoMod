/*
 * Copyright (c) 2022. All copyright reserved
 */

package io.github.kingstefan26.stefans_util.module.render;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleRegistery;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.presistanceDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class quickFind extends basicModule {
    public quickFind() {
        super("quickFind", "click the keybind and start typing to enable modules", moduleManager.Category.RENDER, new keyBindDecorator("quickFind"), new presistanceDecorator());
    }


    @Override
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.isCreated()) {
            if (Keyboard.getEventKeyState()) {
                int keyCode = Keyboard.getEventKey();
                if (keyCode <= 0)
                    return;

                if (keyCode == Keyboard.KEY_H) {
                    if (mc.currentScreen == null) {
                        mc.displayGuiScreen(new quickFindGui());
                    }
                }

            }
        }

        super.onKeyInput(event);
    }


    static class quickFindGui extends GuiScreen {
//        String text;

        ArrayList<String> searchHits = new ArrayList<>();
        // 0 = the search bar; 1..6 = the hits
        int selectCurssorPostiton;
        private GuiTextField text;

        public void initGui() {
            this.text = new GuiTextField(1, this.fontRendererObj, this.width / 2 - 68, this.height / 2 - 46, 137, 20);
            text.setMaxStringLength(30);
            this.text.setFocused(true);
        }

        @Override
        protected void keyTyped(char typedChar, int keyCode) throws IOException {
            super.keyTyped(typedChar, keyCode);
            this.text.textboxKeyTyped(typedChar, keyCode);


            if (keyCode == Keyboard.KEY_DOWN) {
                System.out.println("keydown");
                if (selectCurssorPostiton < 5) {
                    System.out.println("one less");
                    selectCurssorPostiton++;
                }
                System.out.println(selectCurssorPostiton);
            } else if (keyCode == Keyboard.KEY_UP) {
                System.out.println("keyup");
                if (selectCurssorPostiton > 0) {
                    System.out.println("one more");
                    selectCurssorPostiton--;
                }
                System.out.println(selectCurssorPostiton);
            } else if (keyCode == Keyboard.KEY_RETURN) {
                int a = 0;
                for (String hit : searchHits) {
                    a++;
                    if (a == selectCurssorPostiton) {
                        moduleRegistery.getModuleRegistery().getModuleByName(hit).toggle();
                    }
                }
            }

            if (text.getText().length() == 0) {
                searchHits.clear();
            } else {

                TreeMap<Integer, String> hits = new TreeMap<>();

                for (basicModule b : moduleRegistery.getModuleRegistery().loadedModules) {
                    hits.put(FuzzySearch.weightedRatio(text.getText(), b.getName()), b.getName());
                }

                searchHits = Lists.newArrayList
                        (Iterables.limit(hits.descendingMap().values(), 5));
            }

        }

        public void updateScreen() {
            super.updateScreen();
            this.text.updateCursorCounter();
        }

        public void drawScreen(int par1, int par2, float par3) {
//            this.drawDefaultBackground();

            ScaledResolution sr = new ScaledResolution(mc);
            this.text.drawTextBox();

            int y = 200;
            int a = 0;
            for (String hit : searchHits) {
                a++;
                drawRect((sr.getScaledWidth() / 2) - 150, y, (sr.getScaledWidth() / 2) + 150, y + fontRendererObj.FONT_HEIGHT, a == selectCurssorPostiton ? 0xffffff : 0x80000000);
                mc.fontRendererObj.drawString(hit, (sr.getScaledWidth() / 2) - (mc.fontRendererObj.getStringWidth(hit) / 2), y, moduleRegistery.getModuleRegistery().getModuleByName(hit).isToggled() ? 0xffffff : 0x878787);
                // top corrner x y, bottom corner x y
                y += mc.fontRendererObj.FONT_HEIGHT;
            }

//            IntStream.range(0, 6).forEachOrdered(n -> {
//            });


            super.drawScreen(par1, par2, par3);
        }

        protected void mouseClicked(int x, int y, int btn) throws IOException {
            super.mouseClicked(x, y, btn);
            this.text.mouseClicked(x, y, btn);
        }


        @Override
        public boolean doesGuiPauseGame() {
            return false;
        }
    }


}
