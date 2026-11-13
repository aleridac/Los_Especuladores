import java.util.List;
import java.util.ArrayList;

/**
 * Model the common elements of an Electric bike (EB) that operates
 * within the simulation, moving towards a target and potentially recharging.
 * @author DP classes
 * @author Los Especuladores
 * @version 2026.11.11
 */
public class ElectricBike
{
    private String idBike;
    private EBCompany company;
    private Location location;
    private Location targetLocation;
    private Location rechargingLocation;
    private int idleCount;
    private int batteryCapacity;
    private int batteryLevel;
    private int wsCharged;
    private int chargesCount;
    private float chargesCost;
    private List<Dock> docks;






    //=====================================CONSTRUCTOR=====================================






    /**
     * Constructor of class ElectricBike.
     * @param company The EB's operating company. Must not be null.
     * @param location The EB's starting {@link Location}. Must not be null.
     * @param targetLocation The EB's final destination {@link Location}. Must not be null.
     * @param id The identificator of the bike.
     * @param batteryCapacity The maximum capacity of the battery.
     * @throws NullPointerException If company, location, or targetLocation is null.
     * @throws IllegalArgumentException If the id is null or empty, or the capacity is not greater than 0.
     */
    public ElectricBike(EBCompany company, Location location, Location targetLocation, String id, int batteryCapacity)
    {
        checkNotNull(company, "company");
        checkNotNull(location, "location");
        checkNotNull(targetLocation, "target location");
        checkId(id);
        checkBatteryCapacity(batteryCapacity);
        this.company = company;
        this.location = location;
        this.targetLocation = targetLocation;
        this.idBike = id;
        this.batteryCapacity = batteryCapacity;
        this.batteryLevel = batteryCapacity;
        this.rechargingLocation = null;
        this.idleCount = 0;
        this.wsCharged = 0;
        this.chargesCount = 0;
        this.chargesCost = 0.0f;
        this.docks = new ArrayList<>();
    }






    //===================================REQUIRED METHODS===================================






    /**
     * Calculates the optimal route for the bike.
     * If there isn't enough battery to reach the target, it attempts to find an intermediate
     * {@link BikeStation} and sets it as the {@code rechargingLocation}.
     */
    public void calculateRoute()
    {
        if(enoughBattery()) {
            rechargingLocation = null;
        }
        else {
            calculateRechargingPosition();
        }
    }
    
    

    /**
     * Gets a string representation of the planned route, including the recharging stop if one exists.
     * @return A string showing the route: {@code currentLocation -> [rechargingLocation ->] targetLocation}.
     */
    public String getStringRoute()
    {
        String route = location + " -> ";
        if(rechargingLocation != null) {
            route += rechargingLocation + " -> ";
        }
        return route + targetLocation;
    }
    
    
    

    /**
     * Checks if the current battery level is sufficient to cover a given distance.
     * @return true if the battery is enough to reach the target location, false otherwise.
     */
    public boolean enoughBattery()
    {
        return batteryLevel >= distanceToTheTargetLocation() * EBCompany.MOVINGCOST;
    }
    
    
    
    
    

    /**
     * Determines the optimal intermediate {@link BikeStation} to visit for recharging
     * if the bike cannot reach the final target directly.
     * Sets {@code rechargingLocation} to the chosen station's location.
     */
    public void calculateRechargingPosition()
    {
        BikeStation bestStation = null;
        int bestDistance = 0;
        for(BikeStation station : company.getStations()) {
            Location stationLocation = station.getLocation();
            int distanceToStation = location.distance(stationLocation);
            boolean reachable = batteryLevel >= distanceToStation * EBCompany.MOVINGCOST;
            if(!stationLocation.equals(location) && reachable) {
                int totalDistance = distanceToStation + stationLocation.distance(targetLocation);
                if(bestStation == null || totalDistance < bestDistance) {
                    bestStation = station;
                    bestDistance = totalDistance;
                }
            }
        }
        if(bestStation != null) {
            rechargingLocation = bestStation.getLocation();
        }
        else {
            rechargingLocation = null;
        }
    }
    
    
    
    
    

