package POJOs;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Patient extends User {
    private List<Report> reports;
    private LocalDate dob;

    //Constructor para recibir un paciente del server, al recibir x pacientes se llamara x veces
    public Patient(String id, String email, String fullName, LocalDate dob) {
        super(id, email, fullName);
        this.dob = dob;
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
        return super.getFullName();
    }
}