package com.gdx.uch2.networking.client;

import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.entities.Block;
import com.gdx.uch2.networking.*;
import com.gdx.uch2.networking.kryo.NettyKryoEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.util.TimerTask;

import static io.netty.buffer.Unpooled.buffer;

public class sendPlayerState extends TimerTask {

    private PlayerContext ctx;

    public sendPlayerState(PlayerContext ctx){
        this.ctx = ctx;
    }

    @Override
    public void run() {
        ctx.out.writeMessage(ClientPlayerStateTickManager.getInstance().getCurrentState());
    }
}
