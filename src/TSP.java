package berlinTSP;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

/*
 * Experiment Harness
 * takes parameters from user and runs the GA
 *  output is written to files and separated based
 *  on experiment params, avg fitness, best fitness,
 *  and best solution
 * random seed is generated from system time
 */

public class TSP {
	
	private int mapSize;
	private int popSize;
	private int maxGen;
	private int xoverType;
	private double xoverRate;
	private double mutRate;
	private long rngSeed;
	
	Scanner sc = new Scanner(System.in);
	Writer fw;
	
	CityMap berlin = new CityMap();
	GA tspGA;
	Population generation;
	
	// holds best and avg fitness scores for each generation
	private double[] genBestFit;
	private double[] genAvgFit;
	
	// hold best fitness score and route overall
	private double bestFitness;
	private int[] bestRoute;
	
	
	public TSP() {
		rngSeed = System.currentTimeMillis();
		setupRun(); // get input from user
		createMap(); // create initial map object as read from file
		tspGA = new GA(popSize, mapSize, rngSeed); // init the GA
		generation = tspGA.initialGen(berlin); // generate random initial pop'n
		genBestFit = new double[maxGen];
		genAvgFit = new double [maxGen];
		
		// run the GA
		for (int gen = 0; gen < maxGen; gen++) {
			Population nextGeneration = new Population();
			int pop = popSize;
				
			// create new generation from current one
			while (pop > 0) {
				Tour parent1 = tspGA.tournamentSelect(generation);
				Tour parent2 = tspGA.tournamentSelect(generation);
				Population offspring = new Population();
				
				while (parent1 == parent2) {
					parent2 = tspGA.tournamentSelect(generation);
				}
				
				if (tspGA.rng.nextInt(100) > xoverRate) { // chance of no xover
					offspring.addIndividual(parent1);
					offspring.addIndividual(parent2);
				} else { // xover to obtain 2 new children
					if (xoverType == 1) {
						offspring = tspGA.uox(parent1, parent2);
					} else {
						offspring = tspGA.ox(parent1, parent2);
					}
				}
					
				// add children to new generation
				nextGeneration.addIndividual(offspring.getTour(0));
				nextGeneration.addIndividual(offspring.getTour(1));

				pop -= 2; // decrease population count by 2
			}
			
			// mutate some members before next generation
			for (int mut = 0; mut < (popSize * mutRate); mut++) {
				int mutIndex = tspGA.rng.nextInt(52);
				Tour mutTour = nextGeneration.getTour(mutIndex);
				Tour mutChild = tspGA.mutate(mutTour);
			}
			generation = nextGeneration;
			// save best and avg fitness scores for generation
			Tour genFittest = generation.getFittest();
			genBestFit[gen] = genFittest.getFitness();
			genAvgFit[gen] = generation.avgFitness();
		}
		
		// end of run get best fitness score and corresponding route
		Tour solution = generation.getFittest();
		bestFitness = solution.getFitness();
		bestRoute = solution.getRoute();
		
		printOutput(); // print output to file
		

	}
	
	// ask user for GA params
	private void setupRun() {
		System.out.println("TSP Genetic Algorithm: Please specify parameters.");
		System.out.println("Population Size: ");
		popSize = sc.nextInt();
		System.out.println("Number of Generations: ");
		maxGen = sc.nextInt();
		System.out.println("Available Crossover Types: ");
		System.out.println("\t1. Uniform order crossover");
		System.out.println("\t2. Order crossover");
		System.out.println("Select Crossover Type: ");
		xoverType = sc.nextInt();
		System.out.println("Crossover Rate (0-100): ");
		xoverRate = sc.nextInt();
		System.out.println("Mutation Rate (0-100): ");
		mutRate = (sc.nextInt() / 100.0);
	}
	
	/*
	 * createMap creates cities as read from text file and
	 * stores them in a CityMap object
	 */
	private void createMap() {
		
		String line;
		String[] citySize;
		String[] cityData;
		
		try {
			Scanner scf = new Scanner(new File("berlin52.tsp"));
			
			line = scf.nextLine();
			
			// fastfwd to map size
			while (!line.startsWith("DIMENSION")) {
				line = scf.nextLine();
			}
			// extract number of cities
			citySize = line.split(" ");
			mapSize = Integer.parseInt(citySize[1]);
			
			// fastfwd to city data
			while (!line.startsWith("1")) {
				line = scf.nextLine();
			}
			// extract city data
			while(!line.startsWith("EOF")) {
				cityData = line.split(" ");
				berlin.addCity(new City(cityData[0], cityData[1], cityData[2]));
				line = scf.nextLine();
			}
			
			scf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	
	}
	
	// write data to file
	public void printOutput() {
		System.out.println("Enter a filename to write data: ");
		String diskFile =  sc.next();
		try {
			// save experiment data
			fw = new FileWriter(diskFile + "-expData");
			fw.write("Random Seed: " + Long.toString(rngSeed) +"\n");
			fw.write("Population Size: " + Integer.toString(popSize) +"\n");
			fw.write("# of Generations: " + Integer.toString(maxGen) +"\n");
			if (xoverType == 1) {
				fw.write("Crossover Type: Uniform order crossover\n");
			} else {
				fw.write("Crossover Type: Order crossover\n");
			}
			fw.write("Crossover Rate: " + Double.toString(xoverRate) +"\n");
			fw.write("Mutation Rate: " + Double.toString(mutRate) +"\n");
			fw.close();
			
			// save best fit generation data
			fw = new FileWriter(diskFile + "-genBestFit");
			for (int i = 0; i < genBestFit.length; i++) {
				fw.write(Double.toString(genBestFit[i]) +"\n");
			}
			fw.close();
			
			// save avg fit generation data
			fw = new FileWriter(diskFile + "-genAvgFit");
			for (int i = 0; i < genAvgFit.length; i++) {
				fw.write(Double.toString(genAvgFit[i]) +"\n");
			}
			fw.close();
			
			// save bestfit data
			fw = new FileWriter(diskFile + "-solutionData");
			fw.write(Double.toString(bestFitness));
			fw.write("\nRoute:\n");
			for (int i = 0; i < bestRoute.length; i++) {
				fw.write(Integer.toString(bestRoute[i]) + "\n");
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

	// run it
	public static void main(String args[]) {
		new TSP();
	}
	
}
