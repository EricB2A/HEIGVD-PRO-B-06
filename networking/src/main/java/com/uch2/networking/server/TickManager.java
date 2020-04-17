package com.uch2.networking.server;

import io.netty.channel.group.ChannelGroup;

import java.util.Timer;

public class TickManager {
    private static class Instance{
        static final TickManager instance = new TickManager();
    }

    private Timer timer;
    private ChannelGroup players;

    private TickManager(){
    }

    public static TickManager getInstance(){
        return Instance.instance;
    }

    public void setPlayers(ChannelGroup players){
        this.players = players;
    }

    public void start(int delay, int tickDuration){
        this.timer = new Timer();
        timer.schedule(new SendUpdate(players), delay, tickDuration);
    }


}
