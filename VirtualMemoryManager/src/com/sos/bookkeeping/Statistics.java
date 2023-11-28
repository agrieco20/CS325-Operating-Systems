/* File: Statistics.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 17 Sep 2023
 * Licence: GNU GPLv3
 * Purpose:
 *  A singleton class for recording SimulatedOS run statistics.
 */


package com.sos.bookkeeping;

import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static java.util.Arrays.sort;

public class Statistics {
    //Singleton Methods/Variables
    private static Statistics instance = null;

    public static Statistics getStatLog(){
        if(instance == null)
            instance = new Statistics();
        return instance;
    }

    public static void destroy(){
        instance.writeOut();
        instance = null;
    }

    //Instance Methods and Variables
    private final String filename;
    private final HashMap<String, Object> stats;

    private Statistics(){
        LocalDateTime current = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
        filename = "os_run_stats_" + current.format(format) + ".log";
        stats = new HashMap<>();
    }

    public void register(String stat, Object value){
        stats.put(stat, value);
    }

    public Object retrieve(String stat){
        if(!stats.containsKey(stat))return null;
        return stats.get(stat);
    }

    public void writeOut(){
        try {
            PrintStream ps = new PrintStream(filename);
            ps.println("OS Simulation run statistics.");
            String[] sorted_keys = stats.keySet().toArray(new String[0]);
            Arrays.sort(sorted_keys);
            for(String key : sorted_keys){
                ps.printf("%s %s\n", key, stats.get(key));
            }
            ps.close();
        }
        catch(IOException exp){
            System.err.println("Problem writing simulation statistics.");
            exp.printStackTrace(System.err);
        }
    }
}
