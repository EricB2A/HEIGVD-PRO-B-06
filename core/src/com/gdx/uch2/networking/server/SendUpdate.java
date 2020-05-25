package com.gdx.uch2.networking.server;


import com.gdx.uch2.networking.GameState;
import com.gdx.uch2.networking.PlayerContext;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SendUpdate extends TimerTask {
    private PlayerContext[] players;
    private Timer timer;

    public SendUpdate(Timer timer, PlayerContext[] players){
        this.timer = timer;
        this.players = players;
    }

    @Override
    public void run() {
        GameState gs = ServerGameStateTickManager.getInstance().getGameState();
        for(PlayerContext ctx : players){
            ctx.out.writeMessage(gs);

            if (ctx.out.e != null) {
                System.out.println("SRV: Worker fermé");

                timer.cancel();
                timer.purge();
            }
        }
    }
}
