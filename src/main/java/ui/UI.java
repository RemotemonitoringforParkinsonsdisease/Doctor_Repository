package ui;

import java.io.Console;
import java.net.Socket;

public class UI {

    private Connection connection;

    public static void main(String[] args) {

        UI ui = new UI();
        // Ejemplo de ip y puerto, hay que ver si se solicita por consola o se mantiene constante
        String ipAddress = "localhost";
        int port = 9000;
        ui.startConnection(ipAddress, port);

        preLoggedMenu();


    }
    private void startConnection(String ipAddress, int port) {
        this.connection = new Connection(ipAddress, port);
    }
    private static void preLoggedMenu(){
        System.out.println("WELCOME TO THE DOCTOR APPLICATION\n\n");
        int option = 0;
        do {
            System.out.println("1) Login\n2) Register\n3) Exit\n");
            option = Utilities.readInteger("Select an option: ");
            switch (option){
                case 1: loginMenu();
                    break;
                case 2: registerMenu();
                    break;
                case 3: exitMenu();
                    break;
                default:
                    System.out.println("Please select a valid option.\n");
                    break;
            }
        } while(option != 3);

    }
    private static void registerMenu(){

    }
    private static void loginMenu(){

    }
    private static void exitMenu(){

    }
    private static void LoggedMenu(){
        //Cargas del servidor tu propio Doctor, y tus Patient


    }


}
