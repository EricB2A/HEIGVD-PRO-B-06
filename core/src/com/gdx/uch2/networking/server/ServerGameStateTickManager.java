package com.gdx.uch2.networking.server;

import com.gdx.uch2.networking.GameState;
import com.gdx.uch2.networking.PlayerState;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.Timer;

public class ServerGameStateTickManager {
    private static class Instance{
        static final ServerGameStateTickManager instance = new ServerGameStateTickManager();
    }

    private Timer timer;
    private List<ChannelHandlerContext> players;
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

    public void setPlayers(List<ChannelHandlerContext> players){
        this.players = players;
    }

    public void setPlayerState(PlayerState newState){
        int id = newState.getPlayerID();
        gameState.setPosX(newState.getPosX(), id);
        gameState.setPosY(newState.getPosX(), id);
    }

    //Crée le timer et envoie régulièrement un nouveau gameState à tous les channels dans players.
    public void start(int delay, int tickDuration){
        initGameState(players.size(), 10, 10); //TODO initialiser la partie selon des paramètres moins arbitraires

        this.timer = new Timer();
        timer.schedule(new SendUpdate(players), delay, tickDuration);
    }

    public GameState getGameState(){
        return gameState;
    }

    private void initGameState(int nbPlayers, int posX, int posY){
        PlayerState[] newPlayers = new PlayerState[nbPlayers];

        for(int i = 0; i < nbPlayers; ++i){
            newPlayers[i] = new PlayerState(i, posX, posY); //Place tous les joueurs au même endroit
        }
        gameState = new GameState(newPlayers);
    }


}
