package com.gdx.uch2.networking.server;


import com.gdx.uch2.networking.GameState;
import com.gdx.uch2.networking.PlayerState;
import com.gdx.uch2.networking.kryo.NettyKryoEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.util.CharsetUtil;

import java.util.List;
import java.util.TimerTask;

public class SendUpdate extends TimerTask {
    private List<ChannelHandlerContext> players;

    public SendUpdate(List<ChannelHandlerContext> players, Object toSend){  //TODO remplacer Object toSend par un GameState
        this.players = players;
    }

    @Override
    public void run() {
        String str;
        PlayerState[] ppp = new PlayerState[]{new PlayerState(6, 6, 6)};
        GameState state2 = new GameState(ppp);

        for(ChannelHandlerContext ch : players){
            NettyKryoEncoder kkk = new NettyKryoEncoder();
            ByteBuf out = Unpooled.buffer(1024);
            kkk.encode(state2, out, 'a');
            ch.writeAndFlush(out);

            //TODO : remplacer par un envoi du gameState "toSend" sérialisé
            str = "coucou \n";
            //ch.writeAndFlush(Unpooled.copiedBuffer(str, CharsetUtil.UTF_8));

            ch.writeAndFlush(state2);
        }
    }
}
