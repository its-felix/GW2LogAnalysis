import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

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
			System.out.println("Error: Cannot initialize the CustomJSONParsing class.");
			e.printStackTrace();
		}
		
		writeOverview(s);
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
	//TODO: Implement sections! 
	private void writeHeaders(Settings s) {
		String sectioning = "";
		String result = "";
		for (String key : Constants.HEADERS.keySet()) {
			if (s.getDesiredStats().equals("Healing") && key.equals("dpsAll"))
				continue;
			if (s.getDesiredStats().equals("Damage") && (key.equals("support") || key.equals("outgoingHealing")))
				continue;
			
			System.out.println("key is" + key);
			sectioning += key + ", ";
			for (String str : Constants.HEADERS.get(key)) {
				sectioning += ", ";
				result += str + ", ";
			}
			result += ", ";
		}

		//buffs: 
		for (String str : s.getBoonSettings().getBuffs()) {
			result += str + ", ";
		}

		//sectioning? spaces between different sections?
		writeString(sectioning);
		writeNewLine();
		writeString(result);
		writeNewLine();
	}

	
	/*
	 * function called for each log, from the Analysis class. 
	 * Writes in all the needed information. 
	 */
	public static void writeToCSV(JSONObject json, JSONObject player, Settings s) {
		for (String key : Constants.HEADERS.keySet()) {
			if (key.equals("general")) {
				writeString(getStringFromObj(json, Constants.HEADERS.get(key)));
				continue;
			}
			if (s.getDesiredStats().equals("Healing") && key.equals("dpsAll"))
				continue;
			if (s.getDesiredStats().equals("Damage") && (key.equals("support") || key.equals("outgoingHealing")))
				continue;
			if (key.equals("outgoingHealing")) //further inside object. 
				writeString(getStringFromJSONArray((JSONObject)player.get("extHealingStats"), key, Constants.HEADERS.get(key) ));
			else
				writeString(getStringFromJSONArray(player, key, Constants.HEADERS.get(key)));
		}
		
		//buffs:
		if (s.getBoonSettings().getDisplay())
			writeString(getStringForBuffs( (JSONArray)player.get("groupBuffsActive") , s.getBoonSettings().getBuffs() ));
		
		
		
		writeNewLine();
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
		JSONObject JObj = (JSONObject) ((JSONArray) player.get(listName)).get(0);
		return getStringFromObj(JObj, list);
	}
	
	//Gets values from a JSON object as a whole (for example, the whole report). Only values immediately accessible. 
	public static String getStringFromObj(JSONObject obj, List<String> list) {
		String result = "";
		
		for (String s : list) {
			if (!obj.containsKey(s)) {
				System.out.println("Error: Trying to get the key " + s + ", which is not contained in the object.");
			}
				
			result += obj.get(s) + ", ";
		}
		result += ", ";
		return result;
	}

	//TODO: Do we want more than just "generation"? 
	//There is also, "over stack", "wasted", "unknownExtended", "byExtension", and "extended" 
	public static String getStringForBuffs(JSONArray section, List<String> desired) {
		String result = "";
		List<String> list = new ArrayList<String>(Collections.nCopies(desired.size(), "0"));
		
		for (Object o : section) {
			JSONObject boon = (JSONObject) o;
			int id = ((Long)boon.get("id")).intValue();
			if (!Constants.buffs.containsKey(id)) {
				System.out.println("Hey, you're missing a boon id for: " + id);
				continue;
			}
			
			String boonName = Constants.buffs.get(id);
			if (desired.contains(boonName)) {
				JSONObject buffData = (JSONObject) ((JSONArray) boon.get("buffData")).get(0);
				list.set(desired.indexOf(boonName), ((Double) buffData.get("generation")).toString() );
			}
		}
		
		for (String s : list) {
			result += s + ", ";
		}
		
		
		return result;
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
			System.out.println("Error writting the string: " + s);
			e.printStackTrace();
		}
	}
	
	//Final write before you're done writing the csv. This is needed because we are using the BufferedWriter. 
	public void endParsing() {
		try {
			bw.close();
		} catch (IOException e) {
			System.out.println("Error when ending Parsing.");
			e.printStackTrace();
		}
	}
	
}
