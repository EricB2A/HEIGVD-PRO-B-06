package com.gdx.uch2.networking.server;

import com.gdx.uch2.controller.LevelLoader;
import com.gdx.uch2.entities.Level;
import com.gdx.uch2.networking.GameState;
import com.gdx.uch2.networking.MessageType;
import com.gdx.uch2.networking.PlayerContext;
import com.gdx.uch2.networking.client.ErrorHandler;
import com.gdx.uch2.util.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameServer implements Runnable {
    //2 premiers joueurs à se connecter.
    private PlayerContext[] players;

    //indique si la partie est pleine
    private boolean full = false;
    private boolean gameStarted = false;

    private int port;
    private Level level;
    private int numlevel;
    private int nbPlayers;
    private int nbRounds;

    public GameServer(int port, int noLevel, int nbPlayers, int nbRounds){
        this.port = port;
        this.numlevel = noLevel;
        this.level = LevelLoader.loadLevel(noLevel);
        this.nbPlayers = nbPlayers;
        players = new PlayerContext[nbPlayers];
        this.nbRounds = nbRounds;
    }

    @Override
    public void run() {
        System.out.println("Start server");
        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            ErrorHandler.getInstance().setError(ex.toString());
            return;
        }

        int id = -1;
        while (!full) {
            try {
                Socket clientSocket = serverSocket.accept();

                for (int i = 0; i < players.length; ++i) {
                    if (players[i] != null) {
                        players[i].out.writeMessage(MessageType.Ping);
                        System.out.println(i + " : " + players[i].in.getType());
                        if (players[i].in.e != null) {
                            if (id < 0) {
                                id = i;
                            } else {
                                players[i] = null;
                            }
                        }
                    } else if (id < 0) {
                        id = i;
                    }
                }

                PlayerContext ctx = new PlayerContext(id, clientSocket);
                players[id] = ctx;

                if(id == nbPlayers - 1){
                    full = true;
                    serverSocket.close();
                    startGame();
                }

                id = -1;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    private void startGame(){
        if(players.length > nbPlayers) throw new RuntimeException("Nombre de joueurs trop élevé");

        gameStarted = true;
        System.out.println(players.length + " joueurs connectés. Lancer la partie.");

        CentralGameManager manager = new CentralGameManager(players, level, nbRounds);

        for (PlayerContext player : players) {
            Thread t = new Thread(new PlayerHandler(manager, player));
            t.start();
        }

        //Notifie les joueurs et ajoute un MovementHandler aux connexions avec les joueurs
        for(PlayerContext ctx : players){

            ctx.out.writeMessage(MessageType.GameStart);
            ctx.out.writeMessage(ctx.getId());
            ctx.out.writeMessage(numlevel);

            System.out.println("Message envoyé au joueur #" + ctx.getId());
        }

        //Démarre les ticks de serveur
        ServerGameStateTickManager.getInstance().setPlayers(players);
        ServerGameStateTickManager.getInstance().start(1000, Constants.TICK_DURATION);
    }

    public static void main(String[] args) throws Exception {
        int port = 12345;
        int level = 1;

        new GameServer(port, level,2 ,10).run();
    }

}
