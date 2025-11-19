package manageData;

import POJOs.*;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


    public Patient recievePatient(){
        Patient patient = null;
        try {
            String id = dataInputStream.readUTF();
            String fullName = dataInputStream.readUTF();
            String date = dataInputStream.readUTF();
            String email = dataInputStream.readUTF();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dob = LocalDate.parse(date, formatter);
            patient = new Patient(id, email, fullName, dob);
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
            String reportId = dataInputStream.readUTF();
            Patient patient = recievePatient();
            String date = dataInputStream.readUTF();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateReport = LocalDate.parse(date, formatter);
            String patientObservation = dataInputStream.readUTF();
            String doctorObservation = dataInputStream.readUTF();
            List<Symptoms> symptoms = receiveSymptoms();
            Set<Signal> signals = receiveSignals();

            report = new Report(reportId, patient, dateReport, patientObservation, symptoms, signals, doctorObservation);
        } catch (IOException e) {
            System.err.println("Error al leer el flujo de entrada: " + e.getMessage());
        }
        return report;
    }

    public Set<Signal> receiveSignals() throws IOException{
        Set<Signal> signals = new HashSet<>();
        try {
            int numSignals = dataInputStream.readInt();

            for (int i = 0; i < numSignals; i++) {
                String typeSignal = dataInputStream.readUTF();
                SignalType type = SignalType.valueOf(typeSignal);
                String signalId = dataInputStream.readUTF();
                String valuesString = dataInputStream.readUTF();
                Signal signal = new Signal(type, signalId);
                //Esto recibe la lista completa de valores de la señal
                signal.stringToFloatValues(valuesString);
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

    public Doctor receiveDoctor() throws IOException{
        String id = dataInputStream.readUTF();
        String email = dataInputStream.readUTF();
        String fullName = dataInputStream.readUTF();
        String password = dataInputStream.readUTF();
        String date = dataInputStream.readUTF();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dob = LocalDate.parse(date, formatter);
        List<Patient> patients = receivePatients();

        return  new Doctor(id, email, password, fullName, dob, patients);
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
