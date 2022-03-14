package at.jojokobi.blockykingdom.players;

import org.bukkit.Material;

public enum CharacterSpecies implements Iconable{
	HUMAN (0, 0, 0, 0, 0, Material.PLAYER_HEAD),
	GOBLIN (1, -2, 1, 0, 0, Material.ZOMBIE_HEAD),
	DWARF(0, 2, -1, -1, 0, Material.IRON_AXE),
	LIZARDMAN(0, 0, 2, 0, -2, Material.SCUTE),
	ELF(-1, -1, 0, 2, 0, Material.END_ROD),
	GIANT(2, 0, -3, -1, 2, Material.PORKCHOP),
	BARBARIAN(2, 0, -1, -1, 0, Material.IRON_SWORD),
	OCEAN_MAN(0, -1, 0, 2, -1, Material.KELP),
	VAMPIRE(2, -2, 0, 1, -1, Material.REDSTONE);
	
	private final Material material;
	private final int attackOffset;
	private final int defenseOffset;
	private final int speedOffset;
	private final int magicOffset;
	private final int healthOffset;
	
	private CharacterSpecies(int attackOffset, int defenseOffset, int speedOffset, int magicOffset, int healthOffset, Material material) {
		this.attackOffset = attackOffset;
		this.defenseOffset = defenseOffset;
		this.speedOffset = speedOffset;
		this.magicOffset = magicOffset;
		this.healthOffset = healthOffset;
		this.material = material;
	}
	
	@Override
	public Material getMaterial() {
		return material;
	}

	public int getAttackOffset() {
		return attackOffset;
	}
	public int getDefenseOffset() {
		return defenseOffset;
	}
	public int getSpeedOffset() {
		return speedOffset;
	}
	public int getMagicOffset() {
		return magicOffset;
	}
	public int getHealthOffset() {
		return healthOffset;
	}
	
}
