package com.gdx.uch2.networking.client;

import java.util.ArrayList;
import java.util.List;

import com.gdx.uch2.entities.OnlinePlayerManager;
import com.gdx.uch2.entities.World;
import com.gdx.uch2.networking.GamePhase;
import com.gdx.uch2.networking.GameState;
import com.gdx.uch2.networking.MessageType;
import com.gdx.uch2.networking.ObjectPlacement;
import com.gdx.uch2.networking.PlayerState;
import com.gdx.uch2.networking.kryo.NettyKryoDecoder;
import com.gdx.uch2.util.Constants;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class GameClientHandler extends ChannelInboundHandlerAdapter {

    private int playerID = -1;
    private NettyKryoDecoder decoder = new NettyKryoDecoder();
    static public GamePhase currentPhase; //TODO quand même c'est un peu abusé là
    private ChannelHandlerContext ctx = null;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if(this.ctx == null) this.ctx = ctx;

        ByteBuf m = (ByteBuf) msg;

        int msgType = m.readChar();
        //System.out.println("CLI : msgType = " + msgType);
        try{
            if(msgType == MessageType.GameStateUpdate.getChar()){
                if(currentPhase == GamePhase.Moving){
                    processGameStateUpdate(m);
                }
//                else System.out.println("CLI: Gamestate Recu mais on est en phase de placement");
            }
            else if(msgType == MessageType.GameStart.getChar()){
                processGameStart(m);
            }
            else if(msgType == MessageType.BlockPlaced.getChar()) {
                processBlockPlacement(m);
            }
            else if(msgType == MessageType.AckBlockPlaced.getChar()) {
                processAckBlockPlaced();
            }
            else if(msgType == MessageType.StartMovementPhase.getChar()) {
                startMovementPhase();
            }
            //else if(msgType == MessageType.StartEditingPhase.getChar()) {
            //    startEditingPhase();
            //}
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
//        System.out.println("CLI: Gamestate reçu par le client :" + objects.get(0).toString());
    }

    private void processGameStart(ByteBuf m){

        playerID = m.readInt();
        ClientPlayerStateTickManager.getInstance().setPlayerID(playerID);
        OnlinePlayerManager.getInstance().init(playerID);
        System.out.println("CLI: PlayerID = " + playerID);
        startSending(ctx);

        //Sends an ACK to the server meaning the client recieved it's ID
        ByteBuf out = Unpooled.buffer(128);
        out.writeChar(MessageType.AckGameStart.getChar());
        //out.writeInt(playerID);
        ctx.writeAndFlush(out);
    }

    private void processBlockPlacement(ByteBuf m){
        List<Object> objects = new ArrayList<>();
        decoder.decode(m, objects);
        ObjectPlacement op = (ObjectPlacement) objects.get(0);
        System.out.println("CLI: placement de bloc recu avec ID = " + op.getPlayerID() + " , block = " + (op.getBlock() == null? "null":"NOT null"));

        if(op.getBlock() == null) {
            startEditingPhase();
        }else{
            World.currentWorld.placeBlock(op.getBlock());
            System.out.println("CLI: placement du bloc reçu");
        }
        if(op.getPlayerID() == playerID){
            ClientPlayerStateTickManager.getInstance().setCanPlace(true);
        }else if(op.getPlayerID() == -1){
            startMovementPhase();
        }
    }

    private void processAckBlockPlaced(){
        ClientPlayerStateTickManager.getInstance().setRecievedAck(true);
    }

    private void startSending(ChannelHandlerContext ctx){
        ClientPlayerStateTickManager.getInstance().setContext(ctx);
        ClientPlayerStateTickManager.getInstance().setCurrentState(new PlayerState(playerID, 20, 30, 0));
        ClientPlayerStateTickManager.getInstance().start(1000, Constants.TICK_DURATION);
    }


    private void startMovementPhase(){
        //TODO Permettre aux personnages de bouger
        currentPhase = GamePhase.Moving;
        System.out.println("CLI: START MOVEMENT PHASE");
    }

    private void startEditingPhase(){
        //TODO interdire aux personnages de bouger
        currentPhase = GamePhase.Editing;
        ClientPlayerStateTickManager.getInstance().setCanPlace(false);
        System.out.println("CLI: START EDITING PHASE");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
