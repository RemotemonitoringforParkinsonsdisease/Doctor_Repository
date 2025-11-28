package manageData;

import POJOs.Doctor;
import java.io.*;

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

