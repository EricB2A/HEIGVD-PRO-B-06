package com.gdx.uch2.networking.client;

import com.gdx.uch2.entities.Block;
import com.gdx.uch2.networking.MessageType;
import com.gdx.uch2.networking.ObjectPlacement;
import com.gdx.uch2.networking.PlayerState;
import com.gdx.uch2.networking.UserActionSequence;
import com.gdx.uch2.networking.kryo.NettyKryoDecoder;
import com.gdx.uch2.networking.kryo.NettyKryoEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.util.Timer;

import static io.netty.buffer.Unpooled.buffer;

public class ClientPlayerStateTickManager {
    private static class Instance{
        static final ClientPlayerStateTickManager instance = new ClientPlayerStateTickManager();
    }

    NettyKryoEncoder encoder = new NettyKryoEncoder();

    private Timer timer;
    private ChannelHandlerContext ctx;
    private PlayerState currentState;
    private int playerID = -1;
    private boolean canPlace;
    private boolean recievedAck = false;


    public boolean getRecievedAck() {
        return recievedAck;
    }

    public void setRecievedAck(boolean recievedAck) {
        this.recievedAck = recievedAck;
    }

    public boolean getCanPlace() {
        return canPlace;
    }

    public void setCanPlace(boolean canPlace) {
        this.canPlace = canPlace;
        System.out.println("CLI: set Can place = " + canPlace);
    }



    private ClientPlayerStateTickManager(){
    }

    public static ClientPlayerStateTickManager getInstance(){
        return ClientPlayerStateTickManager.Instance.instance;
    }

    public PlayerState getCurrentState(){
        return currentState;
    }

    public void setCurrentState(PlayerState newState){
        this.currentState = newState;
    }

    public void setContext(ChannelHandlerContext ctx){
        this.ctx = ctx;
    }

    public void setPlayerID(int playerID){
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }


    public void sendFinish(){
        ByteBuf out = Unpooled.buffer(128);
        out.writeChar(MessageType.ReachedEnd.getChar());
        out.writeInt(currentState.getPlayerID());
        ctx.channel().writeAndFlush(out);
    }

    //TODO placer dans un endroit plus évident ou renommer la classe
    public void sendBlockPlacement(final Block block){
        setCanPlace(false);


        new Runnable(){

            @Override
            public void run() {
                ObjectPlacement op = new ObjectPlacement(playerID, block);
                ByteBuf out = buffer(1024);
                encoder.encode(op, out, MessageType.BlockPlaced.getChar());
                while(!getRecievedAck()){
                    try{
                        ctx.writeAndFlush(out);
                        System.out.println("CLI: Tentative d'envoi de BlocPlacement...");
                        Thread.sleep(100);
                    }catch (InterruptedException ex){
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("CLI: BlocPlacement envoyé avec succes!");
                }
                setRecievedAck(false);
            }
        }.run();

        System.out.println("CLI: Sending block placement as player #" + playerID);
        ObjectPlacement op = new ObjectPlacement(playerID, block);
        ByteBuf out = Unpooled.buffer(2048);
        encoder.encode(op, out, MessageType.BlockPlaced.getChar());
        ctx.writeAndFlush(out);
    }

    //Crée le timer et envoie régulièrement une séquence d'acitons au serveur.
    public void start(int delay, int tickDuration){
        this.timer = new Timer();
        timer.schedule(new sendPlayerState(ctx), delay, tickDuration);
    }

}
