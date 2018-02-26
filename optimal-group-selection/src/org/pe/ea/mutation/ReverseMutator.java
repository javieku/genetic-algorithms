package org.pe.ea.mutation;

import org.pe.ea.chromosome.Chromosome;
import org.pe.ea.chromosome.Gen;
import org.pe.ui.Main;

/**
 * Mutación por inversión
 * 
 * */

public class ReverseMutator<T> implements Mutator<T>{

	@Override
	public boolean mutation(Chromosome<T> c) {
		Gen<T>[] genes = c.getGenes();
		
		Gen<T> aux;
		int p1 = Main.randomGenerator.nextInt(genes.length);
		int p2 = Main.randomGenerator.nextInt(genes.length);
		
		int posIni = Math.min(p1, p2);
		int n = Math.abs(p2 - p1);
		int j = 0;
		for(int i = posIni; i < posIni + n/2; i++){
			aux = genes[i];
			genes[i] = genes[(posIni + n - 1) - j];
			genes[(posIni + n - 1) - j] = aux;
			j++;
		}
		
		return true;
	}

}
