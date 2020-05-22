package com.gdx.uch2.networking2.serialization;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.Semaphore;

public class MyOuputStream {
    private Semaphore mutex;
    private DataOutputStream stream;

    public MyOuputStream(DataOutputStream stream) {
        this.mutex = new Semaphore(1);
        this.stream = stream;
    }

    public void writeMessage(MessageType messageType){
        try {
            mutex.acquire();
            try {
                stream.writeChar(messageType.getChar());
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
