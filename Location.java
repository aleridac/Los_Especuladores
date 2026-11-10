/**ckck
 * Model a location in a two-dimensional grid city using (x, y) coordinates.
 * The x coordinate is the row and the y coordinate is the column of the grid,
 * so both must be between 0 and the grid size minus one
 * ({@link EBikeDemo#MAXX} and {@link EBikeDemo#MAXY}).
 * @author David J. Barnes and Michael Kölling
 * @author DP classes
 * @author Alvaro
 * @version 2026.11.10
 */
public class Location
{
    private int x;
    private int y;

    /**
     * Model a location in the city.
     * @param x The x coordinate (row). Must be between 0 and {@link EBikeDemo#MAXX} - 1.
     * @param y The y coordinate (column). Must be between 0 and {@link EBikeDemo#MAXY} - 1.
     * @throws IllegalArgumentException If a coordinate is out of the grid.
     */
    public Location(int x, int y)
    {
        checkX(x);
        checkY(y);
        this.x = x;
        this.y = y;
    }

    /**
     * Generates the next location to visit on the way from this location
     * to the destination. In each step, 1 is added to or subtracted from
     * every coordinate that is still different from the destination one,
     * so the movement is diagonal until one coordinate matches, and then
     * straight along the other one.
     * @param destination The {@link Location} we want to get to.
     * @return The next {@link Location} one step closer to the destination.
     * Returns a location equal to the destination if this location is
     * already the destination.
     * @throws IllegalArgumentException If the destination is null.
     */
    public Location nextLocation(Location destination)
    {
        if(destination == null) {
            throw new IllegalArgumentException("The destination cannot be null");
        }
        int nextX = getX();
        int nextY = getY();

        if(getX() < destination.getX()) {
            nextX++;
        }
        else if(getX() > destination.getX()) {
            nextX--;
        }

        if(getY() < destination.getY()) {
            nextY++;
        }
        else if(getY() > destination.getY()) {
            nextY--;
        }

        return new Location(nextX, nextY);
    }

    /**
     * Determines the number of steps required to get from here to the
     * destination. It is the maximum of the absolute differences between
     * the x coordinates and the y coordinates of both locations.
     * @param destination The required destination {@link Location}.
     * @return The number of steps to get to the destination.
     * @throws IllegalArgumentException If the destination is null.
     */
    public int distance(Location destination)
    {
        if(destination == null) {
            throw new IllegalArgumentException("The destination cannot be null");
        }
        int difX = getX() - destination.getX();
        int difY = getY() - destination.getY();

        if(difX < 0) {
            difX = -difX;
        }
        if(difY < 0) {
            difY = -difY;
        }

        if(difX > difY) {
            return difX;
        }
        else {
            return difY;
        }
    }

    /**
     * Gets the x coordinate (row) of the location.
     * @return The x coordinate.
     */
    public int getX()
    {
        return x;
    }

    /**
     * Sets the x coordinate (row) of the location.
     * @param x The new x coordinate. Must be between 0 and {@link EBikeDemo#MAXX} - 1.
     * @throws IllegalArgumentException If the coordinate is out of the grid.
     */
    public void setX(int x)
    {
        checkX(x);
        this.x = x;
    }

    /**
     * Gets the y coordinate (column) of the location.
     * @return The y coordinate.
     */
    public int getY()
    {
        return y;
    }

    /**
     * Sets the y coordinate (column) of the location.
     * @param y The new y coordinate. Must be between 0 and {@link EBikeDemo#MAXY} - 1.
     * @throws IllegalArgumentException If the coordinate is out of the grid.
     */
    public void setY(int y)
    {
        checkY(y);
        this.y = y;
    }

    /**
     * Checks that an x coordinate is inside the grid.
     * It is only for internal use of the class (constructor and setX).
     * @param x The x coordinate to check.
     * @throws IllegalArgumentException If the coordinate is out of the grid.
     */
    private void checkX(int x)
    {
        if(x < 0 || x >= EBikeDemo.MAXX) {
            throw new IllegalArgumentException(
                        "x-coordinate out of the grid (0-" + (EBikeDemo.MAXX - 1) + "): " + x);
        }
    }

    /**
     * Checks that a y coordinate is inside the grid.
     * It is only for internal use of the class (constructor and setY).
     * @param y The y coordinate to check.
     * @throws IllegalArgumentException If the coordinate is out of the grid.
     */
    private void checkY(int y)
    {
        if(y < 0 || y >= EBikeDemo.MAXY) {
            throw new IllegalArgumentException(
                        "y-coordinate out of the grid (0-" + (EBikeDemo.MAXY - 1) + "): " + y);
        }
    }

    /**
     * Checks whether this location is equal to another object.
     * Two locations are considered equal if they have the same x and y coordinates.
     * @param obj The object to compare with this location.
     * @return true if obj is a {@link Location} with the same x and y
     * coordinates, false otherwise.
     */
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj) {
            return true;
        }
        if(!(obj instanceof Location)) {
            return false;
        }
        Location other = (Location) obj;
        return getX() == other.getX() && getY() == other.getY();
    }

    /**
     * Returns a hash code for this location, based on its x and y coordinates
     * (the same fields used in {@link #equals(Object)}).
     * Hashcode technique taken from Effective Java by Joshua Bloch.
     * @return The hash code of the location.
     */
    @Override
    public int hashCode()
    {
        int result = 7;
        result = 3 * result + getX();
        result = 5 * result + getY();
        return result;
    }

    /**
     * Returns a string representation of the location in the format "x-y",
     * as required by the simulation output.
     * @return A string representation of the location.
     */
    @Override
    public String toString()
    {
        return getX() + "-" + getY();
    }
}
