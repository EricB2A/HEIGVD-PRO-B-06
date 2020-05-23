package com.gdx.uch2.networking.client;

import com.gdx.uch2.entities.Block;
import com.gdx.uch2.networking.*;

import java.util.Timer;

public class ClientPlayerStateTickManager {
    private static class Instance{
        static final ClientPlayerStateTickManager instance = new ClientPlayerStateTickManager();
    }

    private Timer timer;
    private PlayerContext ctx;
    private PlayerState currentState;
    private int playerID = -1;
    private boolean canPlace;
    private boolean recievedAck = false;


    public boolean getRecievedAck() {
        return recievedAck;
    }

    public void setRecievedAck(boolean recievedAck) {
        this.recievedAck = recievedAck;
    }

    public boolean getCanPlace() {
        return canPlace;
    }

    public void setCanPlace(boolean canPlace) {
        this.canPlace = canPlace;
        System.out.println("CLI: set Can place = " + canPlace);
    }

    private ClientPlayerStateTickManager(){
    }

    public static ClientPlayerStateTickManager getInstance(){
        return ClientPlayerStateTickManager.Instance.instance;
    }

    public PlayerState getCurrentState(){
        return currentState;
    }

    public void setCurrentState(PlayerState newState){
        this.currentState = newState;
    }

    public void setContext(PlayerContext ctx){
        this.ctx = ctx;
    }

    public void setPlayerID(int playerID){
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }


    public void sendFinish(){
        System.out.println("CLI: Envoi ReachEnd");
        ctx.out.writeMessage(MessageType.ReachedEnd);
    }

    public void sendDeath() {
        ctx.out.writeMessage(MessageType.Death);
    }

    //TODO placer dans un endroit plus évident ou renommer la classe
    public void sendBlockPlacement(final Block block){
        setCanPlace(false);

        System.out.println("CLI: Sending block placement as player #" + playerID);
        ctx.out.writeMessage(new ObjectPlacement(playerID, block));
    }

    //Crée le timer et envoie régulièrement une séquence d'acitons au serveur.
    public void start(int delay, int tickDuration){
        this.timer = new Timer();
        timer.schedule(new SendPlayerState(ctx), delay, tickDuration);
    }

}
