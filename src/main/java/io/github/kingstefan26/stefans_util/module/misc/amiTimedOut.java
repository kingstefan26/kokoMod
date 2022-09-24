package io.github.kingstefan26.stefans_util.module.misc;

import io.github.kingstefan26.stefans_util.core.module.ModuleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleframes.BasicModule;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.util.StefanutilEvents;
import io.github.kingstefan26.stefans_util.util.renderUtil.drawCenterString;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class amiTimedOut extends BasicModule {
    private long checkTimer;
    private boolean wasEnabled;


    public amiTimedOut() {
        super("am i Timed out?", "shows warnings based on your network status", ModuleManager.Category.MISC);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.ClientTickEvent e) {
        if (mc == null || mc.theWorld == null || mc.thePlayer == null) return;
        if (System.currentTimeMillis() - checkTimer > 2000) {
            mc.displayGuiScreen(new GuiScreen() {
                @Override
                public void initGui() {
                    this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - (this.height / 4) + 10, "couldn't care less"));
                    super.initGui();
                }

                @Override
				public void drawScreen(int mouseX, int mouseY, float partialTicks) {
					this.drawDefaultBackground();
					drawCenterString.drawCenterStringOnScreen(mc, "server stopped responding", "ff002f");
					super.drawScreen(mouseX, mouseY, partialTicks);
				}
    			@Override
				protected void actionPerformed(GuiButton button) {
					if (button.id == 0) {
						mc.thePlayer.closeScreen();
					}
				}
				@Override
				public boolean doesGuiPauseGame() {
					return false;
				}
            });
            wasEnabled = true;
        } else {
            if (wasEnabled) {
                mc.setIngameFocus();
                wasEnabled = false;
            }
        }
    }

    @SubscribeEvent
    public void onKeepAlivePacet(StefanutilEvents.receivedKeepAlivePacketEvent e) {
        checkTimer = System.currentTimeMillis();
    }


    @Override
    public void onEnable() {
        if (mc.isSingleplayer()) {
            chatService.queueClientChatMessage("you are in singleplayer nono", chatService.chatEnum.PREFIX);
            this.setToggled(false);
            return;
        }
        checkTimer = System.currentTimeMillis();
		super.onEnable();
	}
}