package com.gdx.uch2.networking.kryo;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class NettyKryoProtocolInitalizer extends ChannelInitializer<SocketChannel> {
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		//pipeline.addLast("decoder", new NettyKryoDecoder());
		//pipeline.addLast("encoder", new NettyKryoEncoder());
		//pipeline.addLast("nettyKryoHandler", new NettyKryoHandler());
		
	}

}