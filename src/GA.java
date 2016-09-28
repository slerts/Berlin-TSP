package berlinTSP;

import java.util.Random;

/*
 * The heart of the GA, 
 * initializes the 1st generation
 * creates children from 2 parents and a mask determined
 *  by type of crossover
 * uox and ox are initiated by creating random masks
 * tournament selection is used for picking individuals
 *  for crossover
 *  mutation is simple random swap of 2 cities in chromosome
 */

public class GA {
	
	int popSize;
	int mapSize;
	public Random rng;
	
	public GA(int popSize, int mapSize, long rngSeed) {
		this.popSize = popSize;
		this.mapSize = mapSize;
		rng = new Random(rngSeed);
	}
	
	// create random first generation
	public Population initialGen(CityMap map) {
		Population gen = new Population();
		
		
		for (int i = 0; i < popSize; i++) {
			CityMap newMap = new CityMap();
			int routeSize = 52;
			
			while (routeSize > 0) {
				int city = rng.nextInt(map.listSize());
				if (!newMap.hasCity(city)) {
					newMap.addCity(map.getCity(city));
					routeSize--;
				}
			}
			
			Tour newTour = new Tour(newMap, mapSize);
			gen.addIndividual(newTour);
			
		}
		
		return gen;
	}
	
	/*
	 * takes two parents and a mask and returns an object containing 2 children
	 */
	public Population makeChildren(Tour parent1, Tour parent2, boolean[] mask) {
		
		CityMap p1 = parent1.getMap();
		CityMap p2 = parent2.getMap();
		CityMap c1 = new CityMap(mapSize);
		CityMap c2 = new CityMap(mapSize);
		
		int childCnt = 0;
		int parCnt = 0;
		
		for (int i = 0; i < mapSize; i++) {
			if (mask[i]) {
				c1.addCity(p1.getCity(i), i);
				c2.addCity(p2.getCity(i), i);
			}
		}
		
		while (childCnt < mapSize && parCnt < mapSize) {
			// if current location is null and child doesn't already exit
			if (c1.getCity(childCnt) == null && !c1.hasCity(p2.getCity(parCnt))) {
				c1.setCity(p2.getCity(parCnt), childCnt);
				childCnt++;
				parCnt++;
			} else if (c1.getCity(childCnt) != null) { // if child loc. is not null
				childCnt++;
			} else if (c1.hasCity(p2.getCity(parCnt))) { // if child already has city
				parCnt++;
			}
			
			
		}
		
		//reset counters
		childCnt = 0;
		parCnt = 0;
		
		while (childCnt < mapSize && parCnt < mapSize) {
			// if current location is null and child doesn't already exit
			if (c2.getCity(childCnt) == null && !c2.hasCity(p1.getCity(parCnt))) {
				c2.setCity(p1.getCity(parCnt), childCnt);
				childCnt++;
				parCnt++;
			} else if (c2.getCity(childCnt) != null) { // if child loc. is not null
				childCnt++;
			} else if (c2.hasCity(p1.getCity(parCnt))) { // if child already has city
				parCnt++;
			}
			
		}
		
		Tour child1 = new Tour(c1, mapSize);
		Tour child2 = new Tour(c2, mapSize);
		Population offspring = new Population();
		offspring.addIndividual(child1);
		offspring.addIndividual(child2);
		
		return offspring;
	}
	
	// select k individuals at random and get fittest
	public Tour tournamentSelect(Population currentGen) {
		Population gen = new Population();
		int selectInd;
		int k = 3;
		
		while (k > 0) {
			selectInd = rng.nextInt(52);
			gen.addIndividual(currentGen.getTour(selectInd));
			k--;
		}
		
		return gen.getFittest();
	}
	
	// uniform order crossover
	public Population uox(Tour p1, Tour p2) {
		boolean[] mask = new boolean[52];
		
		// create random mask
		for (int i = 0; i < mask.length; i++) {
			if (rng.nextInt(2) == 1)
				mask[i] = true;
		}
		
		// create offspring from uox mask
		return makeChildren(p1, p2, mask);
		
	}
	
	// order crossover
	public Population ox(Tour p1, Tour p2) {
		boolean[] mask = new boolean[52];
		// rng is buffered to land between 4 - 46 to ensure no replication occurs via crossover
		int cut1 = rng.nextInt(42) + 4;
		int cut2 = rng.nextInt(42) + 4;
		
		// create mask based on above random cuts
		for (int i = cut1; i <= cut2; i++) {
			mask[i] = true;
		}
		
		// create offspring from ox mask
		return makeChildren(p1, p2, mask);
		
	}
	
	// mutate by swapping 2 random cities
	public Tour mutate(Tour child) {
		int gene1 = rng.nextInt(52);
		int gene2 = rng.nextInt(52);
		City swap;
		
		swap = child.getMap().getCity(gene1);
		child.getMap().setCity(child.getMap().getCity(gene2), gene1);
		child.getMap().setCity(swap, gene2);;
		return child;
	}
}
