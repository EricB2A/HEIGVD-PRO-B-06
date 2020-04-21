package com.uch2.networking.kryo;

import com.uch2.networking.GameState;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ObjectStateHandler extends SimpleChannelInboundHandler<GameState> {

	@Override
    public void channelActive(ChannelHandlerContext ctx) {
		System.out.println("STATE:: channelActive N send message");
		GameState state = new GameState(4, 4, 4);
		ctx.write(state);

    }
	
	@Override
	public void channelRead0(ChannelHandlerContext ctx, GameState msg) throws Exception {
		//System.out.println(String.format("CLIENT::channelRead0 : %s, %s", msg.getId(), msg.getName()));
		System.out.println("STATE::channelRead0" + msg);
		GameState state = new GameState(3, 3, 3);
		//System.out.println("client write");
		//ctx.write(state);
	}

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	System.out.println("exceptionCaught");
        cause.printStackTrace();
        ctx.close();
    }
    
}
