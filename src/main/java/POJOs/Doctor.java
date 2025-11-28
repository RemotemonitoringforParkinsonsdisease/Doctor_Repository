package POJOs;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Doctor {
    private String fullName;
    private Integer userId;
    private Integer doctorId;
    private String doctorPassword; //TODO: Encriptar;
    private LocalDate dob;
    private List<Patient> patients;

    public Doctor(Integer userID, Integer doctorId, String fullName , String doctorPassword, LocalDate dob) {
        this.userId = userID;
        this.doctorId = doctorId;
        this.fullName = fullName;
        this.doctorPassword = doctorPassword;
        this.dob = dob;
        this.patients = new ArrayList<Patient>();
    }

    public Doctor(String fullName , String doctorPassword, LocalDate dob) {
        this.fullName = fullName;
        this.doctorPassword = doctorPassword;
        this.dob = dob;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public String getDoctorPassword() {
        return doctorPassword;
    }

    public LocalDate getDob() {
        return dob;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }
}
