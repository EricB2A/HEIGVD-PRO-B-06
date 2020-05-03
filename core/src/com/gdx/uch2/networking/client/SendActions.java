package com.gdx.uch2.networking.client;

import com.gdx.uch2.networking.MessageType;
import com.gdx.uch2.networking.UserAction;
import com.gdx.uch2.networking.UserActionSequence;
import com.gdx.uch2.networking.kryo.NettyKryoEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.util.TimerTask;

public class SendActions  extends TimerTask {

    private ChannelHandlerContext ctx;


    public SendActions(ChannelHandlerContext ctx){
        this.ctx = ctx;
    }

    @Override
    public void run() {
        UserActionSequence as = new UserActionSequence(1);
        as.addAction(UserAction.RIGHT);

        NettyKryoEncoder encoder = new NettyKryoEncoder();
        ByteBuf out = Unpooled.buffer(1024);
        encoder.encode(as, out, MessageType.UserAction.getChar());
        ctx.channel().writeAndFlush(out);
        //System.out.println("sent action");
    }
}
