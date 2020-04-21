package com.gdx.uch2.networking.kryo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

public class NettyKryoDecoder extends ByteToMessageDecoder {

	Kryo kryo;
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		
		if(kryo == null) kryo = new Kryo();
		int length = msg.readableBytes();
		
		if(length == 0) return ;
		
		Input input = null;
		try {
			byte[] bytes = new byte[msg.readableBytes()];
			msg.readBytes(bytes);
			// System.out.println(new String(bytes));
			input = new Input(300);
			input.setBuffer(bytes);
			out.add(kryo.readClassAndObject(input));
		} finally {  
			input.close();
		}
	        
	}

}
