package org.pe.ea.crossover;

import org.pe.ea.chromosome.Chromosome;
import org.pe.ea.chromosome.Gen;
import org.pe.ui.Main;

/**
 * Implementación del cruce por orden con posiciones prioritarias
 * 
 * */
public class OXAltCrosser<T> implements Crosser<T>{

	private int numAttempts;
	
	public OXAltCrosser(int numAttempts){
		this.numAttempts = numAttempts;
	}
	
	@SuppressWarnings("unchecked")
	public void crossover(Chromosome<T> h1, Chromosome<T> h2) {
		int chroSize = h1.getNumGenes();
		Gen<T>[] genesh1 = h1.getGenes();
		Gen<T>[] genesh2 = h2.getGenes();
	
		Chromosome<T> p1 = (Chromosome<T>)h1.clone();
		Chromosome<T> p2 = (Chromosome<T>)h2.clone();
		
		// Inicializamos los hijos
		for(int i = 0; i< chroSize; i++){
				genesh1[i] = new Gen<T>();
				genesh2[i] = new Gen<T>();
		}
	
		// Swap entre puntos de cruce
		for (int i = 0; i < numAttempts; i++){
				int j =  Main.randomGenerator.nextInt(chroSize);
				genesh1[j] = p2.getGenes()[j];
				genesh2[j] = p1.getGenes()[j];
		}
		
		// Completamos los hijos
		completeDescendents(p1.getGenes(), genesh1);
		
		completeDescendents(p2.getGenes(), genesh2);
		
		h1.setAptitude(h1.evaluate());
		h2.setAptitude(h2.evaluate());
	}
	
	/**	 
	 *  Realiza el llenado de huecos de los hijos conservando el orden de los padres
	 * 
	 * */
	private void completeDescendents(Gen<T>[] p, Gen<T>[] h) {
		int i = 0;
		int finalPos;
		int j = h.length;
		boolean found = false;
		
		// Busca el ultimo hueco donde se pueda asignar un gen
		while (!found && j > 0){
			j--;
			found = h[j].getBits().isEmpty();
		}	
		finalPos = j;
		
		j = 0;
		while (j != finalPos){
			// Si corresponde se toma el gen del padre y se asigna al hijo
			if (isCompatible(h, p[i])){
				j = nextPosFree(h,j);
				
				h[j] = p[i];
			}
			
			i = (i + 1) % p.length;
		}	
	}

	private int nextPosFree(Gen<T>[] h, int currentPos) {
		int j = currentPos;
		while(!h[j].getBits().isEmpty()){ 
			j = (j + 1) % h.length; 
		}	
		return j;
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
