import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

/**
 * Models a station for electric bikes in the city.
 * A bike station has a location and contains several {@link Dock} units
 * where the {@link ElectricBike}s can recharge. The docks are always kept
 * sorted by charging speed (descending), then by charging fee (ascending)
 * and then by ID (ascending), using {@link ComparatorDocksChargingSpeed}.
 * @author DP classes
 * @author Alvaro
 * @version 2026.11.10
 */
public class BikeStation
{
    private String idStation;
    private Location location;
    private List<Dock> docks;

    /**
     * Constructor for objects of class BikeStation.
     * The station is created without docks.
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

    /**
     * Adds a new {@link Dock} to the station. After adding it, the docks
     * are sorted again so that they keep the order required.
     * @param dock The new dock to add. Must not be null and must not be already in the station.
     * @throws IllegalArgumentException If the dock is null or it is already in the station.
     */
    public void addDock(Dock dock)
    {
        checkDock(dock, docks);
        docks.add(dock);
        Collections.sort(docks, new ComparatorDocksChargingSpeed());
    }

    /**
     * Returns the {@link Dock} of the station with the given ID.
     * The search stops as soon as the dock is found.
     * @param idDock The ID of the dock to look for.
     * @return The dock with that ID, or null if there is no dock with that ID in the station.
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
     * Returns the first free {@link Dock} of the station, following the
     * order in which the docks are kept. The search stops as soon as a
     * free dock is found.
     * @return The first free dock, or null if all the docks are occupied
     * or the station has no docks.
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
     * Calculates the total number of bikes recharged in all the docks of the station.
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

    /**
     * Gets the unique identifier of the station.
     * @return The ID of the station.
     */
    public String getId()
    {
        return idStation;
    }

    /**
     * Sets the unique identifier of the station.
     * @param id The new ID of the station. Must not be null or empty.
     * @throws IllegalArgumentException If the ID is null or empty.
     */
    public void setId(String id)
    {
        checkId(id);
        this.idStation = id;
    }

    /**
     * Gets the location of the station.
     * @return The location of the station in the city.
     */
    public Location getLocation()
    {
        return location;
    }

    /**
     * Sets the location of the station.
     * @param location The new location of the station. Must not be null.
     * @throws NullPointerException If the location is null.
     */
    public void setLocation(Location location)
    {
        checkLocation(location);
        this.location = location;
    }

    /**
     * Gets the docks of the station, in the order in which they are kept.
     * A copy of the list is returned, so the internal list of the station
     * cannot be modified from outside.
     * @return A copy of the list of docks of the station.
     */
    public List<Dock> getDocks()
    {
        return new ArrayList<>(docks);
    }

    /**
     * Sets the docks of the station. Every dock of the list is checked in the
     * same way as in {@link #addDock(Dock)}, and the list is only stored if
     * all of them are valid, so the station never keeps an invalid list.
     * The station stores a copy of the list received, sorted with the order
     * required for the docks.
     * @param docks The new list of docks. Must not be null and must not
     * contain null or repeated docks.
     * @throws IllegalArgumentException If the list is null or it contains
     * a null or repeated dock.
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

    /**
     * Checks that a station identifier is valid.
     * It is only for internal use of the class (constructor and setId).
     * @param id The identifier to check.
     * @throws IllegalArgumentException If the identifier is null or empty.
     */
    private void checkId(String id)
    {
        if(id == null || id.trim().length() == 0) {
            throw new IllegalArgumentException("The station ID cannot be null or empty");
        }
    }

    /**
     * Checks that a location is valid.
     * It is only for internal use of the class (constructor and setLocation).
     * @param location The location to check.
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
     * It is only for internal use of the class (addDock and setDocks).
     * @param dock The dock to check.
     * @param list The list of docks where the dock is going to be added.
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

    /**
     * Checks whether this station is equal to another object.
     * Two stations are considered equal if they have the same idStation.
     * @param obj The object to compare with this station.
     * @return true if obj is a {@link BikeStation} with the same idStation, false otherwise.
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
     * Returns a hash code for this station, based on its idStation
     * (the same field used in {@link #equals(Object)}).
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
     * Returns a string representation of the station with the format required
     * by the simulation output: ID, number of bikes recharged and location.
     * Example: (BikeStation: CC00, 0, 10-5)
     * @return A string representation of the station.
     */
    @Override
    public String toString()
    {
        return "(BikeStation: " + getId() + ", " + getTotalBikesRecharged() + ", "
                + getLocation() + ")";
    }
}
