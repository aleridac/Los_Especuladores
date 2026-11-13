import java.util.*;

/**
 * Compares two {@link Dock} objects based on their ID in ascending order.
 * It is used as the final tie-breaker of {@link ComparatorDocksChargingSpeed}.
 * @author DP classes
 * @author Los Especuladores
 * @version 2026.11.10
 */
public class ComparatorDocksId implements Comparator<Dock>
{
    /**
     * Compares its two {@link Dock} arguments for order.
     * The comparison is based on the lexicographical order of their IDs.
     * @param c1 The first dock to be compared.
     * @param c2 The second dock to be compared.
     * @return A negative integer, zero, or a positive integer as the first
     * dock's ID is less than, equal to, or greater than the second one's.
     */
    @Override
    public int compare(Dock c1, Dock c2)
    {
        return c1.getIdDock().compareTo(c2.getIdDock());
    }
}
