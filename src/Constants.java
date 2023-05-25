import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Constants {
	Constants() {
		
	}
	
	public static Map<String, String> LOCATIONS = new HashMap<String, String>() {{
		put("Any", "");
		put("WvW - RBL", "Detailed WvW - Red Desert Borderlands");
	}};
	
	public static List<String> DESIRED_STATS = Arrays.asList("All", "Damage", "Healing");
	
	//stats to find from logs: 
	public static Map<String, List<String>> HEADERS = new HashMap<String, List<String>>() {{
		put("general", Arrays.asList("fightName", "success", "durationMS"));
		put("statsAll", Arrays.asList("distToCom", "swapCount", "totalDmg"));
		put("support", Arrays.asList("resurrects", "condiCleanse", "condiCleanseSelf", "boonStrips"));
	}};
	
	//Professions and specializations:
	public static Map<String, List<String>> profSpec = new HashMap<String, List<String>>() {{
		put("Any", Arrays.asList(""));
		put("Guardian", Arrays.asList("Guardian","Dragonhunter","Firebrand","Willbender"));
		put("Revenant",Arrays.asList("Revenant", "Herald","Renegade","Vindicator"));
		put("Warrior",Arrays.asList("Warrior","Berserker","Spellbreaker","Bladesworn"));
		
		put("Engineer",Arrays.asList("Engineer","Scrapper","Holosmith","Mechanist"));
		put("Ranger",Arrays.asList("Ranger","Druid","Soulbeast","Untamed"));
		put("Thief",Arrays.asList("Thief","Daredevil","Deadeye","Specter"));
		
		put("Elementalist", Arrays.asList("Elementalist", "Tempest", "Weaver", "Catalyst"));
		put("Mesmer",Arrays.asList("Mesmer","Chronomancer","Mirage","Virtuoso"));
		put("Necromancer",Arrays.asList("Necromancer","Reaper","Scourge","Harbinger"));
	}};
	
	
	//buffs, more details found at https://wiki.guildwars2.com/wiki/User:Frvwfr2/buffids
	public static Map<Integer, String> buffs = new HashMap<Integer, String>() {{
		put(17675, "Aegis");
		put(725, "Fury");
		put(740, "Might");
		put(717, "Protection");
		put(1187, "Quickness");
		put(718, "Regeneration");
		put(17674, "Regeneration");
		put(26980, "Resistance");
		put(873, "Retaliation");
		put(1122, "Stability");
		put(719, "Swiftness");
		put(726, "Vigor");
		
			//Auras
		put(10332, "Chaos armor Aura");
		put(5677, "Fire Shield Aura");
		put(5577, "Shocking Aura");
		put(5579, "Frost Aura");
		put(5684, "Magnetic Aura");
		put(25518, "Light Aura");
		
			//Class specific:
		//Thief
		put(33162, "Bounding Dodge");
		put(32200, "Lotus Training");
		put(32931, "Unhindered Combatant");
		put(13094, "Devourer Venom");
		put(13036, "Skale Venom");
		put(13133, "Basilisk Venom");
		put(890, "Revealed");
		put(13017, "Stealth");
		
		//Elementalist
		put(5575, "Air Att.");
		put(5585, "Fire Att.");
		put(5586, "Water Att.");
		put(5580, "Earth Att.");
		put(5587, "Soothing Mist");
		put(15789, "Flame Axe");
		put(15792, "Fiery Greatsword");
		put(15788, "Earth Shield");
		put(15791, "Lightnight Hammer");
		put(15790, "Ice Bow");
		
		
	}};
}
