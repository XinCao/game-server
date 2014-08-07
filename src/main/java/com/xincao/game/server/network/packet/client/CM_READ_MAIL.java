package com.xincao.game.server.network.packet.client;

import com.xincao.game.server.model.Player;
import com.xincao.game.server.network.core.AionConnection;
import com.xincao.game.server.service.MailBoxService;
import java.nio.ByteBuffer;

public class CM_READ_MAIL extends AionClientPacket {

    private static final MailBoxService mailBoxService = getBean(MailBoxService.class);
    private int mailId;

    public CM_READ_MAIL(ByteBuffer buf, AionConnection client, Integer opcode) {
        super(buf, client, opcode);
    }

    @Override
    protected void readImpl() {
        this.mailId = readD();
    }

    @Override
    protected void runImpl() {
        mailBoxService.readMail(getConnection().<Player>getObject(), mailId);
    }
}
