package com.gdx.uch2.networking.client;

import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.entities.Block;
import com.gdx.uch2.networking.*;
import com.gdx.uch2.networking.kryo.NettyKryoEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.util.TimerTask;

public class sendPlayerState extends TimerTask {

    private ChannelHandlerContext ctx;

    public sendPlayerState(ChannelHandlerContext ctx){
        this.ctx = ctx;
    }

    @Override
    public void run() {

        NettyKryoEncoder encoder = new NettyKryoEncoder();
        ByteBuf out = Unpooled.buffer(1024);
        encoder.encode(ClientPlayerStateTickManager.getInstance().getCurrentState(), out, MessageType.PlayerStateUpdate.getChar());
        ctx.channel().writeAndFlush(out);
    }
}
