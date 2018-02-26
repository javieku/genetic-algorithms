package org.pe.ea.selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.pe.ea.chromosome.Chromosome;
import org.pe.ui.Main;

/**
 * Torneo determinístico se itera N veces la toma de 3 cromosomas de la población al azar 
 * y se elige el mejor.
 *  
 * */
public class DeterministicTournament<T> implements Selector<T>{

	private double size;
	
	public DeterministicTournament(double size){
		this.size = size;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Chromosome<T>> selection(List<Chromosome<T>> population) {
		int competitor;	
		List<Chromosome<T>> selCompetitors = new ArrayList<Chromosome<T>>();//seleccionados para competir
		List<Chromosome<T>> nextPopulation = new ArrayList<Chromosome<T>>();
		for(int i = 0; i < population.size(); i++){
			for(int j = 0; j < size; j++){
				competitor = Main.randomGenerator.nextInt(population.size());
				selCompetitors.add(population.get(competitor));
			}
			Collections.sort(selCompetitors);
			nextPopulation.add((Chromosome<T>)selCompetitors.get(0).clone());
		}
		return  nextPopulation;
	}

}
