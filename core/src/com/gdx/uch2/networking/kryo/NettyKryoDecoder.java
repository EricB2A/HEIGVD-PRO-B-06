package com.gdx.uch2.networking.kryo;

import com.gdx.uch2.networking.GameState;
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
	}

	public void decode( ByteBuf msg, List<Object> out) {

		if(kryo == null) kryo = new Kryo();
		int length = msg.readableBytes();
		
		if(length == 0) return ;
		
		Input input = null;
		try {
			byte[] bytes = new byte[msg.readableBytes()];
			msg.readBytes(bytes);
			input = new Input(300);
			input.setBuffer(bytes);
			out.add(kryo.readClassAndObject(input));
		} finally {
			if(input != null){
				input.close();
			}

		}
	        
	}

}
