public class Main {

    public static void main(String[] args) {
        GameServer srv = new GameServer();

        GameClient cli = new GameClient();

        srv.serveClients();
    }

}
