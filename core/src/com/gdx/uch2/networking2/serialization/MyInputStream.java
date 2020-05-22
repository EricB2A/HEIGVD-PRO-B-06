package com.gdx.uch2.networking2.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Semaphore;

public class MyInputStream {

    private Semaphore mutex;
    private InputStream stream;

    public MyInputStream(InputStream stream) {
        this.mutex = new Semaphore(1);
        this.stream = stream;
    }

    public void read(){
        try {
            mutex.acquire();
            try {
                char message = (char) stream.read();
                System.out.println("Message reçu :" + message);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
