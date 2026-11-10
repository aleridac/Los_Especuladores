import java.util.ArrayList;
import java.util.List;

/**
 * Model a dock unit within a {@link BikeStation}.
 * It tracks its charging capabilities (speed and fee), whether it is free,
 * the electric bikes it has recharged and the amount of money collected.
 * @author David J. Barnes and Michael Kölling
 * @author DP classes
 * @author Alvaro
 * @version 2026.11.10
 */
public class Dock
{
    private String idDock;
    private int chargingSpeed;
    private float chargingFee;
    private List<ElectricBike> bikesRecharged;
    private float amountCollected;
    private boolean free;

    /**
     * Constructor for objects of class Dock.
     * The dock starts free, without recharged bikes and without money collected.
     * @param id The unique identifier of the dock (e.g., CC01_02). Must not be null or empty.
     * @param speed The charging speed of the dock in wh. Must be greater than 0.
     * @param fee The price in € / wh of the charge. Must not be negative.
     * @throws IllegalArgumentException If any parameter is not valid.
     */
    public Dock(String id, int speed, float fee)
    {
        checkId(id);
        checkChargingSpeed(speed);
        checkChargingFee(fee);
        this.idDock = id;
        this.chargingSpeed = speed;
        this.chargingFee = fee;
        this.bikesRecharged = new ArrayList<>();
        this.amountCollected = 0.0f;
        this.free = true;
    }

    /**
     * Recharges an {@link ElectricBike} in this dock.
     * The bike is added to the list of recharged bikes and the cost of the
     * charge (wh charged * charging fee) is added to the amount collected.
     * @param bike The electric bike to be recharged. Must not be null.
     * @param whToCharge The amount of energy (wh) to charge the bike with. Must not be negative.
     * @return The cost of the charge in €.
     * @throws IllegalArgumentException If the bike is null or whToCharge is negative.
     */
    public float recharge(ElectricBike bike, int whToCharge)
    {
        if(whToCharge < 0) {
            throw new IllegalArgumentException(
                        "The energy to charge cannot be negative: " + whToCharge);
        }
        float cost = whToCharge * chargingFee;
        addBikeRecharged(bike);
        updateAmountCollected(cost);
        return cost;
    }

    /**
     * Adds a bike to the list of bikes recharged in this dock.
     * The bikes are kept in the order in which they were recharged.
     * @param bike The electric bike recharged. Must not be null.
     * @throws IllegalArgumentException If the bike is null.
     */
    public void addBikeRecharged(ElectricBike bike)
    {
        if(bike == null) {
            throw new IllegalArgumentException("The bike to add cannot be null");
        }
        bikesRecharged.add(bike);
    }

    /**
     * Updates the amount collected by the dock, adding the cost of a charge.
     * @param cost The cost of the charge in €. Must not be negative.
     * @throws IllegalArgumentException If the cost is negative.
     */
    public void updateAmountCollected(float cost)
    {
        checkAmount(cost);
        amountCollected += cost;
    }

    /**
     * Gets the number of bikes that have been recharged in this dock.
     * @return The total number of {@link ElectricBike}s recharged in this dock.
     */
    public int getNumerEVRecharged()
    {
        return bikesRecharged.size();
    }

    /**
     * Gets the unique identifier of the dock.
     * @return The ID of the dock.
     */
    public String getIdDock()
    {
        return idDock;
    }

    /**
     * Sets the unique identifier of the dock.
     * @param idDock The new ID of the dock. Must not be null or empty.
     * @throws IllegalArgumentException If the ID is null or empty.
     */
    public void setIdDock(String idDock)
    {
        checkId(idDock);
        this.idDock = idDock;
    }

    /**
     * Gets the charging speed of the dock.
     * @return The charging speed in wh.
     */
    public int getChargingSpeed()
    {
        return chargingSpeed;
    }

    /**
     * Sets the charging speed of the dock.
     * @param chargingSpeed The new charging speed in wh. Must be greater than 0.
     * @throws IllegalArgumentException If the speed is not greater than 0.
     */
    public void setChargingSpeed(int chargingSpeed)
    {
        checkChargingSpeed(chargingSpeed);
        this.chargingSpeed = chargingSpeed;
    }

    /**
     * Gets the charging fee of the dock.
     * @return The charging fee in € / wh.
     */
    public float getChargingFee()
    {
        return chargingFee;
    }

    /**
     * Sets the charging fee of the dock.
     * @param chargingFee The new charging fee in € / wh. Must not be negative.
     * @throws IllegalArgumentException If the fee is negative.
     */
    public void setChargingFee(float chargingFee)
    {
        checkChargingFee(chargingFee);
        this.chargingFee = chargingFee;
    }

