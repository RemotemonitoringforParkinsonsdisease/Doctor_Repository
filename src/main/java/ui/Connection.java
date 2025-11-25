package ui;

import manageData.ReceiveDataViaNetwork;
import manageData.SendDataViaNetwork;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connection {
    private Socket socket;
    private SendDataViaNetwork sendDataViaNetwork;
    private ReceiveDataViaNetwork receiveDataViaNetwork;

    public Connection(String ipAddress, int port) {
        try {
            this.socket = new Socket(ipAddress, port);
            this.sendDataViaNetwork = new SendDataViaNetwork(socket);
            this.receiveDataViaNetwork = new ReceiveDataViaNetwork(socket);
        } catch (IOException e) {
            System.out.println("Error establishing connection to " + ipAddress + " on port " + port);
            e.printStackTrace(); // Muestra más detalles sobre el error (como el tipo exacto de excepción)
        }
    }


    public SendDataViaNetwork getSendViaNetwork() {
        return sendDataViaNetwork;
    }

    public ReceiveDataViaNetwork getReceiveViaNetwork() {
        return receiveDataViaNetwork;
    }

    void releaseResources() {
        sendDataViaNetwork.releaseResources();
        receiveDataViaNetwork.releaseResources();
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
