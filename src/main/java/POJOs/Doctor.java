package POJOs;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a doctor in the system, including their identification data,
 * login credentials, personal information, and the list of assigned patients.
 *
 * A Doctor contains a full name, user ID, doctor-specific ID, password,
 * date of birth, and an optional list of {@link Patient} objects.
 */
public class Doctor {
    private String fullName;
    private Integer userId;
    private Integer doctorId;
    private String doctorPassword;
    private LocalDate dob;
    private List<Patient> patients;

    /**
     * Constructor used when creating a doctor with full identification data
     * (userId, doctorId, fullName, doctorPassword, dob).
     *
     * @param userID the associated user identifier
     * @param doctorId the doctor-specific identifier
     * @param fullName the doctor's full name
     * @param doctorPassword the doctor's password
     * @param dob date of birth of the doctor
     */
    public Doctor(Integer userID, Integer doctorId, String fullName , String doctorPassword, LocalDate dob) {
        this.userId = userID;
        this.doctorId = doctorId;
        this.fullName = fullName;
        this.doctorPassword = doctorPassword;
        this.dob = dob;
        this.patients = new ArrayList<Patient>();
    }

    /**
     * Constructor used when creating a doctor without associated user or doctor ID.
     *
     * @param fullName the doctor's full name
     * @param doctorPassword the doctor's password
     * @param dob the doctor's date of birth
     */
    public Doctor(String fullName , String doctorPassword, LocalDate dob) {
        this.fullName = fullName;
        this.doctorPassword = doctorPassword;
        this.dob = dob;
    }

    /**
     * @return the doctor's password
     */
    public String getDoctorPassword() {
        return doctorPassword;
    }

    /**
     * @return the doctor's date of birth
     */
    public LocalDate getDob() {
        return dob;
    }

    /**
     * @return the list of patients assigned to the doctor
     */
    public List<Patient> getPatients() {
        return patients;
    }

    /**
     * Sets the list of patients assigned to the doctor.
     *
     * @param patients the list of patients to assign
     */
    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    /**
     * @return the doctor's full name
     */
    public String getFullName() {
        return fullName;
    }
}
