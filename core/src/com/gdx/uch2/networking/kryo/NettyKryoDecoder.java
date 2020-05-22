package com.gdx.uch2.networking.kryo;

import com.gdx.uch2.entities.Block;
import com.gdx.uch2.networking.GameState;
import com.gdx.uch2.networking.ObjectPlacement;
import io.netty.buffer.ByteBuf;

import java.util.List;
import java.util.concurrent.Semaphore;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

public class NettyKryoDecoder {

	Kryo kryo;
	private Semaphore kryoAccessMutex = new Semaphore(1);

	public NettyKryoDecoder() {
		this.kryo = new Kryo();
		this.kryo.register(GameState.class);
		this.kryo.register(ObjectPlacement.class);
		this.kryo.register(Block.class);
	}

	public boolean decode( ByteBuf msg, List<Object> out) {

		int length = msg.readableBytes();
		boolean ret = true;

		if(length == 0) return false;

		Input input = null;
		ByteBuf msgCopy = null;
		try {
			kryoAccessMutex.acquire();
			byte[] bytes = new byte[length];
			msgCopy = msg.copy();
			msgCopy.readBytes(bytes);
			//msg.readBytes(msg.readableBytes());
			input = new Input(bytes);

			out.add(kryo.readClassAndObject(input));
			kryoAccessMutex.release();
		}
		catch(Exception ex){
			ex.printStackTrace();
			ret = false;
		}
		finally {
			if(msgCopy != null)
				msgCopy.release();

			if(input != null){
				input.close();
			}

		}

		return ret;
	        
	}

}
