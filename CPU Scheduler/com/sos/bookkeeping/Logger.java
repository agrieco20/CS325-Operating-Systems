/* File: Logger.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 20 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *  A singleton class for logging the goings-on of the SimulatedOS
 */


package com.sos.bookkeeping;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    //Singleton Methods/Variables
    private static Logger instance = null;

    public static Logger getLog(){
        if(instance == null)
            instance = new Logger();
        return instance;
    }

    public static void destroy(){
        instance = null;
    }

    //Instance Methods and Variables
    private final String filename;
    private final String errorFilename;

    private Logger(){
        LocalDateTime current = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
        filename = "os_log_" + current.format(format) + ".log";
        errorFilename = "os_error_log_" + current.format(format) + ".log";
    }

    public void log(String line){
        try {
            FileWriter fout = new FileWriter(filename, true);
            fout.write(line);
            fout.write('\n');
            fout.close();
        }
        catch(IOException exp){
            System.err.println("Problem with logging file.");
            exp.printStackTrace(System.err);
        }
    }

    public void error(String line){
        try {
            FileWriter fout = new FileWriter(errorFilename, true);
            fout.write(line);
            fout.write('\n');
            fout.close();
        }
        catch(IOException exp){
            System.err.println("Problem with error logging file.");
            exp.printStackTrace(System.err);
        }
    }
}
