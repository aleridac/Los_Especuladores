import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Model a dock unit within a {@link BikeStation}.
 * It tracks its charging capabilities, fee, and the electric bikes it has recharged.
 * @author Tu Nombre
 * @version 2026.10.13
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
     * Initializes the dock with its identifier, charging speed, and fee.
     * 
     * @param id The unique identifier of the dock (e.g., CC01_02).
     * @param speed The charging speed of the dock in wh.
     * @param fee The price in € / wh of the charge.
     */
    public Dock(String id, int speed, float fee)
    {
        this.idDock = id;
        this.chargingSpeed = speed;
        this.chargingFee = fee;
        this.bikesRecharged = new ArrayList<>();
        this.amountCollected = 0.0f;
        this.free = true;
    }

    /**
     * Returns a string representation of the dock, including its ID, speed, fee, and the number of EVs recharged.
     * @return A string representation of the dock.
     */
    @Override
    public String toString()
    {
        return "(Dock: " + idDock + ", " + chargingSpeed + "wh, " + chargingFee + "€, " + bikesRecharged.size() + ", " + amountCollected + "€)";
    }   

    /**
     * @return The total number of {@link ElectricBike}s that have been recharged by this dock.
     */
    public int getNumerEVRecharged(){
        return bikesRecharged.size();
    }
    
    /**
     * Simulates the charging process for an {@link ElectricBike}.
     * Increases the amount collected and registers the bike as recharged.
     * @param bike The electric bike to be recharged.
     * @param whToCharge The amount of energy to charge the bike with.
     * @return The cost of the charging process.
     */
    public float recharge(ElectricBike bike, int whToCharge){
        float cost = whToCharge * this.chargingFee;
        this.amountCollected += cost;

        if(bike != null)
        {
            bikesRecharged.add(bike);
        }
        return cost;
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
     * @param idDock The new ID for the dock.
     */
    public void setIdDock(String idDock)
    {
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
     * @param chargingSpeed The new charging speed in wh.
     */
    public void setChargingSpeed(int chargingSpeed)
    {
        this.chargingSpeed = chargingSpeed;
    }

    /**
     * Gets the charging fee of the dock.
     * @return The charging fee in €/wh.
     */
    public float getChargingFee()
    {
        return chargingFee;
    }

    /**
     * Sets the charging fee of the dock.
     * @param chargingFee The new charging fee in €/wh.
     */
    public void setChargingFee(float chargingFee)
    {
        this.chargingFee = chargingFee;
    }

    /**
     * Gets the list of bikes that have been recharged at this dock.
     * @return The list of recharged bikes.
     */
    public List<ElectricBike> getBikesRecharged() 
    {
        return bikesRecharged;
    }
    
    /**
     * Sets the list of bikes that have been recharged at this dock.
     * @param bikesRecharged The new list of recharged bikes.
     */
    public void setBikesRecharged(List<ElectricBike> bikesRecharged) 
    {
        this.bikesRecharged = bikesRecharged;
    }

    /**
     * Checks if the dock is free.
     * @return true if the dock is free, false otherwise.
     */
    public boolean isFree()
    {
        return free;
    }

    /**
     * Sets the availability of the dock.
     * @param free true if the dock is free, false if occupied.
     */
    public void setFree(boolean free)
    {
        this.free = free;
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
     * @param amountCollected The new amount collected in €.
     */
    public void setAmountCollected(float amountCollected)
    {
        this.amountCollected = amountCollected;
    }

    /**
     * Implementa la igualdad de contenido para determinar si dos docks son el mismo.
     * 
     * @param o El objeto a comparar con este dock.
     * @return true si el objeto proporcionado es un Dock con el mismo idDock.
     */
    @Override
    public boolean equals(Object o) 
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dock dock = (Dock) o;
        return Objects.equals(idDock, dock.idDock);
    }

    /**
     * Genera un código hash para el dock basándose en su idDock.
     * 
     * @return El valor del hash del dock.
     */
    @Override
    public int hashCode() 
    {
        return Objects.hash(idDock);
    }
}
