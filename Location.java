import java.util.*;

/**
 * Model a location in a two-dimensional grid city using (x, y) coordinates.
 * @author David J. Barnes and Michael Kölling
 * @author DP classes 
 * @version 2026.10.13
 */
public class Location
{
    private int x;
    private int y;

    /**
     * Model a location in the city.
     * @param x The x coordinate. Must be non-negative.
     * @param y The y coordinate. Must be non-negative.
     * @throws IllegalArgumentException If a coordinate is negative.
     */
    public Location(int x, int y)
    {
        if(x < 0) {
            throw new IllegalArgumentException(
                        "Negative x-coordinate: " + x);
        }
        if(y < 0) {
            throw new IllegalArgumentException(
                        "Negative y-coordinate: " + y);
        }        
        this.x = x;
        this.y = y;
    }
    
    /**
     * Generates the next location to visit in a direct line (Manhattan-style diagonal/straight move) 
     * from this location to the destination.
     * @param destination The {@link Location} we want to get to.
     * @return The next {@link Location} one step closer to the destination. Returns the destination
     * if this location is already the destination.
     */
    public Location nextLocation(Location destination){
         
        int a = this.x;
        int b = this.y;
        
        if (this.x != destination.x){
            if(this.x < destination.x){
                a++;
            }
            else{
                a--;
            }
        }
        
        if (this.y != destination.y){
            if(this.y < destination.y){
                b++;
            }
            else{
                b--;
            }
        }
        
        return new Location(a,b);
        
    }

    /**
     * Determines the number of movements required to get from here to the destination, 
     * using the Chebyshev distance (maximum of the absolute differences of the coordinates).
     * @param destination The required destination {@link Location}.
     * @return The number of movement steps.
     */
    public int distance(Location destination){
        
        int difX = this.x - destination.x;
        int difY = this.y - destination.y;
        int res;
        
        if (difX < 0){
            difX = -difX;
        }
        
        if (difY < 0){
            difY = -difY;
        }
        
        if (difX > difY){
            res = difX;
        }
        else{
            res = difY;
        }
        
        return res;
        
    }
    /**
     * Implement content equality for locations.
     * @param other The object to compare with.
     * @return {@code true} if the other object is a {@link Location} and has the same x and y coordinates, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object other)
    {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Location)) {
            return false;
        }
        Location location = (Location) other;
        return x == location.x && y == location.y;
    }
    
    /**
     * @return A string representation of the location in the format "x-y".
     */
    @Override
    public String toString()
    {
        return x + "-" + y;
    }

    /**
     * Generates a hash code for the location.
     * It uses the top 16 bits for the y value and the bottom 16 bits for the x value
     * to ensure a unique hash code for most grid sizes.
     * @return A hashcode for the location.
     */
    @Override
    public int hashCode()
    {
        return (y << 16) + x;
    }

    /**
     * @return The x coordinate.
     */
    public int getX()
    {
        return x;
    }

    /**
     * @return The y coordinate.
     */
    public int getY()
    {
        return y;
    }
}