    /**
     * Get the Manhattan-like distance to the final target location from the current location.
     * @return The distance to the target location.
     */
    public int distanceToTheTargetLocation()
    {
        return location.distance(targetLocation);
    }

    
    /**
     * Simulates the recharging process when the bike arrives at a {@code rechargingLocation}.
     * The battery is charged to full capacity, the cost is calculated, and the route is recalculated.
     * @param step The current step of the simulation (for the output message).
     * @throws IllegalStateException If the bike is not at a bike station or the station has no free dock.
     */
    public void recharge(int step)
    {
        BikeStation station = company.getBikeStation(location);
        if(station == null) {
            throw new IllegalStateException(
                        "The bike " + idBike + " is not at a bike station: " + location);
        }
        Dock dock = station.getFreeDock();
        if(dock == null) {
            throw new IllegalStateException(
                        "The station " + station.getId() + " has no free dock for the bike " + idBike);
        }
        int whToCharge = batteryCapacity - batteryLevel;
        float cost = dock.recharge(this, whToCharge);
        docks.add(dock);
        batteryLevel = batteryCapacity;
        wsCharged += whToCharge;
        incrementCharges();
        incrementChargesCost(cost);
        System.out.println("(step: " + step + " - ElectricBike: " + idBike + " recharges: "
                + whToCharge + "wh at dock: " + dock.getIdDock() + " with cost: " + cost + "€ ********)");
        calculateRoute();
    }

    /**
     * Increments the count of recharges performed by this bike.
     */
    public void incrementCharges()
    {
        chargesCount++;
    }

    /**
     * Adds a cost amount to the total charges cost.
     * @param cost The cost of the recharge in €. Must not be negative.
     * @throws IllegalArgumentException If the cost is negative.
     */
    public void incrementChargesCost(float cost)
    {
        checkNotNegative(cost, "cost of a recharge");
        chargesCost += cost;
    }

    /**
     * Carries out a single step of the bike's actions.
     * Moves one step towards the target (recharging or final) or stays idle.
     * @param step The current step of the simulation.
     */
    public void act(int step)
    {
        Location destination = getNextDestination();
        if(destination == null || location.equals(destination)) {
            idleCount++;
        }
        else {
            location = location.nextLocation(destination);
            reduceBatteryLevel();
            if(location.equals(targetLocation)) {
                rechargingLocation = null;
                System.out.println("(step: " + step + " - ElectricBike: " + idBike
                        + " at target destination ********)");
            }
            else if(location.equals(rechargingLocation)
                    && company.getBikeStation(location).getFreeDock() != null) {
                recharge(step);
            }
        }
    }

    
    /**
     * Reduces the battery level by the cost of one movement step (defined in {@link EBCompany#MOVINGCOST}).
     * Ensures the battery level does not go below zero.
     */
    public void reduceBatteryLevel()
    {
        batteryLevel -= EBCompany.MOVINGCOST;
        if(batteryLevel < 0) {
            batteryLevel = 0;
        }
    }






    //=================================GETTERS AND SETTERS=================================






    /**
     * @return The id of the electric bike.
     */
    public String getIdBike()
    {
        return idBike;
    }
    

    /**
     * Changes the identifier of the bike.
     * @param idBike The new ID of the bike. It cannot be null or empty.
     * @throws IllegalArgumentException If the ID is null or empty.
     */
    public void setIdBike(String idBike)
    {
        checkId(idBike);
        this.idBike = idBike;
    }

    
    
    /**
     * Returns the company of the bike.
     * @return The company of the bike.
     */
    public EBCompany getCompany()
    {
        return company;
    }

    
    
    /**
     * Changes the company of the bike.
     * @param company The new company. It cannot be null.
     * @throws NullPointerException If the company is null.
     */
    public void setCompany(EBCompany company)
    {
        checkNotNull(company, "company");
        this.company = company;
    }

    /**
     * Returns the location where the bike is now.
     * @return The location of the bike.
     */
    public Location getLocation()
    {
        return location;
    }
    
    

    /**
     * Set the current location.
     * @param location Where it is. Must not be null.
     * @throws NullPointerException If location is null.
     */
    public void setLocation(Location location)
    {
        checkNotNull(location, "location");
        this.location = location;
    }

    /**
     * Get the final target location.
     * @return Where this bike is ultimately headed.
     */
    public Location getTargetLocation()
    {
        return targetLocation;
    }
    
    
    

    /**
     * Set the required final target location.
     * @param location Where to go. Must not be null.
     * @throws NullPointerException If location is null.
     */
    public void setTargetLocation(Location location)
    {
        checkNotNull(location, "target location");
        this.targetLocation = location;
    }

    /**
     * Get the temporary recharging location.
     * @return The {@link Location} of the next {@link BikeStation} to visit, or null if no recharge is planned.
     */
    public Location getRechargingLocation()
    {
        return rechargingLocation;
    }
    

