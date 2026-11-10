import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Collections;

/**
 * Models an Electric Vehicle Charging Station.
 * A charging station contains multiple {@link Dock} units and manages 
 * the recharging process for electric bikes in the city.
 * 
 * @author Alvaro
 * @version 2026.11.16
 */
public class BikeStation
{
    private String idStation;
    private Location location;
    private List<Dock> docks;

    /**
     * Default constructor for objects of class BikeStation.
     * Initializes an empty station with default values.
     */
    public BikeStation()
    {
        this.idStation = "";
        this.location = null;
        this.docks = new ArrayList<>();
    }

    /**
     * Parameterized constructor for objects of class BikeStation.
     * 
     * @param id The unique identifier of the station (e.g., CC01).
     * @param location The physical location of the station in the city grid.
     */
    public BikeStation(String id, Location location)
    {
        this.idStation = id;
        this.location = location;
        this.docks = new ArrayList<>();
    }
    
    // --- GETTERS & SETTERS (Requisito Mínimo) ---

    /**
     * Devuelve la localización donde está la estación de bicicletas.
     * @return The location object representing the station's coordinates.
     */
    public Location getLocation()
    {
        return location;
    }
    
    /**
     * Establece la localización de la estación.
     * @param location The new location to assign to this station.
     */
    public void setLocation(Location location) 
    {
        this.location = location;
    }

    /**
     * Devuelve la colección docks.
     * @return A list containing the station's docks.
     */
    public List<Dock> getDocks()
    {
        return docks;
    }
    
    /**
     * Establece la lista de dársenas (docks) de la estación.
     * @param docks La nueva lista de docks.
     */
    public void setDocks(List<Dock> docks)
    {
        this.docks = docks;
    }

    /**
     * Devuelve el identificador de la estación.
     * @return A string representing the station's ID.
     */
    public String getId()
    {
        return idStation;
    }
    
    /**
     * Establece el identificador de la estación.
     * @param idStation The new ID string.
     */
    public void setIdStation(String idStation) 
    {
        this.idStation = idStation;
    }

    // --- FUNCIONALIDAD REQUERIDA Y MÉTODOS EXTRAS ---

    /**
     * Permite devolver un objeto de tipo Dock de la colección docks según su índice.
     * @param i The index of the dock to retrieve.
     * @return The Dock object at the specified index, or null if out of bounds.
     */
    public Dock getDock(int i)
    {
        if(i >= 0 && i < docks.size()){
            return docks.get(i);
        }
        return null;
    } 

    /**
     * Retrieves the first free {@link Dock} found at the station.
     * Cumple con el requisito extra de utilizar correctamente un Iterator y
     * el requisito de calidad de utilizar un bucle while para iteración indefinida.
     * 
     * @return A free {@link Dock}, or null if all docks are occupied.
     */
    public Dock getFreeDock()
    {
        Iterator<Dock> it = docks.iterator();
        while(it.hasNext()) {
            Dock actualDock = it.next();
            if(actualDock.isFree()) {
                return actualDock;
            }
        }
        return null;
    }

    /**
     * Permite añadir un nuevo dock de tipo Dock a la colección docks y mantiene 
     * la estructura ordenada de acuerdo al criterio exigido mediante un Comparator.
     * 
     * @param dock The new dock unit to add.
     */
    public void addDock(Dock dock)
    {
        if(dock != null) {
            docks.add(dock);
            // Requisito extra: Utiliza el método de Collections adecuado junto con el comparador entregado.
            Collections.sort(docks, new DockComparator());
        }
    }
    
    /**
     * Calcula el número total de bicicletas eléctricas recargadas en todos los docks.
     * Cumple con el requisito extra de usar un Iterator al menos dos veces.
     * 
     * @return The total number of bikes recharged in this station.
     */
    public int getTotalBikesRecharged()
    {
        int total = 0;
        Iterator<Dock> it = docks.iterator();
        while(it.hasNext()) {
            total += it.next().getNumerEVRecharged();
        }
        return total;
    }

    // --- MÉTODOS AUXILIARES ---

    /**
     * Generates a string representation of the BikeStation matching the exact 
     * simulation output format required for the demo.
     * 
     * @return A string representing the station.
     */
    @Override
    public String toString()
    {
        return "(BikeStation: " + idStation + ", " + getTotalBikesRecharged() + ", " + location.toString() + ")";
    }
    
    /**
     * Implementa la igualdad de contenido para determinar si dos estaciones son la misma.
     * Dos estaciones son idénticas si tienen el mismo idStation.
     * 
     * @param o El objeto a comparar con esta estación.
     * @return true si el objeto proporcionado es una BikeStation con el mismo idStation.
     */
    @Override
    public boolean equals(Object o) 
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BikeStation that = (BikeStation) o;
        return Objects.equals(idStation, that.idStation);
    }

    /**
     * Genera un código hash para la estación basándose en su idStation.
     * 
     * @return El valor del hash de la estación.
     */
    @Override
    public int hashCode() 
    {
        return Objects.hash(idStation);
    }
}
