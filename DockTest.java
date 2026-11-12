import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link Dock}. It checks with JUnit the methods of the class
 * that return a value or change the state of the dock (except the getters
 * and setters of the fields).
 * @author Los Especuladores
 * @version 2026.11.12
 */
public class DockTest
{
    private Dock dock1;
    private EBCompany company1;
    private Location location1;
    private Location location2;
    private ElectricBike electric1;

    /**
     * Default constructor for test class DockTest.
     */
    public DockTest()
    {
    }

    /**
     * Sets up the test fixture: a dock, a company, two locations and an
     * electric bike that we use in all the tests.
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
        dock1 = new Dock("CC01_003", 80, 0.8f);
        company1 = new EBCompany("EBBike");
        location1 = new Location(10, 13);
        location2 = new Location(1, 1);
        electric1 = new ElectricBike(company1, location1, location2, "EBCC0", 15);
    }

    /**
     * Tears down the test fixture.
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
        // No teardown required for Dock tests
    }

    /**
     * Checks that recharge returns the right cost (wh * fee), adds the bike
     * to the list of recharged bikes and updates the money collected.
     */
    @Test
    public void testRecharge()
    {
        assertEquals(8.0, dock1.recharge(electric1, 10), 0.01);
        assertEquals(1, dock1.getNumerEVRecharged());
        assertEquals(8.0, dock1.getAmountCollected(), 0.01);
        java.util.ArrayList<ElectricBike> arrayLis1 = (java.util.ArrayList<ElectricBike>)dock1.getBikesRecharged();
        assertEquals(true, arrayLis1.contains(electric1));
    }

    /**
     * Checks that addBikeRecharged adds the bike to the list of recharged bikes.
     */
    @Test
    public void testAddBikeRecharged()
    {
        dock1.addBikeRecharged(electric1);
        assertEquals(1, dock1.getNumerEVRecharged());
        java.util.ArrayList<ElectricBike> arrayLis2 = (java.util.ArrayList<ElectricBike>)dock1.getBikesRecharged();
        assertEquals(true, arrayLis2.contains(electric1));
    }

    /**
     * Checks that updateAmountCollected adds each cost to the money collected.
     */
    @Test
    public void testUpdateAmountCollected()
    {
        dock1.updateAmountCollected(8.0f);
        dock1.updateAmountCollected(4.0f);
        assertEquals(12.0, dock1.getAmountCollected(), 0.01);
    }

    /**
     * Checks that getNumerEVRecharged counts the bikes recharged in the dock.
     */
    @Test
    public void testGetNumerEVRecharged()
    {
        assertEquals(0, dock1.getNumerEVRecharged());
        dock1.recharge(electric1, 10);
        dock1.recharge(electric1, 5);
        assertEquals(2, dock1.getNumerEVRecharged());
    }

    /**
     * Checks that toString returns the information of the dock with the
     * format of the output of the simulation, before and after a recharge.
     */
    @Test
    public void testToString()
    {
        assertEquals("(Dock: CC01_003, 80wh, 0.8€, 0, 0.0€)", dock1.toString());
        dock1.recharge(electric1, 10);
        assertEquals("(Dock: CC01_003, 80wh, 0.8€, 1, 8.0€)", dock1.toString());
    }

    /**
     * Checks that two docks are equal if they have the same ID, and that a
     * dock is not equal to a dock with another ID or to null.
     */
    @Test
    public void testEquals()
    {
        Dock dock2 = new Dock("CC01_003", 20, 0.2f);
        Dock dock3 = new Dock("CC02_001", 80, 0.8f);
        assertEquals(true, dock1.equals(dock2));
        assertEquals(false, dock1.equals(dock3));
        assertEquals(false, dock1.equals(null));
    }

    /**
     * Checks that two equal docks (same ID) have the same hash code.
     */
    @Test
    public void testHashCode()
    {
        Dock dock2 = new Dock("CC01_003", 20, 0.2f);
        assertEquals(1559709546, dock1.hashCode());
        assertEquals(1559709546, dock2.hashCode());
    }
}
