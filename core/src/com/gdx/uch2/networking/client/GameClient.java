package com.gdx.uch2.networking.client;

import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.entities.OnlinePlayerManager;
import com.gdx.uch2.entities.Player;
import com.gdx.uch2.entities.World;
import com.gdx.uch2.networking.MessageType;
import com.gdx.uch2.networking.PlayerContext;
import com.gdx.uch2.networking.PlayerState;
import com.gdx.uch2.util.Constants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Client du jeu
 */
public class GameClient {
    private final int port;
    private final String hostname;
    private final String nickname;
    private static GameClientWorker worker;

    /**
     * Constructeur publique
     * @param hostname hostname du serveur de la partie à rejoindre
     * @param port port sur lequel communique le serveur de la partie à rejoindre
     * @param nickname nom du joueur
     */
    public GameClient(String hostname, int port, String nickname){
        this.port = port;
        this.hostname = hostname;
        this.nickname = nickname;
        worker = new GameClientWorker();
        Thread t = new Thread(worker);
        t.start();
    }

    private class GameClientWorker implements Runnable {
        private Socket socket;
        private PlayerContext context;
        private boolean closeRequested;

        @Override
        public void run() {
            MessageType type;
            socket = null;
            context = null;

            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(hostname, port), 2000);

                context = new PlayerContext(socket);
                GameClientHandler handler = new GameClientHandler(context);
                context.out.writeMessage(nickname);

                type = context.in.getType();

                if (type == MessageType.CloseConnection) {
                    return;
                } else if (type != MessageType.GameStart) {
                    System.out.println("CLI: Message de départ inconnu");
                    ErrorHandler.getInstance().setError("Something went wrong.");
                    return;
                }

                int id = context.in.readInt();
                context.setId(id);
                processGameStart(context);

                while (type != MessageType.EndGame) {

                    type = context.in.getType();

                    if (type == MessageType.CloseConnection) {
                        break;
                    } else if (type != null) {
                        handler.readMessage(type);
                    }

                    if (context.in.e != null) {
                        throw context.in.e;
                    }
                }
            } catch (IOException e) {
                if (!closeRequested)
                    ErrorHandler.getInstance().setError(e.toString());
            }
            finally {
                System.out.println("CLI: Fermeture de la connexion");
                if (socket != null) {
                    if (context != null) {
                        if (context.in != null) {
                            context.in.close();
                        }
                        if (context.out != null) {
                            context.out.close();
                        }
                    }

                    try {
                        socket.close();
                    } catch (IOException ex1) {
                        ex1.printStackTrace();
                    }
                }
            }
        }

        /**
         * Traite un message reçu de type GameStart
         * @param ctx le contexte du joueur
         */
        private void processGameStart(PlayerContext ctx) {
            MessageSender.getInstance().setPlayerID(ctx.getId());
            OnlinePlayerManager.getInstance().init(ctx.getId(), nickname);
            World.currentWorld = new World(ctx.in.readInt());
            int nbPlayers = ctx.in.readInt();

            for (int i = 0; i < nbPlayers; ++i) {
                OnlinePlayerManager.getInstance().initPlayer(ctx.in.readInt(), ctx.in.readString());
            }

            System.out.println("CLI: PlayerID = " + ctx.getId());

            ctx.out.writeMessage(MessageType.AckGameStart);
            startSending(ctx);
        }

        /**
         * Initialise le MessageSender et commence l'envoi régulier de PlayerStates
         * @param ctx
         */
        private void startSending(PlayerContext ctx) {
            Vector2 pos = World.currentWorld.getLevel().getSpanPosition();
            MessageSender.getInstance().setContext(ctx);
            MessageSender.getInstance().setCurrentState(new PlayerState(ctx.getId(),
                    Player.State.IDLE, pos.x, pos.y, 0));
            MessageSender.getInstance().start(0, Constants.TICK_DURATION);
        }
    }

    /**
     * Envoie un message au serveur pour terminer la connexion
     */
    public static void closeConnection() {
        worker.closeRequested = true;
        worker.context.out.writeMessage(MessageType.CloseConnection);
    }
}
