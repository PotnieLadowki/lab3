import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This class stores the basic state necessary for the A* algorithm to compute a
 * path across a map.  This state includes a collection of "open waypoints" and
 * another collection of "closed waypoints."  In addition, this class provides
 * the basic operations that the A* pathfinding algorithm needs to perform its
 * processing.
 **/
public class AStarState
{
    /** This is a reference to the map that the A* algorithm is navigating. **/
    private Map2D map;
    
    private Map<Location, Waypoint> openWayPoints = new HashMap<Location, Waypoint>();
    private Map<Location, Waypoint> closedWayPoints = new HashMap<Location, Waypoint>();


    /**
     * Initialize a new state object for the A* pathfinding algorithm to use.
     **/
    public AStarState(Map2D map)
    {
        if (map == null)
            throw new NullPointerException("map cannot be null");

        this.map = map;
    }

    /** Returns the map that the A* pathfinder is navigating. **/
    public Map2D getMap()
    {
        return map;
    }

    /**
     * This method scans through all open waypoints, and returns the waypoint
     * with the minimum total cost.  If there are no open waypoints, this method
     * returns <code>null</code>.
     **/
    public Waypoint getMinOpenWaypoint()
    {
    	if (numOpenWaypoints() == 0) return null;
    	
    	@SuppressWarnings("rawtypes")
		Set openWaypointKeys = openWayPoints.keySet();
    	@SuppressWarnings("rawtypes")
		Iterator i = openWaypointKeys.iterator();
    	
    	Waypoint best = null;
    	float bestCost = Float.MAX_VALUE;
    	
    	while (i.hasNext()) {
    		Location location = (Location) i.next();
    		Waypoint waypoint = openWayPoints.get(location);
    		float waypointTotalCost = waypoint.getTotalCost();
    		
    		if (waypointTotalCost < bestCost) {
    			best = openWayPoints.get(location);
    			bestCost = waypointTotalCost;
    		}
    	}
    	
    	return best;
    }

    /**
     * This method adds a waypoint to (or potentially updates a waypoint already
     * in) the "open waypoints" collection.  If there is not already an open
     * waypoint at the new waypoint's location then the new waypoint is simply
     * added to the collection.  However, if there is already a waypoint at the
     * new waypoint's location, the new waypoint replaces the old one <em>only
     * if</em> the new waypoint's "previous cost" value is less than the current
     * waypoint's "previous cost" value.
     **/
    public boolean addOpenWaypoint(Waypoint newWP)
    {
    	Location location = newWP.getLocation();
    	
    	if (openWayPoints.containsKey(location)) {
    		Waypoint currentWayPoint = openWayPoints.get(location);
    		
    		if (newWP.getPreviousCost() < currentWayPoint.getPreviousCost()) {
				openWayPoints.put(location, newWP);	
				return true;
    		}    		
    		
    		return false;
    	}
    	
    	openWayPoints.put(location, newWP);
    	return true;
    }


    /** Returns the current number of open waypoints. **/
    public int numOpenWaypoints()
    {
    	return openWayPoints.size();
    }


    /**
     * This method moves the waypoint at the specified location from the
     * open list to the closed list.
     **/
    public void closeWaypoint(Location loc)
    {
        Waypoint waypoint = openWayPoints.remove(loc);
        closedWayPoints.put(loc, waypoint);
    }

    /**
     * Returns true if the collection of closed waypoints contains a waypoint
     * for the specified location.
     **/
    public boolean isLocationClosed(Location loc)
    {
    	return closedWayPoints.containsKey(loc);
    }
}