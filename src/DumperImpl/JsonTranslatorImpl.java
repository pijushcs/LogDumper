package DumperImpl;

import Interfaces.LogTranslator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.UnknownFormatConversionException;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * JSON Translator for translating the user log to JSON.
 * Note: DumperLog is the format of the JSON used during translation using Jackson Mapper
 */
public class JsonTranslatorImpl implements LogTranslator {
    /**
     * Translates the user dumped log to json.
     * @param log The message logged by the client
     * @return The translated json string
     * @throws IOException on JSON conversion
     * @throws UnknownFormatConversionException on log field errors
     */
    @Override
    public String translateLog(String log) throws IOException, UnknownFormatConversionException {
        String jsonLog = null;
        StringTokenizer stringTokenizer = new StringTokenizer(log, ",");
        ArrayList<String> logFields = new ArrayList<String>();

        // Create a list of log fields from the user logged string
        while(stringTokenizer.hasMoreTokens())
            logFields.add(stringTokenizer.nextToken());

        ObjectMapper jsonMapper = new ObjectMapper();
        try {
            DumperLog dumperLog = DumperLog.createDumperLogFromList(logFields); // Create the user log
            jsonLog = jsonMapper.writeValueAsString(dumperLog); // Translate to JSON
        } catch (UnknownFormatConversionException e) {
            throw new UnknownFormatConversionException("Incorrect Log Format");
        } catch (IOException e) {
            throw new IOException("Error Converting Log to JSON");
        }

        return jsonLog;
    }
}
