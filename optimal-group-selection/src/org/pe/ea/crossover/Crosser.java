package org.pe.ea.crossover;

import org.pe.ea.chromosome.Chromosome;


/**
 * Cualquier técnica de cruce que se desee añadir al algoritmo tiene que implementar esta interfaz.
 * 
 * */
public interface Crosser<T> {

	public void crossover(Chromosome<T> chro1, Chromosome<T> chro2);
	
}
