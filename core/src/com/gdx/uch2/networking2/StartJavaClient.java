package com.gdx.uch2.networking2;

public class StartJavaClient {
    public static void main(String[] args) {
        GameClient cli = new GameClient();
        cli.connect();
    }
}
