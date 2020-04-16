package com.uch2.networking.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class PlayersAmountHandler extends ChannelInboundHandlerAdapter {
    //2 premiers joueurs à se connecter.
    static ChannelGroup players = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    //indique si la partie est pleine
    static boolean full = false;
    static final int MAX_PLAYERS = 2;

    static boolean gameStarted = false;

    @Override
    public void channelActive(final ChannelHandlerContext ctx){
        full = players.size() >= MAX_PLAYERS;
        //Compose le message de welcome et l'ajoute dans un buffer
        final String welcome = "Welcome. Nombre de joueurs actuels : " + players.size() + ". Plein? :" + full + " Partie en cours? : " + gameStarted + "\n";
        final ByteBuf msg = ctx.alloc().buffer(200);
        msg.writeBytes(Unpooled.wrappedBuffer(welcome.getBytes()));



        //Envoie le message de welcome suivi du message d'erreur indiquant que le serveur est plein, puis ferme la connexion
        if(full || gameStarted){
            msg.writeBytes(Unpooled.wrappedBuffer(("Ce serveur est plein ou la partie a déjà commencé.\n").getBytes()));
            final ChannelFuture f = ctx.writeAndFlush(msg);
            f.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    assert f == future;
                    ctx.close();
                }
            });

        }
        //Envoie le message de welcome et lance la partie si 2 joueurs sont présents
        else{
            ctx.writeAndFlush(Unpooled.wrappedBuffer(welcome.getBytes()));
            players.add(ctx.channel());
            if(players.size() == MAX_PLAYERS){
                full = true;
                gameStarted = true;
                System.out.println("2 joueurs connectés. Lancer la partie.");

                for(Channel ch : players){
                    ch.writeAndFlush(Unpooled.wrappedBuffer(("Lancement de la partie!\n").getBytes()));
                }
                //TODO ben lancer la partie du coup
            }
        }

    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx){
        //Supprime le joueur
        players.remove(ctx.channel());
    }


}
