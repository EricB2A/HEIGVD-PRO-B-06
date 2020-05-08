package com.gdx.uch2.networking.client;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RunClient {
    public static void main(String[] args) throws Exception {
        GameClient c = new GameClient("localhost", 12345);

        c.run();

    }
}
