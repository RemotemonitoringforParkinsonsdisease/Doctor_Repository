package manageData;
import POJOs.Doctor;
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SendDataViaNetwork {


    private DataOutputStream dataOutputStream;


    public SendDataViaNetwork(Socket socket) {
        try {
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void sendStrings(String message) throws IOException {
        dataOutputStream.writeUTF(message);
    }


    public void sendInt(Integer message) throws IOException{
        dataOutputStream.writeInt(message);
    }




    public void sendDoctor(Doctor doctor) throws IOException{
        dataOutputStream.writeInt(doctor);

    }


    public void releaseResources() {
        try {
            dataOutputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(SendDataViaNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

