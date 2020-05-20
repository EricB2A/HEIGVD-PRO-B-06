package com.gdx.uch2.networking.kryo;

import com.gdx.uch2.entities.Block;
import com.gdx.uch2.networking.GameState;
import com.gdx.uch2.networking.ObjectPlacement;
import com.gdx.uch2.networking.PlayerIDGiver;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

public class NettyKryoDecoder {

	Kryo kryo;

	public NettyKryoDecoder() {
		this.kryo = new Kryo();
		this.kryo.register(GameState.class);
		this.kryo.register(ObjectPlacement.class);
		this.kryo.register(Block.class);
		this.kryo.register(PlayerIDGiver.class);
	}

	public void decode( ByteBuf msg, List<Object> out) {

		if(kryo == null) kryo = new Kryo();
		int length = msg.readableBytes();
		
		if(length == 0) return ;
		
		Input input = null;
		ByteBuf msgCopy = null;
		try {
			byte[] bytes = new byte[length];
			msgCopy = msg.copy();
			msgCopy.readBytes(bytes);
			//msg.readBytes(msg.readableBytes());
			input = new Input(bytes);
			out.add(kryo.readClassAndObject(input));
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally {
			if(msgCopy != null)
				msgCopy.release();

			if(input != null){
				input.close();
			}

		}
	        
	}

}
