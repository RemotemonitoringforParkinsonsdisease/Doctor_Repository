package POJOs;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a medical report associated with a patient, containing information
 * about the date of the report, symptoms, observations, and the path to the signals file.
 *
 * A Report includes a report ID, the ID of the patient it belongs to, the report date,
 * a file path to the recorded signals (ECG, EDA, EMG, ACC), a list of {@link Symptoms},
 * and both patient and doctor observations.
 */
public class Report {

    private Integer reportId;
    private Integer patientId;
    private LocalDate reportDate;
    private String signalsFilePath;
    private List<Symptoms> symptoms;
    private String patientObservation;
    private String doctorObservation;

    /**
     * Constructor used to create a report with full information
     * (reportId, patientId, reportDate, signalsFilePath, symptoms, patientObservation, doctorObservation).
     *
     * @param reportId the identifier of the report
     * @param patientId the identifier of the patient associated with the report
     * @param reportDate the date of the report
     * @param signalsFilePath file path of the recorded signals (ECG, EDA, EMG, ACC)
     * @param symptoms list of symptoms reported by the patient
     * @param patientObservation the patient's own observation or comment
     * @param doctorObservation the doctor's analysis or comment on the report
     */
    public Report(Integer reportId, Integer patientId, LocalDate reportDate, String signalsFilePath,
                  List<Symptoms> symptoms, String patientObservation, String doctorObservation) {
        this.reportId = reportId;
        this.patientId = patientId;
        this.reportDate = reportDate;
        this.signalsFilePath = signalsFilePath;
        this.symptoms = symptoms;
        this.patientObservation = patientObservation;
        this.doctorObservation = doctorObservation;
    }

    /**
     * @return the report identifier
     */
    public Integer getReportId() {
        return reportId;
    }

    /**
     * @return the date of the report
     */
    public LocalDate getReportDate() {
        return reportDate;
    }

    /**
     * @return the list of symptoms associated with the report
     */
    public List<Symptoms> getSymptoms() {
        return symptoms;
    }

    /**
     * @return the patient's observation
     */
    public String getPatientObservation() {
        return patientObservation;
    }

    /**
     * @return the doctor's observation
     */
    public String getDoctorObservation() {
        return doctorObservation;
    }

    /**
     * Sets the doctor's observation included in the report.
     *
     * @param doctorObservation the doctor's updated comment or analysis
     */
    public void setDoctorObservation(String doctorObservation) {
        this.doctorObservation = doctorObservation;
    }

    /**
     * @return the file path of the recorded signals
     */
    public String getSignalsFilePath() {
        return signalsFilePath;
    }
}
