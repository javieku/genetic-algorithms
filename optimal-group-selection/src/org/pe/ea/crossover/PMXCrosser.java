package org.pe.ea.crossover;

import org.pe.ea.chromosome.Chromosome;
import org.pe.ea.chromosome.Gen;
import org.pe.ui.Main;

/**
 * Implementación del cruce PMX clásico.
 * 
 * */

public class PMXCrosser<T> implements Crosser<T> {

	@SuppressWarnings("unchecked")
	@Override
	public void crossover(Chromosome<T> h1, Chromosome<T> h2) {
		int chroSize = h1.getNumGenes();
		Gen<T>[] genesh1 = h1.getGenes();
		Gen<T>[] genesh2 = h2.getGenes();
		Gen<T> aux;
		int a = 0, b = 0;
		while(a == b){
			a = Main.randomGenerator.nextInt(chroSize);
			b = Main.randomGenerator.nextInt(chroSize);
		}
		int crossover_point1 = Math.min(a,b);
		
		int crossover_point2 = Math.max(a,b);
		
		Chromosome<T> p1 = (Chromosome<T>)h1.clone();
		
		Chromosome<T> p2 = (Chromosome<T>)h2.clone();
		
		// Inicializamos los hijos
		for(int i = 0; i< chroSize; i++){
			if(i < crossover_point1 || i >= crossover_point2){
				genesh1[i] = new Gen<T>();
				
				genesh2[i] = new Gen<T>();
			}
		}
		
		// Swap entre puntos de cruce
		for (int j = crossover_point1; j < crossover_point2; j++){
				aux = genesh1[j];
				genesh1[j] = genesh2[j];
				genesh2[j] = aux;
		}
		
		// Si el valor no está en la subcadena intercambiada se copia igual
 		// Swap entre puntos de cruce
		for (int j = 0; j < chroSize; j++){
			if(j < crossover_point1 || j >= crossover_point2){
				if(isCompatible(genesh1, p1.getGenes()[j]) == -1){
					genesh1[j] = p1.getGenes()[j];
				}
				if(isCompatible(genesh2, p2.getGenes()[j]) == -1){
					genesh2[j] = p2.getGenes()[j];
				}
			}
		}
		
		// Si el valor está en la subcadena intercambiada se sustituye por el valor que tenga dicha cadena en el otro padre
		completeConflictiveDescendents(p1.getGenes(), genesh1);
		
		completeConflictiveDescendents(p2.getGenes(), genesh2);
		
		h1.setAptitude(h1.evaluate());
		h2.setAptitude(h2.evaluate());
	}
	
	/**	 
	 * Busca el hueco en el hijo, después toma el valor correspodiente en el padre y se busca en el hijo la posición conflicto
	 * 
	 * */
	private void completeConflictiveDescendents(Gen<T>[] p,  Gen<T>[] h) {
		int pos = 0;
		for(int i = 0; i < p.length; i++){
			if(h[i].getBits().isEmpty()){
				pos = isCompatible(h, p[i]);
				h[i] = findPair(h, p, pos);
			}
		}
	}
	
	/**
	 * Se busca el valor no conflictivo para el hueco
	 * 
	 * */
	private Gen<T> findPair(Gen<T>[] h, Gen<T>[] p, int pos) {
		int res = pos;
		int auxPos = isCompatible(h, p[pos]);
		while(auxPos != -1){
			res = auxPos;
			auxPos = isCompatible(h, p[auxPos]);
			
		}
		return p[res];
	}

	/**
	 * Buscamos la posición del gen conflictivo
	 * 
	 * */
	private int isCompatible(Gen<T>[] genes, Gen<T> gen) {
		boolean found = false;
		int i = 0 ;
		while (i < genes.length && !found){
			found = genes[i].equals(gen);
			i++;
		}
		if(found){
			i--;
			return i;
		}else
			return -1;
	}

}
