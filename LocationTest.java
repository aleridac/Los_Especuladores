import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test implementation of the {@link Location} class.
 * Provides unit tests for methods like {@code distance()} and {@code nextLocation()}.
 * @author DP classes
 * @author Los Especuladores
 * @version 2026.11.12
 */
public class LocationTest
{
    private Location p0;
    private Location p1;
    private Location p2;
    private Location p3;
    private Location p4;

    /**
     * Default constructor for test class LocationTest.
     */
    public LocationTest()
    {
    }

    /**
     * Sets up the test fixture: the locations p0, p1, p2, p3 and p4 of the
     * examples of the project statement.
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
        p0 = new Location(0, 0);
        p1 = new Location(1, 5);
        p2 = new Location(6, 6);
        p3 = new Location(5, 2);
        p4 = new Location(3, 4);
    }

    /**
     * Tears down the test fixture.
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
        // No teardown required for Location tests
    }

    /**
     * Test the {@code distance} method of the {@link Location} class.
     * Checks distances from a central location to surrounding points.
     */
    @Test
    public void testDistance()
    {
        assertEquals(5, p0.distance(p1));
        assertEquals(4, p2.distance(p3));
        assertEquals(4, p0.distance(p4));
        assertEquals(0, p0.distance(p0));
    }

    /**
     * Test the {@code nextLocation} method when the destination is adjacent
     * (one step away in any direction).
     */
    @Test
    public void testAdjacentLocations()
    {
        Location ady = new Location(1, 1);
        Location next1 = p0.nextLocation(ady);
        assertEquals(true, next1.equals(ady));
        assertEquals(1, p0.distance(next1));
    }

    /**
     * Test the {@code nextLocation} method when the destination is not adjacent
     * (more than one step away).
     */
    @Test
    public void testNonAdjacentLocations()
    {
        Location next1 = p0.nextLocation(p1);
        assertEquals("1-1", next1.toString());
        assertEquals(1, p0.distance(next1));
        Location next2 = p2.nextLocation(p3);
        assertEquals("5-5", next2.toString());
        Location next3 = p0.nextLocation(p4);
        assertEquals("1-1", next3.toString());
    }

    /**
     * Checks that two locations are equal if they have the same x and y,
     * and that a location is not equal to another location or to null.
     */
    @Test
    public void testEquals()
    {
        Location same = new Location(1, 5);
        assertEquals(true, p1.equals(same));
        assertEquals(false, p1.equals(p2));
        assertEquals(false, p1.equals(null));
    }

    /**
     * Checks that two equal locations (same x and y) have the same hash code.
     */
    @Test
    public void testHashCode()
    {
        Location same = new Location(1, 5);
        assertEquals(115, p1.hashCode());
        assertEquals(115, same.hashCode());
    }

    /**
     * Checks that toString returns the location with the format "x-y".
     */
    @Test
    public void testToString()
    {
        assertEquals("0-0", p0.toString());
        assertEquals("1-5", p1.toString());
    }
}