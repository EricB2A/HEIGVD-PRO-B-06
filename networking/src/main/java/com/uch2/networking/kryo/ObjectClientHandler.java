package com.uch2.networking.kryo;

import com.uch2.networking.GameState;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ObjectClientHandler extends SimpleChannelInboundHandler<GameState> {

	@Override
    public void channelActive(ChannelHandlerContext ctx) {
		System.out.println("CLIENT:: channelActive N send message");
		GameState state = new GameState(4, 4, 4);
		/*
		User user = new User();
		user.setId("fbwotjq");
		user.setName("홍길동");
/*		user.getHistorySet().add("의적");
		user.getHistorySet().add("부자");
		user.getHistorySet().add("도둑");
		*/
		ctx.write(state);

    }
	
	@Override
	public void channelRead0(ChannelHandlerContext ctx, GameState msg) throws Exception {
		//System.out.println(String.format("CLIENT::channelRead0 : %s, %s", msg.getId(), msg.getName()));
		GameState state = new GameState(3, 3, 3);
/*		user.getHistorySet().add("의적1");
		user.getHistorySet().add("부자1");
		user.getHistorySet().add("도둑1");*/
		/*
		if(Client.requestIndex == Client.requestEndIndex) {
			System.out.println(System.currentTimeMillis()-Client.startPoint);
			System.exit(-1);
		} else {
			Client.requestIndex++;
		}
		*/
		
		//System.out.println("client write");
		//ctx.write(user);
		
	}

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	System.out.println("exceptionCaught");
        cause.printStackTrace();
        ctx.close();
    }
    
}
