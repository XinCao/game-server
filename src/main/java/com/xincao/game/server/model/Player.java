package com.xincao.game.server.model;

import com.xincao.common.nio.IConnection;
import com.xincao.game.server.core.network.AionConnection;

public class Player extends Root {

    private int id;
    private String account;
    private String name;
    private MailBox mailBox;
    private IConnection connection;

    public Player(boolean isDelay) {
        if (!isDelay) {
            this.mailBox = new MailBox(this);
        }
    }

    public Player() {
        this(true);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MailBox getMailBox() {
        return mailBox;
    }

    public void setMailBox(MailBox mailBox) {
        this.mailBox = mailBox;
    }

    public void setConnection(IConnection connection) {
        this.connection = connection;
    }

    public AionConnection getConnection() {
        return (AionConnection)this.connection;
    }
}