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

    private Timer timer;
    private ChannelHandlerContext ctx;
    private PlayerState currentState;
    private int playerID = -1;

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

    /*
    public void setHasFinished(boolean value){
        hasFinished = value;
    }

    public boolean hasFinished(){
        return hasFinished;
    }
    */

    public void sendFinish(){
        ByteBuf out = Unpooled.buffer(128);
        out.writeChar(MessageType.ReachedEnd.getChar());
        out.writeInt(currentState.getPlayerID());
        ctx.channel().writeAndFlush(out);
    }

    //TODO placer dans un endroit plus évident ou renommer la classe
    public void sendBlockPlacement(Block block){
        ObjectPlacement op = new ObjectPlacement(playerID, block, false); //TODO trap?
        ByteBuf out = Unpooled.buffer(1024);
        NettyKryoEncoder encoder = new NettyKryoEncoder();
        encoder.encode(op, out, MessageType.BlockPlaced.getChar());
        ctx.channel().writeAndFlush(out);
    }

    //Crée le timer et envoie régulièrement une séquence d'acitons au serveur.
    public void start(int delay, int tickDuration){
        this.timer = new Timer();
        timer.schedule(new sendPlayerState(ctx), delay, tickDuration);
    }

}
