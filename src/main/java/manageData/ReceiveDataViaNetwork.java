package manageData;


import POJOs.*;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for receiving data sent over a network connection using a {@link DataInputStream}.
 * It reconstructs objects (User, Patient, Report, Doctor, etc.) from the data received.
 *
 * The methods in this class read data in the same order in which they must have been sent by the server app,
 * respecting the expected types and formats.
 *
 * The class also supports receiving CSV files containing signals (ECG,EDA,EMG,ACC)
 * and storing them locally on another file.
 */
public class ReceiveDataViaNetwork {

    private DataInputStream dataInputStream;

    /**
     * Constructor of the class.
     * @param dis
     */
    public ReceiveDataViaNetwork(DataInputStream dis) {
        this.dataInputStream = dis;
    }

    /**
     * Receives a UTF-encoded string sent over the network.
     *
     * @return the received string
     * @throws IOException if an I/O error occurs while reading the stream
     */
    public String receiveString() throws IOException{
        return dataInputStream.readUTF();
    }

    /**
     * Reconstructs a {@link Patient} object from the received data (patientId, dat of birth, fullName).
     *
     * @return the reconstructed Patient
     * @throws IOException if an I/O error occurs while reading the stream
     */
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

    /**
     * Receives a list of patients. It first reads the number of patients,
     * then receives each patient individually.
     *
     * @return a list of received patients
     * @throws IOException if an error occurs during reading
     */
    public List<Patient> receivePatients() throws IOException{
        int numPatients = dataInputStream.readInt();
        List<Patient> patients = new ArrayList<Patient>();
        for (int i = 0; i < numPatients; i++) {
            patients.add(recievePatient());
        }
        return patients;
    }

    /**
     * Receives all reports associated with a patient.
     *
     * @return a list of {@link Report} objects
     * @throws IOException if an error occurs while reading
     */
    public List<Report> receiveReportsOfAPatient() throws IOException{
        List<Report> reports = new ArrayList<>();
        int numberOfReports = dataInputStream.readInt();
        for (int i = 0; i < numberOfReports; i++) {
            reports.add(receiveReport());
        }
        return reports;
    }

    /**
     * Reconstructs a {@link Report} from the received data (reportId, patientId, dob, reportDate, signalsFilePath,
     * symptoms, patientObservation, doctorObservation).
     *
     * @return the reconstructed Report
     * @throws IOException if an I/O error occurs
     */
    public Report receiveReport() throws IOException{
        Report report = null;
        Integer reportId = dataInputStream.readInt();
        Integer patientId = dataInputStream.readInt();
        String date = dataInputStream.readUTF();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate reportDate = LocalDate.parse(date, formatter);
        String signalsFilePath = receiveCSVFile();
        List<Symptoms> symptoms = receiveSymptoms();
        String patientObservation = dataInputStream.readUTF();
        String doctorObservation = dataInputStream.readUTF();
        report = new Report(reportId, patientId, reportDate, signalsFilePath, symptoms, patientObservation, doctorObservation);
        return report;
    }

    /**
     * Receives a list of symptoms encoded as a comma-separated string.
     * Example: {@code "TREMOR,ANXIETY..."}.
     *
     * @return a list of {@link Symptoms} values
     * @throws IOException if an I/O error occurs
     */
    public List<Symptoms> receiveSymptoms() throws IOException{
        List<Symptoms> symptoms = new ArrayList<>();
        String symptomsLine = dataInputStream.readUTF();
        for (String s : symptomsLine.split(",")) {
            symptoms.add(Symptoms.valueOf(s.trim()));
        }
        return symptoms;
    }

    /**
     * Reconstructs a {@link Doctor} object from the received data (userId, doctorId, fullName,
     * doctorPassword, dob, list of patients), including their list of assigned patients.
     *
     * @return the reconstructed Doctor
     * @throws IOException if an error occurs during reading
     */
    public Doctor receiveDoctor() throws IOException{
        Integer userId = dataInputStream.readInt();
        Integer doctorId = dataInputStream.readInt();
        String fullName = dataInputStream.readUTF();
        String doctorPassword = dataInputStream.readUTF();
        String date = dataInputStream.readUTF();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dob = LocalDate.parse(date, formatter);
        Doctor doctor = new Doctor(userId, doctorId, fullName, doctorPassword,dob);
        List<Patient> patients = receivePatients();
        doctor.setPatients(patients);
        return doctor;
    }

    /**
     * Receives a CSV file sent over the network and saves it in the signals_recived/ directory.
     *
     * @return the full path to the saved file
     * @throws IOException if an error occurs while writing the file or reading the stream
     */
    public String receiveCSVFile() throws IOException {
        String fileName = dataInputStream.readUTF();
        long fileSize = dataInputStream.readLong();

        String folder = "signals_recived/";
        Files.createDirectories(Paths.get(folder));

        Path filePath = Paths.get(folder + fileName);
        FileOutputStream fos = new FileOutputStream(filePath.toFile());

        byte[] buffer = new byte[4096];
        long remaining = fileSize;
        int bytesRead;

        while (remaining > 0 && (bytesRead = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, remaining))) != -1) {
            fos.write(buffer, 0, bytesRead);
            remaining -= bytesRead;
        }

        fos.close();
        return filePath.toString();
    }
}