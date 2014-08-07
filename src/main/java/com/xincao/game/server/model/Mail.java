package com.xincao.game.server.model;

import java.util.Date;

/**
 * 邮件
 *
 */
public class Mail extends Node implements Comparable<Mail> {

    public enum Status {

        NOT_READ("1"),
        READ("2"),
        DELETE("3"),
        ;

        private final String no;

        private Status(String no) {
            this.no = no;
        }

        public String getNo() {
            return this.no;
        }
    }

    private int id;
    private int mailBoxId;
    private int senderId;
    private String content;
    private Date createAt;
    private String status;

    public Mail() {
    }

    public Mail(MailBox mailBox) {
        super(mailBox);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMailBoxId() {
        return mailBoxId;
    }

    public void setMailBoxId(int mailBoxId) {
        this.mailBoxId = mailBoxId;
    }

    public int getSenderId() {
        return this.senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateAt() {
        return this.createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Mail other = (Mail) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "Mail [id=" + id + ", mailBoxId=" + mailBoxId + ", senderId=" + senderId + ", content=" + content + ", createAt=" + createAt + ", status=" + status + "]";
    }

    /**
     * 最新的邮件排在前
     * 
     * @param o
     * @return 
     */
    @Override
    public int compareTo(Mail o) {
        if (o == null) {
            return 1;
        }
        return o.getId() - this.getId();
    }
}