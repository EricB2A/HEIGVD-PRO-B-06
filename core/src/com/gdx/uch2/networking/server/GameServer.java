package com.gdx.uch2.networking.server;

import com.badlogic.gdx.Game;
import com.gdx.uch2.networking.GameState;
import com.gdx.uch2.networking.PlayerState;
import com.gdx.uch2.networking.kryo.NettyKryoProtocolInitalizer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class GameServer {
    private int port;

    public GameServer(int port){
        this.port = port;
        GameState.setUpKryo();
    }

    public void run() throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new PlayersAmountHandler());
                            ch.pipeline().addLast(new NettyKryoProtocolInitalizer());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
                    //.childHandler(new NettyKryoProtocolInitalizer());


            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } finally{
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 12345;

        new GameServer(port).run();
    }
}
