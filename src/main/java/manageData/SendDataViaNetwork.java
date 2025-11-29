package manageData;

import POJOs.Doctor;
import java.io.*;

/**
 * Class responsible for sending data through a network connection using a {@link DataOutputStream}.
 * It sends primitive values and objects (such as a registered Doctor) in the expected order and format for the receiving side.
 *
 * The methods in this class write data in the same order in which they must be read
 * by the server or client application on the other end.
 */
public class SendDataViaNetwork {

    private DataOutputStream dataOutputStream;

    /**
     * Constructor of the class.
     * @param dos the output stream used to send data over the network
     */
    public SendDataViaNetwork(DataOutputStream dos) {
        this.dataOutputStream = dos;
    }

    /**
     * Sends a UTF-encoded string over the network.
     *
     * @param message the string to send
     * @throws IOException if an I/O error occurs while writing to the stream
     */
    public void sendStrings(String message) throws IOException {
        dataOutputStream.writeUTF(message);
    }

    /**
     * Sends an integer value over the network.
     *
     * @param message the integer to send
     * @throws IOException if an I/O error occurs while writing to the stream
     */
    public void sendInt(Integer message) throws IOException{
        dataOutputStream.writeInt(message);
    }

    /**
     * Sends the basic information of a registered {@link Doctor}
     * (fullName, password, date of birth).
     *
     * @param doctor the doctor whose data is being sent
     * @throws IOException if an I/O error occurs while writing to the stream
     */
    public void sendRegisteredDoctor(Doctor doctor) throws IOException{
        dataOutputStream.writeUTF(doctor.getFullName());
        dataOutputStream.writeUTF(doctor.getDoctorPassword());
        dataOutputStream.writeUTF(doctor.getDob().toString());
    }
}

