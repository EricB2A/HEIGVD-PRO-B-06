package com.gdx.uch2.networking.kryo;

import com.gdx.uch2.entities.Block;
import com.gdx.uch2.networking.GameState;
import com.gdx.uch2.networking.ObjectPlacement;
import io.netty.buffer.ByteBuf;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

import java.util.concurrent.Semaphore;

public class NettyKryoEncoder {
	
	Kryo kryo;
	private Semaphore kryoAccessMutex = new Semaphore(1);

	public NettyKryoEncoder() {
		this.kryo = new Kryo();
		this.kryo.register(GameState.class);
		this.kryo.register(ObjectPlacement.class);
		this.kryo.register(Block.class);
	}

	public void encode(Object msg, ByteBuf out, char prelude) {

        Output output = new Output(200);
        output.writeChar(prelude);
        try {
        	kryoAccessMutex.acquire();
	        kryo.writeClassAndObject(output, msg);
	        kryoAccessMutex.release();
	        output.flush();  
	        output.close();
        } catch (Exception e){
        	e.printStackTrace();
        }
        
        byte[] byteArray = output.toBytes();
        out.writeBytes(byteArray);
	}


}