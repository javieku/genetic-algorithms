package org.pe.ea.mutation;

import org.pe.ea.chromosome.Chromosome;
import org.pe.ea.chromosome.Gen;
import org.pe.ui.Main;

/**
 * Intercambia para cromosomas de contenido T dos genes al azar.
 * 
 * */

public class InterchangeMutator<T> implements Mutator<T>{
	@Override
	public boolean mutation(Chromosome<T> c) {
		Gen<T>[] genes = c.getGenes();
		
		// se genera un numero aleatorio en [0 1)
		int pos1 = Main.randomGenerator.nextInt(genes.length);
		int pos2 = Main.randomGenerator.nextInt(genes.length);
		
		// swap
		Gen<T> aux = genes[pos1];
		genes[pos1] = genes[pos2];
		genes[pos2] = aux;
		
		return true;
	}
}
