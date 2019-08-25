package DumperImpl;

import Interfaces.LogFileTracker;
import Utils.Constants;
import Utils.DumperLogHelper;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * The default implementation of LogFileTracker for getting the client application logs.
 */
public class LogFileTrackerImpl implements LogFileTracker {
    /**
     * The constructor creates the directory where the client logs are to be dumped for processing
     * @throws IOException on directory creation error
     */
    public LogFileTrackerImpl() throws IOException {
        final Path path = Paths.get(Constants.LOG_DIR_PATH);
        try {
            if (Files.notExists(path)) Files.createDirectory(path);
        } catch (IOException e) {
            throw new IOException("Directory Creation Error");
        }
    }

    /**
     * This methods reads a particular client log file and returns the list of logs logged by the application.
     * @param inputStream The log file input stream
     * @return The client logs as a list of Strings
     * @throws IOException
     */
    private ArrayList<String> readFile(FileInputStream inputStream) throws IOException {
        ArrayList<String> linesRead = new ArrayList<String>();
        String curLine;

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        while((curLine = reader.readLine()) != null) linesRead.add(curLine);
        return linesRead;
    }

    /**
     * Remove the logs after successful read!
     * @param outputStream The log file output stream
     * @throws IOException
     */
    private void removeFileContents(FileOutputStream outputStream) throws IOException {
        BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        writer.write("");
        writer.flush();
    }

    /**
     * This method gets an exclusive lock on the client log file and then processes it.
     * @param curFile The client log file
     * @return The logs logged by the client as a list
     * @throws IOException
     */
    ArrayList<String> processFile(File curFile) throws IOException {
        ArrayList<String> logList = null;
        try {
            FileChannel fileChannel = new RandomAccessFile(curFile, "rw").getChannel(); // Open for read/write
            FileLock lock = fileChannel.lock(); // Get an exclusive lock on the file

            logList = readFile(new FileInputStream(curFile));   // Read the logs
            removeFileContents(new FileOutputStream(curFile));  // Remove the logs on successful read

            lock.release();
        } catch (IOException exception){
            if(logList != null)
                DumperLogHelper.saveCurrentProcessingLogsOnError(logList);  // Save the logs on error!
            throw new IOException("Couldn't Process Files");
        }

        return logList;
    }

    /**
     * Iterates over all the running clients application logs and returns the logs as a list
     * @return The list of logs of all the running clients.
     * @throws IOException
     */
    @Override
    public ArrayList<String> getLogs() throws IOException {
        File folder = Paths.get(Constants.LOG_DIR_PATH).toFile();
        ArrayList<String> logList = new ArrayList<String>();
        File[] fileList = folder.listFiles();

        for (File curFile : fileList) {
            if(curFile.isFile()) {
                ArrayList<String> logs = processFile(curFile);  // Lock and process each log file
                logList.addAll(logs);
            }
        }

        return logList;
    }
}
