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
		put("defenses", Arrays.asList("damageTaken","damageBarrier","blockedCount","evadedCount","damageBarrier"));
		//support: healing and boon strips.
		put("support", Arrays.asList("resurrects", "condiCleanse", "condiCleanseSelf", "boonStrips"));
		put("outgoingHealing", Arrays.asList("hps", "healing")); //array object one. outgoingHelaing is inside extHealingStats. 
		//Damage:
		put("dpsAll", Arrays.asList("dps", "damage", "condiDps", "condiDamage", "powerDps", "powerDamage"));
		
	}};
	
	public static List<String> WEAPONS = Arrays.asList("Any","Axe","Dagger","Mace","Pistol","Sword","Scepter","Focus","Shield","Torch","Warhorn","Greatsword","Hammer",
			"Longbow","Rifle","Short bow","Staff"/* aquatic weapons,"Harpoon gun","Spear","Trident"*/);
	
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
	
	public static final List<String> BOON_TABLE_CHECKS = Arrays.asList("Aegis", "Fury", "Might", "Protection", "Quickness",
								"Regeneration", "Resistance", "Retaliation", "Stability", "Swiftness", "Vigor");

	public static Map<String, List<String>> PROFESSION_BUFFS_TABLE_CHECKS = new HashMap<String, List<String>>(){{
		put("Elementalist", Arrays.asList("Air Att.","Fire Att.","Water Att.","Earth Att.","Soothing Mist","Flame Axe","Fiery Greatsword","Earth Shield","Lightnight Hammer","Ice Bow"));
		put("Thief", Arrays.asList("Bounding Dodge", "Lotus Training","Unhindered Combatant", "Devourer Venom","Skale Venom", "Basilisk Venom", "Revealed","Stealth"));
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
		put(30328,"Alacrity");
		
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
		
		//Warrior
		put(14222,"Empower Allies");
		put(14417,"Strength Banner");
		put(14449,"Discipline Banner");
		put(14543,"Defence Banner");
		put(14450,"Tactics Banner");
		
		//Revenant
		put(27890, "Shiro");
		put(27581,"Impossible Odds");
		put(27972,"Ventari");
		put(27928, "Mallyx");
		put(28001,"Embrace the Darkness");
		put(27205,"Jalis");
		put(27273,"Vengeful Hammers");
		put(27732,"Glint");
		put(29379,"Naturalistic Resonance");
		put(42883,"Kalla's Fervor");
		put(44682,"Breakrazo's Bastion");
		put(41016,"Razorclaw's Rage");
		put(45026,"Soulcleave's SUmmit");
		put(26854,"Assassin's Presence");
		put(26646,"Battle Scars");
		
		//Mesmer
		put(10243,"Distortion");
		put(21751,"Signet of the Ether");
		put(10335,"Blur");
		put(30426,"Fencer's Finesse");
		put(44691,"Phantasmal Force");
		
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
		
		//Guardian
		put(9114,"Justice");
		put(9119,"Resolve");
		put(9113,"Courage");
		put(29632,"Spear of Justice");
		put(30308,"Wings of Resolve");
		put(29523,"Shield Courage");
		put(9103,"Zealot's Flame");
		
		//Engineer
		put(38333,"Pinpoint Distribution");
		
		//Necromancer
		put(30285,"Vampiric Aura");
		put(29446,"Reaper's Shroud");
		put(790,"Deth Shroud");
		put(30129,"Infusing Terror");
		
		//Ranger
		put(31803,"Glyph of Empowerment");
		put(14055,"Spotter");
		put(34062,"Grace of the Land");
		put(30449,"Natural Healing");
		put(31508,"Celestial Avatar");
		put(31584,"Ancestral Grace");
		put(12549,"Storm Spirit");
		put(12544,"Frost Spirit");
		put(1254,"Sun Spirit");
		put(12547,"Stone Spirit");
		
		
		
		
	}};
}
