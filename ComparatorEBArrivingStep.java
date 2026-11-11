import java.util.*;

/**
 * Compares two {@link ElectricBike} objects by the step in which they arrived
 * at their target location, in ascending order. The bikes that have not
 * arrived go after the ones that have arrived. In case of a tie, they are
 * compared by their ID in ascending order (using {@link ComparatorEBIdBike}).
 * A bike has arrived if it is at its target location. Once a bike arrives,
 * it stays there and its idle count increases by one in each step, so
 * between two bikes that have arrived, the one with the higher idle count
 * is the one that arrived first.
 * It is used to show the bikes in the final information of the simulation.
 * @author Alvaro
 * @version 2026.11.11
 */
public class ComparatorEBArrivingStep implements Comparator<ElectricBike>
{
    /**
     * Compares its two {@link ElectricBike} arguments for order.
     * @param eb1 The first electric bike to be compared.
     * @param eb2 The second electric bike to be compared.
     * @return A negative integer if eb1 must go before eb2, zero if both are
     * equivalent, or a positive integer if eb1 must go after eb2.
     */
    @Override
    public int compare(ElectricBike eb1, ElectricBike eb2)
    {
        boolean arrived1 = eb1.getLocation().equals(eb1.getTargetLocation());
        boolean arrived2 = eb2.getLocation().equals(eb2.getTargetLocation());
        if(arrived1 && arrived2) {
            if(eb1.getIdleCount() == eb2.getIdleCount()) {
                return new ComparatorEBIdBike().compare(eb1, eb2);
            }
            else if(eb1.getIdleCount() > eb2.getIdleCount()) {
                return -1;
            }
            else {
                return 1;
            }
        }
        else if(arrived1) {
            return -1;
        }
        else if(arrived2) {
            return 1;
        }
        else {
            return new ComparatorEBIdBike().compare(eb1, eb2);
        }
    }
}
