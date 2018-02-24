package org.pe.ea.selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.pe.ea.chromosome.Chromosome;

/**
 * Linear Ranking
 * 
 * */

public class Ranking  implements Selector{

	private double beta;
	
	
	public Ranking(double beta){
		this.beta = beta;
	}
	
	public List<Chromosome> selection(List<Chromosome> population) {
		int numDescendants;
		double[] ranking;
		double segment;
		double p; 
		List<Chromosome> nextPopulation = new ArrayList<Chromosome>();
		int popSize = population.size();

		// Ordenamos la población
		Collections.sort(population);
        
		nextPopulation.add((Chromosome) population.get(0).clone());
		nextPopulation.add((Chromosome) population.get(0).clone());
        numDescendants = 2;
        ranking = rankPopulation(popSize);
        segment = ranking[ranking.length - 1];
        while (numDescendants < popSize) {
            p = (double) (Math.random() * segment);
            if (p <= ranking[0]) {
                // Seleccionado el primer individuo
            	nextPopulation.add((Chromosome) population.get(0).clone());
                numDescendants++;
            } else {
                for (int i = 1; i < popSize; i++) {
                    // Se selecciona el elemento i-ésimo
                	if (p > ranking[i - 1] && p <= ranking[i]) {
                		nextPopulation.add((Chromosome) population.get(i).clone());
                        numDescendants++;
                    }
                }
            }
        }
        
        return nextPopulation;
	}
	
    private double[] rankPopulation(int populationSize){
    	double[] fitnessSegments = new double[populationSize];
    	for(int i=0 ; i<fitnessSegments.length ; i++){
    	double probOfIth = (double)i/populationSize;
    	probOfIth = probOfIth*2*(beta-1);
    	probOfIth = beta - probOfIth;
    	probOfIth = (double)probOfIth*((double)1/populationSize);
    	if(i!=0)
    		fitnessSegments[i] = fitnessSegments[i-1] + probOfIth;
    	else
    		fitnessSegments[i] = probOfIth;
    	}
    	return fitnessSegments;
    }
    


}
