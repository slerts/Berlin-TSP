package berlinTSP;

import java.util.ArrayList;

/*
 * Population holds an entire generation of tours
 * can obtain the fittest tour in the generation as well
 * as calculate the average fitness for the generation
 */

public class Population {
	private ArrayList<Tour> population;
	
	public Population() {
		population = new ArrayList<Tour>();
	}
	
	public void addIndividual(Tour tour) {
		population.add(tour);
	}
	
	// return fittest tour
	public Tour getFittest() {
		Tour fittest = population.get(0);
		for (int i = 1; i < population.size(); i++) {
			if (population.get(i).getFitness() < fittest.getFitness()) {
				fittest = population.get(i);
			}
		}
		return fittest;
	}
	
	// return the average fitness for the whole population
	public double avgFitness() {
		double sumFit = 0;
		
		for (int i = 0; i < population.size() - 1; i++) {
			sumFit += population.get(i).getFitness();
		}
		
		return (sumFit / population.size());
	}
	
	public Tour getTour(int i) {
		return population.get(i);
	}
	

	
}
