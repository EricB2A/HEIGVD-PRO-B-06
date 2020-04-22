package com.gdx.uch2.networking.client;

import com.gdx.uch2.networking.GameState;
import com.gdx.uch2.networking.PlayerState;
import com.gdx.uch2.networking.UserActionSequence;
import com.gdx.uch2.networking.server.SendUpdate;
import com.gdx.uch2.networking.server.ServerGameStateTickManager;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.Timer;

public class ClientActionsTickManager {
    private static class Instance{
        static final ClientActionsTickManager instance = new ClientActionsTickManager();
    }

    private Timer timer;
    private ChannelHandlerContext ctx;
    private UserActionSequence sq;

    private ClientActionsTickManager(){
    }

    public static ClientActionsTickManager getInstance(){
        return ClientActionsTickManager.Instance.instance;
    }

    public void initSequence(int playerID){ //TODO l'ID devrait être stocké dans le contexte
        sq = new UserActionSequence(playerID);
    }

    public void setContext(ChannelHandlerContext ctx){
        this.ctx = ctx;
    }

    //Crée le timer et envoie régulièrement une séquence d'acitons au serveur.
    public void start(int delay, int tickDuration){
        this.timer = new Timer();
        timer.schedule(new SendActions(ctx), delay, tickDuration);
    }

    public UserActionSequence getSequence(){
        return sq;
    }


}
