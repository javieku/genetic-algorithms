package org.pe.ea.mutation;

import org.pe.ea.chromosome.Chromosome;
import org.pe.ea.chromosome.Gen;
import org.pe.ui.Main;

/**
 * Implementa mutator realiza un flip de los bits de un gen dado
 * 
 * */

public class BitMutator implements Mutator<Boolean> {
	
	private double prob_mutation;
	
	
	public BitMutator(double prob_mutation){
		this.prob_mutation = prob_mutation;
	}

	@Override
	public boolean mutation(Chromosome<Boolean> c) {
		boolean mutated = false;
		Gen<Boolean>[] genes = c.getGenes();
		for(Gen<Boolean> gen:genes){
			mutated |= bitmutation(gen,prob_mutation);
		}
		return mutated;
	}
	
	private boolean bitmutation(Gen<Boolean> gen, double prob_mutation){
		boolean mutated = false;
		double prob;

		int i = 0; 
		for(Boolean bit :  gen.getBits()){
			// se genera un numero aleatorio en [0 1)
			prob = Main.randomGenerator.nextDouble();
	
			// mutan los genes con prob < mutation_prob
			if(prob < prob_mutation){
				gen.getBits().set(i,!bit);
				
				mutated = true;
			}
			i++;
		}
		
		return mutated;
	}

}
