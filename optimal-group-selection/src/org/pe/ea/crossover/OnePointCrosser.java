package org.pe.ea.crossover;

import org.pe.ea.chromosome.Chromosome;
import org.pe.ea.chromosome.Gen;
import org.pe.ui.Main;

/**
 * Cruce monopunto sobre los bits de los genes que componen un cromosoma.
 * (i.e. Crosser<T> - - > Gen compuesto de bits - - > puede haber mezcla de bits de genes)
 * 
 * */

public class OnePointCrosser<T> implements Crosser<T>{

	@Override
	public void crossover(Chromosome<T> chro1, Chromosome<T> chro2) {
		int crossover_point = Main.randomGenerator.nextInt(chro1.getSize());
		Gen<T>[] genes = chro1.getGenes();
		
		// buscamos el gen en el que empieza a realizarse el cruce
		int acc_size = 0;
		if(genes.length > 0)
			acc_size = genes[0].getSize();
		boolean found = crossover_point < acc_size;
		int posGen = 0;
		while(!found && posGen < chro1.getNumGenes()){
			found = crossover_point < acc_size;
			if(!found){	
				posGen++;
				acc_size += genes[posGen].getSize();	
			}
		}
		
		if(found){
			
			// primer gen solo cruce parcial de bits
			acc_size -= genes[posGen].getSize();
			int posIni = crossover_point - acc_size;
			Gen<T> gc1 = chro1.getGenes()[posGen];
			Gen<T> gc2 = chro2.getGenes()[posGen];
			
			crossover(gc1,gc2,posIni,gc1.getSize());
			
			// el resto hasta el final cruce completo de todos sus bits
			for (int j = posGen + 1; j < chro1.getNumGenes(); j++){
				// swap
				gc1 = chro1.getGenes()[j];
				gc2 = chro2.getGenes()[j];
				crossover(gc1,gc2,0,gc1.getSize());
			}
			
			chro1.setAptitude(chro1.evaluate());
			chro2.setAptitude(chro2.evaluate());
		}
	}
	
	private void crossover(Gen<T> genChro1, Gen<T> genChro2, int posIni, int posEnd) {
		T aux1;
		for (int i = posIni; i < posEnd; i++){
			// swap
			aux1 = genChro1.get(i);
			genChro1.set(i,genChro2.get(i));
			genChro2.set(i, aux1);
		}
	}
}
