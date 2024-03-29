#### `LogDumper Application:`
LogDumper is the main background application that supports multiple clients apps to log messages that are to be dumped to the server. The dumper application checks the logs of all the running applications that are registered to it, converts it to JSON and sends them to the server.

<b>Usage:</b> java -jar LogDumper.jar [server-host-name] [server-port]

---

#### `Some Design Notes`:
> Each client writes to a separate log file to scale well. While listing the raw user logs, each client log file is locked with an exclusive lock using FileLock and processed. The JSON translation happens after the lock is released to have the clients get access to the log file. 
>> <b>Note:</b> The client applications are not blocked due to the exclusive lock as the LogDumperLib library used by clients provides asynchronous logging by default.

> Exceptions are modelled to have the upstream calls handle the excetions and accordings throws the exception. Custom exceptions may be added for better error reporting.
>> <b>Note:</b> Care has been taken to ensure that no user log data is lost during an error during graceful exit.

> The user raw logs are validated and modelled to a class DumperLog that is used by Jackson to create the JSON

> The default processors/translators have been provided as a signleton that can be created using a factory method from the utility class (Check below section).

---

#### `Some Class Specific Notes:`
> LogDumper: Orchestrates the entire workflow.

#### Interfaces (src/Interfaces):
> <b>LogFileTracker:</b>    
Interface for the main client log processor.

> <b>LogTranslaotr:</b>   
Interface for the translating the user log to JSON/XML

> <b>DLogConnection:</b>   
Interface for the seting up the server connection (TCP/UDP etc)

---

#### Implementations (src/DumperImpl):
> <b>LogFileTrackerImpl:</b>   
Default implementation of the processor. The main task of the processor is to lock client logs for processing and add it to a log-list and return the list of raw user log. 

> <b>JsonTranslatorImpl:</b>   
The JSON Translator implementation for LogTranslator. The main task of the translator is to form a JSON from the raw user log.

> <b>DefaultLogConnection:</b>   
Implemention of the TCP Connection to the Dumper Server

> <b>DumperLog:</b>  
Representation of the user log used by <i>Jackson</i> for JSON translation.

#### Utils and Constants (src/Utils)
> <b>Constants:</b>   
The class has a few Constants and Enums

> <b>DumperLogHelper</b>   
Most of the frequest helper/factory methods and are defined here

---
