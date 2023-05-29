import java.util.List;
import java.util.ArrayList;

public class SettingsBuffs {
	Boolean phaseDuration = false;
	Boolean phaseActiveDuration = true;
	Boolean uptime = true;
	Boolean generationSelf = false;
	Boolean generationGroup = false;
	
	Boolean display = false;
	List<String> buffs = new ArrayList<>();
	
	public SettingsBuffs() {};
	public SettingsBuffs(Boolean phaseDuration, Boolean phaseActiveDuration, Boolean uptime, Boolean generationSelf,
			Boolean generationGroup, Boolean display, List<String> buffs) {
		super();
		this.phaseDuration = phaseDuration;
		this.phaseActiveDuration = phaseActiveDuration;
		this.uptime = uptime;
		this.generationSelf = generationSelf;
		this.generationGroup = generationGroup;
		this.display = display;
		this.buffs = buffs;
	}
	public Boolean getPhaseDuration() {
		return phaseDuration;
	}
	public void setPhaseDuration(Boolean phaseDuration) {
		this.phaseDuration = phaseDuration;
	}
	public Boolean getPhaseActiveDuration() {
		return phaseActiveDuration;
	}
	public void setPhaseActiveDuration(Boolean phaseActiveDuration) {
		this.phaseActiveDuration = phaseActiveDuration;
	}
	public Boolean getUptime() {
		return uptime;
	}
	public void setUptime(Boolean uptime) {
		this.uptime = uptime;
	}
	public Boolean getGenerationSelf() {
		return generationSelf;
	}
	public void setGenerationSelf(Boolean generationSelf) {
		this.generationSelf = generationSelf;
	}
	public Boolean getGenerationGroup() {
		return generationGroup;
	}
	public void setGenerationGroup(Boolean generationGroup) {
		this.generationGroup = generationGroup;
	}
	public Boolean getDisplay() {
		return display;
	}
	public void setDisplay(Boolean display) {
		this.display = display;
	}
	public List<String> getBuffs() {
		return buffs;
	}
	public void setBuffs(List<String> buffs) {
		this.buffs = buffs;
	}
	
}
