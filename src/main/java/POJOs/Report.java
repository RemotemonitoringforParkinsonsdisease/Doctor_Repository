package POJOs;

import java.time.LocalDate;
import java.util.Set;

public class Report {
    private Integer reportId;
    private Integer patientId;
    private Integer doctorId;
    private LocalDate reportDate;
    private String patientObservation; //El texto que le manda el paciente al doctor, podemos hacer booleans como dijo arecha también
    private Set<String> signal; //La señal grabada por el bitalino del paciente, supongo que seran varios canales, puede ser un Set / List de String, hay que mirar tipo de datos del Bitalino
    private boolean isSeen; //Ha sido visto por doctor, alomejor habría que implementar si ha sido respondido

    public Report(Integer patientId, Integer doctorId, LocalDate reportDate, String patientObservation) { //Report solo con texto
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.isSeen = false;
    }
    public Report(Integer patientId, Integer doctorId, LocalDate reportDate, Set<String> signal) { //Report solo con señal
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.reportDate = reportDate;
        this.signal = signal;
        this.isSeen = false;
    }
    public Report(Integer patientId, Integer doctorId, LocalDate reportDate, String patientText, String signal) { //Report con ambas
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.reportDate = reportDate;
        this.patientObservation = patientText;
        this.isSeen = false;
    }
    public Integer getReportId() {
        return reportId;
    }
    public Integer getPatientId() {
        return patientId;
    }
    public String getPatientObservation() {
        return patientObservation;
    }
    public Set<String> getSignal() {
        return signal;
    }
    public boolean getIsSeen() {
        return isSeen;
    }
    public void setIsSeen(boolean isSeen) {
        this.isSeen = isSeen;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }
}
