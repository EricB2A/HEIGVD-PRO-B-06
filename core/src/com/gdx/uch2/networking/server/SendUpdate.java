package com.gdx.uch2.networking.server;


import com.gdx.uch2.networking.PlayerContext;
import com.gdx.uch2.networking.messages.GameState;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TimerTask envoyant le GameState à tous les joueurs régulièrement
 */
public class SendUpdate extends TimerTask {
    private PlayerContext[] players;
    private Timer timer;

    /**
     * Constructeur
     * @param timer timer de l'action
     * @param players Contextes de tous les joueurs qui doivent recevoir les mises à jour de gameState
     */
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
