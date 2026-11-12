import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link ElectricBike}. It checks with JUnit the creation of
 * the bikes and the methods of the class that return a value or change the
 * state of the bike (except the getters and setters of the fields). The
 * fixture uses the stations of the SIMPLE demo, with one dock in each one.
 * @author Los Especuladores
 * @version 2026.11.12
 */
public class ElectricBikeTest
{
    private EBCompany company1;
    private Location location1;
    private Location location2;
    private Location location3;
    private Location location4;
    private BikeStation station0;
    private BikeStation station1;
    private BikeStation station2;
    private BikeStation station3;
    private Location location5;
    private Location location6;
    private Location location7;
    private ElectricBike bike0;
    private ElectricBike bike1;

    /**
     * Default constructor for test class ElectricBikeTest.
     */
    public ElectricBikeTest()
    {
    }

    /**
     * Sets up the test fixture: a company with the four stations of the
     * SIMPLE demo (each one with a dock of 80 wh) and the two bikes of the
     * SIMPLE demo (EBCC0 and EBCC1).
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
        company1 = new EBCompany("EBBike");
        location1 = new Location(10, 5);
        location2 = new Location(10, 11);
        location3 = new Location(14, 16);
        location4 = new Location(8, 4);
        station0 = new BikeStation("CC00", location1);
        station1 = new BikeStation("CC01", location2);
        station2 = new BikeStation("CC02", location3);
        station3 = new BikeStation("CC03", location4);
        station0.addDock(new Dock("CC00_003", 80, 0.8f));
        station1.addDock(new Dock("CC01_003", 80, 0.8f));
        station2.addDock(new Dock("CC02_003", 80, 0.8f));
        station3.addDock(new Dock("CC03_003", 80, 0.8f));
        company1.addBikeStation(station0);
        company1.addBikeStation(station1);
        company1.addBikeStation(station2);
        company1.addBikeStation(station3);
        location5 = new Location(10, 13);
        location6 = new Location(1, 1);
        location7 = new Location(19, 19);
        bike0 = new ElectricBike(company1, location5, location6, "EBCC0", 15);
        bike1 = new ElectricBike(company1, location4, location7, "EBCC1", 30);
    }

    /**
     * Tears down the test fixture.
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
        // No teardown required for ElectricBike tests
    }

    /**
     * Checks that a new bike is created with the right values: full battery,
     * no recharges, no cost, no idle steps and no recharging location.
     */
    @Test
    public void testCreation()
    {
        assertEquals("EBCC0", bike0.getIdBike());
        assertEquals(15, bike0.getBatteryCapacity());
        assertEquals(15, bike0.getBatteryLevel());
        assertEquals(0, bike0.getChargesCount());
        assertEquals(0.0, bike0.getChargesCost(), 0.01);
        assertEquals(0, bike0.getIdleCount());
        assertEquals(0, bike0.getWsCharged());
        assertEquals(true, bike0.getLocation().equals(location5));
        assertEquals(true, bike0.getTargetLocation().equals(location6));
        assertNull(bike0.getRechargingLocation());
        assertEquals(0, bike0.getDocks().size());
    }

    /**
     * Checks that enoughBattery says if the bike can arrive to its target
     * location with its battery (5 wh for each step).
     */
    @Test
    public void testEnoughBattery()
    {
        assertEquals(false, bike0.enoughBattery());
        Location location8 = new Location(1, 3);
        ElectricBike bike2 = new ElectricBike(company1, location8, location6, "EBCC2", 15);
        assertEquals(true, bike2.enoughBattery());
    }

    /**
     * Checks that distanceToTheTargetLocation returns the number of steps
     * from the location of the bike to its target location.
     */
    @Test
    public void testDistanceToTheTargetLocation()
    {
        assertEquals(12, bike0.distanceToTheTargetLocation());
        assertEquals(15, bike1.distanceToTheTargetLocation());
    }

    /**
     * Checks that calculateRoute goes directly to the target location if the
     * bike has enough battery, and to the best station if it does not.
     */
    @Test
    public void testCalculateRoute()
    {
        bike0.calculateRoute();
        Location location9 = bike0.getRechargingLocation();
        assertEquals("10-11", location9.toString());
        Location location8 = new Location(1, 3);
        ElectricBike bike2 = new ElectricBike(company1, location8, location6, "EBCC2", 15);
        bike2.calculateRoute();
        assertNull(bike2.getRechargingLocation());
    }

