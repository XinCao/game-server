package com.xincao.game.server.network.client;

import com.xincao.game.server.model.Mail;
import com.xincao.game.server.model.Player;
import com.xincao.game.server.core.network.AionConnection;
import com.xincao.game.server.service.MailBoxService;
import com.xincao.game.server.service.PlayerService;
import java.nio.ByteBuffer;

public class CM_SEND_MAIL extends AionClientPacket {

    private static final MailBoxService mailBoxService = getBean(MailBoxService.class);
    private static final PlayerService playerService = getBean(PlayerService.class);
    private int targetPlayerId;
    private String content;

    public CM_SEND_MAIL(ByteBuffer buf, AionConnection client, Integer opcode) {
        super(buf, client, opcode);
    }

    @Override
    protected void readImpl() {
        this.targetPlayerId = readD();
        this.content = readS();
    }

    @Override
    protected void runImpl() {
        Player targetPlayer = playerService.getPlayerByPlaterId(targetPlayerId);
        if (targetPlayer != null) {
            Mail m = new Mail();
            m.setMailBoxId(targetPlayer.getMailBox().getId());
            m.setSenderId(this.getConnection().<Player>getObject().getId());
            m.setContent(content);
            m.setStatus(Mail.Status.NOT_READ.getNo());
            mailBoxService.sendMail(targetPlayer, m);        	
        }
    }
}