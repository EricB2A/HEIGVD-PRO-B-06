package com.gdx.uch2.networking.kryo;

import com.gdx.uch2.networking.GameState;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

public class NettyKryoEncoder {
	
	Kryo kryo;

	public NettyKryoEncoder() {
		this.kryo = new Kryo();
		this.kryo.register(GameState.class);

	}

	public void encode(Object msg, ByteBuf out) {
		
		if(kryo == null) kryo = new Kryo();

        Output output = new Output(200);
        try { 
	        kryo.writeClassAndObject(output, msg);
	        output.flush();  
	        output.close();
        } catch (Exception e){
        	e.printStackTrace();
        }
        
        byte[] byteArray = output.toBytes();
        out.writeBytes(byteArray);

        /*
        ctx.write(Unpooled.copiedBuffer(out));
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);
         */
	}


}