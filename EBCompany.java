import java.util.*;

/**
 * Models the operation of an Electric Bike (EB) Company. 
 * It manages a fleet of {@link ElectricBike}s and a network of {@link BikeStation}s.
 * @author David J. Barnes and Michael Kölling
 * @author DP classes
 * @version 2026.10.13
 */
public class EBCompany  
{
    /** Energy (wh) that a bike consumes for each step it moves. */
    public static final int MOVINGCOST = 5;

    private String name;
    private List<ElectricBike> subscribedBikes;
    private List<BikeStation> stations;

    /**
     * Constructor for objects of class EBCompany.
     * @param name The name of the company.
     */
    public EBCompany(String name)
    {
        this.name = name;
        this.subscribedBikes = new ArrayList<>();
        this.stations = new ArrayList<>();
    }

    /**
     * @return The name of the company.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of the company.
     * @param name The new name.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return The list of bikes subscribed to the company.
     */
    public List<ElectricBike> getSubscribedBikes()
    {
        return subscribedBikes;
    }

    /**
     * @return The list of bike stations managed by the company.
     */
    public List<BikeStation> getStations()
    {
        return stations;
    }

    /**
     * Adds an {@link ElectricBike} to the company's fleet.
     * @param bike The electric bike to add.
     */
    public void addElectricBike(ElectricBike bike)
    {
        if (bike != null) {
            subscribedBikes.add(bike);
        }
    }

    /**
     * Adds a {@link BikeStation} to the company's network.
     * @param station The bike station to add.
     */
    public void addBikeStation(BikeStation station)
    {
        if (station != null) {
            stations.add(station);
        }
    }

    /**
     * Retrieves a {@link BikeStation} by its unique ID.
     * @param id The ID of the station to find.
     * @return The {@link BikeStation} with the matching ID, or {@code null} if not found.
     */
    public BikeStation getBikeStation(String id)
    {
        BikeStation found = null;
        Iterator<BikeStation> it = stations.iterator();
        // Paramos cuando la encontramos o se acaba la lista
        while (found == null && it.hasNext()) {
            BikeStation station = it.next();
            if (id != null && id.equals(station.getId())) {
                found = station;
            }
        }
        return found;
    }

    /**
     * Retrieves a {@link BikeStation} by its {@link Location}.
     * @param location The {@link Location} of the station to find.
     * @return The {@link BikeStation} at the matching location, or {@code null} if not found.
     */
    public BikeStation getBikeStation(Location location)
    {
        BikeStation found = null;
        Iterator<BikeStation> it = stations.iterator();
        while (found == null && it.hasNext()) {
            BikeStation station = it.next();
            if (location != null && location.equals(station.getLocation())) {
                found = station;
            }
        }
        return found;
    }

    /**
     * @return The total number of managed {@link BikeStation}s.
     */
    public int getNumberOfStations()
    {
        return stations.size();
    }

    /**
     * Clears all managed bikes and stations, resetting the company to an empty state.
     */
    public void reset()
    {
        subscribedBikes.clear();
        stations.clear();
    }
}