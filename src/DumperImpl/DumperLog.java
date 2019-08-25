package DumperImpl;

import Utils.Constants;

import java.util.ArrayList;
import java.util.UnknownFormatConversionException;

/**
 * This class represents a particular user log.
 * Format: Time-Stamp, Application-Name, Type-of-Message(Info/Error), Cleint-Specific-Log]
 */
public class DumperLog {
    private String timeStamp;
    private String application;
    private String type;
    private String log;

    /* Getters and Setters Starting */

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    /* Getters and Setters Defined! */

    /**
     * Creae DumperLog from list of log fields provided
     * @param logFeilds list of log fields
     * @return DumperLog Instance
     * @throws UnknownFormatConversionException on log field error!
     */
    public static DumperLog createDumperLogFromList(ArrayList<String> logFeilds) throws UnknownFormatConversionException {
        if(logFeilds.size() < Constants.NUMBER_OF_LOG_FIELDS) throw new UnknownFormatConversionException("All Log Fields are Not Present");
        return new DumperLog(logFeilds.get(0), logFeilds.get(1), logFeilds.get(2), logFeilds.get(3));
    }

    /**
     * The dumper log constructor
     */
    DumperLog(String timeStamp, String application, String type, String log) {
        this.timeStamp = timeStamp;
        this.application = application;
        this.type = type;
        this.log = log;
    }
}
