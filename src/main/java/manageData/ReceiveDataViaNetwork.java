package manageData;


import POJOs.*;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class ReceiveDataViaNetwork {

    private DataInputStream dataInputStream;


    public ReceiveDataViaNetwork(Socket socket) {
        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("Error al inicializar el flujo de entrada: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String receiveString() throws IOException{
        return dataInputStream.readUTF();
    }

    public User receiveUser() throws IOException{
        Integer userId = dataInputStream.readInt();
        String email = dataInputStream.readUTF();
        return new User(userId, email);
    }


    public Patient recievePatient(){
        Patient patient = null;
        try {
            Integer patientId = dataInputStream.readInt();
            String date = dataInputStream.readUTF();
            String fullName = dataInputStream.readUTF();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dob = LocalDate.parse(date, formatter);
            patient = new Patient(patientId, dob, fullName);
        } catch (EOFException ex) {
            System.out.println("Todos los datos fueron leídos correctamente.");
        } catch (IOException ex) {
            System.err.println("Error al recibir datos del paciente: " + ex.getMessage());
            ex.printStackTrace();
        }
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

    //TODO: Ver como implementamos la recepción para todos los casos de Report

    public List<Report> receiveReportsOfAPatient(Patient patient) throws IOException{
        int numReports = dataInputStream.readInt();
        List<Report> reports = new ArrayList<Report>();
        for (int i = 0; i < numReports; i++) {
            reports.add(receiveReport());
        }
        return reports;
    }


    public Report receiveReport() throws IOException{
        Report report = null;
        try {
            Integer reportId = dataInputStream.readInt();
            Integer patientId = dataInputStream.readInt();
            String date = dataInputStream.readUTF();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateReport = LocalDate.parse(date, formatter);
            List<Signal> signals = receiveSignals();
            List<Symptoms> symptoms = receiveSymptoms();
            String patientObservation = dataInputStream.readUTF();
            String doctorObservation = dataInputStream.readUTF();



            report = new Report(reportId, patientId, dateReport, patientObservation, doctorObservation);
            report.setSignals(signals);
            report.setSymptoms(symptoms);
            report.setPatientObservation(patientObservation);
            //TODO: Pensar como gestionar casos de recibir parámetros nulos

        } catch (IOException e) {
            System.err.println("Error al leer el flujo de entrada: " + e.getMessage());
        }
        return report;
    }

    public List<Signal> receiveSignals() throws IOException{
        List<Signal> signals = new ArrayList<>();
        try {
            int numSignals = dataInputStream.readInt();

            for (int i = 0; i < numSignals; i++) {
                String typeSignal = dataInputStream.readUTF();
                SignalType type = SignalType.valueOf(typeSignal);
                Integer signalId = dataInputStream.readInt();
                String valuesString = dataInputStream.readUTF();
                Signal signal = new Signal(signalId, type);
                //Esto recibe la lista completa de valores de la señal
                signal.stringToIntValues(valuesString);
                signals.add(signal);
            }
            return signals;
        } catch (IOException e) {
            System.out.println("Error al leer el flujo de entrada " + e.getMessage());
        }
        return signals;
    }

    public List<Symptoms> receiveSymptoms() throws IOException{
        List<Symptoms> symptoms = new ArrayList<>();
        try {
            String symptomsLine = dataInputStream.readUTF();
            for (String s : symptomsLine.split(",")) {
                symptoms.add(Symptoms.valueOf(s.trim()));
            }
            return symptoms;
        } catch (IOException e) {
            System.err.println("Error al leer el flujo de entrada: " + e.getMessage());
        }
        return symptoms;
    }

    //Integer doctorId, String doctorPassword, LocalDate dob, String fullName
    public Doctor receiveDoctor() throws IOException{
        Integer doctorId = dataInputStream.readInt();
        String doctorPassword = dataInputStream.readUTF();
        Integer patientId = dataInputStream.readInt();
        String date = dataInputStream.readUTF();
        String fullName = dataInputStream.readUTF();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dob = LocalDate.parse(date, formatter);

        Doctor doctor = new Doctor(doctorId, doctorPassword, dob, fullName);
        List<Patient> patients = receivePatients(); //Lo hago por separado para que se cree el doctor aunque falle un paciente
        doctor.setPatients(patients);
        return doctor;
    }

    public int receiveInt() {
        int message = 0;
        try {
            message = dataInputStream.readInt();
        } catch (IOException e) {
            System.err.println("Error al recibir int: " + e.getMessage());
            e.printStackTrace();
        }
        return message;
    }

    public void releaseResources() {
        try {
            if (dataInputStream != null) {
                dataInputStream.close();
            }
        } catch (IOException e) {
            System.err.println("Error al liberar los recursos: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
