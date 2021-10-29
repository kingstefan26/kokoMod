package io.github.kingstefan26.stefans_util.service.impl;

import io.github.kingstefan26.stefans_util.service.Service;
import io.github.kingstefan26.stefans_util.util.util;
import net.minecraft.client.settings.KeyBinding;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


public class keyControlService extends Service {
    public keyControlService() {
        super("keyMovementEngine");
    }

    static int forwardKeyCode, backKeyCode, rightKeyCode, leftKeyCode, attackKeyCode;

    static Queue<command> taskQueue = new LinkedList<>();


    public static class action {
        public enum walk {
            walkforward, walkright, walkleft, walkback,
            walkforwardLeft, walkforwardRight, walkbackLeft, walkbackRight
        }

        public enum hand {
            punch, place
        }
    }

    public static void submitCommand(command c) {
        taskQueue.add(c);
        chatService.queueCleanChatMessage("queued new key control engine command to:" + (c.walkAction != null ? String.valueOf(c.walkAction) : String.valueOf(c.handAction)));
    }


    static Queue<command> ASYNCQueue = new LinkedList<>();


    public static void submitCommandASYNC(command c) {
        ASYNCQueue.add(c);
        addedCommand();
        chatService.queueCleanChatMessage("queued new key control engine command to:" + (c.walkAction != null ? String.valueOf(c.walkAction) : String.valueOf(c.handAction)));
    }

    static command CurentlyExecuted;
    private static void addedCommand(){
        if(CurentlyExecuted == null){
            chatService.removeLastChatMessage();
            chatService.queueCleanChatMessage("checking if CurentlyExecuted is null: TRUE");
            CurentlyExecuted = ASYNCQueue.poll();
            if(CurentlyExecuted == null) {
                chatService.removeLastChatMessage();
                chatService.queueCleanChatMessage("getting head of queue: HEAD IS NULL");
                return;
            }else{
                chatService.removeLastChatMessage();
                chatService.queueCleanChatMessage("getting head of queue: HEAD IS NOT NULL");
                executeCommand(CurentlyExecuted);
                chatService.removeLastChatMessage();
                chatService.queueCleanChatMessage("executing command: DONE");
                util.setTimeout(() -> {
                    chatService.queueCleanChatMessage("finished command:" + (CurentlyExecuted.walkAction != null ? String.valueOf(CurentlyExecuted.walkAction) : String.valueOf(CurentlyExecuted.handAction)));
                    negateExecution(CurentlyExecuted);
                    chatService.removeLastChatMessage();
                    chatService.queueCleanChatMessage("negating keypresses: DONE");
                    CurentlyExecuted = null;
                    chatService.removeLastChatMessage();
                    chatService.queueCleanChatMessage("nulling command: DONE");
                    chatService.queueCleanChatMessage("checking if ASYNC queue is empty");
                    if(!ASYNCQueue.isEmpty()) {
                        chatService.removeLastChatMessage();
                        chatService.queueCleanChatMessage("ASYNC queue is not empty calling recursively");
                        addedCommand();
                    }else{
                        chatService.removeLastChatMessage();
                        chatService.queueCleanChatMessage("ASYNC queue is empty EXITING");
                    }
                }, CurentlyExecuted.totalExecuteTime);
                return;
            }
        }
        chatService.queueCleanChatMessage("FALSE");
    }



    public static class command {
        public int totalExecuteTime;
        public long commandExpireTimeStamp;
        public boolean AsyncExpireCallbackFlag = false;
        public boolean IsAsyncExpire = false;
        public action.hand handAction;

        public action.walk walkAction;

        public command(int time, boolean isAsyncExpire, action.walk walkAction) {
            if (isAsyncExpire) {
                IsAsyncExpire = true;
            } else {
                this.totalExecuteTime = time;
                commandExpireTimeStamp = System.currentTimeMillis() + time;
            }
            this.walkAction = walkAction;
        }
        public command(int time, boolean isAsyncExpire, action.hand handAction) {
            if (isAsyncExpire) {
                IsAsyncExpire = true;
            } else {
                this.totalExecuteTime = time;
                commandExpireTimeStamp = System.currentTimeMillis() + time;
            }
            this.handAction = handAction;
        }

    }


    static ArrayList<command> AsyncExpireCommands = new ArrayList<>();

    boolean finished = false;
    static boolean[] executeKeyCodeBoardWalk = new boolean[4];

    static boolean[] executeKeyCodeBoardHand = new boolean[2];


    static command currentExecutedCommand;

