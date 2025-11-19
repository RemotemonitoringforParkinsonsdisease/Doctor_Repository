package ui;

import POJOs.Doctor;
import POJOs.Patient;
import POJOs.Report;
import manageData.ReceiveDataViaNetwork;

import java.io.Console;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;

public class UI {

    private Connection connection;

    public static void main(String[] args) {

        UI ui = new UI();
        //TODO: Excepciones
        //TODO: Ver si salir o volver al menu anterior
        ui.startConnection();
        ui.preLoggedMenu();


    }
    private void startConnection() { //TODO: Iterar hasta conexión correcta
        System.out.println("Select IP Address and Port to connect to :");
        String ipAddress = Utilities.readString("IP Address: ");
        int port = Utilities.readInteger("Port: ");
        this.connection = new Connection(ipAddress, port);
    }
    private void preLoggedMenu(){
        System.out.println("WELCOME TO THE DOCTOR APPLICATION\n\n");
        int option = 0;
        do {
            System.out.println("1) Login\n2) Register\n3) Exit");
            option = Utilities.readInteger("Select an option: ");
            switch (option){
                case 1: this.loginMenu();
                    break;
                case 2: this.registerMenu();
                    break;
                case 3: this.exitMenu();
                    break;
                default:
                    System.out.println("Please select a valid option.\n");
                    break;
            }
        } while(true);

    }
    private void registerMenu(){
        System.out.println("\nREGISTER MENU");
        String fullName = Utilities.readString("Enter your full name: ");
        LocalDate dob = Utilities.readDate("Enter your DOB: ");
        String email = Utilities.readString("Enter your email: ");
        String password = Utilities.readString("Enter your password: ");
        //Enviar al servidor los datos para registrar
        //Se carga el propio doctor y sus pacientes
        //Una vez este confirmado
        this.loggedMenu();
    }
    private void loginMenu() throws IOException {
        System.out.println("\nLOGIN MENU DOCTOR");

        String email;
        boolean valid;
        do {
            email = Utilities.readString("Enter your email: ");
            valid = Utilities.checkEmail(email);

            if (!valid) {
                System.out.println("Please follow the email format: example@example.com\n");
            }
        } while (!valid);
        connection.getSendViaNetwork().sendStrings(email);

        String password;
        do {
            password = Utilities.readString("Enter your password: ");
            if (password == null || password.isEmpty()) {
                System.out.println("Password cannot be empty.\n");
            }
        } while (password == null || password.isEmpty());
        connection.getSendViaNetwork().sendStrings(password);

        String serverResponse = connection.getReceiveViaNetwork().receiveString();

        if (serverResponse.equals("OK")) {
            System.out.println("Login successful!\n");
            Doctor loggedDoctor = connection.getReceiveViaNetwork().receiveDoctor();
            System.out.println("Welcome " + loggedDoctor.getFullName() + "!\n");
            this.loggedMenu(loggedDoctor);
        } else {
            System.out.println("Login failed. Incorrect email or password.\n");
            loginMenu();
        }
    }
    private void loggedMenu(Doctor doctor) throws IOException {
        System.out.println("\nMAIN MENU");
        int option = 0;
        do{
            System.out.println("1) View Patients\n2) Exit");
            switch (option = Utilities.readInteger("Select an option: ")){
                case 1:
                    this.patientListMenu(doctor);
                    break;
                case 2:
                    this.exitMenu();
                    break;
                default:
                    System.out.println("Please select a valid option.\n");
                    break;
            }
        } while(true);
    }
    private void patientListMenu(Doctor doctor){
        System.out.println("\nPATIENT LIST MENU");
        List<Patient> patients = doctor.getPatients();
        int option = 0;
        do{
            System.out.println("0) Back to Main Menu");
            for(int i = 0; i < patients.size(); i++){
                System.out.println((i+1) + ") " + patients.get(i).getFullName());
            }
            option = Utilities.readInteger("Select a patient: ");
            if(option == 0){
                return;
            }
            if (option >= 1 && option <= patients.size()){
                Patient patient = patients.get(option - 1);
                this.patientMenu(patient);
            }
            System.out.println("Please select a valid option.\n");
        } while(true);
    }
    private void patientMenu(Patient patient){
        connection.getSendViaNetwork().sendStrings(patient.getId());//Se manda el id del paciente al server
        patient.setReports(connection.getReceiveViaNetwork().receiveReportsOfAPatient(patient));//Se recibe los records de dicho patient
        System.out.println("\nPATIENT MENU: " + patient.getFullName());
        int option = 0;
        do{
            System.out.println("0)Back to Patient List");
            for(int i = 0; i < patient.getReports().size(); i++){
                System.out.println((i+1) + ") Report Date: " + patient.getReports().get(i).getReportDate());
            }
            option = Utilities.readInteger("Select a report: ");
            if(option == 0){
                return;
            }
            if (option <= patient.getReports().size() + 1){
                this.reportMenu(patient.getReports().get(option - 1));
            }
        } while(true);
    }
    private void reportMenu(Report report){
        System.out.println("\nREPORT MENU: Report Date " + report.getReportDate());
        System.out.println("Patient Observation: " + report.getPatientObservation());
        System.out.println("Symptoms: " + report.getSymptoms().toString()); //TODO: Chekear el toString()
        System.out.println("Signals: " + report.getSignals().toString() + "\n"); //TODO: Chekear el toString()

        int option = 0;
        do{
            System.out.println("0) Back to Report Menu");
            System.out.println("1) Back to the Patient Menu");
            System.out.println("2) Access signal data");
            System.out.println("3) Add an observation");
            option = Utilities.readInteger("Select an option: ");
            switch (option){
                case 0:
                    return;
                case 1:
                    this.patientMenu(report.getPatient());
                    break;
                case 2:
                    this.signalMenu(report);
                    break;
                case 3:
                    this.addObservationMenu(report);
                    break;
                default:
                    System.out.println("Please select a valid option.\n");
                    break;
            }
        } while (true);
    }
    private void exitMenu(){

    }

}
