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
import java.util.Arrays;
import java.util.List;

public class GameHandler extends ChannelInboundHandlerAdapter {

    private List<ChannelHandlerContext> players;
    private boolean[] finished;
    private NettyKryoEncoder encoder = new NettyKryoEncoder();
    private NettyKryoDecoder decoder = new NettyKryoDecoder();
    private GamePhase currentPhase;

    public GameHandler(List<ChannelHandlerContext> players){
        this.players = players;
        finished = new boolean[players.size()];
        Arrays.fill(finished, false);
        currentPhase = GamePhase.Editing;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        try{
            ByteBuf m = (ByteBuf) msg;
            m.readChar();
            if(m.getChar(0) == MessageType.PlayerStateUpdate.getChar()){
                processPlayerState(m);
            }
            else if(m.getChar(0) == MessageType.BlockPlaced.getChar()){
                processObjectPlacement(m);
            }
            else if(m.getChar(0) == MessageType.ReachedEnd.getChar()){
                processPlayerReachedEnd(m);
            }
            else{
                while (m.isReadable()) {
                    System.out.print((char) m.readByte());
                    System.out.flush();
                }
            }
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void startMovementPhase(){
        currentPhase = GamePhase.Moving;
        Arrays.fill(finished, false);
    }

    private void startEditingPhase(){
        currentPhase = GamePhase.Editing;
    }

    private void computePoints(){
        //TODO
    }

    private void resetPlayersPositions(){
        for(ChannelHandlerContext ctx)
        ServerGameStateTickManager.getInstance().setNewState();
    }

    private void processPlayerReachedEnd(ByteBuf m){
        int playerThatFinished = m.readInt();
        finished[playerThatFinished] = true;
        boolean allFinished = true;
        for (boolean b : finished) {
            if (!b) {
                allFinished = false;
                break;
            }
        }
        System.out.println("Le joueur #" + playerThatFinished + " est arrivé à la fin!");

        if(allFinished){
            computePoints();
            startEditingPhase();
        }
    }


    private void sendBlockToAllPlayers(ObjectPlacement object){
        for(ChannelHandlerContext ctx : players){
            ByteBuf out = Unpooled.buffer(1024);
            encoder.encode(object, out, MessageType.BlockPlaced.getChar());
            ctx.channel().writeAndFlush(out);
        }
    }

    private void processPlayerState(ByteBuf m){
        List<Object> objects = new ArrayList<>();
        decoder.decode(m, objects);
        System.out.println("playerState reçu");
        PlayerState state = (PlayerState) objects.get(0);
        ServerGameStateTickManager.getInstance().setPlayerState(state);
    }

    private void processObjectPlacement(ByteBuf m){
        List<Object> objects = new ArrayList<>();
        decoder.decode(m, objects);
        System.out.println("Placement de block reçu");
        sendBlockToAllPlayers((ObjectPlacement) (objects.get(0)));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
