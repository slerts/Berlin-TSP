package berlinTSP;

/*
 * Collection of test functions to ensure parts
 * worked along the way.
 * NOT PART OF ASSIGNMENT
 */

public class TestHarness {
	
	int mapSize = 52;
	
	/*
	 *  print contents of CityMap object
	 *  **** WORKS ****
	 */
	private void printMap(CityMap map) {
		for (int i = 0; i < map.listSize(); i++) {
			City city = map.getCity(i);
			System.out.println(city.getID() + " " + city.getX() + " " + city.getY() + " ");
		}
	}
	
	/*
	 *  test distances method of cities  (co-validates below)
	 *  **** WORKS **** 
	 */
	private void distanceTest(CityMap map) {
		double totalDelta = 0;
		for (int i = 0; i < map.listSize(); i++) {
			City c1 = map.getCity(i);
			City c2;
			if (i+1 < map.listSize()) {
				c2 = map.getCity(i+1);
			} else {
				c2 = map.getCity(0);
			}
			double delta = c1.distance(c2);
			System.out.println(c1.getID() + " " + c1.getX() + " " + c1.getY() + " ");
			System.out.println(c2.getID() + " " + c2.getX() + " " + c2.getY() + " ");
			System.out.println("Distance between " + c1.getID() + " and " + c2.getID() + ": " + delta);
			totalDelta += delta;
		}
		System.out.println("Total Distance: " + totalDelta);

	}
	
	/*
	 * test fitness calculation (co-validates above)
	 * **** WORKS ****
	 */
	private void fitnessTest(CityMap map) {
		Tour rawBerlin = new Tour(map, mapSize);
		System.out.println("Fitness Score: " + rawBerlin.getFitness());
	}
	
	
	/*
	 * test generation creation
	 * **** WORKS ****
	 */
	private void genTest(Population gen, int popSize) {
		
		System.out.println();
		for (int i = 0; i < popSize; i++) {
			int[] currentTour = gen.getTour(i).getRoute();
			for (int j = 0; j < currentTour.length; j++) {
				System.out.print(currentTour[j] + " ");
			}
			System.out.println("Fitness: " + gen.getTour(i).getFitness());
		}
	}
	
	/*
	 * tests tournament selection
	 * **** WORKS ****
	 * requires GA object
	 */
//	private void tournTest(Population gen) {
//		Tour tourFittest = tspGA.tournamentSelect(Gen);
//		System.out.println();
//		System.out.println(tourFittest.getFitness());
//		
//	}

}
