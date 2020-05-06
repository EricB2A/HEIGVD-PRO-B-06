package com.gdx.uch2.networking.server;

import com.gdx.uch2.networking.*;
import com.gdx.uch2.networking.kryo.NettyKryoDecoder;
import com.gdx.uch2.networking.kryo.NettyKryoEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.ArrayList;
import java.util.List;

public class GameHandler extends ChannelInboundHandlerAdapter {

    private List<ChannelHandlerContext> players;

    public GameHandler(List<ChannelHandlerContext> players){
        this.players = players;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf m = (ByteBuf) msg;
        if(m.readChar() == MessageType.UserAction.getChar()){
            NettyKryoDecoder nettyKryoDecoder = new NettyKryoDecoder();
            List<Object> objects = new ArrayList<>();
            try {
                nettyKryoDecoder.decode((ByteBuf) msg, objects);
                System.out.println("sequence d'actions reçue");
                applyActions((UserActionSequence) objects.get(0), ServerGameStateTickManager.getInstance().getGameState());
            } finally {
                ReferenceCountUtil.release(msg);
            }
        }else
        if(m.readChar() == MessageType.BlockPlaced.getChar()){
            NettyKryoDecoder nettyKryoDecoder = new NettyKryoDecoder();
            List<Object> objects = new ArrayList<>();
            try {
                nettyKryoDecoder.decode(m, objects);
                System.out.println("Placement de block reçu");
                sendBlockToAllPlayers((ObjectPlacement) (objects.get(0)));
            } finally {
                ReferenceCountUtil.release(msg);
            }
        }
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

    private void applyAction(UserAction action, GameState state, int playerID){
        //TODO minimal, appliquer les actions plus précisément
        switch (action){
            case UP:
                state.setPosY(state.getPosY(playerID) + 1, playerID);
                System.out.println("Player " + playerID + ": (" + state.getPosX(playerID) + ", " + state.getPosY(playerID) + ")");
                break;
            case LEFT:
                state.setPosX(state.getPosX(playerID) - 1, playerID);
                System.out.println("Player " + playerID + ": (" + state.getPosX(playerID) + ", " + state.getPosY(playerID) + ")");
                break;
            case RIGHT:
                state.setPosX(state.getPosX(playerID) + 1, playerID);
                System.out.println("Player " + playerID + ": (" + state.getPosX(playerID) + ", " + state.getPosY(playerID) + ")");
                break;
            default:
                break;
        }
    }

    private void applyActions(UserActionSequence sequence, GameState state){
        for(UserAction a : sequence.getActions()){
            applyAction(a, state, sequence.getPlayerID());
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
