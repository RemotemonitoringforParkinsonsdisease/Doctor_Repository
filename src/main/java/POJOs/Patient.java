package POJOs;


import java.time.LocalDate;
import java.util.List;


public class Patient  {
    private Integer patientId;
    private LocalDate dob;
    private List<Report> reports;
    private String fullName;

    public Patient(Integer patientId, LocalDate dob, List<Report> reports, String fullName) {
        this.patientId = patientId;
        this.dob = dob;
        this.reports = reports;
        this.fullName = fullName;
    }
    public Patient(Integer patientId, LocalDate dob, String fullName) {
        this.patientId = patientId;
        this.dob = dob;
        this.fullName = fullName;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}