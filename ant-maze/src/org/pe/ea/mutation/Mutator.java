package org.pe.ea.mutation;

import org.pe.ea.chromosome.Chromosome;

/**
 * Cualquier técnica de mutación que se desee añadir al algoritmo tiene que implementar esta interfaz.
 * 
 * */

public interface Mutator {
	
	public boolean mutation(Chromosome c);
	
}
