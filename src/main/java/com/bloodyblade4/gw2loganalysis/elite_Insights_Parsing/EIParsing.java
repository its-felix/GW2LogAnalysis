package com.bloodyblade4.gw2loganalysis.elite_Insights_Parsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.log4j.Logger;

/*
 * Used to parse .zevtc files through Elite Insights to generate JSON/HTML files, which this program uses. 
 * Elite Insights repository: https://github.com/baaron4/GW2-Elite-Insights-Parser
 * 
 * "If you would like to have your logs parsed without the GUI pass the file location of each evtc file as a string. 
 * Call: "GuildWars2EliteInsights.exe -c [config path] [logs]" "
 * 
 * Errors handled here will not end the program nor warn the user directly. 
 * The EI file will already have been verified as a file, and analysis will only try to read json files that exist. 
 */

public class EIParsing {
	static final Logger logger = Logger.getLogger(EIParsing.class); 
	
	public static void runEIBatch(List<String> batch, String EILoc, String confFile) {
		String[] params = new String [4 + batch.size()];
        params[0] = EILoc; //Elite insights location
        params[1] = "-p"; // disables cmd line from opening. 
        params[2] = "-c"; // enables sending config file. 
        params[3] = confFile; // [config path]
        System.out.println("file length is" + batch.size());
        for(int i = 0; i < batch.size(); i++) 
        	params[i+4] = batch.get(i);
        	
        System.out.println("Calling runEIPArsing. with" + params.toString());
        runEIParsing(params);
	}
	
	public static void runEIParsing(String[] params) {
        try {
            final Process p = Runtime.getRuntime().exec(params);
            Thread thread = new Thread() {
                public void run() {
                	BufferedReader input = null;
                    try {
	                    input = new BufferedReader
	                         (new InputStreamReader(p.getInputStream()));
	                    /* Print success and what is generated. 
	                     String line;
	                     try {
							while ((line = input.readLine()) != null)
							     System.out.println(line);
						} catch (IOException e) {
							System.out.println("println failed with: " + e);
							e.printStackTrace();
						}
						*/
                    }
                    //EI could not be read from? 
                    catch (Exception e1) {
                    	logger.error("Exception reading from Elite insights! This means that no .zevtc files were generated for this batch: " + e1);
                    	e1.printStackTrace();
                    	return;
                    }
                    finally {
                    	try {
                    		input.close();
						} catch (IOException e2) {
							logger.warn("Error when closing the Elite Insights input stream. " + e2);
							e2.printStackTrace();
						}
                    }
                } 
            };
            
            thread.start();
            int result = p.waitFor();
            thread.join();
            if (result != 0) {
                logger.error("The Elite Insights parsing process failed with status " + result);
                return;
            }
            else
            	return;
        }
        catch (Exception e3) {
        	logger.error("Exception occured in the Elite Insights parsing thread. " + e3);
        	e3.printStackTrace();
        	return;
        }
	}
	
	//Creates a directory, with a time stamp for unique name. Returns absolute path to new directory.
	public static File createDirectory(String loc) {
		DateTimeFormatter timeStampPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        System.out.println(timeStampPattern.format(java.time.LocalDateTime.now()));
		
		String dirName = loc + "\\GW2LAGeneratedJSON" + timeStampPattern.format(java.time.LocalDateTime.now()).toString();
		File f = new File(dirName);
		if (f.mkdir()) 
			return f;

	    return null;

	}
}
