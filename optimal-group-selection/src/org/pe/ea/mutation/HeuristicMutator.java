package org.pe.ea.mutation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.pe.ea.chromosome.Chromosome;
import org.pe.ea.chromosome.Gen;
import org.pe.ui.Main;
import org.pe.util.PermUtil;
import org.pe.util.SortingAlg;
import org.pe.util.UsefulMethods;

/**
 * Mutación heurística de n genes a permutar para obtener cada uno de los
 * cromosomas que se ordenaran conforme a HCcomparator.
 * 
 * */

public class HeuristicMutator<T> implements Mutator<T>{

	private int n;
	private boolean isMax;
	
	public HeuristicMutator(int n, boolean isMax){
		this.n = n;
		this.isMax = isMax;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean mutation(Chromosome<T> c) {
		Chromosome<T> auxC;
		// Genes originales
		Gen<T>[] genes = c.getGenes();
		// Contiene todos los cromosomas en función de las diferentes permutaciones de n genes
		List<Chromosome<T>> candidates = new ArrayList<Chromosome<T>>();
		// Almacena las permutaciones de genes
		Integer[] lAux;
		int aux = 0;
		// Contiene los índices de los genes a permutar
		Integer[] buffer = new Integer[n];
		
		// Inicializo el array
		for(int i = 0; i < n; i++){
			buffer[i] = -1;
		}
		
		// Se toman n genes al azar
		for(int i = 0; i < n; i++){
			while(repeated(buffer, aux)){
				aux = Main.randomGenerator.nextInt(genes.length);
			}
			buffer[i] = aux;
		}
		
		PermUtil<Integer> permutator = new PermUtil<Integer>(buffer);
	
		// Generamos los candidatos a genes mutados
		int nperm = UsefulMethods.factorial(buffer.length);
		for(int i = 0; i < nperm; i++) {
			lAux = permutator.nextwoRep();
			auxC = (Chromosome<T>) c.clone();
			mutateChro(buffer,auxC,lAux);
			auxC.setAptitude(auxC.evaluate());
			candidates.add(auxC);		  
		}
		
		// Ordenamos para tomar el mejor
		Collections.sort(candidates,new HMComparator());
		
		
		c.setGenes(candidates.get(0).getGenes());
			
		return true;
	}

	private boolean repeated(Integer[] buffer, int aux) {
		boolean found = false;
		int i = 0;
		while(!found && i < buffer.length){
			found = (aux == buffer[i]);
			i++;
		}
		return found;
	}

	@SuppressWarnings("unchecked")
	private void mutateChro(Integer[] buffer, Chromosome<T> auxC, Integer[] lAux) {
		
		SortingAlg.quicksort(buffer, 0, buffer.length-1);
		
		Gen<T>[] genes = (Gen<T>[]) Array.newInstance(Gen.class, auxC.getGenes().length);
		
		System.arraycopy(auxC.getGenes(), 0, genes, 0, auxC.getGenes().length);
		
		int j = 0;
		for(int i : lAux){
			auxC.getGenes()[buffer[j]] = genes[i];
			j++;
		}
	}
	
	/**
	 * El orden natural (Interface comparable) de los cromosomas viene dado por su función de adaptación
	 * pero como en este ámbito no se puede calcular se hace necesario el uso de una clase que implemente
	 * un orden en función de los valores de la aptitud.
	 * 
	 * */
	 private class HMComparator implements Comparator<Chromosome<T>>{

		@Override
		public int compare(Chromosome<T> c1, Chromosome<T> c2) {
			double apt;
			if (isMax)
				 apt = c2.getAptitude() - c1.getAptitude();
			else
				 apt = c1.getAptitude() - c2.getAptitude();
			
			if (apt == 0)
				return 0;
			
			if (apt < 0)
				return -1;
			
			return 1;
		}
		
	}
}
