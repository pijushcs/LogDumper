package Interfaces;

import java.io.IOException;

/**
 * The interface providing the required methods for connecting to the server
 */
public interface DLogConnection {
    /**
     * Create the connection:
     * Note: For TCP servers create/remove connections are required to create the connection
     * while for REST services, we can simply have some sanity health check over HTTP.
     * @return true on success
     */
    boolean createConnection();

    /**
     * Remove the connection
     * @return true on successful disconnection
     */
    boolean removeConnection();

    /**
     * This method sends the translated message to the server.
     * @param message The translated message to be sent
     * @throws IOException
     */
    void sendMessage(String message) throws IOException;
}
