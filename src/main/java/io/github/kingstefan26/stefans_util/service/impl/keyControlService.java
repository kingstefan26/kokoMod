package io.github.kingstefan26.stefans_util.service.impl;

import io.github.kingstefan26.stefans_util.service.Service;
import io.github.kingstefan26.stefans_util.util.util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;


public class keyControlService extends Service {
    public keyControlService() {
        super("keyMovementEngine");
    }

    static int forwardKeyCode, backKeyCode, rightKeyCode, leftKeyCode, attackKeyCode;

    static Queue<simpleCommand> taskQueue = new LinkedList<>();

    public static boolean verbose = false;

    public static class action {
        public enum walk {
            forward, right, left, back,
            forwardLeft, forwardRight, backLeft, backRight
        }
        public enum hand {
            punch, place
        }
    }

    static Queue<simpleCommand> ASYNCQueue = new LinkedList<>();


    long lastStoopedMovingTimer;

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent e) {
        if (mc == null || mc.theWorld == null || mc.thePlayer == null) return;
        if (CurentlyExecuted != null) {
            double playerSpeed = mc.thePlayer.getDistance(mc.thePlayer.lastTickPosX, mc.thePlayer.lastTickPosY, mc.thePlayer.lastTickPosZ);
            if (playerSpeed == 0 && lastStoopedMovingTimer == 0) {
                lastStoopedMovingTimer = System.currentTimeMillis() + 50;
            }
            if (playerSpeed != 0 && lastStoopedMovingTimer != 0) {
                lastStoopedMovingTimer = 0;
            }
            if (playerSpeed == 0 && System.currentTimeMillis() >= lastStoopedMovingTimer) {
                playerStoppedMoving();
                lastStoopedMovingTimer = 0;
            }
        }
    }


    public static void playerStoppedMoving(){
        if(CurentlyExecuted.stoppedmovingCallback != null){
            CurentlyExecuted.stoppedmovingCallback.run();
            CurentlyExecuted = null;
        }
    }

    static simpleCommand CurentlyExecuted;

    public static void clearCommandQueue() {
        ASYNCQueue.clear();
        CurentlyExecuted = null;
    }

    public static void submitCommand(simpleCommand c) {
        taskQueue.add(c);
        if (verbose)
            chatService.queueCleanChatMessage("queued new key control engine command to:" + (c.walkAction != null ? String.valueOf(c.walkAction) : String.valueOf(c.handAction)));
    }

    public static void submitCommandASYNC(simpleCommand c) {
        ASYNCQueue.add(c);
        addedCommand();
        chatService.queueCleanChatMessage("queued new key control engine command to:" + (c.walkAction != null ? String.valueOf(c.walkAction) : String.valueOf(c.handAction)));
    }

    private static void addedCommand() {
        if (CurentlyExecuted == null) {

            if (verbose) chatService.queueCleanChatMessage("checking if CurentlyExecuted is null: TRUE");
            CurentlyExecuted = ASYNCQueue.poll();
            if (CurentlyExecuted == null) {

                if(verbose) chatService.queueCleanChatMessage("getting head of queue: HEAD IS NULL");
                return;
            }else{

                if(verbose) chatService.queueCleanChatMessage("getting head of queue: HEAD IS NOT NULL");
                executeCommand(CurentlyExecuted);

                if(verbose) chatService.queueCleanChatMessage("executing command: DONE");

                if(CurentlyExecuted.totalExecuteTime != 0){
                    util.setTimeout(() -> {
                        if(verbose) chatService.queueCleanChatMessage("finished command:" + (CurentlyExecuted.walkAction != null ? String.valueOf(CurentlyExecuted.walkAction) : String.valueOf(CurentlyExecuted.handAction)));
                        negateExecution(CurentlyExecuted);

                        if(verbose) chatService.queueCleanChatMessage("negating keypresses: DONE");


                        if(CurentlyExecuted.asyncCallback != null){
                            CurentlyExecuted.asyncCallback.run();
                            if(verbose) chatService.queueCleanChatMessage("executing callback: DONE");
                        }else{
                            if(verbose) chatService.queueCleanChatMessage("did not find call back CONTINUE");
                        }

                        CurentlyExecuted = null;

                        if(verbose) chatService.queueCleanChatMessage("nulling command: DONE");
                        if(verbose) chatService.queueCleanChatMessage("checking if ASYNC queue is empty");
                        if(!ASYNCQueue.isEmpty()) {

                            if(verbose) chatService.queueCleanChatMessage("ASYNC queue is not empty calling recursively");
                            addedCommand();
                        }else{

                            if(verbose) chatService.queueCleanChatMessage("ASYNC queue is empty EXITING");
                        }
                    }, CurentlyExecuted.totalExecuteTime);
                }
                return;
            }
        }
        if(verbose) chatService.queueCleanChatMessage("FALSE");
    }

    private static void executeCommand(simpleCommand command) {
        if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null)
            return;
        //null safe on thinnna
        if (command == null) return;

        if (command.walkAction != null) {
            boolean[] executeKeyCodeBoardWalk = new boolean[4];
            switch (command.walkAction) {
                case forward:
                    //image a wall full of switches, each switch corresponds to a button on a keyboard
                    // if you switch the 0-th switch the player presses w and so on
                    executeKeyCodeBoardWalk[0] = true;
                    executeKeyCodeBoardWalk[1] = false;
                    executeKeyCodeBoardWalk[2] = false;
                    executeKeyCodeBoardWalk[3] = false;
                    break;
                case right:
                    executeKeyCodeBoardWalk[0] = false;
                    executeKeyCodeBoardWalk[1] = true;
                    executeKeyCodeBoardWalk[2] = false;
                    executeKeyCodeBoardWalk[3] = false;
                    break;
                case left:
                    executeKeyCodeBoardWalk[0] = false;
                    executeKeyCodeBoardWalk[1] = false;
                    executeKeyCodeBoardWalk[2] = true;
                    executeKeyCodeBoardWalk[3] = false;
                    break;
                case back:
                    executeKeyCodeBoardWalk[0] = false;
                    executeKeyCodeBoardWalk[1] = false;
                    executeKeyCodeBoardWalk[2] = false;
                    executeKeyCodeBoardWalk[3] = true;
                    break;
                case forwardLeft:
                    executeKeyCodeBoardWalk[0] = true;
                    executeKeyCodeBoardWalk[1] = false;
                    executeKeyCodeBoardWalk[2] = true;
                    executeKeyCodeBoardWalk[3] = false;
                    break;
                case forwardRight:
                    executeKeyCodeBoardWalk[0] = true;
                    executeKeyCodeBoardWalk[1] = true;
                    executeKeyCodeBoardWalk[2] = false;
                    executeKeyCodeBoardWalk[3] = false;
                    break;
                case backLeft:
                    executeKeyCodeBoardWalk[0] = false;
                    executeKeyCodeBoardWalk[1] = false;
                    executeKeyCodeBoardWalk[2] = true;
                    executeKeyCodeBoardWalk[3] = true;
                    break;
                case backRight:
                    executeKeyCodeBoardWalk[0] = false;
                    executeKeyCodeBoardWalk[1] = true;
                    executeKeyCodeBoardWalk[2] = false;
                    executeKeyCodeBoardWalk[3] = true;
                    break;
            }
            clickWalkButtonsAccordingToBoard(executeKeyCodeBoardWalk);
        } else if (command.handAction != null) {
            boolean[] executeKeyCodeBoardHand = new boolean[2];
            switch (command.handAction) {
                case punch:
                    executeKeyCodeBoardHand[0] = true;
                    executeKeyCodeBoardHand[1] = false;
                    break;
                case place:
                    executeKeyCodeBoardHand[0] = false;
                    executeKeyCodeBoardHand[1] = true;
                    break;
            }
            clickHandButtonsAccordingToBoard(executeKeyCodeBoardHand);
        } else {
            throw new IllegalStateException("this is not allRIGHT BY THE HAND OF GOD I DECLARE THIS RUNNABLE AS FINISHED");
        }
    }

    private static void negateExecution(simpleCommand command) {
        if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null)
            return;
        //null safe on thinnna
        if (command == null) return;

        if (command.walkAction != null) {
            if (command.walkAction.equals(action.walk.forward)) {
                KeyBinding.setKeyBindState(forwardKeyCode, false);
            } else if (command.walkAction.equals(action.walk.right)) {
                KeyBinding.setKeyBindState(rightKeyCode, false);
            } else if (command.walkAction.equals(action.walk.left)) {
                KeyBinding.setKeyBindState(leftKeyCode, false);
            } else if (command.walkAction.equals(action.walk.back)) {
                KeyBinding.setKeyBindState(backKeyCode, false);
            } else if (command.walkAction.equals(action.walk.forwardLeft)) {
                KeyBinding.setKeyBindState(forwardKeyCode, false);
                KeyBinding.setKeyBindState(leftKeyCode, false);
            } else if (command.walkAction.equals(action.walk.forwardRight)) {
                KeyBinding.setKeyBindState(forwardKeyCode, false);
                KeyBinding.setKeyBindState(rightKeyCode, false);
            } else if (command.walkAction.equals(action.walk.backLeft)) {
                KeyBinding.setKeyBindState(backKeyCode, false);
                KeyBinding.setKeyBindState(leftKeyCode, false);
            } else if (command.walkAction.equals(action.walk.backRight)) {
                KeyBinding.setKeyBindState(backKeyCode, false);
                KeyBinding.setKeyBindState(rightKeyCode, false);
            }
        } else if (command.handAction != null) {
            if (command.handAction == action.hand.punch) {
                KeyBinding.setKeyBindState(attackKeyCode, false);
            }
        } else {
            throw new IllegalStateException("this is not allRIGHT BY THE HAND OF GOD I DECLARE THIS RUNNABLE AS FINISHED");
        }
    }

    void execute() {
        // example code
        new complexCommand(() -> chatService.queueCleanChatMessage("hi"),
                new walkAction(action.walk.forward),
                new stopWhenPure(() -> Minecraft.getMinecraft().thePlayer.getDistance(
                        Minecraft.getMinecraft().thePlayer.lastTickPosX,
                        Minecraft.getMinecraft().thePlayer.lastTickPosY,
                        Minecraft.getMinecraft().thePlayer.lastTickPosZ) == 0),
                new walkForLimitedTimeAction(action.walk.back, 4000),
                new walkUntilTrue(action.walk.forward, () -> Minecraft.getMinecraft().thePlayer.getDistance(
                        Minecraft.getMinecraft().thePlayer.lastTickPosX,
                        Minecraft.getMinecraft().thePlayer.lastTickPosY,
                        Minecraft.getMinecraft().thePlayer.lastTickPosZ) == 0),
                new stopWhenNonPure((a) -> {
                    if (a instanceof Integer) {
                        int x = (int) a;
                        return x == 1;
                    } else {
                        return false;
                    }
                })
        );
    }


    public interface Action {
    }

    public static class simpleCommand {
        public int totalExecuteTime;
        public long commandExpireTimeStamp;
        Runnable asyncCallback = null;
        Runnable stoppedmovingCallback = null;

        public action.hand handAction;

        public action.walk walkAction;

        public simpleCommand(int time, action.walk walkAction) {
            this.totalExecuteTime = time;
            commandExpireTimeStamp = System.currentTimeMillis() + time;
            this.walkAction = walkAction;
        }

        public simpleCommand(int time, action.walk walkAction, Runnable asyncCallback) {
            this.totalExecuteTime = time;
            commandExpireTimeStamp = System.currentTimeMillis() + time;
            this.asyncCallback = asyncCallback;
            this.walkAction = walkAction;
        }

        public simpleCommand(action.walk walkAction, Runnable stoppedmovingCallback) {
            this.stoppedmovingCallback = stoppedmovingCallback;
            this.walkAction = walkAction;
        }


        public simpleCommand(int time, action.hand handAction) {
            this.totalExecuteTime = time;
            commandExpireTimeStamp = System.currentTimeMillis() + time;
            this.handAction = handAction;
        }

        public simpleCommand(int time, action.hand handAction, Runnable asyncCallback) {
            this.totalExecuteTime = time;
            commandExpireTimeStamp = System.currentTimeMillis() + time;
            this.asyncCallback = asyncCallback;
            this.handAction = handAction;
        }

    }

    /**
     * this is complexAction
     * the first param is the call back when the chain of actions finishes
     * these are actions they execute until a condition is fulfilled and then the head of the array is removed and the chain moves on
     * I chose a supplier as I can execute logic in it and it returns a value, perfect for my scenarios like "move until player y is x"
     * a walkAction is executed until a stopWhen's supplier returns true,
     * or walkForLimitedTimeAction expires after x milliseconds
     * or a walkForLimitedTimeAction action executes until its supplier starts returning true
     */
    public static class complexCommand {
        public Action[] actions;
        public Runnable finishedCallback;

        public complexCommand(Runnable finishedCallback, Action... actions) {
            this.actions = actions;
            this.finishedCallback = finishedCallback;
        }
    }

    public static class walkAction implements Action {
        public action.walk walkAction;

        public walkAction(action.walk walkAction) {
            this.walkAction = walkAction;
        }
    }

    public static class walkForLimitedTimeAction implements Action {
        public action.walk walkAction;
        public int delay;

        public walkForLimitedTimeAction(action.walk walkAction, int delay) {
            this.walkAction = walkAction;
            this.delay = delay;
        }

    }

    public static class stopWhenPure implements Action {
        public BooleanSupplier StopCondition;

        public stopWhenPure(BooleanSupplier StopCondition) {
            this.StopCondition = StopCondition;
        }
    }

    @SuppressWarnings("rawtypes")
    public static class stopWhenNonPure implements Action {
        public Predicate StopCondition;

        public stopWhenNonPure(Predicate StopCondition) {
            this.StopCondition = StopCondition;
        }
    }

    public static class walkUntilTrue implements Action {
        public BooleanSupplier StopCondition;
        public action.walk walkAction;

        public walkUntilTrue(action.walk walkAction, BooleanSupplier StopCondition) {
            this.walkAction = walkAction;
            this.StopCondition = StopCondition;
        }
    }

    private static void clickWalkButtonsAccordingToBoard(boolean[] board) {
        if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null)
            return;
        if (!board[0]) {
            KeyBinding.setKeyBindState(forwardKeyCode, false);
            KeyBinding.setKeyBindState(rightKeyCode, false);
            KeyBinding.setKeyBindState(leftKeyCode, false);
            KeyBinding.setKeyBindState(backKeyCode, false);
        }
        KeyBinding.setKeyBindState(forwardKeyCode, board[0]);
        KeyBinding.setKeyBindState(rightKeyCode, board[1]);
        KeyBinding.setKeyBindState(leftKeyCode, board[2]);
        KeyBinding.setKeyBindState(backKeyCode, board[3]);
    }

    private static void clickHandButtonsAccordingToBoard(boolean[] board) {
        if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null) return;
        KeyBinding.setKeyBindState(attackKeyCode, board[0]);
    }

    public void recheckVals() {
        attackKeyCode = mc.gameSettings.keyBindAttack.getKeyCode();
        leftKeyCode = mc.gameSettings.keyBindLeft.getKeyCode();
        rightKeyCode = mc.gameSettings.keyBindRight.getKeyCode();
        backKeyCode = mc.gameSettings.keyBindBack.getKeyCode();
        forwardKeyCode = mc.gameSettings.keyBindForward.getKeyCode();
    }

    @Override
    public void start() {
        recheckVals();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void stop() {}
}
