package com.xincao.game.server.network.packet.server;

import java.nio.ByteBuffer;

import org.springframework.context.ApplicationContext;

import com.xincao.common.nio.packet.BaseServerPacket;
import com.xincao.game.server.network.core.AionConnection;
import com.xincao.game.server.util.AC;

public abstract class AionServerPacket extends BaseServerPacket {

    protected static ApplicationContext ac = AC.getApplicationContext();

    protected AionServerPacket(int opcode) {
        super(opcode);
    }

    public final void write(AionConnection con, ByteBuffer buf) {
        int startPosition = buf.position();
        buf.putShort((short) 0);
        buf.put((byte) getOpcode());
        this.writeImpl(con, buf);
        int endPosition = buf.position();
        short size = (short) (endPosition - startPosition);
        buf.position(startPosition);
        buf.putShort(size);
        buf.position(endPosition);
        buf.flip();
    }

    protected abstract void writeImpl(AionConnection con, ByteBuffer buf);
}