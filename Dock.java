import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a dock of a {@link BikeStation}. A dock is the place
 * where the electric bikes are recharged. Each dock has a charging speed,
 * a price (fee), it can be free or not, and it saves the bikes that it has
 * recharged and the money that it has collected.
 * @author DP classes
 * @author Los Especuladores
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





    //=====================================CONSTRUCTOR=====================================




    /**
     * Constructor for objects of class Dock.
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








    //===================================REQUIRED METHODS===================================









    /**
     * Simulates the charging process for an {@link ElectricBike}.
     * Increases the amount collected and registers the bike as recharged.
     * @param bike The electric bike to be recharged. Must not be null.
     * @param whToCharge The amount of energy (wh) to charge the bike with. Must not be negative.
     * @return The cost of the charging process.
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
     * Adds a bike to the list of bikes recharged in this dock. The bikes
     * stay in the same order in which they were recharged.
     * @param bike The electric bike that was recharged. It cannot be null.
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
     * Updates the money collected by the dock. It adds the price of a charge.
     * @param cost The price of the charge in €. It cannot be negative.
     * @throws IllegalArgumentException If the price is negative.
     */
    public void updateAmountCollected(float cost)
    {
        checkAmount(cost);
        amountCollected += cost;
    }

    /**
     * @return The total number of {@link ElectricBike}s that have been recharged by this dock.
     */
    public int getNumerEVRecharged()
    {
        return bikesRecharged.size();
    }






    //=================================GETTERS AND SETTERS=================================







    /**
     * Returns the identifier of the dock.
     * @return The ID of the dock.
     */
    public String getIdDock()
    {
        return idDock;
    }

    /**
     * Changes the identifier of the dock.
     * @param idDock The new ID of the dock. It cannot be null or empty.
     * @throws IllegalArgumentException If the ID is null or empty.
     */
    public void setIdDock(String idDock)
    {
        checkId(idDock);
        this.idDock = idDock;
    }

    /**
     * Returns the charging speed of the dock.
     * @return The charging speed in wh.
     */
    public int getChargingSpeed()
    {
        return chargingSpeed;
    }

    /**
     * Changes the charging speed of the dock.
     * @param chargingSpeed The new charging speed in wh. It must be bigger than 0.
     * @throws IllegalArgumentException If the speed is not bigger than 0.
     */
    public void setChargingSpeed(int chargingSpeed)
    {
        checkChargingSpeed(chargingSpeed);
        this.chargingSpeed = chargingSpeed;
    }

    /**
     * Returns the price (fee) of the dock.
     * @return The fee in € / wh.
     */
    public float getChargingFee()
    {
        return chargingFee;
    }

    /**
     * Changes the price (fee) of the dock.
     * @param chargingFee The new fee in € / wh. It cannot be negative.
     * @throws IllegalArgumentException If the fee is negative.
     */
    public void setChargingFee(float chargingFee)
    {
        checkChargingFee(chargingFee);
        this.chargingFee = chargingFee;
    }

    /**
     * Returns the bikes that have been recharged in this dock, in the order
     * in which they were recharged. It returns a copy of the list, so nobody
     * can change the list of the dock from outside the class.
     * @return A copy of the list of recharged bikes.
     */
    public List<ElectricBike> getBikesRecharged()
    {
        return new ArrayList<>(bikesRecharged);
    }

    /**
     * Changes the list of bikes recharged in this dock. The dock saves a
     * copy of the list that it receives.
     * @param bikesRecharged The new list of recharged bikes. It cannot be null.
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
     * Returns the total money collected by the dock.
     * @return The money collected in €.
     */
    public float getAmountCollected()
    {
        return amountCollected;
    }

    /**
     * Changes the total money collected by the dock.
     * @param amountCollected The new money collected in €. It cannot be negative.
     * @throws IllegalArgumentException If the money is negative.
     */
    public void setAmountCollected(float amountCollected)
    {
        checkAmount(amountCollected);
        this.amountCollected = amountCollected;
    }

    /**
     * Says if the dock is free or not.
     * @return true if the dock is free, false if it is occupied.
     */
    public boolean isFree()
    {
        return free;
    }

    /**
     * Changes if the dock is free or occupied.
     * @param free true if the dock is free, false if it is occupied.
     */
    public void setFree(boolean free)
    {
        this.free = free;
    }






    //=======================================CHECKERS=======================================







    /**
     * Checks that the identifier of the dock is valid.
     * We only use it inside this class (in the constructor and in setIdDock).
     * @param id The identifier that we want to check.
     * @throws IllegalArgumentException If the identifier is null or empty.
     */
    private void checkId(String id)
    {
        if(id == null || id.trim().length() == 0) {
            throw new IllegalArgumentException("The dock ID cannot be null or empty");
        }
    }

    /**
     * Checks that the charging speed is valid.
     * We only use it inside this class (in the constructor and in setChargingSpeed).
     * @param speed The charging speed that we want to check.
     * @throws IllegalArgumentException If the speed is not bigger than 0.
     */
    private void checkChargingSpeed(int speed)
    {
        if(speed <= 0) {
            throw new IllegalArgumentException(
                        "The charging speed must be greater than 0: " + speed);
        }
    }

    /**
     * Checks that the fee is valid.
     * We only use it inside this class (in the constructor and in setChargingFee).
     * @param fee The fee that we want to check.
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
     * We only use it inside this class (in updateAmountCollected and in setAmountCollected).
     * @param amount The amount of money that we want to check.
     * @throws IllegalArgumentException If the amount is negative.
     */
    private void checkAmount(float amount)
    {
        if(amount < 0) {
            throw new IllegalArgumentException(
                        "The amount of money cannot be negative: " + amount);
        }
    }









    //============================EQUALS, HASHCODE AND TOSTRING============================








    /**
     * Compares this dock with another object. Two docks are equal if they
     * have the same idDock.
     * @param obj The object that we want to compare with this dock.
     * @return true if obj is a {@link Dock} with the same idDock, false if not.
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
     * Returns a hash code for this dock. It uses the idDock, because it is
     * the same field that we use in {@link #equals(Object)}.
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
     * Returns a string representation of the dock, including its ID, speed, fee, and the number of EVs recharged.
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
