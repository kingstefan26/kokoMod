package io.github.kingstefan26.stefans_util.module.macro.wart;

import io.github.kingstefan26.stefans_util.core.module.ModuleMenagers.moduleManager;
import io.github.kingstefan26.stefans_util.core.module.moduleDecorators.impl.keyBindDecorator;
import io.github.kingstefan26.stefans_util.core.module.moduleFrames.basicModule;
import io.github.kingstefan26.stefans_util.core.setting.impl.CheckSetting;
import io.github.kingstefan26.stefans_util.core.setting.impl.ChoseAKeySetting;
import io.github.kingstefan26.stefans_util.core.setting.impl.SliderNoDecimalSetting;
import io.github.kingstefan26.stefans_util.core.setting.impl.SliderSetting;
import io.github.kingstefan26.stefans_util.module.macro.macro;
import io.github.kingstefan26.stefans_util.module.macro.stateMenager;
import io.github.kingstefan26.stefans_util.module.macro.util.util;
import io.github.kingstefan26.stefans_util.module.macro.wart.helper.wartMacroHelpers;
import io.github.kingstefan26.stefans_util.module.macro.wart.routines.wartMacroRoutines;
import io.github.kingstefan26.stefans_util.service.impl.chatService;
import io.github.kingstefan26.stefans_util.util.stefan_utilEvents;
import lombok.Value;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import static io.github.kingstefan26.stefans_util.module.macro.macroStates.*;


public class UniversalWartMacro extends basicModule implements macro {
    public static final String chatprefix = "§d[Wart-Macro]§r ";

    public final wartMacroRoutines routines = wartMacroRoutines.getRoutines(this);
    public final wartMacroHelpers helpers = wartMacroHelpers.getHelper(this);

    public stateMenager state = new stateMenager();

    // values changed by settings
    public int unpauseKey;
    public boolean experimentalGuiFlag;
    public boolean verboseLogging;
    public int wantedPitch;
    public boolean perfectHeadRotation;
    public float rateOfChange = 0.01F;

    // values that change on runtime
    public int playerYaw;
    public int playerPitch;
    public double playerSpeed;
    public int fallCounter;
    public util.macroMenu macroMenu;
    public util.walkStates macroWalkStage = util.walkStates.DEFAULT; // TODO make a better state model (compatible with last left off)



    // runtime routine flags
    public boolean guiCloseGrace;
    public boolean playerTeleported;
    public boolean playerFallen;


    public UniversalWartMacro() {
        super("UniversalWartMacro", "macros wart", moduleManager.Category.MACRO,
                new keyBindDecorator("wartMacro")
        );
    }


    @Value
    public static class Truple{
        BlockPos key;
        BlockPos value;
        BlockPos thisthing;
    }


    @Override
    public void onLoad() {
        new SliderSetting("rate of change", this, 0.01, 0, 0.5F, (newvalue) -> {
            double temp = (Double) newvalue;
            rateOfChange = (float) temp;
        });
        new SliderNoDecimalSetting("pitch", this, 9, 0, 90, (newvalue) ->
                wantedPitch = (int) newvalue);
        new CheckSetting("perfect head rotation", this, true, (newvalue) ->
                perfectHeadRotation = (boolean) newvalue);
        new CheckSetting("experimental gui", this, false, (onUpdateCallbackValue) ->
                experimentalGuiFlag = (boolean) onUpdateCallbackValue);
        new CheckSetting("verbose logging", this, false, (onUpdateCallbackValue) ->
                verboseLogging = (boolean) onUpdateCallbackValue);
        new ChoseAKeySetting("unpause key", this, 0, (onUpdateCallbackValue) ->
                unpauseKey = (int) onUpdateCallbackValue);

        super.onLoad();
    }


    @Override
    public void onWorldRender(RenderWorldLastEvent e) {
        routines.continuousRender(e.partialTicks);
    }

    @Override
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.isCreated()) {
            if (Keyboard.getEventKeyState()) {
                int keyCode = Keyboard.getEventKey();
                if (keyCode <= 0)
                    return;

                if (state.checkState(PAUSED)) {
                    routines.doContinuousPauseChecks(keyCode);
                }
                if (state.checkState(RECALIBRATING)) {
                    routines.recalibratingDisableKeyLisiner(keyCode);
                }
            }
        }
    }


    @Override
    public void onRenderTick(TickEvent.RenderTickEvent e) {
        switch (state.getState()) {
            case IDLE:
                break;
            case PAUSED:
                routines.macroPauseRenderRoutine();
                break;
            case MACROING:
                routines.doMacroRenderRoutine();
                routines.doContinuousMacroChecks();
                break;
            case RECALIBRATING:
                routines.checkMacroConditionsRenderRoutine(e.renderTickTime);
                break;
            case AUTONOMOUS_RECALIBRATING:
                routines.recalibrateMacroRenderRoutine();
                break;
            case STARTING:

                break;
            case STOPPING:
                break;
        }

    }

    @Override
    public void onHighestClientTick(TickEvent.ClientTickEvent event) {
        if (mc == null || mc.theWorld == null || mc.thePlayer == null) {
            state.setState(AUTONOMOUS_RECALIBRATING);
            return;
        }

        switch (state.getState()) {
            case IDLE:
                break;
            case PAUSED:
                break;
            case MACROING:
                if (event.phase == TickEvent.Phase.START) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
                }
                routines.doMacroRoutine();
                break;
            case RECALIBRATING:
                routines.checkMacroConditionsRoutine();
                break;
            case AUTONOMOUS_RECALIBRATING:
                routines.recalibrateMacroRoutine();
                break;
            case STARTING:
                routines.enableMacroRoutine();
                break;
            case STOPPING:
                routines.disableMacroRoutine();
                break;
        }

    }


    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        logger.info("player dissconected from server turning on AUTONOMOUS_RECALIBRATING");
        state.setState(AUTONOMOUS_RECALIBRATING);
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        chatService.queueCleanChatMessage(chatprefix + "you swiched worlds, turning on autonomous recalibration");
        if(state.checkState(MACROING)){
            state.setState(AUTONOMOUS_RECALIBRATING);
        }
    }

    int tickcounter = 0;

    @Override
    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase == TickEvent.Phase.START) {
            tickcounter++;
        }
        if (tickcounter % 20 == 0) {
            routines.continuousChecks();
            tickcounter = 0;
        }

    }


    public void disableFromGui() {
        this.toggle();
    }


    @Override
    public void onEnable() {
        state.setState(STARTING);
        super.onEnable();
    }


    public void playerFallCallBack() {
        if (state.checkState(MACROING) && !playerTeleported) {
            fallCounter++;
            chatService.queueCleanChatMessage(chatprefix + "fallen for the " + fallCounter + " time");
            playerFallen = true;
        }
    }

    @Override
    public void onPlayerTeleportEvent(stefan_utilEvents.playerTeleportEvent event) {
        if (state.checkState(MACROING)) {
            chatService.queueCleanChatMessage(chatprefix + "teleport detected!");
            playerTeleported = true;
        }
    }

    @Override
    public void onGuiOpen(GuiOpenEvent e) {
        if (mc == null || mc.theWorld == null || mc.thePlayer == null) return;
        if(state.checkState(MACROING) || state.checkState(RECALIBRATING)){
            e.setCanceled(true);
        }
    }
}
