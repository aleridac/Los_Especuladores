import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

/**
 * This class represents a station for electric bikes in the city.
 * A station has a location and some {@link Dock}s where the
 * {@link ElectricBike} can recharge. The docks are always in order:
 * first by charging speed (from the biggest to the smallest), then by fee
 * (from the smallest to the biggest) and then by ID (in alphabetical
 * order). For this we use {@link ComparatorDocksChargingSpeed}.
 * @author DP classes
 * @author Los Especuladores
 * @version 2026.11.10
 */
public class BikeStation
{
    private String idStation;
    private Location location;
    private List<Dock> docks;






    //=====================================CONSTRUCTOR=====================================






    /**
     * Constructor for objects of class BikeStation.
     * @param id The unique identifier of the station (e.g., CC01). Must not be null or empty.
     * @param location The location of the station in the city. Must not be null.
     * @throws IllegalArgumentException If the ID is null or empty.
     * @throws NullPointerException If the location is null.
     */
    public BikeStation(String id, Location location)
    {
        checkId(id);
        checkLocation(location);
        this.idStation = id;
        this.location = location;
        this.docks = new ArrayList<>();
    }






    //===================================REQUIRED METHODS===================================






    /**
     * Adds a new {@link Dock} to the station.
     * @param dock The new Dock unit.
     * @throws IllegalArgumentException If the dock is null or it is already in the station.
     */
    public void addDock(Dock dock)
    {
        checkDock(dock, docks);
        docks.add(dock);
        Collections.sort(docks, new ComparatorDocksChargingSpeed());
    }

    /**
     * Looks for the {@link Dock} of the station that has this ID.
     * The search stops when the dock is found.
     * @param idDock The ID of the dock that we are looking for.
     * @return The dock with this ID, or null if the station does not have a dock with this ID.
     */
    public Dock getDock(String idDock)
    {
        Iterator<Dock> it = docks.iterator();
        Dock found = null;
        while(it.hasNext() && found == null) {
            Dock dock = it.next();
            if(dock.getIdDock().equals(idDock)) {
                found = dock;
            }
        }
        return found;
    }
    
    
    

    /**
     * Retrieves the first free {@link Dock} found at the station.
     * @return A free {@link Dock}, or null if none are found.
     */
    public Dock getFreeDock()
    {
        Iterator<Dock> it = docks.iterator();
        Dock freeDock = null;
        while(it.hasNext() && freeDock == null) {
            Dock dock = it.next();
            if(dock.isFree()) {
                freeDock = dock;
            }
        }
        return freeDock;
    }
    

    /**
     * Calculates how many bikes have been recharged in all the docks of the station.
     * @return The total number of bikes recharged in this station.
     */
    public int getTotalBikesRecharged()
    {
        int total = 0;
        for(Dock dock : docks) {
            total += dock.getNumerEVRecharged();
        }
        return total;
    }






    //=================================GETTERS AND SETTERS=================================






    /**
     * @return The unique identifier of the charging station.
     */
    public String getId()
    {
        return idStation;
    }
    
    

    /**
     * Changes the identifier of the station.
     * @param id The new ID of the station. It cannot be null or empty.
     * @throws IllegalArgumentException If the ID is null or empty.
     */
    public void setId(String id)
    {
        checkId(id);
        this.idStation = id;
    }

    /**
     * Returns the location of the station.
     * @return The location of the station in the city.
     */
    public Location getLocation()
    {
        return location;
    }
    
    
    
    
    
    

    /**
     * Set the current location of the charging station.
     * @param location Where it is. Must not be null.
     * @throws NullPointerException If location is null.
     */
    public void setLocation(Location location)
    {
        checkLocation(location);
        this.location = location;
    }
    
    
    
    

    /**
     * Returns the docks of the station, in their order. It returns a copy
     * of the list, so nobody can change the list of the station from
     * outside the class.
     * @return A copy of the list of docks of the station.
     */
    public List<Dock> getDocks()
    {
        return new ArrayList<>(docks);
    }
    
    
    
    

    /**
     * Changes the docks of the station. Each dock of the list is checked
     * like in {@link #addDock(Dock)}, and the list is only saved if all the
     * docks are valid. In this way the station never has a wrong list.
     * The station saves a copy of the list, sorted in the correct order.
     * @param docks The new list of docks. It cannot be null and it cannot
     * have null docks or repeated docks.
     * @throws IllegalArgumentException If the list is null or it has a null
     * dock or a repeated dock.
     */
    public void setDocks(List<Dock> docks)
    {
        if(docks == null) {
            throw new IllegalArgumentException("The list of docks cannot be null");
        }
        List<Dock> newDocks = new ArrayList<>();
        for(Dock dock : docks) {
            checkDock(dock, newDocks);
            newDocks.add(dock);
        }
        Collections.sort(newDocks, new ComparatorDocksChargingSpeed());
        this.docks = newDocks;
    }






    //=======================================CHECKERS=======================================






    /**
     * Checks that the identifier of the station is valid.
     * We only use it inside this class (in the constructor and in setId).
     * @param id The identifier that we want to check.
     * @throws IllegalArgumentException If the identifier is null or empty.
     */
    private void checkId(String id)
    {
        if(id == null || id.trim().length() == 0) {
            throw new IllegalArgumentException("The station ID cannot be null or empty");
        }
    }
    

    /**
     * Checks that the location is valid.
     * We only use it inside this class (in the constructor and in setLocation).
     * @param location The location that we want to check.
     * @throws NullPointerException If the location is null.
     */
    private void checkLocation(Location location)
    {
        if(location == null) {
            throw new NullPointerException("The location of the station cannot be null");
        }
    }

    /**
     * Checks that a dock can be added to a list of docks of the station.
     * We only use it inside this class (in addDock and in setDocks).
     * @param dock The dock that we want to check.
     * @param list The list of docks where we want to add the dock.
     * @throws IllegalArgumentException If the dock is null or it is already in the list.
     */
    private void checkDock(Dock dock, List<Dock> list)
    {
        if(dock == null) {
            throw new IllegalArgumentException("The dock to add cannot be null");
        }
        if(list.contains(dock)) {
            throw new IllegalArgumentException(
                        "The dock " + dock.getIdDock() + " is already in the station " + idStation);
        }
    }






    //============================EQUALS, HASHCODE AND TOSTRING============================






    /**
     * Compares this station with another object. Two stations are equal if
     * they have the same idStation.
     * @param obj The object that we want to compare with this station.
     * @return true if obj is a {@link BikeStation} with the same idStation, false if not.
     */
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj) {
            return true;
        }
        if(!(obj instanceof BikeStation)) {
            return false;
        }
        BikeStation other = (BikeStation) obj;
        return getId().equals(other.getId());
    }
    
    

    /**
     * Returns a hash code for this station. It uses the idStation, because
     * it is the same field that we use in {@link #equals(Object)}.
     * Hashcode technique taken from Effective Java by Joshua Bloch.
     * @return The hash code of the station.
     */
    @Override
    public int hashCode()
    {
        int result = 7;
        result = 3 * result + getId().hashCode();
        return result;
    }
    

    /**
     * Returns the information of the station as a text, with the format of
     * the output of the simulation: ID, number of bikes recharged and location.
     * Example: (BikeStation: CC00, 0, 10-5)
     * @return The information of the station as a text.
     */
    @Override
    public String toString()
    {
        return "(BikeStation: " + getId() + ", " + getTotalBikesRecharged() + ", "
                + getLocation() + ")";
    }
}
