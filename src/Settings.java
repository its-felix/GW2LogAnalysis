public class Settings {
	int minDuration = 0; //in seconds
	
	String CharName = "";
	String InputFile = "";
	String OutputFileName = "LogAnalysisOutput";
	String OutputFile = "";
	String Location = "Any";
	String DesiredStats = "All";
	String Profession = "Any";
	String Specialization ="Any";
	SettingsBuffs boonSettings = new SettingsBuffs(); 
	SettingsBuffs professionBuffsSettings = new SettingsBuffs();
	String WepSet1MainHand="Any";
	String WepSet1Offhand="Any";
	String WepSet2MainHand="Any";
	String WepSet2Offhand="Any";
	Boolean WepFilterStrict = false;

	
	public Boolean getWepFilterStrict() {
		return WepFilterStrict;
	}
	public void setWepFilterStrict(Boolean wepFilterStrict) {
		WepFilterStrict = wepFilterStrict;
	}
	public String getWepSet1MainHand() {
		return WepSet1MainHand;
	}
	public void setWepSet1MainHand(String wepSet1MainHand) {
		WepSet1MainHand = wepSet1MainHand;
	}
	public String getWepSet1Offhand() {
		return WepSet1Offhand;
	}
	public void setWepSet1Offhand(String wepSet1Offhand) {
		WepSet1Offhand = wepSet1Offhand;
	}
	public String getWepSet2MainHand() {
		return WepSet2MainHand;
	}
	public void setWepSet2MainHand(String wepSet2MainHand) {
		WepSet2MainHand = wepSet2MainHand;
	}
	public String getWepSet2Offhand() {
		return WepSet2Offhand;
	}
	public void setWepSet2Offhand(String wepSet2Offhand) {
		WepSet2Offhand = wepSet2Offhand;
	}
	public SettingsBuffs getProfessionBuffsSettings() {
		return professionBuffsSettings;
	}
	public void setProfessionBuffsSettings(SettingsBuffs professionBuffsSettings) {
		this.professionBuffsSettings = professionBuffsSettings;
	}
	public SettingsBuffs getBoonSettings() {
		return boonSettings;
	}
	public void setBoonSettings(SettingsBuffs boonSettings) {
		this.boonSettings = boonSettings;
	}
	public String getProfession() {
		return Profession;
	}
	public void setProfession(String profession) {
		Profession = profession;
	}
	public String getSpecialization() {
		return Specialization;
	}
	public void setSpecialization(String specialization) {
		Specialization = specialization;
	}
	
	
	
	public String getDesiredStats() {
		return DesiredStats;
	}
	public void setDesiredStats(String desiredStats) {
		DesiredStats = desiredStats;
	}
	

	
	public String getLocation() {
		return Location;
	}
	public void setLocation(String location) {
		Location = location;
	}
	public int getMinDuration() {
		return minDuration;
	}
	public void setMinDuration(int minDuration) {
		this.minDuration = minDuration;
	}
	public String getCharName() {
		return CharName;
	}
	public void setCharName(String charName) {
		CharName = charName;
	}
	public String getInputFile() {
		return InputFile;
	}
	public void setInputFile(String inputFile) {
		InputFile = inputFile;
	}
	public String getOutputFileName() {
		return OutputFileName;
	}
	public void setOutputFileName(String outputFileName) {
		OutputFileName = outputFileName;
	}
	public String getOutputFile() {
		return OutputFile;
	}
	public void setOutputFile(String outputFile) {
		OutputFile = outputFile;
	}


}
