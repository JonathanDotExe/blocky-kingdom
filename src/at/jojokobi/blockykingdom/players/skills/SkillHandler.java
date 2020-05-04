package at.jojokobi.blockykingdom.players.skills;

import org.bukkit.Material;

import at.jojokobi.mcutil.Handler;

public final class SkillHandler extends Handler<Skill>{
	
	private static final SkillHandler INSTANCE = new SkillHandler();

	private SkillHandler() {
		
	}

	public static SkillHandler getInstance() {
		return INSTANCE;
	}

	@Override
	protected Skill getStandardInstance(String namespace, String identifier) {
		return new Skill(1, 0, "ERROR") {
			@Override
			public String getNamespace() {
				return namespace;
			}
			
			@Override
			public String getIdentifier() {
				return identifier;
			}

			@Override
			public Material getMaterial() {
				return Material.BARRIER;
			}

			@Override
			public String getDescription() {
				return "Seems like a skill was removed or your savefile is corrupted";
			}
		};
	}

}
