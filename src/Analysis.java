import java.io.File;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class Analysis {
	File outputDirectory;
	File inputDirectory;
	String outputFileName;
	private static Settings curSettings;
	private static CustomJSONParsing parser;
	
	
	

	public static void main(Settings s) {
		curSettings = s;
		System.out.println("Running Analysis with directory path " + s.getOutputFile() +"...");
		File directoryPath = new File(s.getInputFile());
		File filesList[] = directoryPath.listFiles(); 
		
		String outputFileLocation = curSettings.getOutputFile() + "\\" + curSettings.getOutputFileName() + ".csv";
		parser = new CustomJSONParsing(outputFileLocation);
		for (File file : filesList) {
			String fileName = file.getName();
			int i = fileName.lastIndexOf('.');
			int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
			
			if (i > p && fileName.substring(i+1).equals("json")) {
			    System.out.println("file is a json file: " + fileName + ", ");
			    readJSON(file);
			}
			
		}
		parser.endParsing();
		System.out.println("Finished parsing log files!");
	}
	
	//count the number of compatible files that are located in the output Directory. 
	public static int countFiles() {
		
		return 0; 
	}
	
	//passed an individual log file. Returns results based on your settings. 
	public static void readJSON(File file) {
		try {
			JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(file));;
			if (!simpleFilterOkay(obj))
				return;
			
			//find desired player inside of players.
			JSONArray players = (JSONArray) obj.get("players");
			JSONObject player = null;
			for (Object p : players) {
				JSONObject curPlayer = (JSONObject) p;
				if (curPlayer.get("name").equals(curSettings.getCharName())) {
					player = curPlayer;
					break;
				}
			}
			if (player == null) {
				System.out.println("Player not found!");
				return;
			}
			
			
			//send information off to CustomJSONParsing to be put into the CSV. 
			try {
				
				if (CustomJSONParsing.writeToCSV(obj, player))
					CustomJSONParsing.writeNewLine();
			} catch (Exception e){
				System.out.println("something went wrong." + e);
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	private static Boolean simpleFilterOkay(JSONObject obj) {
		//TODO check for the simple filtering options, such as name,
		// location, duration, etc. 
		// all of the info that can be found before turning into a JSONObject. 
		int duration = 0;
        String clean = ((String)obj.get("duration")).replaceAll("\\D+",""); //remove non-digits
        duration += Character.getNumericValue(clean.charAt(0))*10*60;
        duration += Character.getNumericValue(clean.charAt(1))*60;
        duration += Character.getNumericValue(clean.charAt(2))*10;
        duration += Character.getNumericValue(clean.charAt(3));
		/*
		System.out.println("duration, location: " + duration + ", " + obj.get("fightName"));
		System.out.println("desired duration, location: " + curSettings.getMinDuration() + 
							", " + Constants.LOCATIONS.get(curSettings.getLocation()));
		*/
		if (duration < curSettings.getMinDuration() ||
				!obj.get("fightName").equals(Constants.LOCATIONS.get(curSettings.getLocation())) ||
				!obj.containsValue(curSettings.getCharName()))
			return false;
		
		
		return true;
		
	}
	
	//generate the header line, containing all the categories, based on your settings.
	public static String generateHeaders() {
		
		return"To be determined"; 
	}
	
	//creates the CSV file based on the generated strings and output directory/name.
	public static void createCSVFile() {
		
	}
}


