import java.util.*;

/**
 * Compares two {@link BikeStation} objects based on their ID in ascending order.
 * It is used to keep the stations of the simulation sorted by their ID and as
 * the tie-breaker of {@link ComparatorBikeStationNumerRecharged}.
 * @author DP classes
 * @author Los Especuladores
 * @version 2026.11.10
 */
public class ComparatorBikeStationId implements Comparator<BikeStation>
{
    /**
     * Compares its two {@link BikeStation} arguments for order.
     * The comparison is based on the lexicographical order of their IDs.
     * @param st1 The first bike station to be compared.
     * @param st2 The second bike station to be compared.
     * @return A negative integer, zero, or a positive integer as the first
     * station's ID is less than, equal to, or greater than the second one's.
     */
    @Override
    public int compare(BikeStation st1, BikeStation st2)
    {
        return st1.getId().compareTo(st2.getId());
    }
}
