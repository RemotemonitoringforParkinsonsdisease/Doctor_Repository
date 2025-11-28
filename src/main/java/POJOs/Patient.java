package POJOs;

import java.time.LocalDate;
import java.util.List;


public class Patient  {
    private Integer patientId;
    private LocalDate dob;
    private List<Report> reports;
    private String fullName;

    public Patient(Integer patientId, String fullName, LocalDate dob) {
        this.patientId = patientId;
        this.dob = dob;
        this.fullName = fullName;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public LocalDate getDob() {
        return dob;
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
}