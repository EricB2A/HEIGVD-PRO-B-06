package com.gdx.uch2.networking.server;

import com.gdx.uch2.networking.kryo.NettyKryoDecoder;
import com.gdx.uch2.networking.kryo.NettyKryoEncoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class PlayerHandler extends ChannelInboundHandlerAdapter {

    public PlayerHandler(CentralGameManager manager, int playerID) {
        this.manager = manager;
        this.playerID = playerID;
    }

    private CentralGameManager manager;
    private int playerID;
    private NettyKryoEncoder encoder = new NettyKryoEncoder();
    private NettyKryoDecoder decoder = new NettyKryoDecoder();


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        CentralGameManager.BigMutex.acquire();
        manager.readMessage(ctx, msg, playerID, encoder, decoder);
        CentralGameManager.BigMutex.release();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
