package com.gdx.uch2.networking2;

import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.entities.Block;
import com.gdx.uch2.networking.GameState;
import com.gdx.uch2.networking.MessageType;
import com.gdx.uch2.networking.ObjectPlacement;
import com.gdx.uch2.networking.PlayerState;
import com.gdx.uch2.networking2.serialization.MyInputStream;
import com.gdx.uch2.networking2.serialization.MyOuputStream;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class GameClient {

    public void connect(){
        try{
            Socket srv = new Socket("localhost", 12345);
            MyInputStream in = new MyInputStream(srv.getInputStream());
            MyOuputStream out = new MyOuputStream(srv.getOutputStream());


            PlayerState ps1 = new PlayerState(1, 10, 20, System.nanoTime());
            PlayerState ps2 = new PlayerState(2, 100, 200, System.nanoTime());

            GameState gs = new GameState(new PlayerState[]{ps1, ps2});



            System.out.println("Sending ps1");
            out.writeMessage(ps1);
            System.out.println("Sending ps2");
            out.writeMessage(ps2);
            System.out.println("Sending gs");
            out.writeMessage(gs);

            out.writeMessage(MessageType.GameStart);
            out.writeMessage(345);

            out.writeMessage(new ObjectPlacement(23, new Block(new Vector2(3,4))));
            out.writeMessage(new ObjectPlacement(532, null));

            out.flush();

            System.out.println("sent all and flushed");

            Thread.sleep(1000);

            srv.close();
            in.close();
            out.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
