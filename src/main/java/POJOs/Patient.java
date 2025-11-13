package POJOs;


import java.time.LocalDate;
import java.util.Set;

public class Patient extends User {
    private Set<Report> reports;
    private LocalDate dob;

    public Patient(String email, String fullName) {
        super(email, fullName);
    }
}