package ui;

import manageData.ReceiveDataViaNetwork;
import manageData.SendDataViaNetwork;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Manages a network connection to the server using a {@link Socket}, providing
 * access to input/output streams and high-level send/receive utilities.
 *
 * A Connection encapsulates the socket, the data streams, and the corresponding
 * {@link SendDataViaNetwork} and {@link ReceiveDataViaNetwork} handlers used to
 * transfer data between the client and the server.
 *
 * This class also provides a method to safely release all network resources.
 */
public class Connection {

    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;

    private final SendDataViaNetwork send;
    private final ReceiveDataViaNetwork receive;

    /**
     * Creates a connection to the specified IP address and port, initializing
     * the socket and the associated input/output streams.
     *
     * @param ipAddress the server's IP address
     * @param port the port on which the server is listening
     * @throws IOException if an error occurs while establishing the connection
     */
    public Connection(String ipAddress, int port) throws IOException {
        try {
            this.socket = new Socket(ipAddress, port);

            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            this.send = new SendDataViaNetwork(out);
            this.receive = new ReceiveDataViaNetwork(in);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the object used for sending data over the network
     */
    public SendDataViaNetwork getSendViaNetwork() {
        return send;
    }

    /**
     * @return the object used for receiving data over the network
     */
    public ReceiveDataViaNetwork getReceiveViaNetwork() {
        return receive;
    }

    /**
     * Releases all network-related resources, including the input/output
     * streams and the underlying socket. Any I/O errors during closing
     * are ignored.
     */
    public void releaseResources() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException ignored) {}
    }
}