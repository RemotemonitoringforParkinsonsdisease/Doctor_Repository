package POJOs;

import java.time.LocalDate;
import java.util.List;


public class Doctor {
    private Integer userId;
    private Integer doctorId;
    private String doctorPassword; //TODO: Encriptar;
    private LocalDate dob;
    private List<Patient> patients;
    private String fullName;

    public Doctor(Integer userId, Integer doctorId, String doctorPassword, LocalDate dob, List<Patient> patients, String fullName) {
        this.userId = userId;
        this.doctorId = doctorId;
        this.doctorPassword = doctorPassword;
        this.dob = dob;
        this.patients = patients;
        this.fullName = fullName;
    }
    public Doctor(Integer doctorId, String doctorPassword, LocalDate dob, String fullName) {
        this.doctorId = doctorId;
        this.doctorPassword = doctorPassword;
        this.dob = dob;
        this.fullName = fullName;
    }
    public Doctor(Integer userId, Integer doctorId, String doctorPassword, LocalDate dob, String fullName) {
        this.userId = userId;
        this.doctorId = doctorId;
        this.doctorPassword = doctorPassword;
        this.dob = dob;
        this.fullName = fullName;
    }



    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorPassword() {
        return doctorPassword;
    }

    public void setDoctorPassword(String doctorPassword) {
        this.doctorPassword = doctorPassword;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
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

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
