package com.xincao.game.server.network.client;

import com.xincao.game.server.model.Player;
import com.xincao.game.server.core.network.AionConnection;
import com.xincao.game.server.service.MailBoxService;
import java.nio.ByteBuffer;

public class CM_DELETE_MAIL extends AionClientPacket {

    private static final MailBoxService mailBoxService = getBean(MailBoxService.class);
    private int mailId;

    public CM_DELETE_MAIL(ByteBuffer buf, AionConnection client, Integer opcode) {
        super(buf, client, opcode);
    }

    @Override
    protected void readImpl() {
        this.mailId = this.readD();
    }

    @Override
    protected void runImpl() {
        mailBoxService.deleteMail(getConnection().<Player>getObject(), mailId);
    }
}