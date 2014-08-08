package com.xincao.game.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xincao.game.server.mapper.MailBoxMapper;
import com.xincao.game.server.mapper.MailMapper;
import com.xincao.game.server.model.IntPair;
import com.xincao.game.server.model.Mail;
import com.xincao.game.server.model.MailBox;
import com.xincao.game.server.model.Player;
import com.xincao.game.server.core.network.AionPacketHandler.AionServerKind;
import com.xincao.game.server.network.server.SM_MAILLIST_INFO;
import java.util.ArrayList;

@Service
public class MailBoxService {

    @Autowired
    private MailBoxMapper mailBoxMapper;
    @Autowired
    private MailMapper mailMapper;

    /**
     * 创建邮箱
     * 
     * @param player 
     */
    public void createMailBox (Player player) {
        MailBox mailBox = new MailBox();
        mailBox.setDefaultCapacity();
        mailBox.setPlayerId(player.getId());
        mailBoxMapper.insert(mailBox);
    }

    /**
     * 加载邮件信息
     *
     * @param player
     */
    public void loadMailBox(Player player) {
        int playerId = player.getId();
        MailBox mailBox = mailBoxMapper.selectByPlayerId(playerId);
        mailBox.setOwner(player);
        player.setMailBox(mailBox);
        List<Mail> maillist = mailMapper.selectByMailBoxId(new IntPair(mailBox.getId(), mailBox.getCapacity()));
        if (maillist == null) {
            maillist = new ArrayList<Mail>();
        }
        mailBox.setMaillist(maillist);
        player.getConnection().sendPacket(new SM_MAILLIST_INFO(AionServerKind.SM_MAILLIST_INFO.getOpcode()));
    }

    /**
     * 删除邮件
     *
     * @param player
     * @param mailId
     */
    public void deleteMail(Player player, int mailId) {
        MailBox mailBox = player.getMailBox();
        Mail m = mailBox.deleteMail(mailId);
        if (m != null) {
            mailMapper.updateByPrimaryKey(m);
            loadMailBox(player);
        } else {
            // 提醒，删除的邮件不存在
        }
    }

    /**
     * 读邮件
     *
     * @param player
     * @param mailId
     */
    public void readMail(Player player, int mailId) {
        MailBox mailBox = player.getMailBox();
        Mail m = mailBox.readMail(mailId);
        if (m != null) {
            mailMapper.updateByPrimaryKey(m);
            player.getConnection().sendPacket(new SM_MAILLIST_INFO(AionServerKind.SM_MAILLIST_INFO.getOpcode()));
        } else {
            // 提醒，读的邮件不存在
        }
    }

    /**
     * 发送邮件
     *
     * @param targetPlayer
     * @param mail
     */
    public void sendMail(Player targetPlayer, Mail mail) {
        MailBox mailBox = targetPlayer.getMailBox();
        mailBox.addMail(mail);
        mailMapper.insert(mail);
        targetPlayer.getConnection().sendPacket(new SM_MAILLIST_INFO(AionServerKind.SM_MAILLIST_INFO.getOpcode()));
    }
}