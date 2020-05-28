package com.gdx.uch2.networking.server;

import com.gdx.uch2.networking.PlayerContext;
import com.gdx.uch2.networking.messages.MessageType;

import java.io.IOException;

/**
 * Classe représentant la connexion du serveur avec un client en particulier
 */
public class PlayerHandler implements Runnable {

    private CentralGameManager manager;
    private PlayerContext context;

    /**
     * Constructeur
     * @param manager Gestionnaire de la partie
     * @param context Contexte de la connexion
     */
    public PlayerHandler(CentralGameManager manager, PlayerContext context) {
        this.manager = manager;
        this.context = context;
    }


    @Override
    public void run() {
        MessageType type;

        while(!manager.isOver()){

            type = context.in.getType();

            if(type != null && type != MessageType.CloseConnection){
                manager.readMessage(type, context);
            }else {
                GameServer.players[context.getId()] = null;
                context.out.writeMessage(MessageType.CloseConnection);
                break;
            }

        }

        if (!manager.isOver()) {
            manager.disconnectedClient();
        }

        clean();
    }

    private void clean() {
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
    }
}
