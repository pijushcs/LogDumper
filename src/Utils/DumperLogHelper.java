package Utils;

import DumperImpl.DefaultLogConnection;
import DumperImpl.JsonTranslatorImpl;
import DumperImpl.LogFileTrackerImpl;
import Interfaces.DLogConnection;
import Interfaces.LogFileTracker;
import Interfaces.LogTranslator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * This Utility class has some frequent helper methods that are used by the LogDumper Application
 */
public class DumperLogHelper {
    private static LogFileTracker logFileTracker;
    private static DLogConnection dLogConnection;
    private static LogTranslator logTranslator;

    /**
     * Creates or returns the default LogFileTrackerImpl Instance.
     * @return LogFileTrackerImpl instance
     * @throws IOException
     */
    public static LogFileTracker getFilerTracker() throws IOException {
        if(logFileTracker != null) return logFileTracker;
        try {
            logFileTracker = new LogFileTrackerImpl();
        } catch (IOException e) {
            throw new IOException("Could not create File Tracker");
        }
        return logFileTracker;
    }

    /**
     * Factory method that creates or returns the server connection instance.
     * @param dumperServerType The type used for creating the server
     * @param host The host name
     * @param port The port the server is running on
     * @return The server connection implementation instance of DLogConnection
     */
    public static DLogConnection getDumperServer(Constants.DumperServerType dumperServerType, String host, int port) {
        if(dLogConnection != null)
            return dLogConnection;

        // Currently only TCP is supported and is the default connection
        dLogConnection = new DefaultLogConnection(host, port);
        return dLogConnection;
    }

    /**
     * Factory method that creates or returns the translator to be used for formatting the message to be sent
     * @param logTranslatorType The type used for formatting the log to be sent
     * @return The translator implementation instance of LogTranslator
     */
    public static LogTranslator getLogTranslator(Constants.LogTranslatorType logTranslatorType) {
        if(logTranslator!= null)
            return logTranslator;

        // Currently only JSON Translator is implemented
        logTranslator = new JsonTranslatorImpl();
        return logTranslator;
    }

    /**
     * Remove the connection with the server and close the streams on error!
     * @param exception The exception that occurred for printing the stack.
     * @param dLogConnection The server connection to close
     */
    public static void removeConnectionAndExit(Exception exception , DLogConnection dLogConnection) {
        dLogConnection.removeConnection();
        exception.printStackTrace();
        System.exit(Constants.ERROR_STATUS);
    }

    /**
     * Save the current logs that are being processed on error!
     * Note: This method ensures that no data is lost when an error occurs
     * @param logList The list of logs to save
     */
    public static void saveCurrentProcessingLogsOnError(ArrayList<String> logList) {
        File logFile;
        final Path path = Paths.get(Constants.LOG_DIR_PATH + Constants.TEMP_LOG_SAVE_FILE);

        try {
            if (Files.notExists(path)) Files.createFile(path);
            logFile = path.toFile();
            FileOutputStream outputStream = new FileOutputStream(logFile, true);
            BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

            for(String curLog : logList) {
                writer.append(curLog);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();  // Nothing to do! Highly Unlikely though
        }
    }
}
