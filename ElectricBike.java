import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 * Model the common elements of an Electric bike (EB) that operates
 * within the simulation, moving towards a target and potentially recharging.
 *
 * @author Alvaro
 * @version 2026.11.16
 */
public class ElectricBike
{
    private static final int MOVING_COST = 5;

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
    private int arrivalStep;

    /**
     * Constructor of class ElectricBike.
     * @param company The EB's operating company.
     * @param location The EB's starting location.
     * @param targetLocation The EB's final destination.
     * @param id The identifier of the bike.
     * @param batteryCapacity The maximum capacity of the battery.
     */
    public ElectricBike(EBCompany company, Location location,
                       Location targetLocation, String id,
                       int batteryCapacity)
    {
        this.company = Objects.requireNonNull(company);
        this.location = Objects.requireNonNull(location);
        this.targetLocation = Objects.requireNonNull(targetLocation);
        this.idBike = id;
        this.batteryCapacity = batteryCapacity;
        this.batteryLevel = batteryCapacity;
        this.rechargingLocation = null;
        this.idleCount = 0;
        this.wsCharged = 0;
        this.chargesCount = 0;
        this.chargesCost = 0.0f;
        this.docks = new ArrayList<>();
        this.arrivalStep = Integer.MAX_VALUE;
    }

    // GETTERS AND SETTERS

    /**
     * @return The id of the electric bike.
     */
    public String getIdBike()
    {
        return idBike;
    }

    /**
     * @param idBike The new identifier.
     */
    public void setIdBike(String idBike)
    {
        this.idBike = idBike;
    }

    /**
     * @return The company managing this bike.
     */
    public EBCompany getCompany()
    {
        return company;
    }

    /**
     * @param company The new company.
     */
    public void setCompany(EBCompany company)
    {
        this.company = Objects.requireNonNull(company);
    }

    /**
     * @return The current location.
     */
    public Location getLocation()
    {
        return location;
    }

    /**
     * Set the current location.
     * @param location Where the bike is.
     */
    public void setLocation(Location location)
    {
        this.location = Objects.requireNonNull(location);
    }

    /**
     * @return The final target location.
     */
    public Location getTargetLocation()
    {
        return targetLocation;
    }

    /**
     * Set the required final target location.
     * @param location Where to go.
     */
    public void setTargetLocation(Location location)
    {
        this.targetLocation = Objects.requireNonNull(location);
    }

    /**
     * @return The temporary recharging location.
     */
    public Location getRechargingLocation()
    {
        return rechargingLocation;
    }

    /**
     * @param rechargingLocation The new recharging location.
     */
    public void setRechargingLocation(Location rechargingLocation)
    {
        this.rechargingLocation = rechargingLocation;
    }

    /**
     * @return The number of idle turns.
     */
    public int getIdleCount()
    {
        return idleCount;
    }

    /**
     * @param idleCount The new idle count.
     */
    public void setIdleCount(int idleCount)
    {
        this.idleCount = idleCount;
    }

    /**
     * @return The maximum battery capacity.
     */
    public int getBatteryCapacity()
    {
        return batteryCapacity;
    }

    /**
     * @param batteryCapacity The new battery capacity.
     */
    public void setBatteryCapacity(int batteryCapacity)
    {
        this.batteryCapacity = batteryCapacity;
    }

    /**
     * @return The current battery level.
     */
    public int getBatteryLevel()
    {
        return batteryLevel;
    }

    /**
     * @param batteryLevel The new battery level.
     */
    public void setBatteryLevel(int batteryLevel)
    {
        this.batteryLevel = batteryLevel;
    }

    /**
     * @return The total Wh charged by the bike.
     */
    public int getWsCharged()
    {
        return wsCharged;
    }

    /**
     * @param wsCharged The new total Wh charged.
     */
    public void setWsCharged(int wsCharged)
    {
        this.wsCharged = wsCharged;
    }

    /**
     * @return The number of recharges performed.
     */
    public int getChargesCount()
    {
        return chargesCount;
    }

    /**
     * @param chargesCount The new number of recharges.
     */
    public void setChargesCount(int chargesCount)
    {
        this.chargesCount = chargesCount;
    }

    /**
     * @return The total cost of recharges.
     */
    public float getChargesCost()
    {
        return chargesCost;
    }

    /**
     * @param chargesCost The new total recharge cost.
     */
    public void setChargesCost(float chargesCost)
    {
        this.chargesCost = chargesCost;
    }

    /**
     * @return The docks where this bike has been recharged.
     */
    public List<Dock> getDocks()
    {
        return docks;
    }

    /**
     * @param docks The new list of docks.
     */
    public void setDocks(List<Dock> docks)
    {
        this.docks = docks;
    }

    /**
     * @return The turn in which the bike arrived at its destination.
     */
    public int getArrivalStep()
    {
        return arrivalStep;
    }

    /**
     * @param arrivalStep The arrival turn.
     */
    public void setArrivalStep(int arrivalStep)
    {
        this.arrivalStep = arrivalStep;
    }

    // ROUTE CALCULATION

    /**
     * Calculates the optimal route for the bike.
     * If there is not enough battery to reach the target,
     * it searches for an intermediate recharging station.
     */
    public void calculateRoute()
    {
        if (enoughBattery()) {
            rechargingLocation = null;
        }
        else {
            calculateRechargingPosition();
        }
    }

