package POJOs;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class Report {
    private String reportId;
    private Patient patient;
    private LocalDate reportDate;
    private String patientObservation; //El texto que le manda el paciente al doctor, podemos hacer booleans como dijo arecha también
    private Set<Signal> signals; //La señal grabada por el bitalino del paciente, supongo que seran varios canales, puede ser un Set / List de String, hay que mirar tipo de datos del Bitalino
    private List<Symptoms> symptoms;
    private String doctorObservation; //El texto que le manda el doctor al paciente

    public Report(String reportId, Patient patient, LocalDate reportDate, String patientObservation, List<Symptoms> symptoms, Set<Signal> signals, String doctorObservation) {
        this.reportId = reportId;
        this.patient = patient;
        this.reportDate = reportDate;
        this.patientObservation = patientObservation;
        this.symptoms = symptoms;
        this.signals = signals;
        this.doctorObservation = doctorObservation;
    }


    public LocalDate getReportDate() {
        return reportDate;
    }
    public List<Symptoms> getSymptoms() {
        return symptoms;
    }
    public Set<Signal> getSignals() {
        return signals;
    }
    public String getPatientObservation() {
        return patientObservation;
    }
    public Patient getPatient(){
        return patient;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public void setPatientObservation(String patientObservation) {
        this.patientObservation = patientObservation;
    }

    public void setSignals(Set<Signal> signals) {
        this.signals = signals;
    }

    public void setSymptoms(List<Symptoms> symptoms) {
        this.symptoms = symptoms;
    }

    public String getDoctorObservation() {
        return doctorObservation;
    }

    public void setDoctorObservation(String doctorObservation) {
        this.doctorObservation = doctorObservation;
    }

    public Signal getSignalByType(SignalType signalType){
        for(Signal signal : signals){
            if(signal.getSignalType() ==  signalType){
                return signal;
            }
        }
        return null;
    }
}
