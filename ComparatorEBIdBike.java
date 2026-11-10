import java.util.*;

/**
 * Compares two {@link ElectricBike} objects based on their ID in ascending order.
 * It is used to keep the bikes of the simulation sorted by their ID, which is
 * the order in which they are shown and in which they act in each step.
 * @author DP classes
 * @author Alvaro
 * @version 2026.11.10
 */
public class ComparatorEBIdBike implements Comparator<ElectricBike>
{
    /**
     * Compares its two {@link ElectricBike} arguments for order.
     * The comparison is based on the lexicographical order of their IDs.
     * @param eb1 The first electric bike to be compared.
     * @param eb2 The second electric bike to be compared.
     * @return A negative integer, zero, or a positive integer as the first
     * bike's ID is less than, equal to, or greater than the second one's.
     */
    @Override
    public int compare(ElectricBike eb1, ElectricBike eb2)
    {
        return eb1.getIdBike().compareTo(eb2.getIdBike());
    }
}
