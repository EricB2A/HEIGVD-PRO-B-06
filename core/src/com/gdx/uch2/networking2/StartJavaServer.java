package com.gdx.uch2.networking2;

public class StartJavaServer {

    public static void main(String[] args) {
        GameServer srv = new GameServer();
        srv.serveClients();

    }

}
