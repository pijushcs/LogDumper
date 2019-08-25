package Interfaces;

import java.io.IOException;

/**
 * The translator interface having the required methods for formatting the client application logs.
 */
public interface LogTranslator {
    /**
     * Returns the translated user log
     * @param log The message logged by the client
     * @return the formatted string (JSON/XML)
     * @throws IOException
     */
    String translateLog(String log) throws IOException;
}
