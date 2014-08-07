package com.xincao.game.server.network.packet.client;

import com.xincao.game.server.model.Player;
import com.xincao.game.server.network.core.AionConnection;
import com.xincao.game.server.service.MailBoxService;
import java.nio.ByteBuffer;

public class CM_MAILLIST_INFO extends AionClientPacket {

    private static final MailBoxService mailBoxService = getBean(MailBoxService.class);

    public CM_MAILLIST_INFO(ByteBuffer buf, AionConnection client, Integer opcode) {
        super(buf, client, opcode);
    }

    @Override
    protected void readImpl() {}

    @Override
    protected void runImpl() {
        mailBoxService.loadMailBox(getConnection().<Player>getObject());
    }

}
