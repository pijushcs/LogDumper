import Interfaces.DLogConnection;
import Interfaces.LogFileTracker;
import Interfaces.LogTranslator;
import Utils.Constants;
import Utils.DumperLogHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UnknownFormatConversionException;

import static java.lang.Thread.sleep;

public class LogDumper {
    public static void main(String[] args) {
        String host = Constants.DEFAULT_HOST;
        int port = Constants.DEFAULT_PORT;

        // Get Server Details from args
        if (args.length == 2) {
            host = args[0];
            port = Integer.parseInt(args[1]);
        }

        // Get the translator and connection instance from the factory method
        LogTranslator logTranslator = DumperLogHelper.getLogTranslator(Constants.LogTranslatorType.JSON);
        DLogConnection dLogConnection = DumperLogHelper.getDumperServer(Constants.DumperServerType.TCP, host, port);

        LogFileTracker fileTracker = null;
        try {
            fileTracker = DumperLogHelper.getFilerTracker(); // Get the default client log processor
        } catch (IOException e) {
            DumperLogHelper.removeConnectionAndExit(e, dLogConnection); // Remove connection and exit on error!
        }

        ArrayList<String> logList = null;
        while(true) {
            try {
                sleep(Constants.SLEET_TIME_IN_MILLI_SECONDS); // Wait for some duration

                logList = fileTracker.getLogs(); // Get the logs from all the running clients
                for (String log : logList) {
                    String jsonLog = logTranslator.translateLog(log); // translate to JSON
                    dLogConnection.sendMessage(jsonLog); // Send the JSON String to the Server
                }
            } catch (UnknownFormatConversionException | InterruptedException | IOException e) {
                if(logList != null)
                    DumperLogHelper.saveCurrentProcessingLogsOnError(logList); // Save current logs on error

                // As of now LogDumper exits on any log processing faliure.
                DumperLogHelper.removeConnectionAndExit(e, dLogConnection);
            }
        }
    }
}
