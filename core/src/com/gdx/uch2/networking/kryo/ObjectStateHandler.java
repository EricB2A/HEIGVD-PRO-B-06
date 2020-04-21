package com.gdx.uch2.networking.kryo;

import com.gdx.uch2.networking.PlayerState;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ObjectStateHandler extends SimpleChannelInboundHandler<PlayerState> {

	@Override
    public void channelActive(ChannelHandlerContext ctx) {
		System.out.println("STATE:: channelActive N send message");
		PlayerState state = new PlayerState(4, 4, 4);
		ctx.write(state);

    }
	
	@Override
	public void channelRead0(ChannelHandlerContext ctx, PlayerState msg) throws Exception {
		//System.out.println(String.format("CLIENT::channelRead0 : %s, %s", msg.getId(), msg.getName()));
		System.out.println("STATE::channelRead0" + msg);
		PlayerState state = new PlayerState(3, 3, 3);
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
