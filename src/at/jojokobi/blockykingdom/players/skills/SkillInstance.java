package at.jojokobi.blockykingdom.players.skills;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class SkillInstance implements ConfigurationSerializable {

	private String identifier;
	private String namespace;
	private int level;
	private boolean activated;
	
	public SkillInstance(String identifier, String namespace, int level) {
		super();
		this.identifier = identifier;
		this.namespace = namespace;
		this.level = level;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object> ();
		
		map.put("key", identifier);
		map.put("namespace", namespace);
		map.put("level", level);

		return map;
	}
	
	public String getIdentifier() {
		return identifier;
	}

	public String getNamespace() {
		return namespace;
	}

	public int getLevel() {
		return level;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public static SkillInstance valueOf (Map<String, Object> map) {
		return new SkillInstance(map.get("key").toString(), map.get("namespace").toString(), (int) map.get("level"));
	}

}
