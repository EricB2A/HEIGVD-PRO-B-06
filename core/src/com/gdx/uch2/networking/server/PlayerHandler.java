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
        boolean shouldRun = true;
        MessageType type;

        try {

            while((shouldRun)){ // TODO : vraie condition d'arrêt

                type = context.in.getType();

                if(type != null){
                    manager.readMessage(type, context);
                }else break;

            }

            context.in.close();
            context.out.close();
            context.getSocket().close();

        } catch (IOException ex) {
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
                    ex.printStackTrace();
                }
            }
        }
    }
}
