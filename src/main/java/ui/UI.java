package ui;

import java.io.Console;
import java.net.Socket;
import java.time.LocalDate;

public class UI {

    private Connection connection;

    public static void main(String[] args) {

        UI ui = new UI();

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


    }
    private void exitMenu(){

    }

}
