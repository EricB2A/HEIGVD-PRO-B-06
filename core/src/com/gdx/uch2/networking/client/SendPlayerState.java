package com.gdx.uch2.networking.client;

import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.entities.Block;
import com.gdx.uch2.networking.*;
import com.gdx.uch2.ui.ErrorHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.util.Timer;
import java.util.TimerTask;

import static io.netty.buffer.Unpooled.buffer;

public class SendPlayerState extends TimerTask {

    private PlayerContext ctx;
    private Timer timer;

    public SendPlayerState(Timer timer, PlayerContext ctx){
        this.timer = timer;
        this.ctx = ctx;
    }

    @Override
    public void run() {
        ctx.out.writeMessage(ClientPlayerStateTickManager.getInstance().getCurrentState());

        if (ctx.out.e != null) {
            timer.cancel();
            timer.purge();
            ErrorHandler.getInstance().setError(ctx.out.e.toString());
        }
    }
}
