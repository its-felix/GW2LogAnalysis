public class Settings {
	int minDuration = 0; //in seconds
	
	String CharName = "";
	String InputFile = "";
	String OutputFileName = "";
	String OutputFile = "";
	String Location = "";
	String DesiredStats = "";
	String Profession = "";
	String Specialization ="";
	SettingsBuffs boonSettings = new SettingsBuffs(); 
	SettingsBuffs professionBuffsSettings = new SettingsBuffs();

	
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
