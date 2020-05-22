package com.gdx.uch2.networking.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class PlayerHandler extends ChannelInboundHandlerAdapter {

    public PlayerHandler(CentralGameManager manager, int playerID) {
        this.manager = manager;
        this.playerID = playerID;
    }

    private CentralGameManager manager;
    private int playerID;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //System.out.println("Message reçu de #" + playerID);
        manager.readMessage(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
