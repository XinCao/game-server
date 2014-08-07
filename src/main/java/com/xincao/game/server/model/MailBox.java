package com.xincao.game.server.model;

import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 邮箱
 * 
 */
public class MailBox extends Node {

    private static final Logger logger = LoggerFactory.getLogger(MailBox.class);
    private static final int DEFAULT_CAPACITY = 20;
    private int id;
    private int playerId;
    private int capacity;
    private List<Mail> maillist;

    public MailBox() {}

    public MailBox(Player player) {
        super(player);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity >= MailBox.DEFAULT_CAPACITY ? capacity : MailBox.DEFAULT_CAPACITY;
    }

    public void setDefaultCapacity() {
        this.setCapacity(Integer.MIN_VALUE);
    }

    public List<Mail> getMaillist() {
        return maillist;
    }

    public void setMaillist(List<Mail> maillist) {
        this.maillist = maillist;
    }

    /**
     * 增加邮件
     * 
     * @param mail
     */
    public void addMail(Mail mail) {
        this.maillist.add(mail);
        Collections.sort(maillist);
        if (isFull()) {
            maillist.remove(capacity-1);
        }
    }

    /**
     * 删除邮件
     * 
     * @param mailId 
     * @return  
     */
    public Mail deleteMail(int mailId) {
        Mail m = getMailByMailId(mailId);
        if (m != null) {
            m.setStatus(Mail.Status.DELETE.getNo());
            return m;
        }
        logger.info ("Player = {} delete email = {} does not exist", playerId, mailId);
        return null;
    }

    /**
     * 读邮件
     * 
     * @param mailId
     * @return 
     */
    public Mail readMail(int mailId) {
        Mail m = getMailByMailId(mailId);
        if (m != null) {
            m.setStatus(Mail.Status.READ.getNo());
            return m;
        }
        logger.info ("Player = {} read email = {} does not exist", playerId, mailId);
        return null;
    }

    /**
     * 获得邮件
     * 
     * @param mailId
     * @return 
     */
    public Mail getMailByMailId(int mailId) {
        for (Mail m : maillist) {
            if (mailId == m.getId()) {
                return m;
            }
        }
        return null;
    }

    /**
     * 向客户端显示的邮件，不能大于邮箱容量
     * 
     * @return 
     */
    public int haveBeenUsingTheSpace() {
        int size = maillist.size();
        return size <= capacity ? size : capacity;
    }

    public boolean isFull () {
        int size = maillist.size();
        return size >= capacity;
    }
}