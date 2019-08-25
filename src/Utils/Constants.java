package Utils;

/**
 * Defining Constants and Required Enums
 */
public class Constants {
    public static final String LOG_DIR_PATH = "/tmp/LogDumper/";
    public static final int SLEET_TIME_IN_MILLI_SECONDS = 3000;
    public static final int NUMBER_OF_LOG_FIELDS = 4;
    public static final int ERROR_STATUS = -1;


    public static final int DEFAULT_PORT = 5000;
    public static final String DEFAULT_HOST = "127.0.0.1";
    public static final String TEMP_LOG_SAVE_FILE = "TempLogData.log";

    // The server connection type
    public enum DumperServerType {
        TCP,
        UDP,
        REST
    };

    // The format supported by the LogDumper Server
    public enum LogTranslatorType {
        JSON,
        XML,
        TEXT
    };
}
