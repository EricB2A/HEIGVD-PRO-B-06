package com.gdx.uch2.networking.server;


import com.gdx.uch2.networking.GameState;
import com.gdx.uch2.networking.PlayerContext;

import java.util.List;
import java.util.TimerTask;

public class SendUpdate extends TimerTask {
    //TODO: Eric Broadcast
    private List<PlayerContext> players;

    public SendUpdate(List<PlayerContext> players){  //TODO remplacer Object toSend par un GameState
        this.players = players;
    }

    @Override
    public void run() {
        GameState gs = ServerGameStateTickManager.getInstance().getGameState();
        for(PlayerContext ctx : players){
            ctx.out.writeMessage(gs);
        }
    }
}
