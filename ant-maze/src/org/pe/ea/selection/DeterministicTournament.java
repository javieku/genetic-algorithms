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
public class DeterministicTournament implements Selector {

	private double size;
	
	public DeterministicTournament(double size){
		this.size = size;
	}
	
	@Override
	public List<Chromosome> selection(List<Chromosome> population) {
		int competitor;	
		List<Chromosome> selCompetitors = new ArrayList<Chromosome>();//seleccionados para competir
		List<Chromosome> nextPopulation = new ArrayList<Chromosome>();
		for(int i = 0; i < population.size(); i++){
			for(int j = 0; j < size; j++){
				competitor = Main.randomGenerator.nextInt(population.size());
				selCompetitors.add(population.get(competitor));
			}
			Collections.sort(selCompetitors);
			nextPopulation.add((Chromosome)selCompetitors.get(0).clone());
			selCompetitors.clear();
		}
		return  nextPopulation;
	}

}
