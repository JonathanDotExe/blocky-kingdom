package at.jojokobi.blockykingdom.players.skills;

import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.CharacterProfession;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.StatHandler;

public class MiningSenseSkill extends Skill {
	
	public static final String IDENTIFIER = "mining_sense";
	
	private static final Note.Tone[] NOTES = {Tone.A, Tone.B, Tone.C, Tone.D, Tone.E, Tone.F, Tone.G};

	public MiningSenseSkill() {
		super(12, 20, "Mining Sense");
	}
	
	@EventHandler
	public void onBlockBreakEvent (BlockBreakEvent event) {
		Player player = event.getPlayer();
		CharacterStats stats = StatHandler.getInstance().getStats(player).getCharacterStats();
		int level = stats.getSkillLevel(this);
		if (level > 0) {
			Location place = event.getBlock().getLocation().add(-level, 0, -level);
			int startX = place.getBlockX();
			int startZ = place.getBlockZ();
			for (int x = 0; x < level * 2 + 1; x++) {
				for (int z = 0; z < level * 2 + 1; z++) {
					place.setX(startX + x);
					place.setZ(startZ + z);
					int note = (int) Math.sqrt(Math.pow(x - level - 0.5, 2) + Math.sqrt(Math.pow(z - level - 0.5, 2)));
					note = Math.min(note, NOTES.length - 1);
					Instrument instrument = getInstrument(place.getBlock().getType());
					if (instrument != null) {
						player.playNote(place, instrument, Note.natural(1, NOTES[NOTES.length - note - 1]));
					}
				}
			}
		}
	}
	
	private Instrument getInstrument (Material material) {
		Instrument instrument = null;
		switch (material) {
//		case COAL_ORE:
//			instrument = Instrument.PIANO;
//			break;
//		case IRON_ORE:
//			instrument = Instrument.IRON_XYLOPHONE;
//			break;
		case GOLD_ORE:
			instrument = Instrument.BELL;
			break;
		case DIAMOND_ORE:
			instrument = Instrument.PLING;
			break;
		case EMERALD_ORE:
			instrument = Instrument.BIT;
			break;
//		case REDSTONE_ORE:
//			instrument = Instrument.GUITAR;
//			break;
//		case LAPIS_ORE:
//			instrument = Instrument.STICKS;
//			break;
		default:
			break;
		}
		return instrument;
	}
	
	@Override
	public boolean canLearn(CharacterStats character) {
		return super.canLearn(character) && character.getProfession() == CharacterProfession.SMITH;
	}

	@Override
	public String getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public String getNamespace() {
		return BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE;
	}

	@Override
	public Material getMaterial() {
		return Material.DIAMOND_PICKAXE;
	}

}
