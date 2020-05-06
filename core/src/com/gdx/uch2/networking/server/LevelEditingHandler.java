package com.gdx.uch2.networking.server;

import com.gdx.uch2.networking.MessageType;
import com.gdx.uch2.networking.ObjectPlacement;
import com.gdx.uch2.networking.UserActionSequence;
import com.gdx.uch2.networking.kryo.NettyKryoDecoder;
import com.gdx.uch2.networking.kryo.NettyKryoEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.ArrayList;
import java.util.List;

public class LevelEditingHandler extends ChannelInboundHandlerAdapter {

    private List<ChannelHandlerContext> players;

    public LevelEditingHandler(List<ChannelHandlerContext> players){
        this.players = players;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        System.out.println("alsdfjaésl fj asfjaélsfjaélsfdjéaldfjélkad");
        ByteBuf m = (ByteBuf) msg;
        if(m.readChar() == MessageType.BlockPlaced.getChar()){
            NettyKryoDecoder nettyKryoDecoder = new NettyKryoDecoder();
            List<Object> objects = new ArrayList<>();
            try {
                //while (m.isReadable()) {
                    nettyKryoDecoder.decode(m, objects);
                //}
                System.out.println("Placement de block reçu");
                sendBlockToAllPlayers((ObjectPlacement) (objects.get(0)));
            } finally {
                ReferenceCountUtil.release(msg);
            }
        }else{
            System.out.println("Pas un placement de block");
        }
    }


    private void sendBlockToAllPlayers(ObjectPlacement object){
        for(ChannelHandlerContext ctx : players){
            NettyKryoEncoder encoder = new NettyKryoEncoder();
            ByteBuf out = Unpooled.buffer(1024);
            encoder.encode(object, out, MessageType.BlockPlaced.getChar());
            ctx.channel().writeAndFlush(out);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
