package com.gdx.uch2.networking.server;

import com.badlogic.gdx.Game;
import com.gdx.uch2.networking.GameState;
import io.netty.channel.group.ChannelGroup;

import java.util.Timer;

public class TickManager {
    private static class Instance{
        static final TickManager instance = new TickManager();
    }

    private Timer timer;
    private ChannelGroup players;
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

    public void setPlayers(ChannelGroup players){
        this.players = players;
    }

    //Crée le timer et envoie régulièrement un nouveau gameState à tous les channels dans players.
    public void start(int delay, int tickDuration){
        this.timer = new Timer();
        timer.schedule(new SendUpdate(players, gameState), delay, tickDuration);
    }

    public GameState getGameState(){
        return gameState;
    }


}
