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
            System.out.println("----------------------------------------------");

            try {
                this.connection = new Connection(ipAddress, port);
                connected = true;
                System.out.print("""
                ╔══════════════════════════════════════════════╗
                ║             CONNECTION SUCCESS!              ║
                ╚══════════════════════════════════════════════╝
                """);
                System.out.println("----------------------------------------------");

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
                    System.out.println("Please select a valid option.\n");
                    System.out.println("----------------------------------------------");

                    break;
            }
            System.out.println("----------------------------------------------");
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
            //System.out.println("email sent: "+ email);
            String message = connection.getReceiveViaNetwork().receiveString();//Recibo un mensaje del servidor
            //System.out.println(message);
            if (message.equals("EMAIL OK")) {
                System.out.println("""
                ╔══════════════════════════════════════════════╗
                ║                EMAIL ACCEPTED                ║
                ╚══════════════════════════════════════════════╝
                """);
                String fullName = Utilities.readString("-> Enter your full name: ");
                LocalDate dob = Utilities.readDate("-> Enter your DOB: ");
                String password = Utilities.readString("-> Enter your password: ");
                Doctor preDoctor = new Doctor(fullName, password, dob);
                connection.getSendViaNetwork().sendRegisteredDoctor(preDoctor);
                return;
            } else if (message.equals("EMAIL ERROR")) {
                System.out.println("-> This email is already associated with a doctor! ");
                return;
            }
        }while(true);
    }

    private void loginMenu() throws IOException {
        do {
            System.out.println("""
            ╔════════════════════════════════════════╗
            ║             LOGIN DOCTOR MENU          ║
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
                System.out.println("""
                ╔══════════════════════════════════════════════╗
                ║                EMAIL ACCEPTED                ║
                ╚══════════════════════════════════════════════╝
                """);

                String password;
                do {
                    password = Utilities.readString("-> Enter your password: ");
                    if (password == null || password.isEmpty()) {
                        System.out.println("----------------------------------------------");
                        System.out.println("-> Password cannot be empty!");
                        System.out.println("----------------------------------------------");
                    }
                } while (password == null || password.isEmpty());

                connection.getSendViaNetwork().sendStrings(password);
                String passwordVerification = connection.getReceiveViaNetwork().receiveString();

                if (passwordVerification.equals("PASSWORD OK")) {
                    System.out.println("""
                    ╔══════════════════════════════════════════════╗
                    ║               LOGIN SUCCESSFUL               ║
                    ╚══════════════════════════════════════════════╝
                    """);
                    this.loggedMenu();
                } else {
                    System.out.println("""
                    ╔══════════════════════════════════════════════╗
                    ║               PASSWORD INCORRECT             ║
                    ╚══════════════════════════════════════════════╝
                    """);
                    return;
                }
            }else {
                System.out.println("""
                ╔══════════════════════════════════════════════╗
                ║                EMAIL NOT FOUND               ║
                ╚══════════════════════════════════════════════╝
                """);
                return;
            }
        }while (true);
    }

    private void loggedMenu() throws IOException {
        Doctor doctor = connection.getReceiveViaNetwork().receiveDoctor();
        do{
            System.out.println("""
            ╔════════════════════════════════════════╗
            ║              MAIN DOCTOR MENU          ║
            ╚════════════════════════════════════════╝
            """);
            System.out.println("-> Welcome Dr " + doctor.getFullName() + "! \n");
            System.out.println("----------------------------------------------");
            System.out.println("""
            ╔════════════════════════════════════════╗
            ║              0) Log out                ║
            ║              1) See Patients           ║
            ╚════════════════════════════════════════╝
            """);
            int option = Utilities.readInteger("-> Select an option: ");

            if (option == 0) {
                connection.getSendViaNetwork().sendInt(0);
                this.preLoggedMenu();

            } else if (option == 1) {
                connection.getSendViaNetwork().sendInt(1);
                showPatientList(doctor);

            } else {
                System.out.println("\n-> Please select a valid option! \n");
            }
        } while(true);
    }

    public void showPatientList(Doctor doctor) throws IOException {
        System.out.println("""
        ╔════════════════════════════════════════╗
        ║              YOUR PATIENTS             ║
        ╚════════════════════════════════════════╝
        """);

        if (doctor.getPatients().isEmpty()) {
            System.out.println("-> You currently have no assigned patients! \n");
            return;
        }

        for (int i = 1; i <= doctor.getPatients().size(); i++) {
            System.out.println("  " + i + ") " + doctor.getPatients().get(i - 1).getFullName());
        }

        System.out.println("\n----------------------------------------------");
        int option = Utilities.readInteger("-> Select a patient (0 to go back): ");
        if (option == 0) {
            return;
        }

        if (option >= 1 && option <= doctor.getPatients().size()) {
            connection.getSendViaNetwork().sendInt(1);
            this.patientMenu(doctor.getPatients().get(option - 1));
        } else {
            System.out.println("-> Invalid option! \n");
        }
    }

    private void patientMenu(Patient patient) throws IOException {
        connection.getSendViaNetwork().sendInt(patient.getPatientId());//Se manda el id del paciente al server
        patient.setReports(connection.getReceiveViaNetwork().receiveReportsOfAPatient());//Se recibe los records de dicho patient
        int option = 0;
        do{
            System.out.println("""
            ╔════════════════════════════════════════╗
            ║               PATIENT MENU             ║
            ╚════════════════════════════════════════╝
            """);
            System.out.println("-> Patient: " + patient.getFullName() + "\n");
            System.out.println("----------------------------------------------");

            System.out.println("""
            ╔════════════════════════════════════════╗
            ║             0) Back to Patients        ║
            ╟────────────────────────────────────────╢
            ║             Reports Available          ║
            ╚════════════════════════════════════════╝
            """);

            if (patient.getReports().isEmpty()) {
                System.out.println("-> This patient has no reports yet! \n");
            } else {
                for (int i = 0; i < patient.getReports().size(); i++) {
                    System.out.println("  " + (i + 1) + ") Report Date: " + patient.getReports().get(i).getReportDate());
                }
                System.out.println();
            }

            option = Utilities.readInteger("-> Select an option: ");

            // Volver atrás
            if (option == 0) {
                connection.getSendViaNetwork().sendInt(0);
                return;
            }

            // Acceder a report
            if (option >= 1 && option <= patient.getReports().size()) {
                connection.getSendViaNetwork().sendInt(1);
                this.reportMenu(patient.getReports().get(option - 1));
            } else {
                System.out.println("\n-> Invalid option. Please try again! \n");
            }
        } while(true);
    }

    private void reportMenu(Report report) throws IOException {
        System.out.println("""
        ╔════════════════════════════════════════╗
        ║                REPORT MENU             ║
        ╚════════════════════════════════════════╝
        """);

        System.out.println("-> Report Date: " + report.getReportDate());
        System.out.println("----------------------------------------------");

        System.out.println("-> Patient Observation:");
        System.out.println("   " + report.getPatientObservation() + "\n");

        System.out.println("-> Symptoms:");
        System.out.println("   " + report.getSymptoms() + "\n");

        System.out.println("-> Recorded Signals (CSV file):");
        Utilities.printCSVFile(report.getSignalsFilePath());

        System.out.println("----------------------------------------------");

        int option;

        do {
            System.out.println("""
            ╔════════════════════════════════════════╗
            ║              0) Back to Menu           ║
            ║              1) Add Observation        ║
            ╚════════════════════════════════════════╝
            """);
            option = Utilities.readInteger("-> Select an option: ");

            switch (option) {
                case 0:
                    return;
                case 1:
                    this.addObservationMenu(report);
                    return;
                default:
                    System.out.println("\n-> Please select a valid option.\n");
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
