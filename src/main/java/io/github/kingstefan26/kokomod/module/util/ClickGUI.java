package io.github.kingstefan26.kokomod.module.util;

import io.github.kingstefan26.kokomod.core.clickgui.ClickGui;
import io.github.kingstefan26.kokomod.core.module.blueprints.UtilModule;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;


@SuppressWarnings("unused")
public class ClickGUI extends UtilModule {

	public ClickGUI() {
		super("ClickGUI");
	}

	@SubscribeEvent
	public void key(InputEvent.KeyInputEvent e) {
		if (mc.theWorld == null || mc.thePlayer == null)
			return;
		try {
			if (Keyboard.isCreated()) {
				if (Keyboard.getEventKeyState()) {
					int keyCode = Keyboard.getEventKey();
					if (keyCode <= 0) return;
					if(keyCode == Keyboard.KEY_RSHIFT){
						mc.displayGuiScreen(ClickGui.getClickGui());
					}
				}
			}
		} catch (Exception q) { q.printStackTrace(); }
	}
}
