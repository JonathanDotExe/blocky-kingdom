package at.jojokobi.blockykingdom.dimensions;

import java.util.HashSet;
import java.util.OptionalLong;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import at.jojokobi.mcutil.JojokobiUtilPlugin;
import at.jojokobi.mcutil.generation.TerrainGenUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.dimension.DimensionType;

public class HeavenDimensionHandler implements Listener {
	
	private static HeavenDimensionHandler instance;

	private HeavenDimensionHandler() {
		
	}
	
	public static HeavenDimensionHandler getInstance() {
		if (instance == null) {
			instance = new HeavenDimensionHandler();
		}
		return instance;
	}

	private Set<UUID> jumping = new HashSet<>();

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		// Jump Particles
		if (jumping.contains(player.getUniqueId())) {
			player.getWorld().spawnParticle(Particle.SPELL_MOB, player.getEyeLocation(), 10);
//			
//			Vector velocity = player.getVelocity();
//			double y = velocity.getY();
//			velocity.setY(0);
//			if (velocity.lengthSquared() != 0.0) {
//				velocity.normalize();
//				velocity.multiply(2);
//			}
//			velocity.setY(y);
//			player.setVelocity(velocity);
			// Remove
			if (event.getPlayer().getLocation().subtract(0, 0.1, 0).getBlock().getType().isSolid()) {
				player.setFallDistance(0);
				jumping.remove(player.getUniqueId());
			}
			//Go to heaven
			if (player.getLocation().getY() > 255) {
				jumping.remove(player.getUniqueId());
				JojokobiUtilPlugin util = JavaPlugin.getPlugin(JojokobiUtilPlugin.class);
				World dimWorld = util.getDimensionHandler().getDimensionWorld(event.getPlayer().getWorld(), HeavenDimension.getInstance());
				if (dimWorld != null) {
					System.out.println("Teleporting to " + dimWorld.getName());
					Location place = new Location(dimWorld, player.getLocation().getX(), 150, player.getLocation().getZ());
					place.setY(place.getWorld().getHighestBlockYAt(place));
					if (place.getBlockY() == 0) {
						place.setY(70);
						place.getBlock().setType(Material.GLASS);
						place.add(0, 1, 0);
					}
					player.teleport(place);
					//doSuperJump(player, 10);
				}
			}
		} else if (HeavenDimension.getInstance().isDimension(player.getWorld())) {
			Location from = event.getFrom();
			Location to = event.getTo();
			if (to != null) {
				// Push up
				if (to.clone().add(0, -0.1, 0).getBlock().getType() == Material.WHITE_WOOL
						&& !from.clone().add(0, -0.1, 0).getBlock().getType().isSolid()
						&& event.getPlayer().getFallDistance() > 0.5f) {
					player.setVelocity(player.getVelocity().setY(Math.abs(player.getVelocity().getY() / 3 * 2)));
				}
				// Super jump
				else if (player.isSprinting() && from.clone().add(0, -0.1, 0).getBlock().getType() == Material.WHITE_WOOL
						&& !to.clone().add(0, -0.1, 0).getBlock().getType().isSolid()) {
					doSuperJump(player, 3);
				}
			}
		}
	}
	
	public void doSuperJump (Player player, double y) {
		Vector velocity = player.getVelocity();
		velocity.setY(0);
		if (velocity.lengthSquared() != 0.0) {
			velocity.normalize();
			velocity.multiply(2);
		}
		velocity.setY(y);
		player.setVelocity(velocity);
		jumping.add(player.getUniqueId());
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getCause() == DamageCause.FALL && (jumping.contains(event.getEntity().getUniqueId())
				|| (event.getEntity().getLocation().add(0, -0.1, 0).getBlock().getType() == Material.WHITE_WOOL
						&& HeavenDimension.getInstance().isDimension(event.getEntity().getWorld())))) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		jumping.remove(event.getPlayer().getUniqueId());
	}
	
	/*@EventHandler
	public void onChunkPopulate(ChunkPopulateEvent event) {
		//Generate heaven
		if (HeavenDimension.getInstance().isDimension(event.getChunk().getWorld())) {
			int x = event.getChunk().getX();
			int z = event.getChunk().getZ();
			//Remove bottom
			NoiseGenerator generator = new SimplexNoiseGenerator(event.getChunk().getWorld().getSeed() + 87);
			for (int xPos = 0; xPos < TerrainGenUtil.CHUNK_WIDTH; xPos++) {
				for (int zPos = 0; zPos < TerrainGenUtil.CHUNK_LENGTH; zPos++) {
					int height = (int) (generator.noise((x * TerrainGenUtil.CHUNK_WIDTH + xPos) * 0.005, (z * TerrainGenUtil.CHUNK_LENGTH + zPos) * 0.005) * 60 + 80);
					boolean ended = false;
					for (int yPos = event.getWorld().getMinHeight(); yPos < height || !ended; yPos++) {
						Material type = event.getChunk().getBlock(xPos, yPos, zPos).getType();
						ended = !type.isSolid() || type.isAir();
						
						if (yPos < height || !ended) {
							BlockState state = event.getChunk().getBlock(xPos, yPos, zPos).getState();
							state.setType(Material.AIR);
							state.update(true, false);
						}
					}
				}
			}
			//Clouds
			for (int y = 30; y < 55; y += 5) {
				NoiseGenerator g = new SimplexNoiseGenerator(event.getChunk().getWorld().getSeed() + y);
				for (int i = 0; i < TerrainGenUtil.CHUNK_WIDTH; i++) {
					for (int j = 0; j < TerrainGenUtil.CHUNK_LENGTH; j++) {
						if (g.noise((x * TerrainGenUtil.CHUNK_WIDTH + i) * 0.025, (z * TerrainGenUtil.CHUNK_LENGTH + j) * 0.025) > 0.7) {
							event.getChunk().getBlock(i, y, j).setType(Material.WHITE_WOOL, false);
							event.getChunk().getBlock(i, y + 1, j).setType(Material.WHITE_WOOL, false);
							event.getChunk().getBlock(i, y + 2, j).setType(Material.WHITE_WOOL, false);
						}
					}
				}
			}
		}
	}*/

}
