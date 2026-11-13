/**
 * This class represents a location in the city. The city is like a grid,
 * so every location has an x coordinate (the row) and a y coordinate
 * (the column). Both coordinates must be between 0 and the size of the
 * grid minus one ({@link EBikeDemo#MAXX} and {@link EBikeDemo#MAXY}).
 * @author DP classes
 * @author Los Especuladores
 * @version 2026.11.10
 */
public class Location
{
    private int x;
    private int y;




    //=====================================CONSTRUCTOR=====================================




    /**
     * Model a location in the city.
     * @param x The x coordinate. Must be non-negative and lower than {@link EBikeDemo#MAXX}.
     * @param y The y coordinate. Must be non-negative and lower than {@link EBikeDemo#MAXY}.
     * @throws IllegalArgumentException If a coordinate is negative or out of the grid.
     */
    public Location(int x, int y)
    {
        checkX(x);
        checkY(y);
        this.x = x;
        this.y = y;
    }











    //===================================REQUIRED METHODS===================================













    /**
     * Generates the next location to visit in a direct line (Manhattan-style diagonal/straight move)
     * from this location to the destination.
     * @param destination The {@link Location} we want to get to.
     * @return The next {@link Location} one step closer to the destination. Returns the destination
     * if this location is already the destination.
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
     * Determines the number of movements required to get from here to the destination,
     * using the Chebyshev distance (maximum of the absolute differences of the coordinates).
     * @param destination The required destination {@link Location}.
     * @return The number of movement steps.
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








    //===============================GETTERS AND SETTERS OF X===============================








    /**
     * @return The x coordinate.
     */
    public int getX()
    {
        return x;
    }

    /**
     * Changes the x coordinate (row) of the location.
     * @param x The new x coordinate. It must be between 0 and {@link EBikeDemo#MAXX} - 1.
     * @throws IllegalArgumentException If the coordinate is outside the grid.
     */
    public void setX(int x)
    {
        checkX(x);
        this.x = x;
    }










    //===============================GETTERS AND SETTERS OF Y===============================



    /**
     * @return The y coordinate.
     */
    public int getY()
    {
        return y;
    }

    /**
     * Changes the y coordinate (column) of the location.
     * @param y The new y coordinate. It must be between 0 and {@link EBikeDemo#MAXY} - 1.
     * @throws IllegalArgumentException If the coordinate is outside the grid.
     */
    public void setY(int y)
    {
        checkY(y);
        this.y = y;
    }







    //=======================================CHECKERS=======================================




    /**
     * Checks that the x coordinate is inside the grid.
     * We only use it inside this class (in the constructor and in setX).
     * @param x The x coordinate that we want to check.
     * @throws IllegalArgumentException If the coordinate is outside the grid.
     */
    private void checkX(int x)
    {
        if(x < 0 || x >= EBikeDemo.MAXX) {
            throw new IllegalArgumentException(
                        "x-coordinate out of the grid (0-" + (EBikeDemo.MAXX - 1) + "): " + x);
        }
    }
    

    /**
     * Checks that the y coordinate is inside the grid.
     * We only use it inside this class (in the constructor and in setY).
     * @param y The y coordinate that we want to check.
     * @throws IllegalArgumentException If the coordinate is outside the grid.
     */
    private void checkY(int y)
    {
        if(y < 0 || y >= EBikeDemo.MAXY) {
            throw new IllegalArgumentException(
                        "y-coordinate out of the grid (0-" + (EBikeDemo.MAXY - 1) + "): " + y);
        }
    }








    //============================EQUALS, HASHCODE AND TOSTRING============================






    /**
     * Implement content equality for locations.
     * @param obj The object to compare with.
     * @return {@code true} if the other object is a {@link Location} and has the same x and y coordinates, {@code false} otherwise.
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
     * Generates a hash code for the location.
     * It uses the 7/3/5 technique explained in unit 5, with the x and y coordinates.
     * @return A hashcode for the location.
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
     * @return A string representation of the location in the format "x-y".
     */
    @Override
    public String toString()
    {
        return getX() + "-" + getY();
    }
}
