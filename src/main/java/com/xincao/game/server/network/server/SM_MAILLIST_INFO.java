package com.xincao.game.server.network.server;

import java.nio.ByteBuffer;
import java.util.List;
import com.xincao.game.server.model.Mail;
import com.xincao.game.server.model.MailBox;
import com.xincao.game.server.model.Player;
import com.xincao.game.server.core.network.AionConnection;
import com.xincao.game.server.constant.Config;

/**
 * 向客户端显示的邮件，不能大于邮箱容量
 *
 */
public class SM_MAILLIST_INFO extends AionServerPacket {

    public SM_MAILLIST_INFO(int opcode) {
        super(opcode);
    }

    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) {
        Player player = con.<Player>getObject();
        MailBox mailBox = player.getMailBox();
        List<Mail> maillist = mailBox.getMaillist();
        if (Config.isDebug()) {
            this.writeS(buf, maillist.toString());
            return;
        }
        this.writeD(buf, mailBox.getCapacity());
        int size = mailBox.haveBeenUsingTheSpace();
        this.writeD(buf, size);
        for (int i = 0, n = size; i < n; i ++) {
            Mail m = maillist.get(i);
            this.writeD(buf, m.getId());
            this.writeD(buf, m.getSenderId());
            this.writeS(buf, m.getContent());
            this.writeS(buf, m.getCreateAt().toString());
            this.writeD(buf, Integer.valueOf(m.getStatus()));
        }
    }
}