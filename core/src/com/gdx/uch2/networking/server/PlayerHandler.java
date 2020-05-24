package com.gdx.uch2.networking.server;

import com.gdx.uch2.networking.*;

import java.io.IOException;

public class PlayerHandler implements Runnable {

    private CentralGameManager manager;
    private PlayerContext context;

    public PlayerHandler(CentralGameManager manager, PlayerContext context) {
        this.manager = manager;
        this.context = context;
    }


    @Override
    public void run() {
        MessageType type;

        while(!manager.isOver()){

            type = context.in.getType();

            if(type != null){
                manager.readMessage(type, context);
            }else {
                System.out.println("SRV: connexion fermée pour le joueur #" + context.getId());
                break;
            }

        }

        if (context.in != null) {
            context.in.close();
        }
        if (context.out != null) {
            context.out.close();
        }
        if (context.getSocket() != null) {
            try {
                context.getSocket().close();
            } catch (IOException ex1) {
                ex1.printStackTrace();
            }
        }

        if (!manager.isOver()) {
            manager.disconnectedClient(context);
        }
    }
}
