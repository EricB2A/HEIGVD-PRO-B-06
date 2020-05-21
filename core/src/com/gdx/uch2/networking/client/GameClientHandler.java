package com.gdx.uch2.networking.client;

import com.gdx.uch2.entities.World;
import com.gdx.uch2.networking.*;
import com.gdx.uch2.entities.OnlinePlayerManager;
import com.gdx.uch2.networking.GameState;
import com.gdx.uch2.networking.MessageType;
import com.gdx.uch2.networking.PlayerState;
import com.gdx.uch2.networking.kryo.NettyKryoDecoder;
import com.gdx.uch2.networking.server.PlayersAmountHandler;
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

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if(this.ctx == null) this.ctx = ctx;

        ByteBuf m = (ByteBuf) msg;

        int msgType = m.readChar();
        //System.out.println("CLI : msgType = " + msgType);
        try{
            if(msgType == MessageType.GameStateUpdate.getChar()){
                //if(currentPhase == GamePhase.Moving){
                    processGameStateUpdate(m);
                //}
            }
            else if(msgType == MessageType.GameStart.getChar()){
                processGameStart(m);
            }
            else if(msgType == MessageType.BlockPlaced.getChar()) {
                if(currentPhase == GamePhase.Editing){
                    processBlockPlacement(m);
                }
            }
            else if(msgType == MessageType.CanPlace.getChar()) {
                //TODO thibaud
            }
            else if(msgType == MessageType.StartMovementPhase.getChar()) {
                //TODO
            }
            else {
                while (m.isReadable()) {
                    System.out.print(m.readByte());
                    System.out.flush();
                }
                System.out.println();
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }

    }

    private void processGameStateUpdate(ByteBuf m){
        List<Object> objects = new ArrayList<>();
        decoder.decode(m, objects);
        OnlinePlayerManager.getInstance().update((GameState) objects.get(0));
        System.out.println("CLI: Gamestate reçu par le client :" + objects.get(0).toString());
    }

    private void processGameStart(ByteBuf m){
        startEditingPhase();

        playerID = m.readInt();
        ClientPlayerStateTickManager.getInstance().setPlayerID(playerID);
        OnlinePlayerManager.getInstance().init(playerID);
        System.out.println("CLI: PlayerID = " + playerID);
        startSending(ctx);
    }

    private void processBlockPlacement(ByteBuf m){
        List<Object> objects = new ArrayList<>();
        decoder.decode(m, objects);
        ObjectPlacement op = (ObjectPlacement) objects.get(0);
        System.out.println("CLI: block placé : " + op.toString());
        World.currentWorld.placeBlock(op.getBlock());
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
