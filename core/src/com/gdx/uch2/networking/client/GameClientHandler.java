package com.gdx.uch2.networking.client;

import com.gdx.uch2.entities.OnlinePlayerManager;
import com.gdx.uch2.entities.World;
import com.gdx.uch2.networking.*;

public class GameClientHandler {

    private int playerID = -1;
    static public GamePhase currentPhase; //TODO quand même c'est un peu abusé là
    static private boolean isOver;
    static private boolean roundOver;
    private PlayerContext ctx;

    public GameClientHandler(PlayerContext ctx) {
        this.ctx = ctx;
        this.playerID = ctx.getId();
        currentPhase = null;
        isOver = false;
    }

    public void readMessage(MessageType type) {
            if(type == MessageType.GameStateUpdate){
                processGameStateUpdate();
//                else System.out.println("CLI: Gamestate Recu mais on est en phase de placement");
            }
            else if(type == MessageType.BlockPlaced) {
                processBlockPlacement();
            }
            else if(type == MessageType.AckBlockPlaced) {
                processAckBlockPlaced();
            }
            else if(type == MessageType.StartMovementPhase) {
                startMovementPhase();
            }
            else if (type == MessageType.EndGame) {
                isOver = true;
            }else if(type == MessageType.Score){
                processScoreUpdate();
            }
            else {
                System.out.println("CLI: Message non traitable par le client : " + type);
            }
    }

    public static boolean isOver() {
        return isOver;
    }

    public static boolean isRoundOver() {
        return roundOver;
    }

    private void processGameStateUpdate(){
        OnlinePlayerManager.getInstance().update(ctx.in.readGameState());
//        System.out.println("CLI: Gamestate reçu par le client :" + objects.get(0).toString());
    }

    private void processScoreUpdate(){
        OnlinePlayerManager.getInstance().setScores(ctx.in.readScore());
        System.out.println("CLI: Received les scores mec.");
    }



    private void processBlockPlacement(){
        ObjectPlacement op = ctx.in.readObjectPlacement();
        System.out.println("CLI: placement de bloc recu avec " + op);

        if(op.getBlock() == null) {
            roundOver = true;
            startEditingPhase();
        }else{
            roundOver = false;
            World.currentWorld.placeBlock(op.getBlock());
            System.out.println("CLI: placement du bloc reçu");
        }

        if(op.getPlayerID() == playerID){
            ClientPlayerStateTickManager.getInstance().setCanPlace(true);
        }else if(op.getPlayerID() == -1){
            startMovementPhase();
        }

        ctx.out.writeMessage(MessageType.AckBlockPlaced);
    }

    private void processAckBlockPlaced(){
        ClientPlayerStateTickManager.getInstance().setRecievedAck(true);
    }




    private void startMovementPhase(){
        currentPhase = GamePhase.Moving;
        System.out.println("CLI: START MOVEMENT PHASE");
    }

    private void startEditingPhase(){
        currentPhase = GamePhase.Editing;
        ClientPlayerStateTickManager.getInstance().setCanPlace(false);
        System.out.println("CLI: START EDITING PHASE");
    }
}
