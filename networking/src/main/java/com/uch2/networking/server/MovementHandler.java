package com.uch2.networking.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;

import java.util.ArrayList;
import java.util.List;

public class MovementHandler extends ChannelInboundHandlerAdapter {
    private ChannelGroup players;

    public MovementHandler(ChannelGroup players){
        this.players = players;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    }
}
