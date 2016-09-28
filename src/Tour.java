package berlinTSP;


/*
 * Tours hold a path around the graph
 * They allow for calculating fitness (ie. distance)
 *  of the given route.
 */
public class Tour {
	private CityMap tour;
	int mapSize;

	
	public Tour(CityMap map, int mapSize) {
		tour = map;
		this.mapSize = mapSize;
	}
	
	// returns an array of the route taken by ID
	public int[] getRoute() {
		int[] route = new int[mapSize];
		for (int i = 0; i < mapSize; i++) {
			route[i] = tour.getCity(i).getID();
		}
		return route;
	}
	
	public CityMap getMap() {
		return tour;
	}
	
	public double getFitness() {
		float distance = 0;
		
		for (int i = 0; i < mapSize; i++) {
			City currentCity = tour.getCity(i);
			City nextCity;
			if (i+1 < mapSize) {
				nextCity = tour.getCity(i+1);
			} else {
				nextCity = tour.getCity(0);
			}
			distance += currentCity.distance(nextCity);
		}
		
		return distance;
	}
	
	
}
