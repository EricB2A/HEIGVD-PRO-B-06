package com.gdx.uch2.networking.server;

import com.gdx.uch2.networking.GameState;
import com.gdx.uch2.networking.PlayerState;
import com.gdx.uch2.networking.UserAction;
import com.gdx.uch2.networking.UserActionSequence;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;

import java.util.List;

public class MovementHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //TODO désérialiser msg (ça se fait tout seul depuis le pipeline?)
        if(!(msg instanceof UserActionSequence)) return;

        applyActions((UserActionSequence) msg, TickManager.getInstance().getGameState());
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
