package com.gdx.uch2.networking2;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class GameClient {

    public void connect(){
        try{
            Socket srv = new Socket("localhost", 12345);
            DataInputStream is = new DataInputStream(new BufferedInputStream(srv.getInputStream()));
            DataOutputStream os = new DataOutputStream(srv.getOutputStream());

            float f = 1000;
            os.writeFloat(f);
            os.flush();// send a byte to the server
            f = is.readFloat();

            System.out.println(f);

            srv.close();
            is.close();
            os.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
