package com.example.protego.web.schemas.firestore;

import java.util.Date;

public class Notification implements Comparable<Notification> {
    private String puid;
    private String duid;
    private String msg;
    private Boolean active;
    private Date date;
    private String type;

    public Notification() {
    }

    public Notification(String puid, String duid, String msg, Boolean active, Date date, String type) {
        this.puid = puid;
        this.duid = duid;
        this.msg = msg;
        this.active = active;
        this.date = date;
        this.type = type;
    }

    public String getPuid() {
        return puid;
    }

    public void setPuid(String puid) {
        this.puid = puid;
    }

    public String getDuid() {
        return duid;
    }

    public void setDuid(String duid) {
        this.duid = duid;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "puid='" + puid + '\'' +
                ", duid='" + duid + '\'' +
                ", msg='" + msg + '\'' +
                ", active=" + active +
                ", date=" + date +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public int compareTo(Notification n) {
        return getDate().compareTo(n.getDate());
    }
}
