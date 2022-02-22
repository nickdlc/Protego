package spec;

public abstract class AssignedTo {
    private String uid;
    private Patient patient;
    private Doctor doctor;
    private Boolean active;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "AssignedTo{" +
                "uid='" + uid + '\'' +
                ", patient=" + patient +
                ", doctor=" + doctor +
                ", active=" + active +
                '}';
    }
}
