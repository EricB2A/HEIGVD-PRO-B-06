package com.gdx.uch2.networking.client;

import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.entities.OnlinePlayerManager;
import com.gdx.uch2.entities.Player;
import com.gdx.uch2.entities.World;
import com.gdx.uch2.networking.*;

/**
 * Classe Traitant les informations reçues du serveur
 */
public class GameClientHandler {

    static public GamePhase currentPhase; //TODO quand même c'est un peu abusé là
    static private boolean isOver;
    static private boolean roundOver;
    private final PlayerContext ctx;

    /**
     * Constructeur
     * @param ctx Contexte du joueur
     */
    public GameClientHandler(PlayerContext ctx) {
        this.ctx = ctx;
        currentPhase = null;
        isOver = false;
    }

    /**
     * Lit et traite un message en fonction du type du message
     * @param type le type du message
     */
    public void readMessage(MessageType type) {

        switch(type){
            case GameStateUpdate:
                processGameStateUpdate();
                break;
            case BlockPlaced:
                processBlockPlacement();
                break;
            case StartMovementPhase:
                startMovementPhase();
                break;
            case EndGame:
                isOver = true;
                break;
            case Score:
                processScoreUpdate();
                break;
            default:
                System.out.println("CLI: Message non traitable par le client : " + type);
                break;
        }

    }

    /**
     * Indique si la partie est terminée
     * @return true si la partie est terminée, false sinon
     */
    public static boolean isOver() {
        return isOver;
    }

    /**
     * Indique si le round est terminé
     * @return true si le round est terminé, false sinon
     */
    public static boolean isRoundOver() {
        return roundOver;
    }

    /**
     * Traite un message de type GameStateUpdate
     */
    private void processGameStateUpdate(){
        OnlinePlayerManager.getInstance().update(ctx.in.readGameState());
    }

    /**
     * Traite un message de type Score
     */
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

        if(op.getPlayerID() == ctx.getId()){
            MessageSender.getInstance().setCanPlace(true);
        }else if(op.getPlayerID() == -1){
            startMovementPhase();
        }
    }


    private void startMovementPhase(){
        currentPhase = GamePhase.Moving;
        System.out.println("CLI: START MOVEMENT PHASE");
    }

    private void startEditingPhase(){
        currentPhase = GamePhase.Editing;
        MessageSender.getInstance().setCanPlace(false);
        Vector2 pos = World.currentWorld.getLevel().getSpanPosition();
        MessageSender.getInstance().setContext(ctx);
        MessageSender.getInstance().setCurrentState(new PlayerState(ctx.getId(),
                Player.State.IDLE, pos.x, pos.y, 0));
        System.out.println("CLI: START EDITING PHASE");
    }
}
