package com.gdx.uch2.networking.client;

import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.entities.OnlinePlayerManager;
import com.gdx.uch2.entities.World;
import com.gdx.uch2.networking.MessageType;
import com.gdx.uch2.networking.PlayerContext;
import com.gdx.uch2.networking.PlayerState;
import com.gdx.uch2.util.Constants;

import java.io.IOException;
import java.net.Socket;

public class GameClient {
    private int port;
    private String hostname;
    private String nickname;

    public GameClient(String hostname, int port, String nickname){
        this.port = port;
        this.hostname = hostname;
        this.nickname = nickname;
        new Thread(new GameClientWorker()).start();
    }

    private class GameClientWorker implements Runnable {
        @Override
        public void run() {
            boolean shouldRun = true;
            MessageType type = null;
            Socket socket = null;
            PlayerContext ctx = null;

            try {
                socket = new Socket(hostname, port);
                ctx = new PlayerContext(socket);

                if (ctx.in.getType() != MessageType.GameStart) {
                    System.out.println("CLI: Message de départ inconnu");
                    ErrorHandler.getInstance().setError("Something went wrong.");
                    return;
                }

                int id = ctx.in.readInt();
                ctx.setId(id);
                processGameStart(ctx);

                GameClientHandler handler = new GameClientHandler(ctx);

                while (type != MessageType.EndGame) {

                    type = ctx.in.getType();

                    if (type != null) {
                        handler.readMessage(type);
                    }

                    if (ctx.in.e != null) {
                        throw ctx.in.e;
                    }
                }
            } catch (IOException e) {
                ErrorHandler.getInstance().setError(e.toString());
            } finally {
                if (socket != null) {
                    if (ctx.in != null) {
                        ctx.in.close();
                    }
                    if (ctx.out != null) {
                        ctx.out.close();
                    }

                    try {
                        socket.close();
                    } catch (IOException ex1) {
                        ex1.printStackTrace();
                    }
                }
            }
        }

        private void processGameStart(PlayerContext ctx) {
            ClientPlayerStateTickManager.getInstance().setPlayerID(ctx.getId());
            OnlinePlayerManager.getInstance().init(ctx.getId());
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