    /**
     * Changes the location of the station where the bike is going to recharge.
     * @param rechargingLocation The location of the next station, or null
     * if the bike does not need to recharge.
     */
    public void setRechargingLocation(Location rechargingLocation)
    {
        this.rechargingLocation = rechargingLocation;
    }
    
    
    /**
     * Returns how many steps the bike has been without moving.
     * @return The idle count.
     */
    public int getIdleCount()
    {
        return idleCount;
    }
    
    

    /**
     * Changes how many steps the bike has been without moving.
     * @param idleCount The new idle count. It cannot be negative.
     * @throws IllegalArgumentException If the idle count is negative.
     */
    public void setIdleCount(int idleCount)
    {
        checkNotNegative(idleCount, "idle count");
        this.idleCount = idleCount;
    }

    /**
     * Returns the maximum capacity of the battery.
     * @return The capacity of the battery in wh.
     */
    public int getBatteryCapacity()
    {
        return batteryCapacity;
    }

    /**
     * Changes the maximum capacity of the battery.
     * @param batteryCapacity The new capacity in wh. It must be bigger than 0
     * and it cannot be smaller than the battery level.
     * @throws IllegalArgumentException If the capacity is not valid.
     */
    public void setBatteryCapacity(int batteryCapacity)
    {
        checkBatteryCapacity(batteryCapacity);
        if(batteryCapacity < batteryLevel) {
            throw new IllegalArgumentException(
                        "The battery capacity cannot be lower than the battery level: " + batteryCapacity);
        }
        this.batteryCapacity = batteryCapacity;
    }

    /**
     * Returns the energy that the battery has now.
     * @return The battery level in wh.
     */
    public int getBatteryLevel()
    {
        return batteryLevel;
    }

    /**
     * Changes the energy that the battery has now.
     * @param batteryLevel The new level in wh. It must be between 0 and the capacity of the battery.
     * @throws IllegalArgumentException If the level is not valid.
     */
    public void setBatteryLevel(int batteryLevel)
    {
        if(batteryLevel < 0 || batteryLevel > batteryCapacity) {
            throw new IllegalArgumentException(
                        "The battery level must be between 0 and " + batteryCapacity + ": " + batteryLevel);
        }
        this.batteryLevel = batteryLevel;
    }

    /**
     * Returns the total energy that the bike has charged.
     * @return The total wh charged.
     */
    public int getWsCharged()
    {
        return wsCharged;
    }

    
    /**
     * Changes the total energy that the bike has charged.
     * @param wsCharged The new total of wh charged. It cannot be negative.
     * @throws IllegalArgumentException If the value is negative.
     */
    public void setWsCharged(int wsCharged)
    {
        checkNotNegative(wsCharged, "energy charged");
        this.wsCharged = wsCharged;
    }

    
    /**
     * Returns how many times the bike has recharged.
     * @return The number of recharges.
     */
    public int getChargesCount()
    {
        return chargesCount;
    }

    /**
     * Changes how many times the bike has recharged.
     * @param chargesCount The new number of recharges. It cannot be negative.
     * @throws IllegalArgumentException If the value is negative.
     */
    public void setChargesCount(int chargesCount)
    {
        checkNotNegative(chargesCount, "number of recharges");
        this.chargesCount = chargesCount;
    }

    /**
     * Returns the total price of all the recharges of the bike.
     * @return The total price in €.
     */
    public float getChargesCost()
    {
        return chargesCost;
    }
    

    /**
     * Changes the total price of all the recharges of the bike.
     * @param chargesCost The new total price in €. It cannot be negative.
     * @throws IllegalArgumentException If the price is negative.
     */
    public void setChargesCost(float chargesCost)
    {
        checkNotNegative(chargesCost, "cost of the recharges");
        this.chargesCost = chargesCost;
    }
    
    

    /**
     * Returns the docks where the bike has recharged, in the same order of
     * the recharges. It returns a copy of the list, so nobody can change the
     * list of the bike from outside the class.
     * @return A copy of the list of docks.
     */
    public List<Dock> getDocks()
    {
        return new ArrayList<>(docks);
    }
    
    
    

    /**
     * Changes the docks where the bike has recharged. The bike saves a copy
     * of the list, and only if the list has no null docks.
     * @param docks The new list of docks. It cannot be null or have null docks.
     * @throws IllegalArgumentException If the list is null or it has a null dock.
     */
    public void setDocks(List<Dock> docks)
    {
        if(docks == null || docks.contains(null)) {
            throw new IllegalArgumentException("The list of docks cannot be null nor contain null docks");
        }
        this.docks = new ArrayList<>(docks);
    }






