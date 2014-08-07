package com.xincao.game.server.network.packet.client;

import java.nio.ByteBuffer;
import org.springframework.context.ApplicationContext;
import com.xincao.common.nio.packet.BaseClientPacket;
import com.xincao.game.server.network.core.AionConnection;
import com.xincao.game.server.network.packet.server.AionServerPacket;
import com.xincao.game.server.util.AC;

public abstract class AionClientPacket extends BaseClientPacket<AionConnection> {

    protected static final ApplicationContext ac = AC.getApplicationContext();

    protected AionClientPacket(ByteBuffer buf, AionConnection client, int opcode) {
        super(buf, opcode);
        this.setConnection(client);
    }

    @Override
    public final void run() {
        try {
            this.runImpl();
        } catch (Throwable e) {
            logger.error("client ip = {"+ getConnection().getIP() +"} opcode = {"+ getOpcode() +"} message :" + this.toString());
        }
    }

    protected void sendPacket(AionServerPacket msg) {
        this.getConnection().sendPacket(msg);
    }

    protected static <T extends Object> T getBean(Class<T> clazz) {
        return ac.getBean(clazz);
    }
}