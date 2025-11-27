package ui;

import POJOs.*;
import manageData.ReceiveDataViaNetwork;
import javax.print.Doc;
import java.io.Console;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UI {

    private Connection connection;

    public static void main(String[] args) throws IOException {
        UI ui = new UI();
        //TODO: Excepciones
        ui.startConnection();
    }

    private void startConnection() throws IOException {
        boolean connected = false;

        while (!connected) {
            System.out.println("Select IP Address and Port to connect to :");
            String ipAddress = Utilities.readString("IP Address: ");
            int port = Utilities.readInteger("Port: ");

            this.connection = new Connection(ipAddress, port);
            connected = true;
        }
        try {
            connection.getSendViaNetwork().sendInt(2);
            String message = connection.getReceiveViaNetwork().receiveString();
            if ("DOCTOR".equals(message)) {
                this.preLoggedMenu();
            }
        } catch (IOException e) {
            System.out.println("Error in communication once it was connected.");
        }
    }

    private void preLoggedMenu() throws IOException {
        do {
            int option = 0;
            System.out.println("-----WELCOME TO THE DOCTOR APPLICATION-----");
            System.out.println("1) Register\n2) Log in\n3) Exit");
            option = Utilities.readInteger("Select an option: ");
            switch (option){
                case 1:
                    connection.getSendViaNetwork().sendInt(option);
                    this.registerMenu();
                    break;
                case 2:
                    connection.getSendViaNetwork().sendInt(option);
                    this.loginMenu();
                    break;
                case 3:
                    connection.getSendViaNetwork().sendInt(option);
                    this.exitMenu();
                    break;
                default:
                    System.out.println("Please select a valid option.\n");
                    break;
            }
        } while(true);

    }

    private void registerMenu() throws IOException {
        do {
            System.out.println("\nREGISTER DOCTOR MENU");
            boolean valid;
            String email;
            do {
                email = Utilities.readString("Enter your email: ");
                valid = Utilities.checkEmail(email);
            }while(!valid);
            connection.getSendViaNetwork().sendStrings(email);//le mando el email al servidor para que lo compruebe
            System.out.println("email sent: "+ email);
            String message = connection.getReceiveViaNetwork().receiveString();//Recibo un mensaje del servidor
            System.out.println(message);
            if (message.equals("EMAIL OK")) {
                String fullName = Utilities.readString("Enter your full name: ");
                LocalDate dob = Utilities.readDate("Enter your DOB: ");
                String password = Utilities.readString("Enter your password: ");
                Doctor preDoctor = new Doctor(fullName, password, dob);
                connection.getSendViaNetwork().sendRegisteredDoctor(preDoctor);
                return;
            } else if (message.equals("EMAIL ERROR")) {
                System.out.println("This email is already associated with a doctor");
            }
        }while(true);
    }

    private void loginMenu() throws IOException {
        do {
            System.out.println("\nLOGIN MENU DOCTOR");
            String email;
            boolean valid;
            do {
                email = Utilities.readString("Enter your email: ");
                valid = Utilities.checkEmail(email);

            } while (!valid);
            connection.getSendViaNetwork().sendStrings(email);
            String emailVerification = connection.getReceiveViaNetwork().receiveString();

            if (emailVerification.equals("EMAIL OK")) {
                String password;
                do {
                    password = Utilities.readString("Enter your password: ");
                    if (password == null || password.isEmpty()) {
                        System.out.println("Password cannot be empty.\n");
                    }
                } while (password == null || password.isEmpty());

                connection.getSendViaNetwork().sendStrings(password);
                String passwordVerification = connection.getReceiveViaNetwork().receiveString();

                if (passwordVerification.equals("PASSWORD OK")) {
                    System.out.println("Login successful!\n");
                    this.loggedMenu();
                } else {
                    System.out.println(passwordVerification);
                    loginMenu();
                }
            }else {
                System.out.println(emailVerification);
                return;
            }
        }while (true);
    }

    private void loggedMenu() throws IOException {
        Doctor doctor = connection.getReceiveViaNetwork().receiveDoctor();
        do{
            System.out.println("Welcome Dr " + doctor.getFullName() + "!\n");
            System.out.println("\nMAIN DOCTOR MENU");
            System.out.println("0) Log out");
            if(doctor.getPatients().isEmpty()){
                System.out.println("Dr "+ doctor.getFullName()+ " you have no patients yet.\n");
            }
            for(int i = 1; i <= doctor.getPatients().size(); i++){
                System.out.println(i+")"+doctor.getPatients().get(i-1).getFullName());
            }
            int option = Utilities.readInteger("Select an option: ");
            if(option == 0){
                connection.getSendViaNetwork().sendInt(0);
               this.preLoggedMenu();
            }else if(option >= 1 && option <= doctor.getPatients().size()){
                connection.getSendViaNetwork().sendInt(1);
                this.patientMenu(doctor.getPatients().get(option - 1));
            }else System.out.println("Please select a valid option.\n");
        } while(true);
    }

    private void patientMenu(Patient patient) throws IOException {
        connection.getSendViaNetwork().sendInt(patient.getPatientId());//Se manda el id del paciente al server
        patient.setReports(connection.getReceiveViaNetwork().receiveReportsOfAPatient());//Se recibe los records de dicho patient
        int option = 0;
        do{
            System.out.println("\nPATIENT MENU: " + patient.getFullName());
            System.out.println("0)Back to Patient List");
            for(int i = 0; i < patient.getReports().size(); i++){
                System.out.println((i+1) + ") Report Date: " + patient.getReports().get(i).getReportDate());
            }
            option = Utilities.readInteger("Select an option: ");
            if(option == 0){
                connection.getSendViaNetwork().sendInt(0);
                return;
            }
            if (option >= 1 && option <= patient.getReports().size()){
                this.reportMenu(patient.getReports().get(option - 1));
            }
        } while(true);
    }

    private void reportMenu(Report report) throws IOException {
        System.out.println("\nREPORT MENU: Report Date " + report.getReportDate());
        System.out.println("Patient Observation: " + report.getPatientObservation());
        System.out.println("Symptoms: " + report.getSymptoms());
        //receiveCSVFile() //TODO
        System.out.println(printCSV()); //TODO
        int option;
        do{
            System.out.println("0) Back to the Patient Menu");
            System.out.println("1) Add an observation");
                option = Utilities.readInteger("Select an option: ");
                switch(option){
                    case 0: return;
                    case 1:
                        this.addObservationMenu(report);
                        break;
                    default:
                        System.out.println("Please select a valid option.\n");
                        break;
                }
        } while(true);
    }

    private void addObservationMenu(Report report) throws IOException {
        String doctorObervation = Utilities.readString("Introduce the observation: ");
        report.setDoctorObservation(doctorObervation);
        connection.getSendViaNetwork().sendInt(report.getReportId());
        connection.getSendViaNetwork().sendStrings(doctorObervation);
        String message = connection.getReceiveViaNetwork().receiveString();
        System.out.println(message);
    }

    private void exitMenu(){
        System.out.println("Exiting Doctor Application");
        connection.releaseResources(); //Alomejor deberíamos envíar algo de feedback al server
        System.exit(0);
    }

}
