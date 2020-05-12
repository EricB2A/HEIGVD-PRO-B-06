package com.gdx.uch2.networking.client;

import com.gdx.uch2.networking.GamePhase;
import com.gdx.uch2.entities.OnlinePlayerManager;
import com.gdx.uch2.networking.MessageType;
import com.gdx.uch2.networking.PlayerState;
import com.gdx.uch2.networking.kryo.NettyKryoDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameClientHandler extends ChannelInboundHandlerAdapter {

    private int playerID = -1;
    private NettyKryoDecoder decoder = new NettyKryoDecoder();
    private GamePhase currentPhase;
    private ChannelHandlerContext ctx = null;

    private void processGameStateUpdate(ByteBuf m){
        List<Object> objects = new ArrayList<>();
        decoder.decode(m, objects);
        System.out.println("Gamestate reçu par le client :" + objects.get(0).toString());
    }

    private void processGameStart(ByteBuf m){
        startEditingPhase();
        playerID = m.readInt();
        OnlinePlayerManager.getInstance().init(playerID);
        System.out.println("PlayerID = " + playerID);
        startSending(ctx);
    }

    private void processBlockPlacement(ByteBuf m){
        List<Object> objects = new ArrayList<>();
        decoder.decode(m, objects);
        System.out.println("block placé : " + objects.get(0).toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if(this.ctx == null) this.ctx = ctx;

        ByteBuf m = (ByteBuf) msg;
        m.readChar();
        try{
            if(m.getChar(0) == MessageType.GameStateUpdate.getChar()){
                if(currentPhase == GamePhase.Moving){
                    processGameStateUpdate(m);
                }
            }
            else if(m.getChar(0) == MessageType.GameStart.getChar()){
                processGameStart(m);
            }
            else if(m.getChar(0) == MessageType.BlockPlaced.getChar()) {
                if(currentPhase == GamePhase.Editing){
                    processBlockPlacement(m);
                }
            }
            else if(m.getChar(0) == MessageType.CanPlace.getChar()) {
                //TODO thibaud
            }
            else if(m.getChar(0) == MessageType.StartMovementPhase.getChar()) {
                //TODO
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
        ClientPlayerStateTickManager.getInstance().setContext(ctx);
        ClientPlayerStateTickManager.getInstance().setCurrentState(new PlayerState(playerID, 20, 30, 0));
        ClientPlayerStateTickManager.getInstance().start(1000, 500);
    }


    private void startMovementPhase(){
        currentPhase = GamePhase.Moving;
    }

    private void startEditingPhase(){
        currentPhase = GamePhase.Editing;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
