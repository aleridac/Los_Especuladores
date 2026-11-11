import java.util.List;
import java.util.ArrayList;

/**
 * Model the common elements of an Electric Bike (EB) that operates within
 * the simulation, moving towards a target location and recharging in the
 * {@link BikeStation}s of its {@link EBCompany} when it does not have enough
 * battery to get there. The bike consumes {@link EBCompany#MOVINGCOST} wh
 * for each step it moves.
 * @author David J. Barnes and Michael Kölling
 * @author DP classes
 * @author Pablo
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

    /**
     * Constructor of class ElectricBike.
     * The bike starts with the battery full, without recharges and without
     * a recharging location (the route is calculated with {@link #calculateRoute()}).
     * @param company The EB's operating company. Must not be null.
     * @param location The EB's starting {@link Location}. Must not be null.
     * @param targetLocation The EB's final destination {@link Location}. Must not be null.
     * @param id The identifier of the bike. Must not be null or empty.
     * @param batteryCapacity The maximum capacity of the battery in wh. Must be greater than 0.
     * @throws NullPointerException If company, location, or targetLocation is null.
     * @throws IllegalArgumentException If the ID is null or empty or the capacity is not greater than 0.
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

    /**
     * Calculates the route of the bike. If it has enough battery to reach
     * its target location, it goes directly there (no recharging location).
     * If not, it looks for the best {@link BikeStation} to recharge
     * (see {@link #calculateRechargingPosition()}).
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
     * Gets a string representation of the planned route, including the
     * recharging stop if there is one.
     * @return A string showing the route: currentLocation -> [rechargingLocation ->] targetLocation.
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
     * Checks if the current battery level is enough to reach the target
     * location, knowing that the bike consumes {@link EBCompany#MOVINGCOST}
     * wh for each step.
     * @return true if the bike has enough battery to reach its target location, false otherwise.
     */
    public boolean enoughBattery()
    {
        return batteryLevel >= distanceToTheTargetLocation() * EBCompany.MOVINGCOST;
    }

    /**
     * Determines the best {@link BikeStation} of the company to recharge and
     * sets its location as the recharging location. The best station is the
     * one with the lowest sum of the distance from the current location to
     * the station and the distance from the station to the target location,
     * among the stations that the bike can reach with its current battery.
     * The station where the bike already is, is not considered. In case of
     * a tie, the first station analysed is selected. If there is no station
     * the bike can reach, the recharging location is set to null.
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
     * Gets the distance from the current location to the final target location.
     * @return The distance to the target location.
     */
    public int distanceToTheTargetLocation()
    {
        return location.distance(targetLocation);
    }

    /**
     * Recharges the bike in the first free {@link Dock} of the {@link BikeStation}
     * where it is. The battery is charged to its full capacity, the dock is
     * stored in the list of docks used, the cost, wh and number of recharges
     * are updated, the bike is registered in the dock and, finally, the route
     * is calculated again. A message with the recharge is shown.
     * @param step The current step of the simulation (for the message).
     * @throws IllegalStateException If the bike is not at a station of its
     * company or the station has no free dock.
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
     * Adds the cost of a recharge to the total cost of the recharges.
     * @param cost The cost of the recharge in €. Must not be negative.
     * @throws IllegalArgumentException If the cost is negative.
     */
    public void incrementChargesCost(float cost)
    {
        checkNotNegative(cost, "cost of a recharge");
        chargesCost += cost;
    }

    /**
     * Carries out a single step of the simulation:
     * <ul>
     * <li>If the bike is already at its target location, or it cannot go
     * anywhere (not enough battery and no station to recharge), or it is
     * waiting at a station without free docks, it stays where it is and its
     * idle count is incremented.</li>
     * <li>Otherwise, it moves one step towards its recharging location (if it
     * has one) or towards its target location. If it reaches its target
     * location, it shows a message (from then on it stays there, so its idle
     * count tells how many steps ago it arrived). If it reaches its recharging
     * location and the station has a free dock, it recharges.</li>
     * </ul>
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
     * Reduces the battery level by the cost of one movement step
     * (defined in {@link EBCompany#MOVINGCOST}).
     * Ensures the battery level does not go below zero.
     */
    public void reduceBatteryLevel()
    {
        batteryLevel -= EBCompany.MOVINGCOST;
        if(batteryLevel < 0) {
            batteryLevel = 0;
        }
    }

    /**
     * Checks if the bike has arrived at its target location, that is, if it
     * is at its target location (once it arrives, it does not move anymore).
     * @return true if the bike has arrived, false otherwise.
     */
    public boolean hasArrived()
    {
        return location.equals(targetLocation);
    }

    /**
     * Returns the information of the bike after a step of the simulation,
     * with the format required by the simulation output.
     * Example: (step: 0 - ElectricBike: EBCC0, 15wh, 10wh, 0, 0.0€, 0, 10-12, 10-11, 1-1)
     * @param step The step of the simulation.
     * @return A string with the step and the information of the bike.
     */
    public String getStepInfo(int step)
    {
        return "(step: " + step + " - " + getDetails() + ")";
    }

    /**
     * Gets the identifier of the bike.
     * @return The ID of the electric bike.
     */
    public String getIdBike()
    {
        return idBike;
    }

    /**
     * Sets the identifier of the bike.
     * @param idBike The new ID of the bike. Must not be null or empty.
     * @throws IllegalArgumentException If the ID is null or empty.
     */
    public void setIdBike(String idBike)
    {
        checkId(idBike);
        this.idBike = idBike;
    }

    /**
     * Gets the company the bike is subscribed to.
     * @return The company of the bike.
     */
    public EBCompany getCompany()
    {
        return company;
    }

    /**
     * Sets the company the bike is subscribed to.
     * @param company The new company. Must not be null.
     * @throws NullPointerException If the company is null.
     */
    public void setCompany(EBCompany company)
    {
        checkNotNull(company, "company");
        this.company = company;
    }

    /**
     * Gets the current location of the bike.
     * @return The current location.
     */
    public Location getLocation()
    {
        return location;
    }

    /**
     * Sets the current location of the bike.
     * @param location Where it is. Must not be null.
     * @throws NullPointerException If the location is null.
     */
    public void setLocation(Location location)
    {
        checkNotNull(location, "location");
        this.location = location;
    }

    /**
     * Gets the final target location of the bike.
     * @return Where this bike is ultimately headed.
     */
    public Location getTargetLocation()
    {
        return targetLocation;
    }

    /**
     * Sets the final target location of the bike.
     * @param location Where to go. Must not be null.
     * @throws NullPointerException If the location is null.
     */
    public void setTargetLocation(Location location)
    {
        checkNotNull(location, "target location");
        this.targetLocation = location;
    }

    /**
     * Gets the temporary recharging location of the bike.
     * @return The {@link Location} of the next {@link BikeStation} to visit,
     * or null if no recharge is planned.
     */
    public Location getRechargingLocation()
    {
        return rechargingLocation;
    }

    /**
     * Sets the temporary recharging location of the bike.
     * @param rechargingLocation The location of the next station to visit,
     * or null if no recharge is planned.
     */
    public void setRechargingLocation(Location rechargingLocation)
    {
        this.rechargingLocation = rechargingLocation;
    }

    /**
     * Gets the number of steps the bike has been idle.
     * @return The idle count.
     */
    public int getIdleCount()
    {
        return idleCount;
    }

    /**
     * Sets the number of steps the bike has been idle.
     * @param idleCount The new idle count. Must not be negative.
     * @throws IllegalArgumentException If the idle count is negative.
     */
    public void setIdleCount(int idleCount)
    {
        checkNotNegative(idleCount, "idle count");
        this.idleCount = idleCount;
    }

    /**
     * Gets the maximum capacity of the battery.
     * @return The battery capacity in wh.
     */
    public int getBatteryCapacity()
    {
        return batteryCapacity;
    }

    /**
     * Sets the maximum capacity of the battery.
     * @param batteryCapacity The new capacity in wh. Must be greater than 0
     * and not lower than the current battery level.
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
     * Gets the current level of the battery.
     * @return The battery level in wh.
     */
    public int getBatteryLevel()
    {
        return batteryLevel;
    }

    /**
     * Sets the current level of the battery.
     * @param batteryLevel The new level in wh. Must be between 0 and the battery capacity.
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
     * Gets the total energy charged in the bike.
     * @return The total wh charged.
     */
    public int getWsCharged()
    {
        return wsCharged;
    }

    /**
     * Sets the total energy charged in the bike.
     * @param wsCharged The new total wh charged. Must not be negative.
     * @throws IllegalArgumentException If the value is negative.
     */
    public void setWsCharged(int wsCharged)
    {
        checkNotNegative(wsCharged, "energy charged");
        this.wsCharged = wsCharged;
    }

    /**
     * Gets the number of recharges performed by the bike.
     * @return The number of recharges.
     */
    public int getChargesCount()
    {
        return chargesCount;
    }

    /**
     * Sets the number of recharges performed by the bike.
     * @param chargesCount The new number of recharges. Must not be negative.
     * @throws IllegalArgumentException If the value is negative.
     */
    public void setChargesCount(int chargesCount)
    {
        checkNotNegative(chargesCount, "number of recharges");
        this.chargesCount = chargesCount;
    }

    /**
     * Gets the total cost of the recharges of the bike.
     * @return The total cost in €.
     */
    public float getChargesCost()
    {
        return chargesCost;
    }

    /**
     * Sets the total cost of the recharges of the bike.
     * @param chargesCost The new total cost in €. Must not be negative.
     * @throws IllegalArgumentException If the cost is negative.
     */
    public void setChargesCost(float chargesCost)
    {
        checkNotNegative(chargesCost, "cost of the recharges");
        this.chargesCost = chargesCost;
    }

    /**
     * Gets the docks where the bike has recharged, in the order in which it
     * recharged. A copy of the list is returned, so the internal list of
     * the bike cannot be modified from outside.
     * @return A copy of the list of docks used.
     */
    public List<Dock> getDocks()
    {
        return new ArrayList<>(docks);
    }

    /**
     * Sets the docks where the bike has recharged. The bike stores a copy
     * of the list received. The list is only stored if it has no null docks.
     * @param docks The new list of docks. Must not be null nor contain null docks.
     * @throws IllegalArgumentException If the list is null or contains a null dock.
     */
    public void setDocks(List<Dock> docks)
    {
        if(docks == null || docks.contains(null)) {
            throw new IllegalArgumentException("The list of docks cannot be null nor contain null docks");
        }
        this.docks = new ArrayList<>(docks);
    }

    /**
     * Gets the location the bike has to move to in this step: its recharging
     * location if it has one, its target location if it has enough battery
     * to get there, or null if it cannot go anywhere.
     * @return The location to move to, or null if the bike cannot move.
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

    /**
     * Gets the information of the bike, without parentheses, shared by
     * {@link #toString()} and {@link #getStepInfo(int)}. The recharging
     * location only appears if the bike has one.
     * @return The information of the bike.
     */
    private String getDetails()
    {
        String details = "ElectricBike: " + idBike + ", " + batteryCapacity + "wh, "
                + batteryLevel + "wh, " + chargesCount + ", " + chargesCost + "€, "
                + idleCount + ", " + location + ", ";
        if(rechargingLocation != null) {
            details += rechargingLocation + ", ";
        }
        return details + targetLocation;
    }

    /**
     * Checks that an object is not null.
     * It is only for internal use of the class (constructor and setters).
     * @param object The object to check.
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
     * Checks that a bike identifier is valid.
     * It is only for internal use of the class (constructor and setIdBike).
     * @param id The identifier to check.
     * @throws IllegalArgumentException If the identifier is null or empty.
     */
    private void checkId(String id)
    {
        if(id == null || id.trim().length() == 0) {
            throw new IllegalArgumentException("The bike ID cannot be null or empty");
        }
    }

    /**
     * Checks that a battery capacity is valid.
     * It is only for internal use of the class (constructor and setBatteryCapacity).
     * @param capacity The capacity to check.
     * @throws IllegalArgumentException If the capacity is not greater than 0.
     */
    private void checkBatteryCapacity(int capacity)
    {
        if(capacity <= 0) {
            throw new IllegalArgumentException(
                        "The battery capacity must be greater than 0: " + capacity);
        }
    }

    /**
     * Checks that an integer value is not negative.
     * It is only for internal use of the class (setters).
     * @param value The value to check.
     * @param name The name of the value, for the error message.
     * @throws IllegalArgumentException If the value is negative.
     */
    private void checkNotNegative(int value, String name)
    {
        if(value < 0) {
            throw new IllegalArgumentException("The " + name + " cannot be negative: " + value);
        }
    }

    /**
     * Checks that a decimal value is not negative.
     * It is only for internal use of the class (setChargesCost and incrementChargesCost).
     * @param value The value to check.
     * @param name The name of the value, for the error message.
     * @throws IllegalArgumentException If the value is negative.
     */
    private void checkNotNegative(float value, String name)
    {
        if(value < 0) {
            throw new IllegalArgumentException("The " + name + " cannot be negative: " + value);
        }
    }

    /**
     * Checks whether this bike is equal to another object.
     * Two bikes are considered equal if they have the same idBike.
     * @param obj The object to compare with this bike.
     * @return true if obj is an {@link ElectricBike} with the same idBike, false otherwise.
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
     * Returns a hash code for this bike, based on its idBike
     * (the same field used in {@link #equals(Object)}).
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
     * Returns a string representation of the electric bike with the format
     * required by the simulation output: ID, battery capacity, battery level,
     * number of recharges, cost of the recharges, idle count, location,
     * recharging location (only if it has one) and target location.
     * Example: (ElectricBike: EBCC0, 15wh, 15wh, 0, 0.0€, 0, 10-13, 10-11, 1-1)
     * @return A string representation of the bike.
     */
    @Override
    public String toString()
    {
        return "(" + getDetails() + ")";
    }
}