    /**
     * Gets a string representation of the planned route.
     * @return The route from the current location to the target.
     */
    public String getStringRoute()
    {
        String route = location.toString();

        if (rechargingLocation != null) {
            route += " -> " + rechargingLocation.toString();
        }

        route += " -> " + targetLocation.toString();

        return route;
    }

    /**
     * Checks if the current battery is sufficient to reach
     * the final destination.
     * @return true if there is enough battery, false otherwise.
     */
    public boolean enoughBattery()
    {
        int energyNeeded =
            distanceToTheTargetLocation() * MOVING_COST;

        return batteryLevel >= energyNeeded;
    }

    /**
     * Determines the best intermediate station to visit.
     * The selected station must be reachable with the current battery.
     */
    public void calculateRechargingPosition()
    {
        int minimumDistance = Integer.MAX_VALUE;
        BikeStation selectedStation = null;

        Iterator<BikeStation> it =
            company.getStations().iterator();

        while (it.hasNext()) {
            BikeStation station = it.next();

            int distanceToStation =
                location.distance(station.getLocation());

            int energyNeeded =
                distanceToStation * MOVING_COST;

            if (batteryLevel >= energyNeeded) {

                int totalDistance =
                    distanceToStation +
                    station.getLocation().distance(targetLocation);

                if (totalDistance < minimumDistance) {
                    minimumDistance = totalDistance;
                    selectedStation = station;
                }
            }
        }

        if (selectedStation != null) {
            rechargingLocation = selectedStation.getLocation();
        }
        else {
            rechargingLocation = null;
        }
    }

    /**
     * Calculates the distance from the current position
     * to the final target location.
     * @return The distance to the target.
     */
    public int distanceToTheTargetLocation()
    {
        return location.distance(targetLocation);
    }

    // RECHARGING

    /**
     * Recharges the bike using the first free dock at its station.
     * Updates the battery, costs and recharge statistics.
     */
    public void recharge()
    {
        if (rechargingLocation == null) {
            return;
        }

        BikeStation station =
            company.getBikeStation(rechargingLocation);

        if (station == null) {
            return;
        }

        Dock dock = station.getFreeDock();

        if (dock == null) {
            return;
        }

        int whToCharge = batteryCapacity - batteryLevel;

        if (whToCharge <= 0) {
            return;
        }

        float cost = dock.recharge(this, whToCharge);

        batteryLevel = batteryCapacity;
        wsCharged += whToCharge;

        incrementCharges();
        incrementChargesCost(cost);

        docks.add(dock);

        calculateRoute();
    }

    /**
     * Increments the number of recharges.
     */
    public void incrementCharges()
    {
        chargesCount++;
    }

    /**
     * Adds an amount to the total recharge cost.
     * @param cost The cost of the recharge.
     */
    public void incrementChargesCost(float cost)
    {
        chargesCost += cost;
    }

    // SIMULATION

    /**
     * Carries out a single simulation step.
     * @param step The current simulation turn.
     */
    public void act(int step)
    {
        // If the bike has already arrived, it remains at its destination.
        if (location.equals(targetLocation)) {
            idleCount++;
            return;
        }

        Location destination = targetLocation;

        if (rechargingLocation != null) {
            destination = rechargingLocation;
        }

        // If there is no possible route, the bike remains stationary.
        if (rechargingLocation == null && !enoughBattery()) {
            return;
        }

        // If the bike is already at the charging station, recharge it.
        if (location.equals(destination)
            && rechargingLocation != null) {

            if (company.getBikeStation(rechargingLocation)
                .getFreeDock() == null) {
                return;
            }

            recharge();
            return;
        }

        // Move one position towards the destination.
        Location nextPosition = location.nextLocation(destination);

        // Check that there is enough energy for the movement.
        if (batteryLevel < MOVING_COST) {
            return;
        }

        location = nextPosition;
        reduceBatteryLevel();

        // Check whether the bike has reached its final destination.
        if (location.equals(targetLocation)) {
            arrivalStep = step;
            System.out.println("(step: " + step
                + " - ElectricBike: " + idBike
                + " at target destination ****)");
        }

        // Check whether the bike has reached the charging station.
        else if (rechargingLocation != null
                 && location.equals(rechargingLocation)) {

            BikeStation station =
                company.getBikeStation(rechargingLocation);

            if (station != null && station.getFreeDock() != null) {
                System.out.println("(step: " + step
                    + " - ElectricBike: " + idBike
                    + " recharges at station ****)");

                recharge();
            }
        }
    }

    /**
     * Reduces the battery level by the energy consumed in one movement.
     */
    public void reduceBatteryLevel()
    {
        batteryLevel -= MOVING_COST;

        if (batteryLevel < 0) {
            batteryLevel = 0;
        }
    }

    // INFORMATION

    /**
     * Returns a detailed string representation of the electric bike.
     * @return The bike information in the required output format.
     */
    @Override
    public String toString()
    {
        String result = "(ElectricBike: " + idBike
            + ", " + batteryCapacity + "wh"
            + ", " + batteryLevel + "wh"
            + ", " + chargesCount
            + ", " + chargesCost + "€"
            + ", " + idleCount
            + ", " + location;

        if (rechargingLocation != null) {
            result += ", " + rechargingLocation;
        }

        result += ", " + targetLocation + ")";

        return result;
    }
}
