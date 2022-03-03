package com.example.Protego.web;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class Note {
    private String noteID;
    private String creator;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm'Z'")
    private Date dateCreated;
    private List<String> approvedDoctors;
    private String content;

    public String getNoteID() {
        return noteID;
    }

    public void setNoteID(String noteID) {
        this.noteID = noteID;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public List<String> getApprovedDoctors() {
        return approvedDoctors;
    }

    public void setApprovedDoctors(List<String> approvedDoctors) {
        this.approvedDoctors = approvedDoctors;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Note{" +
                "noteID='" + noteID + '\'' +
                ", creator=" + creator +
                ", dateCreated=" + dateCreated +
                ", approvedDoctors=" + approvedDoctors +
                ", content='" + content + '\'' +
                '}';
    }
}
