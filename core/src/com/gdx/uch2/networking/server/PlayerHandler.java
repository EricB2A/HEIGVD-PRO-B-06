package com.gdx.uch2.networking.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class PlayerHandler extends ChannelInboundHandlerAdapter {

    public PlayerHandler(CentralGameManager manager) {
        this.manager = manager;
    }

    private CentralGameManager manager;



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        manager.readMessage(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
