package at.jojokobi.blockykingdom.players;

import org.bukkit.Material;

public enum CharacterProfession implements Iconable{
	NINJA(1, 0, 2, 0, 1, Material.STICK),
	ADVENTURER(1, 1, 1, 1, 1, Material.ELYTRA),
	KNIGHT (2, 1, 0, 0, 1, Material.IRON_HELMET),
	VILLAGER(0, 0, 0, 0, 0, Material.BOOK),
	SMITH(1, 2, 0, 0, 1, Material.ANVIL),
	SUMO (0, 0, 0, 0, 3, Material.PORKCHOP),
	MAGE (0, 0, 0, 3, 0, Material.POTION);
	
	private final int initialAttack;
	private final int initialDefense;
	private final int initialSpeed;
	private final int initialMagic;
	private final int initialHealth;
	private final Material material;
	
	private CharacterProfession(int initialAttack, int initialDefense, int initialSpeed, int initialMagic,
			int initialHealth, Material material) {
		this.initialAttack = initialAttack;
		this.initialDefense = initialDefense;
		this.initialSpeed = initialSpeed;
		this.initialMagic = initialMagic;
		this.initialHealth = initialHealth;
		this.material = material;
	}
	
	public void addValues (CharacterStats stats) {
		stats.setAttack(stats.getAttack() + getInitialAttack());
		stats.setDefense(stats.getDefense() + getInitialDefense());
		stats.setSpeed(stats.getSpeed() + getInitialSpeed());
		stats.setMagic(stats.getMagic() + getInitialMagic());
		stats.setHealth(stats.getHealth() + getInitialHealth());
	}
	
	public int getInitialAttack() {
		return initialAttack;
	}
	public int getInitialDefense() {
		return initialDefense;
	}
	public int getInitialSpeed() {
		return initialSpeed;
	}
	public int getInitialMagic() {
		return initialMagic;
	}
	public int getInitialHealth() {
		return initialHealth;
	}

	@Override
	public Material getMaterial() {
		return material;
	}	
	
}
