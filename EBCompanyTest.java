import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link EBCompany}. It checks with JUnit the methods of the
 * class that return a value or change the state of the company (except the
 * getters and setters of the fields).
 * @author Los Especuladores
 * @version 2026.11.12
 */
public class EBCompanyTest
{
    private EBCompany company1;
    private Location location1;
    private Location location2;
    private BikeStation station0;
    private BikeStation station1;
    private Location location3;
    private Location location4;
    private ElectricBike bike0;
    private ElectricBike bike1;

    /**
     * Default constructor for test class EBCompanyTest.
     */
    public EBCompanyTest()
    {
    }

    /**
     * Sets up the test fixture: an empty company, two stations and two bikes
     * that we use in the tests.
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
        company1 = new EBCompany("EBBike");
        location1 = new Location(10, 5);
        location2 = new Location(10, 11);
        station0 = new BikeStation("CC00", location1);
        station1 = new BikeStation("CC01", location2);
        location3 = new Location(10, 13);
        location4 = new Location(1, 1);
        bike0 = new ElectricBike(company1, location3, location4, "EBCC0", 15);
        bike1 = new ElectricBike(company1, location1, location4, "EBCC1", 30);
    }

    /**
     * Tears down the test fixture.
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
        // No teardown required for EBCompany tests
    }

    /**
     * Checks that addElectricBike adds the bikes to the company and keeps
     * them in order by their ID.
     */
    @Test
    public void testAddElectricBike()
    {
        company1.addElectricBike(bike1);
        company1.addElectricBike(bike0);
        assertEquals(2, company1.getNumberOfBikes());
        java.util.ArrayList<ElectricBike> arrayLis1 = (java.util.ArrayList<ElectricBike>)company1.getSubscribedBikes();
        ElectricBike first = arrayLis1.get(0);
        assertEquals("EBCC0", first.getIdBike());
    }

    /**
     * Checks that addBikeStation adds the stations to the company and keeps
     * them in order by their ID.
     */
    @Test
    public void testAddBikeStation()
    {
        company1.addBikeStation(station1);
        company1.addBikeStation(station0);
        assertEquals(2, company1.getNumberOfStations());
        java.util.ArrayList<BikeStation> arrayLis1 = (java.util.ArrayList<BikeStation>)company1.getStations();
        BikeStation first = arrayLis1.get(0);
        assertEquals("CC00", first.getId());
    }

    /**
     * Checks that getBikeStation finds a station by its ID, and returns null
     * if there is no station with this ID.
     */
    @Test
    public void testGetBikeStationById()
    {
        company1.addBikeStation(station0);
        company1.addBikeStation(station1);
        BikeStation found = company1.getBikeStation("CC01");
        assertEquals(true, found.equals(station1));
        assertNull(company1.getBikeStation("CC09"));
    }

    /**
     * Checks that getBikeStation finds a station by its location, and returns
     * null if there is no station in this location.
     */
    @Test
    public void testGetBikeStationByLocation()
    {
        company1.addBikeStation(station0);
        company1.addBikeStation(station1);
        BikeStation found = company1.getBikeStation(location2);
        assertEquals(true, found.equals(station1));
        assertNull(company1.getBikeStation(location3));
    }

    /**
     * Checks that getNumberOfBikes counts the bikes of the company.
     */
    @Test
    public void testGetNumberOfBikes()
    {
        assertEquals(0, company1.getNumberOfBikes());
        company1.addElectricBike(bike0);
        assertEquals(1, company1.getNumberOfBikes());
    }

    /**
     * Checks that getNumberOfStations counts the stations of the company.
     */
    @Test
    public void testGetNumberOfStations()
    {
        assertEquals(0, company1.getNumberOfStations());
        company1.addBikeStation(station0);
        assertEquals(1, company1.getNumberOfStations());
    }

    /**
     * Checks that reset removes all the bikes and stations of the company.
     */
    @Test
    public void testReset()
    {
        company1.addElectricBike(bike0);
        company1.addBikeStation(station0);
        company1.reset();
        assertEquals(0, company1.getNumberOfBikes());
        assertEquals(0, company1.getNumberOfStations());
    }

    /**
     * Checks that two companies are equal if they have the same name, and
     * that a company is not equal to a company with another name or to null.
     */
    @Test
    public void testEquals()
    {
        EBCompany same = new EBCompany("EBBike");
        EBCompany other = new EBCompany("Other");
        assertEquals(true, company1.equals(same));
        assertEquals(false, company1.equals(other));
        assertEquals(false, company1.equals(null));
    }

    /**
     * Checks that two equal companies (same name) have the same hash code.
     */
    @Test
    public void testHashCode()
    {
        EBCompany same = new EBCompany("EBBike");
        assertEquals(2038434355, company1.hashCode());
        assertEquals(2038434355, same.hashCode());
    }

    /**
     * Checks that toString returns the name, the number of bikes and the
     * number of stations of the company.
     */
    @Test
    public void testToString()
    {
        assertEquals("(EBCompany: EBBike, 0, 0)", company1.toString());
        company1.addElectricBike(bike0);
        company1.addBikeStation(station0);
        assertEquals("(EBCompany: EBBike, 1, 1)", company1.toString());
    }
}