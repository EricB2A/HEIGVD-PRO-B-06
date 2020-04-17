package com.uch2.networking.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;

import java.time.Clock;
import java.util.List;
import java.util.Timer;

public class MovementHandler extends ChannelInboundHandlerAdapter {
    private ChannelGroup players;


    public MovementHandler(ChannelGroup players){
        this.players = players;
    }
}
