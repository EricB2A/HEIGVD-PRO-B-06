import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
            BufferedReader in = null;
            PrintWriter out = null;

            public ServantWorker(Socket clientSocket) {
                try {
                    this.clientSocket = clientSocket;
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new PrintWriter(clientSocket.getOutputStream());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void run() {
                String line;
                boolean shouldRun = true;

                try {
                    while ((shouldRun) && (line = in.readLine()) != null) {
                        if (line.equalsIgnoreCase("bye")) {
                            shouldRun = false;
                        }
                        out.println("> " + line.toUpperCase());
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