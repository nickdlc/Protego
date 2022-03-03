package spec;

import java.util.Date;
import java.util.List;

public abstract class Note {
    private String noteID;
    private Patient creator;
    private Date dateCreated;
    private List<Doctor> approvedDoctors;
    private String content;

    public String getNoteID() {
        return noteID;
    }

    public void setNoteID(String noteID) {
        this.noteID = noteID;
    }

    public Patient getCreator() {
        return creator;
    }

    public void setCreator(Patient creator) {
        this.creator = creator;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public List<Doctor> getApprovedDoctors() {
        return approvedDoctors;
    }

    public void setApprovedDoctors(List<Doctor> approvedDoctors) {
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
