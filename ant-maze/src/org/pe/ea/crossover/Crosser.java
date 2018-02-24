package org.pe.ea.crossover;

import org.pe.ea.chromosome.Chromosome;


/**
 * Cualquier técnica de cruce que se desee añadir al algoritmo tiene que implementar esta interfaz.
 * 
 * */
public interface Crosser {

	public void crossover(Chromosome chro1, Chromosome chro2);
	
}
