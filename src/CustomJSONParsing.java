import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/*
 * A class created for each csv file you're creating. ALL writing to file done here.
 */

public class CustomJSONParsing {
	public static BufferedWriter bw;
	
	//Constructor
	public CustomJSONParsing(String fileName, Settings s) {
		FileWriter file;
		try {
			file = new FileWriter(fileName);
			bw = new BufferedWriter(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		writeOverview(s);
		//TODO: insert the headers here, by checking the settings...?
		writeHeaders(s);
	}
	
	//Writes to the top of the csv file, including details such as settings, character, profession, etc. 
	private void writeOverview(Settings s) {
		String result = 
				"Character Name:, " + s.getCharName();
		if (!s.getProfession().equals("Any"))
				result += "\nProfession:, " + s.getSpecialization();
		if (!s.getLocation().equals("Any"))
			result += "\nLocation:, " + s.getLocation();
		//TODO: insert the desired stats along with boon information. For now, we'll just put in the general desired stats. 
			result += "\nStats shown:, " + s.getDesiredStats();
		
		
		//TODO: What else to show here? 
		writeString(result);
		writeNewLine();
	}
	
	//Writes the headings for all the columns/tables.
	//TODO: implement settings for what to display!
	private void writeHeaders(Settings s) {
		String result = "";
		for (String key : Constants.HEADERS.keySet()) {
			System.out.println("key is" + key);
			for (String str : Constants.HEADERS.get(key)) {
				result += str + ", ";
			}
		}

		//sectioning? spaces between different sections?
		
		writeString(result);
		writeNewLine();
	}
	
	public static void writeNewLine() {
		try {
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void writeString(String s) {
		try {
			bw.write(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error writting the string: " + s);
			e.printStackTrace();
		}
	}
	
	//Final write before you're done writing the csv. This is needed because we are using the BufferedWriter. 
	public static void endParsing() {
		try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Move to Constants class or generate inside a function?
	
	
	/*
	 * function called for each log, from the Analysis class. 
	 * Writes in all the needed information. 
	 */
	public static void writeToCSV(JSONObject json, JSONObject player) {
		
		writeString(getStringForGeneral(json, Constants.HEADERS.get("general"))); //TODO: some of these stats should be displayed in the overview, if selected in settings. 
		writeString(getStringFromJSONArray(player, "statsAll", Constants.HEADERS.get("statsAll")));
		writeString(getStringFromJSONArray(player, "support", Constants.HEADERS.get("support")));
		writeNewLine();
	}
	
	//Gets values from a JSON object as a whole (for example, the whole report). Only values immediately accessible. 
	public static String getStringForGeneral(JSONObject obj, List<String> list) {
		String result = "";
		
		for (String s : list) {
			result += obj.get(s) + ", ";
		}
		return result;
	}
	
	/*
	 * A lot of values are stored in an array, inside an object. This will access the first array and use that as it's own object. 
	 * e.g. accessing the desired info from "allStats": getStringFromJSONArray(player, "allStats", allStats)
	 * {
	 * 		"allStats" {
	 * 			[
	 * 				{Desired info. This is the first object in the array.}
	 * 			]
	 * 		}
	 * }
	 */
	public static String getStringFromJSONArray(JSONObject player, String listName, List<String> list) {
		//Check conditions? 
		JSONObject JObj = (JSONObject) ((JSONArray) player.get(listName)).get(0);

		String result = "";
		for (String s : list) {
			result += JObj.get(s) + ", ";
		}
		
		return result;
	}
}