    /**
     * Checks that calculateRechargingPosition chooses the best station that
     * the bike can reach (not the station where the bike is now).
     */
    @Test
    public void testCalculateRechargingPosition()
    {
        bike0.calculateRechargingPosition();
        Location location9 = bike0.getRechargingLocation();
        assertEquals("10-11", location9.toString());
        bike1.calculateRechargingPosition();
        Location location10 = bike1.getRechargingLocation();
        assertEquals("10-5", location10.toString());
    }

    /**
     * Checks that getStringRoute returns the route of the bike, with the
     * station only if the bike has to recharge.
     */
    @Test
    public void testGetStringRoute()
    {
        assertEquals("10-13 -> 1-1", bike0.getStringRoute());
        bike0.calculateRoute();
        assertEquals("10-13 -> 10-11 -> 1-1", bike0.getStringRoute());
    }

    /**
     * Checks that recharge fills the battery in the first free dock of the
     * station and updates the cost, the wh, the recharges and the docks.
     */
    @Test
    public void testRecharge()
    {
        ElectricBike bike2 = new ElectricBike(company1, location2, location6, "EBCC2", 15);
        bike2.reduceBatteryLevel();
        assertEquals(10, bike2.getBatteryLevel());
        bike2.recharge(1);
        assertEquals(15, bike2.getBatteryLevel());
        assertEquals(1, bike2.getChargesCount());
        assertEquals(4.0, bike2.getChargesCost(), 0.01);
        assertEquals(5, bike2.getWsCharged());
        assertEquals(1, bike2.getDocks().size());
    }

    /**
     * Checks that incrementCharges adds 1 to the number of recharges.
     */
    @Test
    public void testIncrementCharges()
    {
        bike0.incrementCharges();
        bike0.incrementCharges();
        assertEquals(2, bike0.getChargesCount());
    }

    /**
     * Checks that incrementChargesCost adds each cost to the total cost.
     */
    @Test
    public void testIncrementChargesCost()
    {
        bike0.incrementChargesCost(8.0f);
        bike0.incrementChargesCost(4.0f);
        assertEquals(12.0, bike0.getChargesCost(), 0.01);
    }

    /**
     * Checks that act moves the bike one step, recharges it when it arrives
     * to its station, and leaves it idle when it cannot go anywhere.
     */
    @Test
    public void testAct()
    {
        bike0.calculateRoute();
        bike0.act(0);
        assertEquals("10-12", bike0.getLocation().toString());
        assertEquals(10, bike0.getBatteryLevel());
        bike0.act(1);
        assertEquals("10-11", bike0.getLocation().toString());
        assertEquals(1, bike0.getChargesCount());
        assertEquals(15, bike0.getBatteryLevel());
        bike0.act(2);
        assertEquals("10-11", bike0.getLocation().toString());
        assertEquals(1, bike0.getIdleCount());
    }

    /**
     * Checks that reduceBatteryLevel takes away 5 wh and that the battery
     * never goes below 0.
     */
    @Test
    public void testReduceBatteryLevel()
    {
        bike0.reduceBatteryLevel();
        assertEquals(10, bike0.getBatteryLevel());
        bike0.reduceBatteryLevel();
        bike0.reduceBatteryLevel();
        bike0.reduceBatteryLevel();
        assertEquals(0, bike0.getBatteryLevel());
    }

    /**
     * Checks that two bikes are equal if they have the same ID, and that a
     * bike is not equal to a bike with another ID or to null.
     */
    @Test
    public void testEquals()
    {
        ElectricBike same = new ElectricBike(company1, location6, location7, "EBCC0", 30);
        assertEquals(true, bike0.equals(same));
        assertEquals(false, bike0.equals(bike1));
        assertEquals(false, bike0.equals(null));
    }

    /**
     * Checks that two equal bikes (same ID) have the same hash code.
     */
    @Test
    public void testHashCode()
    {
        ElectricBike same = new ElectricBike(company1, location6, location7, "EBCC0", 30);
        assertEquals(65755688, bike0.hashCode());
        assertEquals(65755688, same.hashCode());
    }

    /**
     * Checks that toString returns the information of the bike with the
     * format of the output of the simulation, before and after calculating
     * the route.
     */
    @Test
    public void testToString()
    {
        assertEquals("(ElectricBike: EBCC0, 15wh, 15wh, 0, 0.0€, 0, 10-13, 1-1)", bike0.toString());
        bike0.calculateRoute();
        assertEquals("(ElectricBike: EBCC0, 15wh, 15wh, 0, 0.0€, 0, 10-13, 10-11, 1-1)", bike0.toString());
    }
}