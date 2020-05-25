package com.gdx.uch2.networking.client;

import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.entities.OnlinePlayerManager;
import com.gdx.uch2.entities.World;
import com.gdx.uch2.networking.MessageType;
import com.gdx.uch2.networking.PlayerContext;
import com.gdx.uch2.networking.PlayerState;
import com.gdx.uch2.util.Constants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class GameClient {
    private final int port;
    private final String hostname;
    private final String nickname;
    public static PlayerContext context = null;
    public static Thread thread = null;

    public GameClient(String hostname, int port, String nickname){
        this.port = port;
        this.hostname = hostname;
        this.nickname = nickname;
        Thread t = new Thread(new GameClientWorker());
        t.start();
        thread = t;
    }

    private class GameClientWorker implements Runnable {
        @Override
        public void run() {
            MessageType type;
            Socket socket = null;
            context = null;

            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(hostname, port), 2000);

                context = new PlayerContext(socket);
                context.out.writeMessage(nickname);

                while((type = context.in.getType()) == MessageType.Ping) {
                    context.out.writeMessage(MessageType.Ping);

                }

                if (type != MessageType.GameStart) {
                    System.out.println("CLI: Message de départ inconnu");
                    ErrorHandler.getInstance().setError("Something went wrong.");
                    return;
                }

                int id = context.in.readInt();
                context.setId(id);
                processGameStart(context);

                GameClientHandler handler = new GameClientHandler(context);

                while (type != MessageType.EndGame) {

                    type = context.in.getType();

                    if (type != null) {
                        handler.readMessage(type);
                    }

                    if (context.in.e != null) {
                        throw context.in.e;
                    }
                }
            } catch (IOException e) {
                ErrorHandler.getInstance().setError(e.toString());
            }
            finally {
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

            thread = null;
        }

        private void processGameStart(PlayerContext ctx) {
            ClientPlayerStateTickManager.getInstance().setPlayerID(ctx.getId());
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

        private void startSending(PlayerContext ctx) {
            Vector2 pos = World.currentWorld.getLevel().getSpanPosition();
            ClientPlayerStateTickManager.getInstance().setContext(ctx);
            ClientPlayerStateTickManager.getInstance().setCurrentState(new PlayerState(ctx.getId(),
                    pos.x, pos.y, 0));
            ClientPlayerStateTickManager.getInstance().start(0, Constants.TICK_DURATION);
        }
    }
}
