package POJOs;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Patient extends User {
    private Set<Report> reports;
    private LocalDate dob;

    public Patient(String email, String fullName, LocalDate dob, Set<Report> reports) {
        super(email, fullName);
        this.reports = reports;
        this.dob = dob;
    }
}