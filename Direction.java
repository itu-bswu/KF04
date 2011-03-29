import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;

/**
 * An enum class to get a direction. A random direction can be obtained by
 * calling the getRandomDirection() method.
 * 
 * @author Emil
 * 
 */
public enum Direction {
	WEST, EAST, NORTH, SOUTH;

	/**
	 * Shuffle an ArrayList containing the directions and returns the first
	 * value.
	 * 
	 * @return randomDirection A random value of the four directions.
	 */
	public static Direction getRandomDirection() {
		ArrayList<Direction> directions = new ArrayList<Direction>();
		for (Direction d : Direction.values())
			directions.add(d);
		Collections.shuffle(directions);
		Direction randomDirection = directions.get(0);

		return randomDirection;
	}

	/**
	 * Get the opposite direction of the one given.
	 * 
	 * @param direction the given direction
	 * @return Direction the opposite direction of the given
	 */
	public static Direction getOpposite(Direction direction) {
		switch (direction) {
		case NORTH:
			return SOUTH;
		case EAST:
			return WEST;
		case WEST:
			return EAST;
		case SOUTH:
			return NORTH;
		}
		return null;
	}
	
	/**
	 * Get coordinate point 
	 */
	public Point2D coordinatepoint() {
		switch (this) {
		case NORTH:
			return new Point2D.Double(0, -1);
		case EAST:
			return new Point2D.Double(1, 0);
		case WEST:
			return new Point2D.Double(-1, 0);
		case SOUTH:
			return new Point2D.Double(0, 1);
		}
		return null;
	}
}
