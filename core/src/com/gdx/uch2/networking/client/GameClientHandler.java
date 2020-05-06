package com.gdx.uch2.networking.client;

import com.gdx.uch2.networking.MessageType;
import com.gdx.uch2.networking.kryo.NettyKryoDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.ArrayList;
import java.util.List;

public class GameClientHandler extends ChannelInboundHandlerAdapter {

    private boolean isSending;
    private int playerID;
    private NettyKryoDecoder decoder = new NettyKryoDecoder();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        if(!isSending){//TODO placer ça autre part
            startSending(ctx);
            isSending = true;
        }


        ByteBuf m = (ByteBuf) msg;

        //Si on reçoit un état de jeu du serveur
        if(m.getChar(0) == MessageType.GameStateUpdate.getChar()){
            m.readChar();
            //NettyKryoDecoder nettyKryoDecoder = new NettyKryoDecoder();
            List<Object> oof = new ArrayList<>();
            try {
                //while (m.isReadable()) {
                    decoder.decode((ByteBuf) msg, oof);
                    System.out.flush();
                //}
                System.out.println(oof.get(0).toString());
            } finally {
                ReferenceCountUtil.release(msg);
            }
        }else
            //Si on reçoit un message de début de partie
        if(m.getChar(0) == MessageType.GameStart.getChar()){
            m.readChar();
            playerID = m.readInt();
            System.out.println("PlayerID = " + playerID);
        }else
            //Si on reçoit un message de placement de block
        if(m.getChar(0) == MessageType.BlockPlaced.getChar()){
            m.readChar();
            //NettyKryoDecoder nettyKryoDecoder = new NettyKryoDecoder();
            List<Object> obj = new ArrayList<>();
            try {
                //while (m.isReadable()) {
                    decoder.decode(m, obj);
                //}
                System.out.println("block placé : " + obj.get(0).toString());
            } finally {
                ReferenceCountUtil.release(msg);
            }
        }

         //Sinon, on essaie de lire du texte
        else{
            try {
                while (m.isReadable()) {
                    System.out.print((char) m.readByte());
                    System.out.flush();
                }
            } finally {
                ReferenceCountUtil.release(msg);
            }
        }

    }

    private void startSending(ChannelHandlerContext ctx){
        ClientActionsTickManager.getInstance().initSequence(1);
        ClientActionsTickManager.getInstance().setContext(ctx);
        ClientActionsTickManager.getInstance().start(1000, 500);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
