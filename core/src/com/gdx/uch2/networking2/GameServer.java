package com.gdx.uch2.networking2;

import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.entities.Block;
import com.gdx.uch2.networking.GameState;
import com.gdx.uch2.networking.ObjectPlacement;
import com.gdx.uch2.networking.PlayerState;
import com.gdx.uch2.networking2.serialization.MessageType;
import com.gdx.uch2.networking2.serialization.MyInputStream;
import com.gdx.uch2.networking2.serialization.MyOuputStream;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class GameServer {

    private final int PORT = 12345;

    private int protectedStuff = 0;
    private Semaphore mutex = new Semaphore(1);

    public void serveClients() {
        new Thread(new ReceptionistWorker()).start();
    }

    private class ReceptionistWorker implements Runnable {

        @Override
        public void run() {
            ServerSocket serverSocket;

            try {
                serverSocket = new ServerSocket(PORT);
            } catch (IOException ex) {
                ex.printStackTrace();
                return;
            }

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(new ServantWorker(clientSocket)).start();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        }

        private class ServantWorker implements Runnable {

            Socket clientSocket;
            MyInputStream in = null;
            MyOuputStream out = null;

            public ServantWorker(Socket clientSocket) {
                try {
                    this.clientSocket = clientSocket;
                    in = new MyInputStream(clientSocket.getInputStream());
                    out = new MyOuputStream(new DataOutputStream(clientSocket.getOutputStream()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void run() {
                boolean shouldRun = true;

                try {

                    while((shouldRun)){
                        MessageType type = in.getType();

                        switch (type){
                            case GameStateUpdate:
                                GameState gs = in.readGameState();
                                System.out.println("Gamestate lu : " + gs.toString());

                                try {
                                    mutex.acquire();
                                    protectedStuff = 1000;
                                    mutex.release();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("protected stuff (should be 1000): " + protectedStuff);

                                Block b = new Block(new Vector2(5, 5));
                                ObjectPlacement op = new ObjectPlacement(3, b);
                                out.writeMessage(op);

                                break;
                            case PlayerStateUpdate:
                                PlayerState ps = in.readPlayerState();
                                System.out.println("PlayerState lu : " + ps.toString());

                                try {
                                    mutex.acquire();
                                    protectedStuff = 200;
                                    mutex.release();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                System.out.println("protected stuff (should be 200): " + protectedStuff);

                                break;
                            default:
                                break;
                        }
                    }


                    in.close();
                    out.close();
                    clientSocket.close();

                } catch (IOException ex) {
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                    if (clientSocket != null) {
                        try {
                            clientSocket.close();
                        } catch (IOException ex1) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}