package ui;

import POJOs.*;
import java.io.IOException;
import java.time.LocalDate;

public class UI {

    private Connection connection;

    public static void main(String[] args) throws IOException {
        UI ui = new UI();
        ui.startConnection();
    }

    private void startConnection() throws IOException {
        boolean connected = false;

        while (!connected) {
            System.out.println("""
            ╔══════════════════════════════════════════════╗
            ║           DOCTOR APPLICATION - CONNECT       ║
            ║                                              ║
            ║   Please enter the server connection info    ║
            ╚══════════════════════════════════════════════╝
            """);
            String ipAddress = Utilities.readString("-> IP Address: ");
            int port = Utilities.readInteger("-> Port: ");

            try {
                this.connection = new Connection(ipAddress, port);
                connected = true;
                System.out.print("""
                ╔══════════════════════════════════════════════╗
                ║             CONNECTION SUCCESS!              ║
                ╚══════════════════════════════════════════════╝
                """);

            } catch (Exception e) {
                System.out.println("""
                ╔══════════════════════════════════════════════╗
                ║             CONNECTION FAILED!               ║
                ╚══════════════════════════════════════════════╝
                """);
                System.out.println("-> Could not connect to " + ipAddress + ":" + port);
                System.out.println("-> Please try it again!\n");
                System.out.println("----------------------------------------------");
            }
        }
        try {
            connection.getSendViaNetwork().sendInt(2);
            String message = connection.getReceiveViaNetwork().receiveString();
            if ("DOCTOR".equals(message)) {
                this.preLoggedMenu();
            }
        } catch (IOException e) {
            System.out.println("-> Error in communication once it was connected! ");
            System.out.println("----------------------------------------------");
        }
    }

