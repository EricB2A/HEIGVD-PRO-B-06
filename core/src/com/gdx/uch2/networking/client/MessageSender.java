package com.gdx.uch2.networking.client;

import com.gdx.uch2.entities.Block;
import com.gdx.uch2.entities.OnlinePlayerManager;
import com.gdx.uch2.networking.messages.MessageType;
import com.gdx.uch2.networking.messages.ObjectPlacement;
import com.gdx.uch2.networking.PlayerContext;
import com.gdx.uch2.networking.messages.PlayerState;

import java.util.Timer;

/**
 * Classe singleton permettant d'envoyer des messages au serveur depuis un autre thread
 */
public class MessageSender {
    private static class Instance{
        static final MessageSender instance = new MessageSender();
    }

    private MessageSender(){
    }

    /**
     * @return l'instance unique du singleton
     */
    public static MessageSender getInstance(){
        return MessageSender.Instance.instance;
    }

    private PlayerContext ctx;
    private PlayerState currentState;
    private int playerID = -1;
    private boolean canPlace;

    /**
     * @return si le joueur est autorisé à placer un block ou pas
     */
    public boolean getCanPlace() {
        return canPlace;
    }

    /**
     * Autorise ou interdit le joueur à placer un block
     * @param canPlace Booléen indiquant si le joueur peut placer un block ou pas
     */
    public void setCanPlace(boolean canPlace) {
        this.canPlace = canPlace;
        System.out.println("CLI: set Can place = " + canPlace);
    }


    /**
     * @return L'état actuel du joueur
     */
    public PlayerState getCurrentState(){
        return currentState;
    }

    /**
     * Modifie l'état courant du joueur, qui est envoyé régulièrement
     * @param newState le nouvel état du joueur
     */
    public void setCurrentState(PlayerState newState){
        this.currentState = newState;
    }

    /**
     * Donne un contexte pour le joueur
     * @param ctx le contexte à donner au joueur
     */
    public void setContext(PlayerContext ctx){
        this.ctx = ctx;
    }

    /**
     * Donne un ID au joueur
     * @param playerID l'ID du joueur
     */
    public void setPlayerID(int playerID){
        this.playerID = playerID;
    }

    /**
     * @return l'ID du joueur
     */
    public int getPlayerID() {
        return playerID;
    }


    /**
     * Envoie un message au serveur indiquant que le joueur est arrivé à la fin du niveau
     */
    public void sendFinish(){
        ctx.out.writeMessage(MessageType.ReachedEnd);
    }

    /**
     * Envoie un message au serveur indiquant que le joueur est mort
     */
    public void sendDeath() {
        ctx.out.writeMessage(MessageType.Death);
    }

    /**
     * Envoie un placement de block au serveur
     * @param block le block placé
     */
    public void sendBlockPlacement(final Block block){
        setCanPlace(false);
        System.out.println("CLI: Sending block placement as player #" + playerID);
        ctx.out.writeMessage(new ObjectPlacement(playerID, block));
    }

    public void sendBlockMovement(final Block block) {
        ctx.out.writeMessage(new ObjectPlacement(playerID, block), false);
    }


    /**
     * Crée un timer envoyant régulièrement le PlayerState actuel au serveur.
     * @param delay Le délai avant le lancement du timer
     * @param tickDuration la durée d'un tick de timer
     */
    public void start(int delay, int tickDuration){
        Timer timer = new Timer();
        timer.schedule(new SendPlayerState(timer, ctx), delay, tickDuration);
    }

}
