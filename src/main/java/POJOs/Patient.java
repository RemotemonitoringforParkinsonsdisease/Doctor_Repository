package POJOs;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a patient in the system, including their identification data,
 * personal information, and the list of medical reports associated with them.
 *
 * A Patient contains a patient ID, full name, date of birth, and an optional
 * list of {@link Report} objects representing their medical history.
 */
public class Patient  {

    private Integer patientId;
    private LocalDate dob;
    private List<Report> reports;
    private String fullName;

    /**
     * Constructor used when creating a patient with basic identification data
     * (patientId, fullName, dob).
     *
     * @param patientId the patient's identifier
     * @param fullName the patient's full name
     * @param dob the patient's date of birth
     */
    public Patient(Integer patientId, String fullName, LocalDate dob) {
        this.patientId = patientId;
        this.dob = dob;
        this.fullName = fullName;
    }

    /**
     * @return the patient identifier
     */
    public Integer getPatientId() {
        return patientId;
    }

    /**
     * @return the list of reports associated with the patient
     */
    public List<Report> getReports() {
        return reports;
    }

    /**
     * Sets the list of reports associated with the patient.
     *
     * @param reports the list of reports to assign
     */
    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    /**
     * @return the patient's full name
     */
    public String getFullName() {
        return fullName;
    }
}
