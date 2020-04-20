package com.uch2.networking.server;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.uch2.networking.GameState;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.EmptyByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.util.CharsetUtil;

import java.io.ByteArrayOutputStream;
import java.util.TimerTask;

public class SendUpdate extends TimerTask {
    private ChannelGroup players;

    public SendUpdate(ChannelGroup players, Object toSend){  //TODO remplacer Object toSend par un GameState
        this.players = players;
    }
    @Override
    public void run() {
        String str;
        GameState state2 = new GameState(2, 2, 2);
        Kryo kryo = new Kryo();
        for(Channel ch : players){
            //TODO : remplacer par un envoi du gameState "toSend" sérialisé
            str = "coucou \n";
            ch.writeAndFlush(Unpooled.copiedBuffer(str, CharsetUtil.UTF_8));
        }
    }
}
