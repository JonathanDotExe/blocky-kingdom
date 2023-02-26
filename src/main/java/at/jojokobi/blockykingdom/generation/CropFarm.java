package at.jojokobi.blockykingdom.generation;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.entities.kingdomvillagers.Trader;
import at.jojokobi.blockykingdom.items.Dagger;
import at.jojokobi.blockykingdom.items.DoubleBow;
import at.jojokobi.blockykingdom.items.FireWand;
import at.jojokobi.blockykingdom.items.GrapplingHook;
import at.jojokobi.blockykingdom.items.Hammer;
import at.jojokobi.blockykingdom.items.Money;
import at.jojokobi.blockykingdom.items.ProtectingFigure;
import at.jojokobi.blockykingdom.items.Smasher;
import at.jojokobi.blockykingdom.kingdoms.Kingdom;
import at.jojokobi.blockykingdom.kingdoms.KingdomHandler;
import at.jojokobi.blockykingdom.kingdoms.KingdomPoint;
import at.jojokobi.blockykingdom.kingdoms.KingdomState;
import at.jojokobi.mcutil.dimensions.DimensionHandler;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.generation.FurnitureGenUtil;
import at.jojokobi.mcutil.generation.TerrainGenUtil;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.loot.LootInventory;
import at.jojokobi.mcutil.loot.LootItem;

public class CropFarm extends Structure{

	private EntityHandler entityHandler;
	private DimensionHandler dimHandler;
	
	static final List<Material> CROP_BLOCKS = Arrays.asList(Material.CARROTS, Material.BEETROOTS, Material.POTATOES, Material.WHEAT, Material.WHEAT, Material.WHEAT);
	
	public CropFarm(EntityHandler entityHandler, DimensionHandler dimHandler) {
		super(10, 10, 2, 0);
		this.entityHandler = entityHandler;
		this.dimHandler = dimHandler;
		
		setxModifier(-85426);
		setzModifier(-542);
	}
	
	@Override
	public boolean canGenerate(Chunk chunk, long seed) {
		return (super.canGenerate(chunk, seed)) && dimHandler.getDimension(chunk.getWorld()) == null && chunk.getWorld().getEnvironment() == Environment.NORMAL;
	}
	
	@Override
	public int calculatePlacementY(int width, int length, Location place) {
		return super.calculatePlacementY(width, length, place) - 1;
	}
	
	@Override
	public List<StructureInstance<? extends Structure>> generateNaturally(Location place, long seed) {
		TerrainGenUtil.buildGroundBelow(place.clone().add(0, -1, 0), getWidth(), getLength(), b -> b.setType(Material.COBBLESTONE));
		return super.generateNaturally(place, seed);
	}

	@Override
	public List<StructureInstance<? extends Structure>> generate(Location loc, long seed) {
		Location place = loc.clone();
		
		Random random = new Random(generateValueBeasedSeed(loc, seed));
		Material crop = CROP_BLOCKS.get(random.nextInt(CROP_BLOCKS.size()));
		
		for (int x = 0; x < getWidth(); x++) {
			for (int z = 0; z < getLength(); z++) {
				place.setX(loc.getX() + x);
				place.setY(loc.getY());
				place.setZ(loc.getZ() + z);
				if (x == 0 || x == getWidth() - 1 || z == 0 || z == getLength() - 1) {
					place.getBlock().setType(Material.OAK_LOG);
				}
				else if (x >= getWidth()/2 - 1 && x >= getWidth()/2 && z >= getLength()/2 - 1 && z >= getLength()/2) {
					place.getBlock().setType(Material.WATER);
				}
				else {
					place.getBlock().setType(Material.FARMLAND);
					place.add(0, 1, 0);
					place.getBlock().setType(crop);
				}
			}
		}
		
		return Arrays.asList(new StructureInstance<CropFarm>(this, loc, getWidth(), getHeight(), getLength()));
	}

	@Override
	public String getIdentifier() {
		return "crop_farm";
	}

	@Override
	public StructureInstance<? extends Structure> getStandardInstance(Location location) {
		return new StructureInstance<CropFarm>(this, location, getWidth(), getHeight(),getLength());
	}
}
