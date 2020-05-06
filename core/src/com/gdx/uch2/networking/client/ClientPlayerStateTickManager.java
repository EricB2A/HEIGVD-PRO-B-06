package com.gdx.uch2.networking.client;

import com.gdx.uch2.networking.PlayerState;
import com.gdx.uch2.networking.UserActionSequence;
import io.netty.channel.ChannelHandlerContext;

import java.util.Timer;

public class ClientPlayerStateTickManager {
    private static class Instance{
        static final ClientPlayerStateTickManager instance = new ClientPlayerStateTickManager();
    }

    private Timer timer;
    private ChannelHandlerContext ctx;
    private PlayerState currentState;

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

    public void setContext(ChannelHandlerContext ctx){
        this.ctx = ctx;
    }

    //Crée le timer et envoie régulièrement une séquence d'acitons au serveur.
    public void start(int delay, int tickDuration){
        this.timer = new Timer();
        timer.schedule(new sendPlayerState(ctx), delay, tickDuration);
    }

}
