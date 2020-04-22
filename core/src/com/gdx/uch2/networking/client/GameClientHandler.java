package com.gdx.uch2.networking.client;

import com.gdx.uch2.networking.kryo.NettyKryoDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.ArrayList;
import java.util.List;

public class GameClientHandler extends ChannelInboundHandlerAdapter {

    private boolean isSending;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        if(!isSending){//TODO placer ça autre part
            startSending(ctx);
            isSending = true;
        }


        ByteBuf m = (ByteBuf) msg;
        if(m.readChar() == 'a'){
            NettyKryoDecoder nettyKryoDecoder = new NettyKryoDecoder();
            List<Object> oof = new ArrayList<>();
            try {
                while (m.isReadable()) {
                    nettyKryoDecoder.decode((ByteBuf) msg, oof);
                    System.out.flush();
                }
                //System.out.println("===========================0");
                System.out.println(oof.get(0).toString());
            } finally {
                ReferenceCountUtil.release(msg);
            }
        }else{
            System.out.println("pas un Gamestate");
        }

    }

    private void startSending(ChannelHandlerContext ctx){
        ClientActionsTickManager.getInstance().initSequence(1);
        ClientActionsTickManager.getInstance().setContext(ctx);
        ClientActionsTickManager.getInstance().start(1000, 200);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
