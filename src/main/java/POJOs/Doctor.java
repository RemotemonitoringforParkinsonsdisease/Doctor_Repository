package POJOs;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Doctor extends User {
    private String password; //TODO: Encriptar;
    private LocalDate dob;
    private List<Patient> patients;

    //Constrctor al registrarse (antes enviar a server)
    public Doctor(String email, String fullName, String password, LocalDate dob) {
        super(email, fullName);
        this.password = password;
        this.dob = dob;
    }
    //Constructor al hacer login (antes de enviar a server)
    public Doctor(String email, String password){
        super(email);
        this.password = password;
    }

    //Constructor con todos los atributos (una vez recibido del server), o llamamos a este constructor, o hacemos uno sin id y sin pacientes, y usamos setters
    public Doctor(String id, String email, String fullName ,String password, LocalDate dob, List<Patient> patients) {
        super(id, email, fullName);
        this.password = password;
        this.dob = dob;
        this.patients = patients;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }
}