    /**
     * Gets the bikes that have been recharged in this dock, in the order
     * in which they were recharged. A copy of the list is returned, so the
     * internal list of the dock cannot be modified from outside.
     * @return A copy of the list of recharged bikes.
     */
    public List<ElectricBike> getBikesRecharged()
    {
        return new ArrayList<>(bikesRecharged);
    }

    /**
     * Sets the bikes that have been recharged in this dock.
     * The dock stores a copy of the list received.
     * @param bikesRecharged The new list of recharged bikes. Must not be null.
     * @throws IllegalArgumentException If the list is null.
     */
    public void setBikesRecharged(List<ElectricBike> bikesRecharged)
    {
        if(bikesRecharged == null) {
            throw new IllegalArgumentException("The list of recharged bikes cannot be null");
        }
        this.bikesRecharged = new ArrayList<>(bikesRecharged);
    }

    /**
     * Gets the total amount collected by the dock.
     * @return The amount collected in €.
     */
    public float getAmountCollected()
    {
        return amountCollected;
    }

    /**
     * Sets the total amount collected by the dock.
     * @param amountCollected The new amount collected in €. Must not be negative.
     * @throws IllegalArgumentException If the amount is negative.
     */
    public void setAmountCollected(float amountCollected)
    {
        checkAmount(amountCollected);
        this.amountCollected = amountCollected;
    }

    /**
     * Checks if the dock is free.
     * @return true if the dock is free, false if it is occupied.
     */
    public boolean isFree()
    {
        return free;
    }

    /**
     * Sets the availability of the dock.
     * @param free true if the dock is free, false if it is occupied.
     */
    public void setFree(boolean free)
    {
        this.free = free;
    }

    /**
     * Checks that a dock identifier is valid.
     * It is only for internal use of the class (constructor and setIdDock).
     * @param id The identifier to check.
     * @throws IllegalArgumentException If the identifier is null or empty.
     */
    private void checkId(String id)
    {
        if(id == null || id.trim().length() == 0) {
            throw new IllegalArgumentException("The dock ID cannot be null or empty");
        }
    }

    /**
     * Checks that a charging speed is valid.
     * It is only for internal use of the class (constructor and setChargingSpeed).
     * @param speed The charging speed to check.
     * @throws IllegalArgumentException If the speed is not greater than 0.
     */
    private void checkChargingSpeed(int speed)
    {
        if(speed <= 0) {
            throw new IllegalArgumentException(
                        "The charging speed must be greater than 0: " + speed);
        }
    }

    /**
     * Checks that a charging fee is valid.
     * It is only for internal use of the class (constructor and setChargingFee).
     * @param fee The charging fee to check.
     * @throws IllegalArgumentException If the fee is negative.
     */
    private void checkChargingFee(float fee)
    {
        if(fee < 0) {
            throw new IllegalArgumentException(
                        "The charging fee cannot be negative: " + fee);
        }
    }

    /**
     * Checks that an amount of money is valid.
     * It is only for internal use of the class (updateAmountCollected and setAmountCollected).
     * @param amount The amount to check.
     * @throws IllegalArgumentException If the amount is negative.
     */
    private void checkAmount(float amount)
    {
        if(amount < 0) {
            throw new IllegalArgumentException(
                        "The amount of money cannot be negative: " + amount);
        }
    }

    /**
     * Checks whether this dock is equal to another object.
     * Two docks are considered equal if they have the same idDock.
     * @param obj The object to compare with this dock.
     * @return true if obj is a {@link Dock} with the same idDock, false otherwise.
     */
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj) {
            return true;
        }
        if(!(obj instanceof Dock)) {
            return false;
        }
        Dock other = (Dock) obj;
        return getIdDock().equals(other.getIdDock());
    }

    /**
     * Returns a hash code for this dock, based on its idDock
     * (the same field used in {@link #equals(Object)}).
     * Hashcode technique taken from Effective Java by Joshua Bloch.
     * @return The hash code of the dock.
     */
    @Override
    public int hashCode()
    {
        int result = 7;
        result = 3 * result + getIdDock().hashCode();
        return result;
    }

    /**
     * Returns a string representation of the dock with the format required
     * by the simulation output: ID, charging speed, charging fee, number of
     * bikes recharged and amount collected.
     * Example: (Dock: CC00_003, 80wh, 0.8€, 0, 0.0€)
     * @return A string representation of the dock.
     */
    @Override
    public String toString()
    {
        return "(Dock: " + getIdDock() + ", " + getChargingSpeed() + "wh, "
                + getChargingFee() + "€, " + getNumerEVRecharged() + ", "
                + getAmountCollected() + "€)";
    }
}