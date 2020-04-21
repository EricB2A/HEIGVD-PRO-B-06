package com.gdx.uch2.networking.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;

public class MovementHandler extends ChannelInboundHandlerAdapter {
    private ChannelGroup players;

    public MovementHandler(ChannelGroup players){
        this.players = players;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    }
}
