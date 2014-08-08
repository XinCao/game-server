package com.xincao.game.server.network.server;

import com.xincao.game.server.model.Player;
import com.xincao.game.server.core.network.AionConnection;
import java.nio.ByteBuffer;

/**
 * 心跳消息
 *
 */
public class SM_HEART_BEAT extends AionServerPacket {

    public SM_HEART_BEAT(Integer opcode) {
        super(opcode);
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        Player player = con.<Player>getObject();
        writeS(buf, player.getName() + " : heart beat");
    }
}