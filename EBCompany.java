import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

/**
 * This class represents the company of electric bikes (EB). The company
 * has a group of {@link ElectricBike}s that are subscribed to it, and a
 * group of {@link BikeStation}s that it controls. The bikes are always in
 * order by their ID (we use {@link ComparatorEBIdBike}) and the stations
 * are always in order by their ID too (we use {@link ComparatorBikeStationId}).
 * @author DP classes
 * @author Los Especuladores
 * @version 2026.11.10
 */
public class EBCompany
{
    /** Energy (wh) that a bike uses in each step that it moves. */
    public static final int MOVINGCOST = 5;

    private String name;
    private List<ElectricBike> subscribedBikes;
    private List<BikeStation> stations;






    //=====================================CONSTRUCTOR=====================================






    /**
     * Constructor for objects of class EVCompany.
     * @param name The name of the company.
     * @throws IllegalArgumentException If the name is null or empty.
     */
    public EBCompany(String name)
    {
        checkName(name);
        this.name = name;
        this.subscribedBikes = new ArrayList<>();
        this.stations = new ArrayList<>();
    }






    //===================================REQUIRED METHODS===================================






    /**
     * Adds an {@link ElectricBike} to the company's fleet.
     * @param bike The electric bike to add.
     * @throws IllegalArgumentException If the bike is null or it is already subscribed.
     */
    public void addElectricBike(ElectricBike bike)
    {
        checkBike(bike, subscribedBikes);
        subscribedBikes.add(bike);
        Collections.sort(subscribedBikes, new ComparatorEBIdBike());
    }
    
    
    

    /**
     * Adds a {@link BikeStation} to the company's network.
     * @param station The bike station to add.
     * @throws IllegalArgumentException If the station is null or it is already in the company.
     */
    public void addBikeStation(BikeStation station)
    {
        checkStation(station, stations);
        stations.add(station);
        Collections.sort(stations, new ComparatorBikeStationId());
    }
    
    

    /**
     * Retrieves a {@link BikeStation} by its unique ID.
     * @param id The ID of the station to find.
     * @return The {@link BikeStation} with the matching ID, or {@code null} if not found.
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
     * Retrieves a {@link BikeStation} by its {@link Location}.
     * @param location The {@link Location} of the station to find.
     * @return The {@link BikeStation} at the matching location, or {@code null} if not found.
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
     * Returns how many bikes are subscribed to the company.
     * @return The number of subscribed {@link ElectricBike}s.
     */
    public int getNumberOfBikes()
    {
        return subscribedBikes.size();
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






    //=================================GETTERS AND SETTERS=================================






    /**
     * Returns the name of the company.
     * @return The name of the company.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Changes the name of the company.
     * @param name The new name of the company. It cannot be null or empty.
     * @throws IllegalArgumentException If the name is null or empty.
     */
    public void setName(String name)
    {
        checkName(name);
        this.name = name;
    }
    
    

    /**
     * Returns the bikes subscribed to the company, in order by their ID.
     * It returns a copy of the list, so nobody can change the list of the
     * company from outside the class.
     * @return A copy of the list of subscribed bikes.
     */
    public List<ElectricBike> getSubscribedBikes()
    {
        return new ArrayList<>(subscribedBikes);
    }
    
    

    /**
     * Changes the bikes subscribed to the company. Each bike of the list is
     * checked like in {@link #addElectricBike(ElectricBike)}, and the list
     * is only saved if all the bikes are valid. The company saves a copy of
     * the list, sorted by the ID of the bikes.
     * @param subscribedBikes The new list of bikes. It cannot be null and it
     * cannot have null bikes or repeated bikes.
     * @throws IllegalArgumentException If the list is null or it has a null
     * bike or a repeated bike.
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
     * Returns the stations of the company, in order by their ID. It returns
     * a copy of the list, so nobody can change the list of the company from
     * outside the class.
     * @return A copy of the list of stations.
     */
    public List<BikeStation> getStations()
    {
        return new ArrayList<>(stations);
    }

    
    
    
    /**
     * Changes the stations of the company. Each station of the list is
     * checked like in {@link #addBikeStation(BikeStation)}, and the list is
     * only saved if all the stations are valid. The company saves a copy of
     * the list, sorted by the ID of the stations.
     * @param stations The new list of stations. It cannot be null and it
     * cannot have null stations or repeated stations.
     * @throws IllegalArgumentException If the list is null or it has a null
     * station or a repeated station.
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






    //=======================================CHECKERS=======================================






    /**
     * Checks that the name of the company is valid.
     * We only use it inside this class (in the constructor and in setName).
     * @param name The name that we want to check.
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
     * We only use it inside this class (in addElectricBike and in setSubscribedBikes).
     * @param bike The bike that we want to check.
     * @param list The list of bikes where we want to add the bike.
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
     * We only use it inside this class (in addBikeStation and in setStations).
     * @param station The station that we want to check.
     * @param list The list of stations where we want to add the station.
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






    //============================EQUALS, HASHCODE AND TOSTRING============================






    /**
     * Compares this company with another object. Two companies are equal if
     * they have the same name.
     * @param obj The object that we want to compare with this company.
     * @return true if obj is an {@link EBCompany} with the same name, false if not.
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
     * Returns a hash code for this company. It uses the name, because it is
     * the same field that we use in {@link #equals(Object)}.
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
     * Returns the information of the company as a text: the name, the
     * number of subscribed bikes and the number of stations.
     * Example: (EBCompany: EBBike Cáceres, 2, 4)
     * @return The information of the company as a text.
     */
    @Override
    public String toString()
    {
        return "(EBCompany: " + getName() + ", " + getNumberOfBikes() + ", "
                + getNumberOfStations() + ")";
    }
}
