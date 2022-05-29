package at.jojokobi.blockykingdom.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;

import at.jojokobi.blockykingdom.dimensions.HeavenDimension;
import at.jojokobi.blockykingdom.entities.FlyingSheep;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.generation.population.Structure;
import at.jojokobi.mcutil.generation.population.StructureInstance;

public class FlyingSheepFlock extends Structure{

	private EntityHandler entityHandler;
	
	public FlyingSheepFlock(EntityHandler entityHandler) {
		super(16, 16, 1, 50);
		this.entityHandler = entityHandler;
	}
	
	@Override
	public boolean canGenerate(Chunk chunk, long seed) {
		return super.canGenerate(chunk, seed) && HeavenDimension.getInstance().isDimension(chunk.getWorld());
	}

	@Override
	public List<StructureInstance<? extends Structure>> generate(Location loc, long seed) {
		Random random = new Random (generateValueBeasedSeed(loc, seed));
		int amount = 2 + random.nextInt(6);
		for (int i = 0; i < amount; i++) {
			Location place = loc.clone();
			place.add(random.nextDouble() * getWidth(), 0, random.nextDouble() * getLength());
			place.setY(place.getWorld().getHighestBlockYAt(place));
			if (place.getY() > 0) {
				entityHandler.addSavedEntity(new FlyingSheep(place, entityHandler));
			}
		}
		return new ArrayList<>();
	}

	@Override
	public String getIdentifier() {
		return "flying_sheep_flock";
	}

	@Override
	public StructureInstance<? extends Structure> getStandardInstance(Location location) {
		return new StructureInstance<FlyingSheepFlock>(this, location, getWidth(), getHeight(),getLength());
	}

}
