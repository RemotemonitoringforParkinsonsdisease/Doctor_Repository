package ui;

import POJOs.Patient;
import POJOs.Report;

import java.io.Console;
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
    private void loginMenu(){
        System.out.println("\nLOGIN MENU");
        String email = Utilities.readString("Enter your email: ");
        String password = Utilities.readString("Enter your password: ");
        //Enviar al servidor los datos para loguear
        //Se carga el propio doctor
        //Una vez este confirmado
        this.loggedMenu();
    }
    private void loggedMenu(){
        System.out.println("\nMAIN MENU");
        int option = 0;
        do{
            System.out.println("1) View Patients\n2) Exit");
            switch (option = Utilities.readInteger("Select an option: ")){
                case 1:
                    this.patientListMenu();
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
    private void patientListMenu(){
        System.out.println("\nPATIENT LIST MENU");
        List<Patient> patients; //TODO: Cargar pacientes del doctor logueado
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
            if (option <= patients.size() + 1){
                Patient patient = patients.get(option - 1);
                this.patientMenu(patient);
            }
            System.out.println("Please select a valid option.\n");
        } while(true);
    }
    private void patientMenu(Patient patient){
        List<Report> reports; //TODO: Cargar reports del paciente seleccionado
        System.out.println("\nPATIENT MENU: " + patient.getFullName());
        int option = 0;
        do{
            System.out.println("0)Back to Patient List");
            for(int i = 0; i < reports.size(); i++){
                System.out.println((i+1) + ") Report Date: " + reports.get(i).getReportDate());
            }
            option = Utilities.readInteger("Select a report: ");
            if(option == 0){
                return;
            }
            if (option <= reports.size() + 1){
                Report report = reports.get(option - 1);
                this.reportMenu(report);
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
