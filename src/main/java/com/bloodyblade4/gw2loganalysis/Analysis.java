package com.bloodyblade4.gw2loganalysis;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.SwingWorker;

import com.bloodyblade4.gw2loganalysis.components.Constants;
import com.bloodyblade4.gw2loganalysis.components.FileHelper;
import com.bloodyblade4.gw2loganalysis.elite_Insights_Parsing.EISettings;
import com.bloodyblade4.gw2loganalysis.gui_components.FileBar;
import com.bloodyblade4.gw2loganalysis.gui_components.LoadingScreen;
import com.bloodyblade4.gw2loganalysis.json_Parsing.JSONParsing;
import com.bloodyblade4.gw2loganalysis.json_Parsing.JSONUtility;
import com.bloodyblade4.gw2loganalysis.settings.Settings;
import com.bloodyblade4.gw2loganalysis.elite_Insights_Parsing.EIParsing;
import com.bloodyblade4.gw2loganalysis.components.DialogHelper;
import com.bloodyblade4.gw2loganalysis.xlsx_Parsing.csvToXLSX;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Analysis {
	File outputDirectory;
	File inputDirectory;
	String outputFileName;
	private Settings curSettings;
	private JSONParsing parser;
	static final Logger logger = Logger.getLogger(Analysis.class); 
	
	Analysis() {}
	
	public void analize(Settings s) {
		logger.info("Starting analysis of logs.");
		curSettings = s;
		String outputFileLocation = curSettings.getOutputFile() + "\\" + curSettings.getOutputFileName();
		
		File JSONDirectory = (s.getUseEI()) ? 
								EIParsing.createDirectory(s.getOutputFile()) : new File(s.getInputFile()); 
		if (s.getUseEI() && JSONDirectory == null)
			return;
		
		//Get the file list and check size. 
		FileFilter fFilter = (s.getUseEI()) ?  
				getFileFilterWithDate("zevtc", curSettings.getFromDate(), curSettings.getToDate()) 
				:
				getFileFilterWithDate("json", curSettings.getFromDate(), curSettings.getToDate());
		File[] filesList= (new File(s.getInputFile())).listFiles(fFilter);  //Origin, either json files or the zevtc files. 
		if(filesList.length == 0) {
			DialogHelper.errorMessage("Filtering Error","There are no files that match your filtering. This is likely due to date range filtering. \nPlease check your setting and try again.");
			return;
		}
		
		//Load JSON parsing class. Exception handling done internally, just return in case of failure. 
		try {
			parser = new JSONParsing(outputFileLocation + ".csv", s);
		}
		catch (Exception e) {
			return;
		}
		
		//Generate the loading screen with a list of "FileBar" objects, used to hold state and path.
		LoadingScreen loadingScreen = new LoadingScreen(
				Arrays.stream(filesList).map(f -> new FileBar(f.getAbsolutePath())).toArray(FileBar[]::new)
				);
		
		logger.info("Starting to parse the " + filesList.length + " files that match application filtering.");
		SwingWorker<?,?> sw = new SwingWorker<>() {
			@Override 
			protected String doInBackground() throws Exception 	{
				return batchRead(loadingScreen, JSONDirectory);
			}
			
			@Override protected void done() {
				if (!isCancelled()) {
					try {
						get();
						
						parser.endParsing();
						csvToXLSX.csvParseToXLSX(outputFileLocation, parser.headerRow);
						
						if(curSettings.getUseEI() && !curSettings.getSaveJSON()) {
							FileHelper.deleteAllInDirectory(JSONDirectory); //Ensure the folder is empty
							JSONDirectory.delete(); //delete. 
						}
					} catch(Exception e) {
						DialogHelper.errorMessage("Runtime Error", "Cannot create Elite Insights configuration file. Exception: " + e);
						System.out.println("Get resulted in an exception. " + e);
					}
				}
				else {
					//DialogHelper.errorMessage("Runtime Error", "Cannot create Elite Insights configuration file. Exception: " + e);
					System.out.println("The process was canceled successfully! ");
				}
					
				loadingScreen.endLoading(outputFileLocation);
			}
		};
		sw.execute();
	}
	
	private void updateBatchState(FileBar[] fileList, FileBar.FileState state) {
		for(FileBar f : fileList) {
			f.setState(state);
		}
	}
	
	private String batchRead(LoadingScreen loadingScreen, File jsonDirectory) throws Exception {
		//Generate EI conf file, if needed. Will bubble exception upwards to cancel the thread worker.
		String confFile = null;
		if (curSettings.getUseEI())
			confFile = EISettings.generateEIConf(curSettings, jsonDirectory);

		
		while(!loadingScreen.isDone() && loadingScreen.visible) {
			FileBar[] batch = loadingScreen.getBatch();

			//EI Parsing. 
			if(curSettings.getUseEI()) {
				updateBatchState(batch, FileBar.FileState.PARSING_ZEVTC);
				
				//Run the files into EI. Will bubble exception upwards to cancel the thread worker. 
				EIParsing.runEIBatch(Arrays.stream(batch).map(fb -> fb.getFilePath()).collect(Collectors.toList()), 
									curSettings.getEIAbsPath(), confFile);
				
				/*
				 * Match the newly generated files to their corresponding FileBar. O(BATCH_SIZE*BATCH_SIZE) 
				 * If the files don't match, the EI didn't generate any JSON, but this can be purposeful. These File Bars are set to null and failed. 
				 */
				File[] jsonFiles = jsonDirectory.listFiles(FileHelper.getFileFilterByExtension("json"));
				for (FileBar fb : batch) {
					String name = fb.generateFileName();
					name = name.substring(0, name.length()-6); //Gets name, without ".json".
					fb.setFilePath(null); //In the case that the json file is never found. 
					
					for(File f : jsonFiles) {
						if (f.getName().contains(name)) {
							fb.setFilePath(f.getAbsolutePath());
							break;
						}
					}
				}	
			}
			
			//Read JSON.
			updateBatchState(batch, FileBar.FileState.READING_JSON);
			for (FileBar fb : batch) {
				if (fb.getFilePath() == null) 
					fb.setState(FileBar.FileState.FAILED);
				else {
					File jsonFile = new File(fb.getFilePath());
					if (jsonFile.exists()) //Double check that the file exists.
						fb.finishedFile(readJSON(jsonFile));
					else {
						logger.error("JSON file is supposed to exist, yet it is not found. " + fb.getFilePath());
						fb.setState(FileBar.FileState.ERROR);
					}
				}
				loadingScreen.increaseProgress();
			}
			
			if(curSettings.getUseEI() && !curSettings.getSaveJSON()) {
				if(curSettings.getEmbbedHTML())
					FileHelper.deleteAllInDirectory(jsonDirectory, FileHelper.getFileFilterExcludeExtension("html"));
				else
					FileHelper.deleteAllInDirectory(jsonDirectory);
			}
			loadingScreen.nextBatch();
		}
		return "finished batchEIRead";
	}
	
	//passed an individual log file. Returns results based on your main.java.com.bloodybade4.gw2loganalysis.settings.
	public String readJSON(File file) {
		try {
			JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(file));
			String filterOK = simpleFilterOkay(obj);
			if (!filterOK.equals("SUCCESS")) 
				return filterOK;
			
			//find desired player inside of players.
			JSONObject player = null;
			for (Object p : (JSONArray) obj.get("players")) {
				JSONObject curPlayer = (JSONObject) p;
				if (curPlayer.get("name").equals(curSettings.getCharName()) ||
						curPlayer.get("account").equals(curSettings.getCharName())) {
					player = curPlayer;
					break;
				}
			}
			if (player == null) 
				return "Failed: Player not found.";
			
			filterOK = advancedFilterOkay(player, JSONUtility.jsonObjGetDouble(obj, "durationMS"));//((Long)obj.get("durationMS")).intValue());
			if (!filterOK.equals("SUCCESS")) 
				return filterOK;
			
			//Get html, if active. 
			String htmlFilePath = "NA";
			if (curSettings.getEmbbedHTML()) {
				String path = file.getAbsolutePath();
				path = path.substring(0, path.length()-4) + "HTML";
				if((new File(path)).exists()) {
					htmlFilePath = path;
				}
			}

			//send information off to CustomJSONParsing to be put into the CSV. 
			//logger.info("Starting json parsing for the file \"" + file.getName() + "\"." );
			parser.writeToCSV(obj, player, htmlFilePath, curSettings);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			logger.error("Failed to read json due to file/parsing issue: " + e);
			return "Failed: Check logs for info.";
		} catch (NullPointerException nE) {
			nE.printStackTrace();
			logger.warn("Log file is missing a required key for parsing: " + nE);
			return "Failed: log is missing a key required for parsing.";
		}
		return "SUCCESS";
	}
	
	private String simpleFilterOkay(JSONObject obj)  throws NullPointerException{
		//duration.
		int durationMS = JSONUtility.jsonObjGetint(obj, "durationMS");
		if (((curSettings.getMinDuration() !=0) && (durationMS < (curSettings.getMinDuration() *100)))	) 
			return "Failed to pass minimum duration.";
		
		//Location
		String location = curSettings.getLocation();
		if(!location.equals("Any")){ 
			int ei = JSONUtility.jsonObjGetint(obj, "eiEncounterID");
			if(ei == 0x010000) //Unsupported encounter id. 
				return "Failed: Log has an unsupported encounter id.";
			
			if ( !Constants.ENCOUNTER_IDS.get(location).containsKey(ei) || //The encounter is not found in the encounter id list... or
					(curSettings.getEncounterID() != 0 && curSettings.getEncounterID() != ei) ) //The encounter is set to something, and this JSON encounter isn't the same. 
				return "Failed: Incorrect location.";
		}
		
		//Recorded date range checks
		if ( curSettings.getDatesByLog() &&	curSettings.getFromDate() != null) {
			Date timeStart = stringToDate( (String)obj.get("timeStart") );
			if(	!timeStart.after(curSettings.getFromDate()) && !timeStart.equals(curSettings.getFromDate()) )
				return "Failed: Log is older than your filter main.java.com.bloodybade4.gw2loganalysis.settings allow.";
		}
		if (  curSettings.getDatesByLog() && curSettings.getToDate() != null) {
			Date timeEnd = stringToDate( (String)obj.get("timeEnd") );
			if( !timeEnd.before(curSettings.getToDate()) && !timeEnd.equals(curSettings.getToDate()) )
				return "Failed: Log is newer than your filter main.java.com.bloodybade4.gw2loganalysis.settings allow.";
		}
		
		//Number of players in squad
		int squadSize = ((JSONArray)obj.get("players")).size();
		if(squadSize < curSettings.getMinSquadSize())
			return "Only " + squadSize + " players in the logs' squad. Filtering for minimum of " + curSettings.getMinSquadSize() + ".";
		
		return "SUCCESS";
	}
	
	private String advancedFilterOkay(JSONObject obj, double duration) throws NullPointerException{
		//profession/specialization check
		if (!curSettings.getProfession().equals("Any") && !obj.get("profession").equals(curSettings.getSpecialization())) 
			return "Failed: The profession/specialization you selected doesn't match. This character is running " + obj.get("profession");
		
		//statsAll object
		JSONObject statsAll = (JSONObject) ((JSONArray)obj.get("statsAll")).get(0);
		
		//Distance to tag.
		if(	curSettings.getMinComDistance() != 0 && ((Double)statsAll.get("distToCom")).intValue() > curSettings.getMinComDistance() ) 
			return "Failed: Distance to commander is greater than your current main.java.com.bloodybade4.gw2loganalysis.settings allow. Log shows: " + ((Double)statsAll.get("distToCom")).intValue();
		
		//Percent time alive
		if( curSettings.getMinPercentTimeAlive() != 0 && obj.containsKey("deathRecap")) {
			Double TOD = JSONUtility.jsonObjGetDouble(JSONUtility.getJsonObjFromArrayInObj(obj, "deathRecap"), "deathTime");
			Double percent = (TOD/duration)*100;
			if(percent < curSettings.getMinPercentTimeAlive())
				return "Failed: Percentage of time alive during the fight is less than main.java.com.bloodybade4.gw2loganalysis.settings allow.";
		}

		
		//Weapons check:
		List<String> objWeapons = (List<String>) obj.get("weapons");
		if (objWeapons.size() < 4) {
			if(curSettings.getWepFilterStrict())
				return "Failed: Weapon(s) unidentifiable. Due to strict filtering, this log is discarded.";
			return "SUCCESS";
		}
		if (!weaponFoundCheck(String.valueOf(objWeapons.get(0)), curSettings.getWepSet1MainHand()) ||
			!weaponFoundCheck(String.valueOf(objWeapons.get(1)), curSettings.getWepSet1Offhand()) ||
			!weaponFoundCheck(String.valueOf(objWeapons.get(2)), curSettings.getWepSet2MainHand()) ||
			!weaponFoundCheck(String.valueOf(objWeapons.get(3)), curSettings.getWepSet2Offhand())) 
			return "Failed: Equipped weapons do not match the weapon filter.";
		
		return "SUCCESS";
	}
	
	private Date stringToDate(String str) {
		Date res = new Date(Integer.parseInt(str.substring(0, 4))-1900, Integer.parseInt(str.substring(5,7)), Integer.parseInt(str.substring(8, 10)));
		return res;
	}
	
	private Boolean weaponFoundCheck(String objWeapon, String weapon) {
		if (!weapon.equals("Any") && !objWeapon.equals(weapon)) {
			if ( !((objWeapon.equals("Unknown")) && !curSettings.getWepFilterStrict()) ) //~("unknown" && ~S)
				return false;
		}
		return true;
	}

	private FileFilter getFileFilterWithDate(String extension, Date fromDate, Date toDate) {
		if (curSettings.getDatesByLog())
			return FileHelper.getFileFilterByExtension(extension);
		return new FileFilter() {
			public boolean accept(File f) {
				Date d = new Date(f.lastModified());
				Boolean fromPass = (fromDate == null || (d.after(fromDate) || d.equals(fromDate)));
				Boolean toPass = (toDate == null || (d.before(toDate) || d.equals(toDate)));
				return (f.getName().endsWith(extension) && fromPass && toPass);
			}		
		};
	}
}


