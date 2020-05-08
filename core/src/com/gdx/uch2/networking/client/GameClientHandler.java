package com.gdx.uch2.networking.client;

import com.gdx.uch2.networking.MessageType;
import com.gdx.uch2.networking.PlayerState;
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


    private void processGameStateUpdate(ByteBuf m){
        List<Object> objects = new ArrayList<>();
        decoder.decode(m, objects);
        System.out.println("Gamestate reçu par le client :" + objects.get(0).toString());
    }

    private void processGameStart(ByteBuf m){
        playerID = m.readInt();
        System.out.println("PlayerID = " + playerID);
    }

    private void processBlockPlacement(ByteBuf m){
        List<Object> objects = new ArrayList<>();
        decoder.decode(m, objects);
        System.out.println("block placé : " + objects.get(0).toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        if(!isSending){//TODO placer ça autre part
            startSending(ctx);
            isSending = true;
        }

        ByteBuf m = (ByteBuf) msg;
        m.readChar();
        try{
            if(m.getChar(0) == MessageType.GameStateUpdate.getChar()){
                processGameStateUpdate(m);
            }
            else if(m.getChar(0) == MessageType.GameStart.getChar()){
                processGameStart(m);
            }
            else if(m.getChar(0) == MessageType.BlockPlaced.getChar()) {
                processBlockPlacement(m);
            }
            else {
                while (m.isReadable()) {
                    System.out.print((char) m.readByte());
                    System.out.flush();
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void startSending(ChannelHandlerContext ctx){
        ClientPlayerStateTickManager.getInstance().setCurrentState(new PlayerState(1, 20, 30));
        ClientPlayerStateTickManager.getInstance().setContext(ctx);
        ClientPlayerStateTickManager.getInstance().start(1000, 500);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
