package com.gdx.uch2.networking.server;

import com.gdx.uch2.entities.Player;
import com.gdx.uch2.networking.messages.GameState;
import com.gdx.uch2.networking.messages.PlayerContext;
import com.gdx.uch2.networking.messages.PlayerState;

import java.util.Timer;

public class ServerGameStateTickManager {
    private static class Instance{
        static final ServerGameStateTickManager instance = new ServerGameStateTickManager();
    }

    private Timer timer;
    private PlayerContext[] players;
    private GameState gameState;

    private ServerGameStateTickManager(){
    }

    public boolean setNewState(GameState newState){
        if(newState != gameState){
            gameState = newState;
            return true;
        }else{
            return false;
        }
    }

    public static ServerGameStateTickManager getInstance(){
        return Instance.instance;
    }

    public void setPlayers(PlayerContext[] players){
        this.players = players;
    }

    public void setPlayerState(PlayerState newState){
        if (gameState != null) {
            gameState.setPlayerState(newState);
        }
    }

    //Crée le timer et envoie régulièrement un nouveau gameState à tous les channels dans players.
    public void start(int delay, int tickDuration){
        initGameState(players.length, 10, 10); //TODO initialiser la partie selon des paramètres moins arbitraires

        this.timer = new Timer();
        timer.schedule(new SendUpdate(timer, players), delay, tickDuration);
    }

    public GameState getGameState(){
        return gameState;
    }

    private void initGameState(int nbPlayers, int posX, int posY){
        PlayerState[] newPlayers = new PlayerState[nbPlayers];

        for(int i = 0; i < nbPlayers; ++i){
            newPlayers[i] = new PlayerState(i, Player.State.IDLE, posX, posY, 0); //Place tous les joueurs au même endroit
        }
        gameState = new GameState(newPlayers);
    }


}
