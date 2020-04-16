package com.uch2.networking.client;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RunClient {
    public static void main(String[] args) throws Exception {
        GameClient c = new GameClient();
        GameClient d = new GameClient();
        GameClient e = new GameClient();
        GameClient f = new GameClient();

        List<GameClient> clients = new LinkedList<GameClient>();
        clients.add(c);
        clients.add(d);
        clients.add(e);
        clients.add(f);


        for(GameClient cli : clients){
            System.out.println("Tentative de connexion");
            new Thread(cli).start();
            TimeUnit.SECONDS.sleep(2);
        }

    }
}
