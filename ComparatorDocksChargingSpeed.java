import java.util.*;

/**
 * Compares two {@link Dock} objects by their charging speed in descending order.
 * In case of a tie, they are compared by their charging fee in ascending order,
 * and if there is still a tie, by their ID in ascending order
 * (using {@link ComparatorDocksId}).
 * This is the order in which the docks are kept inside a {@link BikeStation}.
 * @author Alvaro
 * @version 2026.11.10
 */
public class ComparatorDocksChargingSpeed implements Comparator<Dock>
{
    /**
     * Compares its two {@link Dock} arguments for order.
     * @param c1 The first dock to be compared.
     * @param c2 The second dock to be compared.
     * @return A negative integer if c1 must go before c2, zero if both are
     * equivalent, or a positive integer if c1 must go after c2.
     */
    @Override
    public int compare(Dock c1, Dock c2)
    {
        if(c1.getChargingSpeed() == c2.getChargingSpeed()) {
            if(c1.getChargingFee() == c2.getChargingFee()) {
                return new ComparatorDocksId().compare(c1, c2);
            }
            else if(c1.getChargingFee() > c2.getChargingFee()) {
                return 1;
            }
            else {
                return -1;
            }
        }
        else if(c1.getChargingSpeed() > c2.getChargingSpeed()) {
            return -1;
        }
        else {
            return 1;
        }
    }
}
