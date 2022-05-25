package com.example.protego.web.schemas.firestore;

import java.util.Date;
import java.util.List;

public class Note {
    private String noteID;
    private String title;
    private String creator;
    private Date dateCreated;
    private List<String> approvedDoctors;
    private String content;
    private String visibility;

    public Note(){

    }

    public Note(String title, Date dateCreated, String visibility, String content){
        this.title = title;
        this.dateCreated = dateCreated;
        this.visibility = visibility;
        this.content = content;
    }
    public String getNoteID() {
        return noteID;
    }

    public void setNoteID(String noteID) {
        this.noteID = noteID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;}

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


    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }


    @Override
    public String toString() {
        return "Note{" +
                "noteID='" + noteID + '\'' +
                ", creator=" + creator +
                ", title=" + title +
                ", dateCreated=" + dateCreated +
                ", visibility=" + visibility +
                ", approvedDoctors=" + approvedDoctors +
                ", content='" + content + '\'' +
                '}';
    }
}
