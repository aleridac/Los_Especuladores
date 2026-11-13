import java.util.*;

/**
 * Compares two {@link BikeStation} objects by the number of bikes recharged
 * in their docks, in descending order. In case of a tie, they are compared
 * by their ID in ascending order (using {@link ComparatorBikeStationId}).
 * It is used to show the stations in the final information of the simulation.
 * @author Los Especuladores
 * @version 2026.11.10
 */
public class ComparatorBikeStationNumerRecharged implements Comparator<BikeStation>
{
    /**
     * Compares its two {@link BikeStation} arguments for order.
     * @param st1 The first bike station to be compared.
     * @param st2 The second bike station to be compared.
     * @return A negative integer if st1 must go before st2, zero if both are
     * equivalent, or a positive integer if st1 must go after st2.
     */
    @Override
    public int compare(BikeStation st1, BikeStation st2)
    {
        if(st1.getTotalBikesRecharged() == st2.getTotalBikesRecharged()) {
            return new ComparatorBikeStationId().compare(st1, st2);
        }
        else if(st1.getTotalBikesRecharged() > st2.getTotalBikesRecharged()) {
            return -1;
        }
        else {
            return 1;
        }
    }
}
