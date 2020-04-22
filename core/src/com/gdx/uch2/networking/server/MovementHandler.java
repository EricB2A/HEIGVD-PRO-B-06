package com.gdx.uch2.networking.server;

import com.gdx.uch2.networking.GameState;
import com.gdx.uch2.networking.UserAction;
import com.gdx.uch2.networking.UserActionSequence;
import com.gdx.uch2.networking.kryo.NettyKryoDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.ArrayList;
import java.util.List;

public class MovementHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf m = (ByteBuf) msg;
        if(m.readChar() == 'b'){
            NettyKryoDecoder nettyKryoDecoder = new NettyKryoDecoder();
            List<Object> objects = new ArrayList<>();
            try {
                while (m.isReadable()) {
                    nettyKryoDecoder.decode((ByteBuf) msg, objects);
                    System.out.flush();
                }
                System.out.println("sequence d'actions reçue");
                applyActions((UserActionSequence) objects.get(0), ServerGameStateTickManager.getInstance().getGameState());
            } finally {
                ReferenceCountUtil.release(msg);
            }
        }else{
            System.out.println("pas une séquence d'actions");
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
}
