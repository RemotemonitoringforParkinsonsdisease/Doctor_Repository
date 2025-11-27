package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    public static int readInteger(String question) {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader buffer = new BufferedReader(input);
        int num;
        String line;
        while (true) {
            try {
                System.out.print(question);
                line = buffer.readLine();
                num = Integer.parseInt(line);
                return num;

            } catch (IOException ioe) {
                System.out.println(" ERROR: Unable to read.");

            } catch (NumberFormatException nfe) {
                System.out.println(" ERROR: Must be a whole number.");
            }
        }
    }

    public static LocalDate readDate(String question) {

        while (true) {
            try {
                System.out.println(question);
                int day = readInteger("   Day: ");
                int month = readInteger("   Month: ");
                int year = readInteger("   Year: ");
                return LocalDate.of(year, month, day);

            } catch (DateTimeException e) {
                System.out.println(" ERROR: Introduce a valid date.");
            }

        }
    }

    public static String readString(String question) {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader buffer = new BufferedReader(input);
        String line;
        while (true) {
            try {
                System.out.print(question);
                line = buffer.readLine();
                return line;

            } catch (IOException ioe) {
                System.out.println(" ERROR: Unable to read.");
            }
        }
    }

    public static boolean checkEmail(String email) {
        Pattern pattern = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");
        Matcher mather = pattern.matcher(email);
        System.out.println("Checking email");
        if (mather.find()) {
            System.out.println("email is valid");
            return true;
        } else {
            System.out.println("Please follow the email format: example@example.com");
            return false;
        }
    }

    public static void printCSVFile(String filePath) {
        System.out.println("\n----- CSV FILE CONTENT -----\n");

        try {
            Path path = Paths.get(filePath);

            // Leer todas las líneas del archivo y escribirlas en consola
            Files.lines(path).forEach(System.out::println);

            System.out.println("\n----- END OF FILE -----\n");

        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
