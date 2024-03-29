package jumperutils.cannontracer.utils;

import org.bukkit.Location;

public class SimpleLocation {
	public double x, y, z;

	public void init(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public SimpleLocation() {
		init(0, 0, 0);
	}

	public SimpleLocation(double x, double y, double z) {
		init(x, y, z);
	}

	public SimpleLocation(Location location) {
		init(location.getX(), location.getY() + 0.49, location.getZ());
	}

	public static SimpleLocation add(SimpleLocation a, SimpleLocation b) {
		return new SimpleLocation(a.x + b.x, a.y + b.y, a.z + b.z);
	}

	public void add(SimpleLocation a) {
		this.x += a.x;
		this.y += a.y;
		this.z += a.z;
	}

	public static SimpleLocation sub(SimpleLocation a, SimpleLocation b) {
		return new SimpleLocation(a.x - b.x, a.y - b.y, a.z - b.z);
	}

	public void sub(SimpleLocation a) {
		this.x -= a.x;
		this.y -= a.y;
		this.z -= a.z;
	}

	public double magnitude() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public boolean equals(SimpleLocation a) {
		return (this.x == a.x && this.y == a.y && this.z == a.z);
	}

	public String getString() {
		return "" + x + "," + y + "," + z;
	}
}
