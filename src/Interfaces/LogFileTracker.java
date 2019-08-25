package Interfaces;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This interface has the required methods for getting the client logs
 */
public interface LogFileTracker {
    /**
     * Gets the client application logs
     * @return The client logs as a list of strings
     * @throws IOException
     */
    ArrayList<String> getLogs() throws IOException;
}