    //==================================AUXILIARY METHODS==================================






    /**
     * Returns the location where the bike has to go in this step: its
     * recharging location if it has one, its target location if it has
     * enough battery to arrive, or null if it cannot go anywhere.
     * @return The location where the bike has to go, or null if it cannot move.
     */
    private Location getNextDestination()
    {
        if(location.equals(targetLocation)) {
            return targetLocation;
        }
        else if(rechargingLocation != null) {
            return rechargingLocation;
        }
        else if(enoughBattery()) {
            return targetLocation;
        }
        else {
            return null;
        }
    }






    //=======================================CHECKERS=======================================






    /**
     * Checks that an object is not null.
     * We only use it inside this class (in the constructor and in the setters).
     * @param object The object that we want to check.
     * @param name The name of the object, for the error message.
     * @throws NullPointerException If the object is null.
     */
    private void checkNotNull(Object object, String name)
    {
        if(object == null) {
            throw new NullPointerException("The " + name + " of the bike cannot be null");
        }
    }
    
    

    /**
     * Checks that the identifier of the bike is valid.
     * We only use it inside this class (in the constructor and in setIdBike).
     * @param id The identifier that we want to check.
     * @throws IllegalArgumentException If the identifier is null or empty.
     */
    private void checkId(String id)
    {
        if(id == null || id.trim().length() == 0) {
            throw new IllegalArgumentException("The bike ID cannot be null or empty");
        }
    }

    /**
     * Checks that the capacity of the battery is valid.
     * We only use it inside this class (in the constructor and in setBatteryCapacity).
     * @param capacity The capacity that we want to check.
     * @throws IllegalArgumentException If the capacity is not bigger than 0.
     */
    private void checkBatteryCapacity(int capacity)
    {
        if(capacity <= 0) {
            throw new IllegalArgumentException(
                        "The battery capacity must be greater than 0: " + capacity);
        }
    }
    
    

    /**
     * Checks that an integer number is not negative.
     * We only use it inside this class (in the setters).
     * @param value The number that we want to check.
     * @param name The name of the number, for the error message.
     * @throws IllegalArgumentException If the number is negative.
     */
    private void checkNotNegative(int value, String name)
    {
        if(value < 0) {
            throw new IllegalArgumentException("The " + name + " cannot be negative: " + value);
        }
    }

    
    
    
    /**
     * Checks that a decimal number is not negative.
     * We only use it inside this class (in setChargesCost and in incrementChargesCost).
     * @param value The number that we want to check.
     * @param name The name of the number, for the error message.
     * @throws IllegalArgumentException If the number is negative.
     */
    private void checkNotNegative(float value, String name)
    {
        if(value < 0) {
            throw new IllegalArgumentException("The " + name + " cannot be negative: " + value);
        }
    }






    //============================EQUALS, HASHCODE AND TOSTRING============================






    /**
     * Compares this bike with another object. Two bikes are equal if they
     * have the same idBike.
     * @param obj The object that we want to compare with this bike.
     * @return true if obj is an {@link ElectricBike} with the same idBike, false if not.
     */
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj) {
            return true;
        }
        if(!(obj instanceof ElectricBike)) {
            return false;
        }
        ElectricBike other = (ElectricBike) obj;
        return getIdBike().equals(other.getIdBike());
    }
    
    
    

    /**
     * Returns a hash code for this bike. It uses the idBike, because it is
     * the same field that we use in {@link #equals(Object)}.
     * Hashcode technique taken from Effective Java by Joshua Bloch.
     * @return The hash code of the bike.
     */
    @Override
    public int hashCode()
    {
        int result = 7;
        result = 3 * result + getIdBike().hashCode();
        return result;
    }

    
    
    /**
     * Returns a detailed string representation of the electric bike.
     * @return A string containing the bike's name, plate, battery info, charge counts, costs, idle count, and route.
     */
    @Override
    public String toString()
    {
        String result = "(ElectricBike: " + idBike + ", " + batteryCapacity + "wh, "
                + batteryLevel + "wh, " + chargesCount + ", " + chargesCost + "€, "
                + idleCount + ", " + location + ", ";
        if(rechargingLocation != null) {
            result += rechargingLocation + ", ";
        }
        return result + targetLocation + ")";
    }
}
