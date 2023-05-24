import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CustomJSONParsing {
	public static BufferedWriter bw;
	
	public CustomJSONParsing(String fileName) {
		FileWriter file;
		try {
			file = new FileWriter(fileName);
			bw = new BufferedWriter(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//TODO: insert the headers here, by checking the settings. 
	}
	
	private void writeOverview() {
		
	}
	
	public static void writeNewLine() {
		try {
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void endParsing() {
		try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public final List<String> statsAllKeys = Arrays.asList("distance to commander", "Weapon swap #", "total Dmg");
	/*public static Map<String, String> statsAll = new HashMap<String, String>() {{
		put("distToCom", null);
		put("swapCount", null);
		put("totalDmg", null);
	}};*/
	public final List<String> general = Arrays.asList("fightName", "success");
	public final static List<String> statsAll = Arrays.asList("distToCom", "swapCount", "totalDmg");
	public final List<String> support = Arrays.asList("resurrects", "condiCleanse", "condiCleanseSelf", "boonStrips");
	
	public static Boolean writeToCSV(JSONObject json, JSONObject player) {
		
		try {
			bw.write(getStringFromJSONArray(player, "statsAll", statsAll));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static String getStringForGeneral(JSONObject obj, JSONObject player) {
		String result = "";
		
		for (String s : Arrays.asList("fightName", "success")) {
			result += obj.get(s) + ", ";
		}
		/*
		for (String s : Arrays.asList(null)) {
			result += obj.get(s) + ", ";
		}
		*/
		return result;
	}
	
	
	public static String getStringFromJSONArray(JSONObject player, String listName, List<String> list) {
		//Check conditions? 
		JSONObject JObj = (JSONObject) ((JSONArray) player.get(listName)).get(0);
		
		/*
		for (Map.Entry<String, String> entry : list.entrySet()) {
			entry.setValue(String.valueOf(JObj.get(entry.getKey())));
		}
		String result = String.valueOf(new ArrayList<String>(list.values()));
		*/
		String result = "";
		for (String s : list) {
			result += JObj.get(s);
		}
		
		return result;
	}
}
