package com.bloodyblade4.gw2loganalysis.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.bloodyblade4.gw2loganalysis.components.Constants;
import com.bloodyblade4.gw2loganalysis.components.FileHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XMLHelper {

	//given the absolute path of the main.java.com.bloodybade4.gw2loganalysis.settings file, will load and run functions to update main.java.com.bloodybade4.gw2loganalysis.settings and fields.
	public static Settings loadSettings(String fileName) throws Exception{
		if (fileName == null)
			throw new Exception("Cannot load main.java.com.bloodybade4.gw2loganalysis.settings because the main.java.com.bloodybade4.gw2loganalysis.settings file name appears to be empty.");
	
		try {
			ObjectMapper mapper = new XmlMapper(); 
			InputStream inputStream = new FileInputStream(new File(fileName));
			TypeReference<Settings> typeRef = new TypeReference<Settings>() {};
			Settings s = mapper.readValue(inputStream, typeRef);
			inputStream.close();
			return s;
		} catch(FileNotFoundException e1) {
			e1.printStackTrace();
			throw new Exception("Failed find the main.java.com.bloodybade4.gw2loganalysis.settings file.");
		}
		catch (UnrecognizedPropertyException e1){
			throw new Exception("Unrecognized properties found or missing in the selected main.java.com.bloodybade4.gw2loganalysis.settings file.");
		} 
		catch(IOException e2) {
			throw new Exception("Failed to open, or close, the desired main.java.com.bloodybade4.gw2loganalysis.settings file.");
		}
		catch (Exception e3) {
			e3.printStackTrace();
			throw new Exception("Something went wrong while trying to load the selected main.java.com.bloodybade4.gw2loganalysis.settings file.");
		}
	}
		
	/*
	 * Searches through main.java.com.bloodybade4.gw2loganalysis.settings file for most recently used main.java.com.bloodybade4.gw2loganalysis.settings file.
	 * If found, will load it. If not found, returns false and allows creation of new main.java.com.bloodybade4.gw2loganalysis.settings.
	 */
	public static String findLastSettings()
	{
		File dir = new File(Constants.GW2_APP_DATA_FILE + "\\Settings");
	     if (!dir.exists()) {dir.mkdir();}
	    
	    File[] files = dir.listFiles(FileHelper.getFileFilterByExtension("xml"));
	    long lastModifiedTime = Long.MIN_VALUE;
	    File chosenFile = null;
	    
	    if (files == null || files.length == 0)
	    	return null;
	    
        for (File file : files)
        {
            if (file.lastModified() > lastModifiedTime)
            {
            	String fileName = file.getName();
        		System.out.println("Found main.java.com.bloodybade4.gw2loganalysis.settings file." + fileName);
    			chosenFile = file;
                lastModifiedTime = file.lastModified();
            }
        }
	    return chosenFile.getAbsolutePath();
	}
}
