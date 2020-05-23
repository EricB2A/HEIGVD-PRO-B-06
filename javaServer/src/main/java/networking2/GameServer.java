package networking2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {

    private final int PORT = 12345;

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
            DataInputStream in = null;
            DataOutputStream out = null;

            public ServantWorker(Socket clientSocket) {
                try {
                    this.clientSocket = clientSocket;
                    in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                    out = new DataOutputStream(clientSocket.getOutputStream());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void run() {
                String line;
                boolean shouldRun = true;

                try {
                    while ((shouldRun)) {
                        float f = in.readFloat();
                        if (f == -1) {
                            shouldRun = false;
                        }
                        out.writeFloat(f + 42f);
                        out.flush();
                    }

                    in.close();
                    out.close();
                    clientSocket.close();

                } catch (IOException ex) {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException ex1) {
                            ex.printStackTrace();
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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