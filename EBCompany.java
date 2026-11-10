import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

/**
 * Models the operation of an Electric Bike (EB) Company.
 * It manages a fleet of subscribed {@link ElectricBike}s and a network of
 * {@link BikeStation}s. The bikes are always kept sorted by their ID
 * (using {@link ComparatorEBIdBike}) and the stations by their ID
 * (using {@link ComparatorBikeStationId}), both in ascending order.
 * @author David J. Barnes and Michael Kölling
 * @author DP classes
 * @author Alejandro
 * @version 2026.11.10
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
     * The company is created without bikes and without stations.
     * @param name The name of the company. Must not be null or empty.
     * @throws IllegalArgumentException If the name is null or empty.
     */
    public EBCompany(String name)
    {
        checkName(name);
        this.name = name;
        this.subscribedBikes = new ArrayList<>();
        this.stations = new ArrayList<>();
    }

    /**
     * Adds an {@link ElectricBike} to the company's fleet. After adding it,
     * the bikes are sorted again by their ID in ascending order.
     * @param bike The electric bike to add. Must not be null and must not be already subscribed.
     * @throws IllegalArgumentException If the bike is null or it is already subscribed.
     */
    public void addElectricBike(ElectricBike bike)
    {
        checkBike(bike, subscribedBikes);
        subscribedBikes.add(bike);
        Collections.sort(subscribedBikes, new ComparatorEBIdBike());
    }

    /**
     * Adds a {@link BikeStation} to the company's network. After adding it,
     * the stations are sorted again by their ID in ascending order.
     * @param station The bike station to add. Must not be null and must not be already in the company.
     * @throws IllegalArgumentException If the station is null or it is already in the company.
     */
    public void addBikeStation(BikeStation station)
    {
        checkStation(station, stations);
        stations.add(station);
        Collections.sort(stations, new ComparatorBikeStationId());
    }

    /**
     * Returns the {@link BikeStation} of the company with the given ID.
     * The search stops as soon as the station is found.
     * @param id The ID of the station to find. Must not be null.
     * @return The station with that ID, or null if there is no station with that ID.
     * @throws IllegalArgumentException If the ID is null.
     */
    public BikeStation getBikeStation(String id)
    {
        if(id == null) {
            throw new IllegalArgumentException("The ID of the station to find cannot be null");
        }
        Iterator<BikeStation> it = stations.iterator();
        BikeStation found = null;
        while(it.hasNext() && found == null) {
            BikeStation station = it.next();
            if(station.getId().equals(id)) {
                found = station;
            }
        }
        return found;
    }

    /**
     * Returns the {@link BikeStation} of the company that is at the given {@link Location}.
     * The search stops as soon as the station is found.
     * @param location The location of the station to find. Must not be null.
     * @return The station at that location, or null if there is no station there.
     * @throws IllegalArgumentException If the location is null.
     */
    public BikeStation getBikeStation(Location location)
    {
        if(location == null) {
            throw new IllegalArgumentException("The location of the station to find cannot be null");
        }
        Iterator<BikeStation> it = stations.iterator();
        BikeStation found = null;
        while(it.hasNext() && found == null) {
            BikeStation station = it.next();
            if(station.getLocation().equals(location)) {
                found = station;
            }
        }
        return found;
    }

    /**
     * Gets the number of bikes subscribed to the company.
     * @return The total number of subscribed {@link ElectricBike}s.
     */
    public int getNumberOfBikes()
    {
        return subscribedBikes.size();
    }

    /**
     * Gets the number of stations managed by the company.
     * @return The total number of managed {@link BikeStation}s.
     */
    public int getNumberOfStations()
    {
        return stations.size();
    }

    /**
     * Removes all the bikes and stations of the company,
     * resetting it to an empty state. The name is kept.
     */
    public void reset()
    {
        subscribedBikes.clear();
        stations.clear();
    }

    /**
     * Gets the name of the company.
     * @return The name of the company.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of the company.
     * @param name The new name of the company. Must not be null or empty.
     * @throws IllegalArgumentException If the name is null or empty.
     */
    public void setName(String name)
    {
        checkName(name);
        this.name = name;
    }

    /**
     * Gets the bikes subscribed to the company, sorted by their ID.
     * A copy of the list is returned, so the internal list of the company
     * cannot be modified from outside.
     * @return A copy of the list of subscribed bikes.
     */
    public List<ElectricBike> getSubscribedBikes()
    {
        return new ArrayList<>(subscribedBikes);
    }

    /**
     * Sets the bikes subscribed to the company. Every bike of the list is
     * checked in the same way as in {@link #addElectricBike(ElectricBike)},
     * and the list is only stored if all of them are valid. The company
     * stores a copy of the list received, sorted by the ID of the bikes.
     * @param subscribedBikes The new list of bikes. Must not be null and
     * must not contain null or repeated bikes.
     * @throws IllegalArgumentException If the list is null or it contains
     * a null or repeated bike.
     */
    public void setSubscribedBikes(List<ElectricBike> subscribedBikes)
    {
        if(subscribedBikes == null) {
            throw new IllegalArgumentException("The list of bikes cannot be null");
        }
        List<ElectricBike> newBikes = new ArrayList<>();
        for(ElectricBike bike : subscribedBikes) {
            checkBike(bike, newBikes);
            newBikes.add(bike);
        }
        Collections.sort(newBikes, new ComparatorEBIdBike());
        this.subscribedBikes = newBikes;
    }

    /**
     * Gets the stations managed by the company, sorted by their ID.
     * A copy of the list is returned, so the internal list of the company
     * cannot be modified from outside.
     * @return A copy of the list of stations.
     */
    public List<BikeStation> getStations()
    {
        return new ArrayList<>(stations);
    }

    /**
     * Sets the stations managed by the company. Every station of the list is
     * checked in the same way as in {@link #addBikeStation(BikeStation)},
     * and the list is only stored if all of them are valid. The company
     * stores a copy of the list received, sorted by the ID of the stations.
     * @param stations The new list of stations. Must not be null and
     * must not contain null or repeated stations.
     * @throws IllegalArgumentException If the list is null or it contains
     * a null or repeated station.
     */
    public void setStations(List<BikeStation> stations)
    {
        if(stations == null) {
            throw new IllegalArgumentException("The list of stations cannot be null");
        }
        List<BikeStation> newStations = new ArrayList<>();
        for(BikeStation station : stations) {
            checkStation(station, newStations);
            newStations.add(station);
        }
        Collections.sort(newStations, new ComparatorBikeStationId());
        this.stations = newStations;
    }

    /**
     * Checks that a company name is valid.
     * It is only for internal use of the class (constructor and setName).
     * @param name The name to check.
     * @throws IllegalArgumentException If the name is null or empty.
     */
    private void checkName(String name)
    {
        if(name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("The company name cannot be null or empty");
        }
    }

    /**
     * Checks that a bike can be added to a list of bikes of the company.
     * It is only for internal use of the class (addElectricBike and setSubscribedBikes).
     * @param bike The bike to check.
     * @param list The list of bikes where the bike is going to be added.
     * @throws IllegalArgumentException If the bike is null or it is already in the list.
     */
    private void checkBike(ElectricBike bike, List<ElectricBike> list)
    {
        if(bike == null) {
            throw new IllegalArgumentException("The bike to add cannot be null");
        }
        if(list.contains(bike)) {
            throw new IllegalArgumentException(
                        "The bike " + bike.getIdBike() + " is already subscribed to the company " + name);
        }
    }

    /**
     * Checks that a station can be added to a list of stations of the company.
     * It is only for internal use of the class (addBikeStation and setStations).
     * @param station The station to check.
     * @param list The list of stations where the station is going to be added.
     * @throws IllegalArgumentException If the station is null or it is already in the list.
     */
    private void checkStation(BikeStation station, List<BikeStation> list)
    {
        if(station == null) {
            throw new IllegalArgumentException("The station to add cannot be null");
        }
        if(list.contains(station)) {
            throw new IllegalArgumentException(
                        "The station " + station.getId() + " is already in the company " + name);
        }
    }

    /**
     * Checks whether this company is equal to another object.
     * Two companies are considered equal if they have the same name.
     * @param obj The object to compare with this company.
     * @return true if obj is an {@link EBCompany} with the same name, false otherwise.
     */
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj) {
            return true;
        }
        if(!(obj instanceof EBCompany)) {
            return false;
        }
        EBCompany other = (EBCompany) obj;
        return getName().equals(other.getName());
    }

    /**
     * Returns a hash code for this company, based on its name
     * (the same field used in {@link #equals(Object)}).
     * Hashcode technique taken from Effective Java by Joshua Bloch.
     * @return The hash code of the company.
     */
    @Override
    public int hashCode()
    {
        int result = 7;
        result = 3 * result + getName().hashCode();
        return result;
    }

    /**
     * Returns a string representation of the company: its name, the number
     * of subscribed bikes and the number of stations.
     * Example: (EBCompany: EBBike Cáceres, 2, 4)
     * @return A string representation of the company.
     */
    @Override
    public String toString()
    {
        return "(EBCompany: " + getName() + ", " + getNumberOfBikes() + ", "
                + getNumberOfStations() + ")";
    }
}
