package com.gdx.uch2.networking.client;

import com.gdx.uch2.networking.GameState;
import com.gdx.uch2.networking.kryo.NettyKryoDecoder;
import com.gdx.uch2.networking.kryo.NettyKryoEncoder;
import com.gdx.uch2.networking.kryo.ObjectStateHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class GameClient implements Runnable {
    public void run(){
        String host = "localhost";
        int port = 12345;
        GameState.setUpKryo();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                public void initChannel(SocketChannel ch) throws Exception{
                    ch.pipeline().addLast(new GameClientHandler());
                    ch.pipeline().addLast(new NettyKryoDecoder());
                    ch.pipeline().addLast(new NettyKryoEncoder());
                    ch.pipeline().addLast(new ObjectStateHandler());
                }
            });

            ChannelFuture f = b.connect(host, port).sync();

            f.channel().closeFuture().sync();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        finally {
            workerGroup.shutdownGracefully();
        }
    }

}
