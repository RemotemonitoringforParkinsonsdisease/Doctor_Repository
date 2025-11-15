package manageData;

import POJOs.Doctor;
import POJOs.Patient;
import POJOs.Report;

import java.io.DataInputStream;
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

    public Patient receivePatient() throws IOException{
        String email = dataInputStream.readUTF();
        String fullName = dataInputStream.readUTF();
        String date = dataInputStream.readUTF();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dob = LocalDate.parse(date, formatter);
        return new Patient(email, fullName, dob);
    }

    public List<Patient> receivePatients() throws IOException{
        int numPatients = dataInputStream.readInt();
        List<Patient> patients = new ArrayList<Patient>();
        for (int i = 0; i < numPatients; i++) {
            patients.add(receivePatient());
        }
        return patients;
    }

    //TODO
    /*public Report receiveReport() throws IOException{

    }*/

    //TODO
    /*
    public Signal receiveSignal() throws IOException{
    }
    */

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
        } catch (IOException ex) {
            System.err.println("Error al recibir int: " + ex.getMessage());
            ex.printStackTrace();
        }
        return message;
    }

    public void releaseResources() {
        try {
            if (dataInputStream != null) {
                dataInputStream.close();
            }
        } catch (IOException ex) {
            System.err.println("Error al liberar los recursos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
