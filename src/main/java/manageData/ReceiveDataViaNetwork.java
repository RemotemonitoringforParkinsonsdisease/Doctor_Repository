package manageData;


import POJOs.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class ReceiveDataViaNetwork {

    private DataInputStream dataInputStream;


    public ReceiveDataViaNetwork(DataInputStream dis) {
        this.dataInputStream = dis;
    }

    public String receiveString() throws IOException{
        return dataInputStream.readUTF();
    }

    public User receiveUser() throws IOException{
        Integer userId = dataInputStream.readInt();
        String email = dataInputStream.readUTF();
        return new User(userId, email);
    }

    public Patient recievePatient() throws IOException{
        Patient patient = null;
        Integer patientId = dataInputStream.readInt();
        String date = dataInputStream.readUTF();
        String fullName = dataInputStream.readUTF();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dob = LocalDate.parse(date, formatter);
        patient = new Patient(patientId,fullName, dob);
        return patient;
    }

    public List<Patient> receivePatients() throws IOException{
        int numPatients = dataInputStream.readInt();
        List<Patient> patients = new ArrayList<Patient>();
        for (int i = 0; i < numPatients; i++) {
            patients.add(recievePatient());
        }
        return patients;
    }

    public List<Report> receiveReportsOfAPatient() throws IOException{
        List<Report> reports = new ArrayList<>();
        int numberOfReports = dataInputStream.readInt();
        for (int i = 0; i < numberOfReports; i++) {
            reports.add(receiveReport());
        }
        return reports;

        }

    public Report receiveReport() throws IOException{
        Report report = null;
        Integer reportId = dataInputStream.readInt();
        Integer patientId = dataInputStream.readInt();
        String date = dataInputStream.readUTF();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate reportDate = LocalDate.parse(date, formatter);
        List<Signal> signals = receiveSignals();
        List<Symptoms> symptoms = receiveSymptoms();
        String patientObservation = dataInputStream.readUTF();
        String doctorObservation = dataInputStream.readUTF();
        report = new Report(reportId, patientId, reportDate, signals, symptoms, patientObservation, doctorObservation);
        return report;
    }

    public List<Signal> receiveSignals() throws IOException{
        List<Signal> signals = new ArrayList<>();
        int numSignals = dataInputStream.readInt();

        for (int i = 0; i < numSignals; i++) {
            Integer signalId = dataInputStream.readInt();
            String typeSignal = dataInputStream.readUTF();
            SignalType type = SignalType.valueOf(typeSignal);
            int sampleFrequency = dataInputStream.readInt();
            String valuesString = dataInputStream.readUTF();
            Signal signal = new Signal(signalId, type, sampleFrequency);
            //Esto recibe la lista completa de valores de la señal
            signal.stringToIntValues(valuesString);
            signals.add(signal);
        }
        return signals;
    }

    public List<Symptoms> receiveSymptoms() throws IOException{
        List<Symptoms> symptoms = new ArrayList<>();
        String symptomsLine = dataInputStream.readUTF();
        for (String s : symptomsLine.split(",")) {
            symptoms.add(Symptoms.valueOf(s.trim()));
        }
        return symptoms;
    }

    public Doctor receiveDoctor() throws IOException{
        Integer userId = dataInputStream.readInt();
        Integer doctorId = dataInputStream.readInt();
        String fullName = dataInputStream.readUTF();
        String doctorPassword = dataInputStream.readUTF();
        String date = dataInputStream.readUTF();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dob = LocalDate.parse(date, formatter);
        Doctor doctor = new Doctor(userId, doctorId, fullName, doctorPassword,dob);
        List<Patient> patients = receivePatients(); //Lo hago por separado para que se cree el doctor aunque falle un paciente
        doctor.setPatients(patients);
        return doctor;
    }
}