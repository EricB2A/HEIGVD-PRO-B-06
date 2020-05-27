package com.gdx.uch2.networking.server;

import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.entities.Player;
import com.gdx.uch2.networking.PlayerContext;
import com.gdx.uch2.networking.messages.GameState;
import com.gdx.uch2.networking.messages.PlayerState;

import java.util.Timer;

/**
 * Singleton s'occupant de lancer l'envoi régulier de GameStates lors du début de la partie et de stocker le GameState actuel
 */
public class ServerGameStateTickManager {
    private static class Instance{
        static final ServerGameStateTickManager instance = new ServerGameStateTickManager();
    }

    private Timer timer;
    private PlayerContext[] players;
    private GameState gameState;

    private ServerGameStateTickManager(){
    }

    /**
     * @return l'unique instance du Singleton
     */
    public static ServerGameStateTickManager getInstance(){
        return Instance.instance;
    }


    /**
     * Donne un nouveau tableau de contextes de joueurs
     * @param players nouveau tableau de contextes de joueurs
     */
    public void setPlayers(PlayerContext[] players){
        this.players = players;
    }

    /**
     * Ajoute ou modifie un Playerstate au sein du GameState en cache
     * @param newState
     */
    public void setPlayerState(PlayerState newState){
        if (gameState != null) {
            gameState.setPlayerState(newState);
        }
    }

    /**
     * Crée le timer et envoie régulièrement un nouveau gameState à tous les joueurs.
     * @param delay délai après lequel l'envoi commencera
     * @param tickDuration durée entre 2 envois
     * @param initPos position initiale
     */
    public void start(int delay, int tickDuration, Vector2 initPos){
        initGameState(players.length, initPos.x, initPos.y);

        this.timer = new Timer();
        timer.schedule(new SendUpdate(timer, players), delay, tickDuration);
    }

    /**
     *
     * @return le gamestate gardé en cache
     */
    public GameState getGameState(){
        return gameState;
    }

    private void initGameState(int nbPlayers, float posX, float posY){
        PlayerState[] newPlayers = new PlayerState[nbPlayers];

        for(int i = 0; i < nbPlayers; ++i){
            newPlayers[i] = new PlayerState(i, Player.State.IDLE, posX, posY, 0); //Place tous les joueurs au même endroit
        }
        gameState = new GameState(newPlayers);
    }


}
