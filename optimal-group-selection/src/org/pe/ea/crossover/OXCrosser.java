package org.pe.ea.crossover;

import org.pe.ea.chromosome.Chromosome;
import org.pe.ea.chromosome.Gen;
import org.pe.ui.Main;

/**
 * Cruce por orden a nivel de gen.(i.e. intercambio directo de genes entre cromosomas)
 * 
 * */

public class OXCrosser<T> implements Crosser<T>{

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
		
		// Completamos los hijos siguiendo el orden relativo de los padres
		completeDescendents(crossover_point2, crossover_point1, p1.getGenes(), genesh1);
		
		completeDescendents(crossover_point2, crossover_point1, p2.getGenes(), genesh2);
		
		h1.setAptitude(h1.evaluate());
		h2.setAptitude(h2.evaluate());
	}
	
	/**	 
	 *  Realiza el llenado de huecos de los hijos conservando el orden de los padres
	 * 
	 * */
	private void completeDescendents(int initialPos, int finalPos, Gen<T>[] p, Gen<T>[] h) {
		int i = initialPos;
		int j = initialPos;
		while (j != finalPos){
			// Si corresponde se toma el gen del padre y se asigna al hijo
			if (isCompatible(h, p[i])){
				h[j] = p[i];
				j = (j + 1) % h.length;
			}
			i = (i + 1) % p.length;
		}	
	}

	/**
	 * Comprobamos si no esta el gen del padre en el hijo ya.
	 * 
	 * */
	private boolean isCompatible(Gen<T>[] genes, Gen<T> gen) {
		boolean found = false;
		int i = 0 ;
		while (i < genes.length && !found){
			found = genes[i].equals(gen);
			i++;
		}
		return !found;
	}
}
