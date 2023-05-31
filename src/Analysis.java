import java.io.File;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

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
		parser = new CustomJSONParsing(outputFileLocation, s);
		for (File file : filesList) {
			String fileName = file.getName();
			if (FileHelper.getFileExtension(fileName).equals("json")) {
				System.out.println("file is a json file: " + fileName + ", ");
			    readJSON(file);
			}
		}
		parser.endParsing();
		System.out.println("Finished parsing log files!");
	}
	
	//passed an individual log file. Returns results based on your settings. 
	public static void readJSON(File file) {
		try {
			JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(file));;
			if (!simpleFilterOkay(obj)) {
				System.out.println("failed to pass simple filter.");
				return;
			}
			
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
			
			if (!advancedFilterOkay(player)) {
				System.out.println("Failed to pass advanced filter.");
				return;
			}
			
			//send information off to CustomJSONParsing to be put into the CSV. 
			try {
				CustomJSONParsing.writeToCSV(obj, player, curSettings);
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
		if (!obj.containsKey("durationMS"))
			return false;
		int durationMS = ((Long)obj.get("durationMS")).intValue();

		if (durationMS < (curSettings.getMinDuration() *100) ||
				!obj.containsValue(curSettings.getCharName()) || 
				( !obj.get("fightName").equals(Constants.LOCATIONS.get(curSettings.getLocation())) && !curSettings.getLocation().equals("Any"))
				)
			return false;
		return true;
		
	}
	
	private static Boolean advancedFilterOkay(JSONObject obj) {
		//profession/specialization check
		if (!curSettings.Profession.equals("Any")) {
			if (!obj.get("profession").equals(curSettings.getSpecialization())) {
				System.out.println("The profession you selected doesn't match this characters log file");
				return false;
			}
		}
		//Weapons check:
		List<String> objWeapons = (List<String>) obj.get("weapons");
		System.out.println("objWeapons is: " + objWeapons);
		if (!weaponFoundCheck(String.valueOf(objWeapons.get(0)), curSettings.getWepSet1MainHand()) ||
			!weaponFoundCheck(String.valueOf(objWeapons.get(1)), curSettings.getWepSet1Offhand()) ||
			!weaponFoundCheck(String.valueOf(objWeapons.get(2)), curSettings.getWepSet2MainHand()) ||
			!weaponFoundCheck(String.valueOf(objWeapons.get(3)), curSettings.getWepSet2Offhand())) 
			return false;
		
		return true;
	}
	
	private static Boolean weaponFoundCheck(String objWeapon, String weapon) {
		if (!weapon.equals("Any") && !objWeapon.equals(weapon)) {
			if ( !((objWeapon.equals("Unknown")) && !curSettings.getWepFilterStrict()) ) //~("unknown" && ~S)
				return false;
		}
		return true;
	}
}


