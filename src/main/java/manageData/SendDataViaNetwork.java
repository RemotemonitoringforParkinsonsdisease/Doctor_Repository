package manageData;
import POJOs.Doctor;
import POJOs.Patient;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SendDataViaNetwork {

    private DataOutputStream dataOutputStream;

    public SendDataViaNetwork(DataOutputStream dos) {
        this.dataOutputStream = dos;
    }
    public void sendStrings(String message) throws IOException {
        dataOutputStream.writeUTF(message);
    }
    public void sendInt(Integer message) throws IOException{
        dataOutputStream.writeInt(message);
    }

    public void sendRegisteredDoctor(Doctor doctor) throws IOException{
        dataOutputStream.writeUTF(doctor.getFullName());
        dataOutputStream.writeUTF(doctor.getDoctorPassword());
        dataOutputStream.writeUTF(doctor.getDob().toString());
    }
}

