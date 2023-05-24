
public class Settings {
	String CharName = "";
	String InputFile = "";
	String OutputFileName = "";
	String OutputFile = "";
	String Location = "";
	int minDuration = 0; //in seconds

	
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