    private static void executeCommand(command command) {
        //null safe on thinnna
        if (command == null) return;

        if (command.walkAction != null) {
            switch (command.walkAction) {
                case walkforward:
                    //image a wall full of switches, each switch corresponds to a button on a keyboard
                    // if you switch the 0-th switch the player presses w and so on
                    executeKeyCodeBoardWalk[0] = true;
                    executeKeyCodeBoardWalk[1] = false;
                    executeKeyCodeBoardWalk[2] = false;
                    executeKeyCodeBoardWalk[3] = false;
                    break;
                case walkright:
                    executeKeyCodeBoardWalk[0] = false;
                    executeKeyCodeBoardWalk[1] = true;
                    executeKeyCodeBoardWalk[2] = false;
                    executeKeyCodeBoardWalk[3] = false;
                    break;
                case walkleft:
                    executeKeyCodeBoardWalk[0] = false;
                    executeKeyCodeBoardWalk[1] = false;
                    executeKeyCodeBoardWalk[2] = true;
                    executeKeyCodeBoardWalk[3] = false;
                    break;
                case walkback:
                    executeKeyCodeBoardWalk[0] = false;
                    executeKeyCodeBoardWalk[1] = false;
                    executeKeyCodeBoardWalk[2] = false;
                    executeKeyCodeBoardWalk[3] = true;
                    break;
                case walkforwardLeft:
                    executeKeyCodeBoardWalk[0] = true;
                    executeKeyCodeBoardWalk[1] = false;
                    executeKeyCodeBoardWalk[2] = true;
                    executeKeyCodeBoardWalk[3] = false;
                    break;
                case walkforwardRight:
                    executeKeyCodeBoardWalk[0] = true;
                    executeKeyCodeBoardWalk[1] = true;
                    executeKeyCodeBoardWalk[2] = false;
                    executeKeyCodeBoardWalk[3] = false;
                    break;
                case walkbackLeft:
                    executeKeyCodeBoardWalk[0] = false;
                    executeKeyCodeBoardWalk[1] = false;
                    executeKeyCodeBoardWalk[2] = true;
                    executeKeyCodeBoardWalk[3] = true;
                    break;
                case walkbackRight:
                    executeKeyCodeBoardWalk[0] = false;
                    executeKeyCodeBoardWalk[1] = true;
                    executeKeyCodeBoardWalk[2] = false;
                    executeKeyCodeBoardWalk[3] = true;
                    break;
            }
            clickWalkButtonsAccordingToBoard(executeKeyCodeBoardWalk);
        } else if (command.handAction != null) {
            switch (currentExecutedCommand.handAction) {
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

    private static void negateExecution(command command){
        //null safe on thinnna
        if (command == null) return;

        if (command.walkAction != null) {
            if (command.walkAction.equals(action.walk.walkforward)) {
                KeyBinding.setKeyBindState(forwardKeyCode, false);
            } else if (command.walkAction.equals(action.walk.walkright)) {
                KeyBinding.setKeyBindState(rightKeyCode, false);
            } else if (command.walkAction.equals(action.walk.walkleft)) {
                KeyBinding.setKeyBindState(leftKeyCode, false);
            } else if (command.walkAction.equals(action.walk.walkback)) {
                KeyBinding.setKeyBindState(backKeyCode, false);
            } else if (command.walkAction.equals(action.walk.walkforwardLeft)) {
                KeyBinding.setKeyBindState(forwardKeyCode, false);
                KeyBinding.setKeyBindState(leftKeyCode, false);
            } else if (command.walkAction.equals(action.walk.walkforwardRight)) {
                KeyBinding.setKeyBindState(forwardKeyCode, false);
                KeyBinding.setKeyBindState(rightKeyCode, false);
            } else if (command.walkAction.equals(action.walk.walkbackLeft)) {
                KeyBinding.setKeyBindState(backKeyCode, false);
                KeyBinding.setKeyBindState(leftKeyCode, false);
            } else if (command.walkAction.equals(action.walk.walkbackRight)) {
                KeyBinding.setKeyBindState(backKeyCode, false);
                KeyBinding.setKeyBindState(rightKeyCode, false);
            }
        } else if (command.handAction != null) {
            if (currentExecutedCommand.handAction == action.hand.punch) {
                KeyBinding.setKeyBindState(attackKeyCode, false);
            }
        } else {
            throw new IllegalStateException("this is not allRIGHT BY THE HAND OF GOD I DECLARE THIS RUNNABLE AS FINISHED");
        }
    }

    private static void clickWalkButtonsAccordingToBoard(boolean[] board) {
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
    }

    @Override
    public void stop() {
        finished = true;
    }
}