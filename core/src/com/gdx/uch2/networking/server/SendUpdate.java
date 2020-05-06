package com.gdx.uch2.networking.server;


import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.entities.Block;
import com.gdx.uch2.networking.MessageType;
import com.gdx.uch2.networking.ObjectPlacement;
import com.gdx.uch2.networking.kryo.NettyKryoDecoder;
import com.gdx.uch2.networking.kryo.NettyKryoEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import sun.jvm.hotspot.runtime.BasicLock;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class SendUpdate extends TimerTask {
    private List<ChannelHandlerContext> players;

    public SendUpdate(List<ChannelHandlerContext> players){  //TODO remplacer Object toSend par un GameState
        this.players = players;
    }

    @Override
    public void run() {

        for(ChannelHandlerContext ch : players){
            NettyKryoEncoder encoder = new NettyKryoEncoder();
            ByteBuf out = Unpooled.buffer(1024);
            encoder.encode(ServerGameStateTickManager.getInstance().getGameState(), out, MessageType.GameStateUpdate.getChar());

            ch.writeAndFlush(out);

        }
    }
}
