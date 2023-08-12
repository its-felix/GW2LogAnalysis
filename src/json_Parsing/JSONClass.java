package json_Parsing;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import settings.Settings;
import settings.SettingsBuff;
import settings.SettingsCategories;


//I'm not sure.. Thinking this one out. 
public class JSONClass {
	private String logLine = "";
	
	public JSONClass(JSONObject json, JSONObject player, String htmlFilePath, Settings s, Map<String, List<String>> buffsMap) {
		writeDefaultInformation(json, player, htmlFilePath, s);
		
		for (SettingsCategories cat : s.getCategories()) {
			if(cat.getObjectName().equals("general"))
				addToLogLine(JSONUtility.categorySettingsWriting(cat, json));
			else
				addToLogLine(JSONUtility.categorySettingsWriting(cat, player));
		}
		/*
		for(SettingsBuff cat : s.getBuffsList()) {
			if (buffsMap.containsKey(cat.getDisplayName()))
				buffSettingsWriting(cat, player, buffsMap.get(cat.getDisplayName()));
		}
		
		if(buffsMap.containsKey(s.getProfessionBuffsSettings().getDisplayName()))
			buffSettingsWriting(s.getProfessionBuffsSettings(), player, buffsMap.get(s.getProfessionBuffsSettings().getDisplayName()));
		*/
	}
	
	public String getLogLine() {
		return this.logLine;
	}
	private void addToLogLine(String str) {
		this.logLine += str;
	}
	private void setLogLine() {
		
	}
	
	
	
	private void writeDefaultInformation(JSONObject obj, JSONObject player, String htmlFilePath, Settings s) {
		//Location, Date	
		String res = "";
		if(s.getEncounterID() == null || s.getEncounterID() == 0) {
			res += JSONUtility.jsonObjGetString(obj, "fightName") + ",";
		}
		if(s.getEmbbedHTML()) {
			res += htmlFilePath + ",";
		}
		
		if(s.getProfession().equals("Any")) {
			res += JSONUtility.jsonObjGetString(player, "profession") +",";
		}
		res += ", ";
		
		if (!res.isEmpty())
			addToLogLine(res);
	}

}









