import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link BikeStation}. It checks with JUnit the methods of the
 * class that return a value or change the state of the station (except the
 * getters and setters of the fields).
 * @author Los Especuladores
 * @version 2026.11.12
 */
public class BikeStationTest
{
    private Location location1;
    private BikeStation station1;
    private Dock dock0;
    private Dock dock3;
    private EBCompany company1;
    private Location location2;
    private Location location3;
    private ElectricBike bike1;

    /**
     * Default constructor for test class BikeStationTest.
     */
    public BikeStationTest()
    {
    }

    /**
     * Sets up the test fixture: a station without docks, two docks, a company,
     * two locations and an electric bike that we use in the tests.
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
        location1 = new Location(10, 11);
        station1 = new BikeStation("CC01", location1);
        dock0 = new Dock("CC01_000", 20, 0.2f);
        dock3 = new Dock("CC01_003", 80, 0.8f);
        company1 = new EBCompany("EBBike");
        location2 = new Location(10, 13);
        location3 = new Location(1, 1);
        bike1 = new ElectricBike(company1, location2, location3, "EBCC0", 15);
    }

    /**
     * Tears down the test fixture.
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
        // No teardown required for BikeStation tests
    }

    /**
     * Checks that addDock adds the docks to the station and keeps them in
     * order (the fastest dock goes first).
     */
    @Test
    public void testAddDock()
    {
        station1.addDock(dock0);
        station1.addDock(dock3);
        java.util.ArrayList<Dock> docks1 = (java.util.ArrayList<Dock>)station1.getDocks();
        assertEquals(2, docks1.size());
        assertEquals(true, docks1.contains(dock0));
        Dock first = docks1.get(0);
        assertEquals("CC01_003", first.getIdDock());
    }

    /**
     * Checks that getDock returns the dock with the given ID, or null if the
     * station does not have a dock with this ID.
     */
    @Test
    public void testGetDock()
    {
        station1.addDock(dock0);
        station1.addDock(dock3);
        Dock found = station1.getDock("CC01_000");
        assertEquals(true, found.equals(dock0));
        assertNull(station1.getDock("CC01_999"));
    }

    /**
     * Checks that getFreeDock returns the first free dock in the order of the
     * docks, the next one if the first is occupied, and null if all of them
     * are occupied.
     */
    @Test
    public void testGetFreeDock()
    {
        station1.addDock(dock0);
        station1.addDock(dock3);
        Dock free1 = station1.getFreeDock();
        assertEquals("CC01_003", free1.getIdDock());
        dock3.setFree(false);
        Dock free2 = station1.getFreeDock();
        assertEquals("CC01_000", free2.getIdDock());
        dock0.setFree(false);
        assertNull(station1.getFreeDock());
    }

    /**
     * Checks that getTotalBikesRecharged adds the bikes recharged in all the
     * docks of the station.
     */
    @Test
    public void testGetTotalBikesRecharged()
    {
        station1.addDock(dock0);
        station1.addDock(dock3);
        assertEquals(0, station1.getTotalBikesRecharged());
        dock3.recharge(bike1, 10);
        dock0.recharge(bike1, 5);
        assertEquals(2, station1.getTotalBikesRecharged());
    }

    /**
     * Checks that two stations are equal if they have the same ID, and that a
     * station is not equal to a station with another ID or to null.
     */
    @Test
    public void testEquals()
    {
        BikeStation same = new BikeStation("CC01", location2);
        BikeStation other = new BikeStation("CC02", location1);
        assertEquals(true, station1.equals(same));
        assertEquals(false, station1.equals(other));
        assertEquals(false, station1.equals(null));
    }

    /**
     * Checks that two equal stations (same ID) have the same hash code.
     */
    @Test
    public void testHashCode()
    {
        BikeStation same = new BikeStation("CC01", location2);
        assertEquals(2061942, station1.hashCode());
        assertEquals(2061942, same.hashCode());
    }

    /**
     * Checks that toString returns the information of the station with the
     * format of the output of the simulation, before and after a recharge.
     */
    @Test
    public void testToString()
    {
        assertEquals("(BikeStation: CC01, 0, 10-11)", station1.toString());
        station1.addDock(dock3);
        dock3.recharge(bike1, 10);
        assertEquals("(BikeStation: CC01, 1, 10-11)", station1.toString());
    }
}
