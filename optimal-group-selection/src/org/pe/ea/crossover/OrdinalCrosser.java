package org.pe.ea.crossover;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.pe.ea.chromosome.Chromosome;
import org.pe.ea.chromosome.Gen;
import org.pe.ui.Main;

/**
 * Implementa cruce ordinal para cualquier par de cromosomas de contenido T
 * 
 */
public class OrdinalCrosser<T> implements Crosser<T> {

	private OnePointCrosser<Integer> crosser;
	
	public OrdinalCrosser(){
		crosser = new OnePointCrosser<Integer>();
	}
	
	@Override
	public void crossover(Chromosome<T> h1, Chromosome<T> h2) {
		Chromosome<Integer> h1Ord = chromosomeToOrdinal(h1);
		Chromosome<Integer> h2Ord = chromosomeToOrdinal(h2);
		
		crosser.crossover(h1Ord, h2Ord);
		
		Chromosome<T> h1Crossed = ordTochro(h1,h1Ord);
		Chromosome<T> h2Crossed = ordTochro(h2,h2Ord);
			
		h1.setGenes(h1Crossed.getGenes());
		h2.setGenes(h2Crossed.getGenes());
		
		h1.setAptitude(h1.evaluate());
		h2.setAptitude(h2.evaluate());
	}

	@SuppressWarnings("unchecked")
	private Chromosome<Integer> chromosomeToOrdinal(Chromosome<T> chro){
		Chromosome<Integer> res =  (Chromosome<Integer>) chro.clone();
		Gen<Integer> gen;
		Gen<T>[] genes = chro.getGenes();
		List<T> list = new ArrayList<T>();
		int pos;
		int p = Main.randomGenerator.nextInt(chro.getNumGenes());
		for(int i = 0; i < chro.getNumGenes(); i++){
			list.add(chro.getGenes()[p].get(0));
			p = (p + 1) % chro.getNumGenes();
		}
		int lSize = list.size();
		for(int i = 0; i < lSize; i++){
			pos = findPos(list,genes[i].get(0));
			list.remove(pos);
			gen = new Gen<Integer>();
			gen.addElement(pos);
			res.getGenes()[i] = gen;
		}
		return res;
	}
	
	private int findPos(List<T> list, T t) {
		int res = 0;
		boolean found = false;
		T aux;
		Iterator<T> it = list.iterator();
		while(!found && it.hasNext()){
			aux = it.next();
			found = aux.equals(t);
			res++;
		}
		if(found)
			res--;
		else
			res = -1;
		return res;
	}

	@SuppressWarnings("unchecked")
	private Chromosome<T> ordTochro(Chromosome<T> chro, Chromosome<Integer> chroCrossed){
		Chromosome<T> res =  (Chromosome<T>) chro.clone();
		Gen<T> gen;
		List<T> list = new ArrayList<T>();
		T elem;
		for(int i = 0; i < chro.getNumGenes(); i++){
			list.add(chro.getGenes()[i].get(0));
		}
		int lSize = list.size();
		for(int i = 0; i < lSize; i++){
			int aux = chroCrossed.getGenes()[i].get(0);
			elem = list.get(aux);
			list.remove(aux);
			
			gen = new Gen<T>();
			gen.addElement(elem);
			res.getGenes()[i] = gen;
		}
		return res;
	}
}