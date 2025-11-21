package POJOs;


import java.time.LocalDate;
import java.util.List;


public class Patient  {
    private Integer patientId;
    private LocalDate dob;
    private List<Report> reports;

    public Patient(Integer patientId, LocalDate dob, List<Report> reports) {
        this.patientId = patientId;
        this.dob = dob;
        this.reports = reports;
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
}