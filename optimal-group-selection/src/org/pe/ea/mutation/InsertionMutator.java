package org.pe.ea.mutation;

import org.pe.ea.chromosome.Chromosome;
import org.pe.ea.chromosome.Gen;
import org.pe.ui.Main;

/**
 * 
 * Mutación por inserción simple (1 sólo desplazamiento)
 * 
 * */

public class InsertionMutator<T> implements Mutator<T>{

	@Override
	public boolean mutation(Chromosome<T> c) {
		
		Gen<T>[] genes = c.getGenes();
		
		Gen<T> aux;
		
		int sel = Main.randomGenerator.nextInt(genes.length);
		int dest = Main.randomGenerator.nextInt(genes.length);
		
		aux = genes[sel];
		
		if(sel < dest)
			for(int i = sel; i < dest; i++){
				genes[i] = genes[i + 1];
			}
		else
			for(int i = sel; i > dest; i--){
				genes[i] = genes[i - 1];
			}
		
		genes[dest] = aux;
		
		return true;
	}

}
