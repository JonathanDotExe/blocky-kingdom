package at.jojokobi.blockykingdom.items;


import org.bukkit.Location;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

public class ShotHook {

	private Location startLocation;
	private Vector velocity;
	private Snowball snowball;
	
	public ShotHook(Location startLocation, Vector velocity, Snowball snowball) {
		super();
		this.startLocation = startLocation;
		this.velocity = velocity;
		this.snowball = snowball;
	}

	public Location getStartLocation() {
		return startLocation;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public Snowball getSnowball() {
		return snowball;
	}

}
