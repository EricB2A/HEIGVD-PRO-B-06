package com.gdx.uch2.networking.server;


import com.gdx.uch2.networking.PlayerState;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.util.CharsetUtil;

import java.util.TimerTask;

public class SendUpdate extends TimerTask {
    private ChannelGroup players;

    public SendUpdate(ChannelGroup players, Object toSend){  //TODO remplacer Object toSend par un GameState
        this.players = players;
    }
    @Override
    public void run() {
        String str;
        PlayerState state2 = new PlayerState(2, 2, 2);
        for(Channel ch : players){
            //TODO : remplacer par un envoi du gameState "toSend" sérialisé
            str = "coucou \n";
            ch.writeAndFlush(Unpooled.copiedBuffer(str, CharsetUtil.UTF_8));
        }
    }
}
