package com.gdx.uch2.networking.server;

import com.badlogic.gdx.Game;
import com.gdx.uch2.networking.GameState;
import com.gdx.uch2.networking.PlayerState;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;

import java.util.List;
import java.util.Timer;

public class TickManager {
    private static class Instance{
        static final TickManager instance = new TickManager();
    }

    private Timer timer;
    private List<ChannelHandlerContext> players;
    private GameState gameState;

    private TickManager(){
    }

    public boolean setNewState(GameState newState){
        if(newState != gameState){
            gameState = newState;
            return true;
        }else{
            return false;
        }
    }

    public static TickManager getInstance(){
        return Instance.instance;
    }

    public void setPlayers(List<ChannelHandlerContext> players){
        this.players = players;
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
            newPlayers[i] = new PlayerState(i+1, posX, posY); //TODO faire qqch de propre avec les IDs
        }
        gameState = new GameState(newPlayers);
    }


}
