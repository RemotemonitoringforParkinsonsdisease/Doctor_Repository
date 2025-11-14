package manageData;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ReceiveDataViaNetwork {


    private DataInputStream dataInputStream;

    /**
     * Constructs a new ReceiveDataViaNetwork instance using the provided socket.
     * Initializes the data input stream for receiving data.
     *
     * @param socket the socket used to establish the network connection.
     */
    public ReceiveDataViaNetwork(Socket socket) {
        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ReceiveDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Receives a string from the connected socket.
     *
     * @return the received string.
     * @throws IOException if there is an error reading from the socket.
     */
    public String receiveString() throws IOException {
        String information;
        information= dataInputStream.readUTF();
        return  information;
    }

    /**
     * Receives a {@link Doctor} object from the connected socket.
     *
     * @return the received {@link Doctor} object, or {@code null} if an error occurs.
     */
    public Doctor receiveDoctor() throws IOException{
        Doctor doctor;
        int id = dataInputStream.readInt();
        String name = dataInputStream.readUTF();
        String surname = dataInputStream.readUTF();
        String date = dataInputStream.readUTF();
        String email = dataInputStream.readUTF();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dob = LocalDate.parse(date, formatter);
        doctor = new Doctor(id, name, surname, dob, email);
        return doctor;
    }

    /**
     * Receives a {@link Patient} object from the connected socket.
     *
     * @return the received {@link Patient} object, or {@code null} if an error occurs.
     */
    public Patient receivePatient() throws IOException{
        Patient patient = null;
        int id = dataInputStream.readInt();
        String name = dataInputStream.readUTF();
        String surname = dataInputStream.readUTF();
        String date = dataInputStream.readUTF();
        String email = dataInputStream.readUTF();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dob = LocalDate.parse(date, formatter);
        patient = new Patient(id, name, surname, dob, email);
        return patient;
    }

    /**
     * Receives an {@link Interpretation} object from the connected socket.
     *
     * @return the received {@link Interpretation} object, or {@code null} if an error occurs.
     */
    public Interpretation receiveInterpretation() throws IOException{
        Interpretation interpretation;
        String stringDate = dataInputStream.readUTF();
        int doctor_id = dataInputStream.readInt();
        String stringEMG = dataInputStream.readUTF();
        int patient_id = dataInputStream.readInt();
        String stringEDA = dataInputStream.readUTF();
        String observation = dataInputStream.readUTF();
        String interpretation1 = dataInputStream.readUTF();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(stringDate, formatter);
        Signal signalEMG = new Signal(Signal.SignalType.EMG);
        signalEMG.setValuesEMG(stringEMG);
        Signal signalEDA = new Signal(Signal.SignalType.EDA);
        signalEDA.setValuesEDA(stringEDA);
        interpretation = new Interpretation(date, interpretation1, signalEMG, signalEDA, patient_id, doctor_id, observation);

        return interpretation;
    }

    /**
     * Receives an integer from the connected socket.
     *
     * @return the received integer, or a default value of 10 if an error occurs.
     */
    public int receiveInt() throws IOException{
        int message = dataInputStream.readInt();

        return message;
    }

    /**
     * Releases resources by closing the data input stream.
     */
    public void releaseResources() {
        try {
            dataInputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(ReceiveDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}


