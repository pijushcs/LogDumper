package DumperImpl;

import Interfaces.DLogConnection;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * TCP Server Connection for Sending Message
 */
public class DefaultLogConnection implements DLogConnection {
    private Socket socket = null;
    private String host;
    private int port;

    /**
     * Instantiates the server details.
     * @param host The server host
     * @param port The port the server is running on
     */
    public DefaultLogConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Creates the TCP connection.
     * @return true on success
     */
    @Override
    public boolean createConnection() {
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Removes the connection.
     * @return
     */
    @Override
    public boolean removeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Sends the message over TCP to the LogDumper Server
     * @param message The translated message to be sent
     * @throws IOException on send/connection error
     */
    @Override
    public void sendMessage(String message) throws IOException {
        if (socket == null) {
            boolean isConnectionSuccessFul = createConnection();
            if (!isConnectionSuccessFul) throw new IOException("Couldn't Connect to Server");
        }

        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(message);
        } catch (IOException e) {
            throw new IOException("Couldn't Send Message");
        }
    }
}
