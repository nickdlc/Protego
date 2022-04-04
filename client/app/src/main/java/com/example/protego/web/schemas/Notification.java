package com.example.protego.web.schemas;

import java.util.Date;

public class Notification implements Comparable<Notification> {
    private String puid;
    private String msg;
    private Boolean active;
    private Date date;

    public Notification() {
    }

    public Notification(String puid, String msg, Boolean active, Date date) {
        this.puid = puid;
        this.msg = msg;
        this.active = active;
        this.date = date;
    }

    public String getPuid() {
        return puid;
    }

    public void setPuid(String puid) {
        this.puid = puid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "puid='" + puid + '\'' +
                ", msg='" + msg + '\'' +
                ", active=" + active +
                ", date=" + date +
                '}';
    }

    @Override
    public int compareTo(Notification n) {
        return getDate().compareTo(n.getDate());
    }
}