    private void preLoggedMenu() throws IOException {
        do {
            int option = 0;
            System.out.println("""
            ╔════════════════════════════════════════╗
            ║     WELCOME TO DOCTOR APPLICATION      ║
            ║                                        ║
            ║     1) Register                        ║
            ║     2) Log in                          ║
            ║     3) Exit                            ║
            ╚════════════════════════════════════════╝
            """);
            option = Utilities.readInteger("-> Select an option: ");
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
                    System.out.println("-> Please select a valid option!");
                    System.out.println("----------------------------------------------");
                    break;
            }
        } while(true);
    }

    private void registerMenu() throws IOException {
        do {
            System.out.println("""
            ╔════════════════════════════════════════╗
            ║          REGISTER DOCTOR MENU          ║
            ╚════════════════════════════════════════╝
            """);
            boolean valid;
            String email;
            do {
                email = Utilities.readString("-> Enter your email: ");
                valid = Utilities.checkEmail(email);

            }while(!valid);

            connection.getSendViaNetwork().sendStrings(email);//le mando el email al servidor para que lo compruebe
            String message = connection.getReceiveViaNetwork().receiveString();//Recibo un mensaje del servidor

            if (message.equals("EMAIL OK")) {
                System.out.println("-> Email accepted! ");
                System.out.println("----------------------------------------------");
                String fullName = Utilities.readString("-> Enter your full name: ");
                LocalDate dob = Utilities.readDate("-> Enter your DOB: ");
                String password = Utilities.readString("-> Enter your password: ");
                Doctor preDoctor = new Doctor(fullName, password, dob);
                connection.getSendViaNetwork().sendRegisteredDoctor(preDoctor);
                System.out.println("-> Register successful! ");
                System.out.println("----------------------------------------------");
                return;
            } else if (message.equals("EMAIL ERROR")) {
                System.out.println("-> This email is already associated with a doctor! ");
                System.out.println("----------------------------------------------");
                return;
            }
        }while(true);
    }

    private void loginMenu() throws IOException {
        do {
            System.out.println("""
            ╔════════════════════════════════════════╗
            ║            DOCTOR LOGIN MENU           ║
            ╚════════════════════════════════════════╝
            """);
            String email;
            boolean valid;
            do {
                email = Utilities.readString("-> Enter your email: ");
                valid = Utilities.checkEmail(email);

            } while (!valid);
            connection.getSendViaNetwork().sendStrings(email);
            String emailVerification = connection.getReceiveViaNetwork().receiveString();

            if (emailVerification.equals("EMAIL OK")) {
                System.out.println("-> Email accepted! ");
                System.out.println("----------------------------------------------");

                String password;
                do {
                    password = Utilities.readString("-> Enter your password: ");
                    if (password == null || password.isEmpty()) {
                        System.out.println("-> Password cannot be empty!");
                        System.out.println("----------------------------------------------");
                    }
                } while (password == null || password.isEmpty());

                connection.getSendViaNetwork().sendStrings(password);
                String passwordVerification = connection.getReceiveViaNetwork().receiveString();

                if (passwordVerification.equals("PASSWORD OK")) {
                    System.out.println("-> Login successful! ");
                    System.out.println("----------------------------------------------");
                    this.loggedMenu();
                } else {
                    System.out.println("-> Password incorrect! ");
                    System.out.println("----------------------------------------------");
                    return;
                }
            }else {
                System.out.println("-> Email not found! ");
                System.out.println("----------------------------------------------");
                return;
            }
        }while (true);
    }

    private void loggedMenu() throws IOException {
        Doctor doctor = connection.getReceiveViaNetwork().receiveDoctor();
        do{
            System.out.println("""
            ╔════════════════════════════════════════╗
            ║           DOCTOR MAIN MENU             ║
            ╚════════════════════════════════════════╝
            """);
            System.out.println("-> Welcome Dr " + doctor.getFullName() + "!");

            if(doctor.getPatients().isEmpty()){
                System.out.println("-> Dr "+ doctor.getFullName()+ " you have no patients yet! ");
                System.out.println("----------------------------------------------");
                connection.getSendViaNetwork().sendInt(0);
                this.preLoggedMenu();
            }else{
                System.out.println("-> Your patients are: ");
                System.out.println("**********************************************");
                for (int i = 1; i <= doctor.getPatients().size(); i++) {
                    System.out.println(i + ") " + doctor.getPatients().get(i - 1).getFullName());
                }
                System.out.println("**********************************************");
            }
            int option = Utilities.readInteger("-> Select a patient to see its info (press 0 to log out): ");
            if (option == 0) {
                connection.getSendViaNetwork().sendInt(0);
                preLoggedMenu();
            } else if (option >= 1 && option <= doctor.getPatients().size()) {
                connection.getSendViaNetwork().sendInt(1);
                this.patientMenu(doctor.getPatients().get(option - 1));
            } else {
                System.out.println("-> Please select a valid option!");
                System.out.println("----------------------------------------------");
            }
        } while(true);
    }

    private void patientMenu(Patient patient) throws IOException {
        connection.getSendViaNetwork().sendInt(patient.getPatientId());//Se manda el id del paciente al server
        patient.setReports(connection.getReceiveViaNetwork().receiveReportsOfAPatient());//Se recibe los records de dicho patient
        int option = 0;
        do{
            System.out.println("""
            ╔════════════════════════════════════════╗
            ║              PATIENT INFO              ║
            ╚════════════════════════════════════════╝
            """);
            System.out.println("-> Patient: " + patient.getFullName());
            System.out.println("**********************************************");
            for(int i = 0; i < patient.getReports().size(); i++){
                System.out.println((i+1) + ") Report Date: " + patient.getReports().get(i).getReportDate());
            }
            System.out.println("**********************************************");
            option = Utilities.readInteger("-> Select a report to review (press 0 to go to the main doctor menu): ");
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
        System.out.println("""
            ╔════════════════════════════════════════╗
            ║              REPORT INFO               ║
            ╚════════════════════════════════════════╝
            """);
        System.out.println("-> Report Date " + report.getReportDate());
        System.out.println("-> Patient Observation: " + report.getPatientObservation());
        if(report.getDoctorObservation() != ""){
            System.out.println("-> Doctor Observation: " + report.getDoctorObservation());
        }
        System.out.println("-> Symptoms: ");
        for(int i = 0; i < report.getSymptoms().size(); i++){
            System.out.println((i+1) + ") " + report.getSymptoms().get(i));
        }
        Utilities.printCSVFile(report.getSignalsFilePath());
        int option;
        do{
            System.out.println("""
            ╔════════════════════════════════════════════╗
            ║      0) Back to the patient info menu      ║
            ║      1) Add an observation                 ║
            ╚════════════════════════════════════════════╝
            """);
            option = Utilities.readInteger("-> Select an option: ");
            switch(option){
                case 0:
                    return;
                case 1:
                    connection.getSendViaNetwork().sendInt(1);
                    this.addObservationMenu(report);
                    return;
                default:
                    System.out.println("-> Please select a valid option! ");
                    break;
                }
        } while(true);
    }

    private void addObservationMenu(Report report) throws IOException {
        System.out.println("----------------------------------------------");
        String doctorObervation = Utilities.readString("-> Introduce the observation: ");
        report.setDoctorObservation(doctorObervation);
        connection.getSendViaNetwork().sendInt(report.getReportId());
        connection.getSendViaNetwork().sendStrings(doctorObervation);
        connection.getReceiveViaNetwork().receiveString();
        System.out.println("-> The observation has been added to the report! ");
        System.out.println("----------------------------------------------");
    }

    private void exitMenu() {
        System.out.println("""
                ╔════════════════════════════════════════╗
                ║      EXITING DOCTOR APPLICATION        ║
                ╚════════════════════════════════════════╝
                """);
        System.out.println("-> Closing Doctor Application...");
        System.out.println("-> Releasing resources...");
        System.out.println("----------------------------------------------");
        connection.releaseResources(); // (Opcional) enviar feedback al server
        System.out.println("-> Goodbye!");
        System.out.println("----------------------------------------------");

        System.exit(0);
    }
}