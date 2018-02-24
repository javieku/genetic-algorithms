package org.pe.ea.selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.pe.ea.chromosome.Chromosome;
import org.pe.ui.Main;

/**
 * Torneo probabilístico se itera N veces la toma de 3 cromosomas de la población al azar 
 * y se elige el mejor.
 *  
 * */
public class ProbabilisticTournament implements Selector {

	private double p;
	private int size;
	
	public ProbabilisticTournament(double p, int size){
		this.p = p;
		this.size = size;
	}
	
	@Override
	public List<Chromosome> selection(List<Chromosome> population) {
		int competitor;	
		Chromosome selected;
		List<Chromosome> selCompetitors = new ArrayList<Chromosome>();//seleccionados para competir
		List<Chromosome> nextPopulation = new ArrayList<Chromosome>();
		for(int i = 0; i < population.size(); i++){
			for(int j = 0; j < size; j++){
				competitor = Main.randomGenerator.nextInt(population.size());
				selCompetitors.add(population.get(competitor));
			}
			Collections.sort(selCompetitors);
			
			// Genera número al azar entre [0,1] si es mayor que p añade el mejor sino el peor 
			double prob = Main.randomGenerator.nextDouble();
			if(prob > p)
				selected = (Chromosome)selCompetitors.get(0).clone();
			else
				selected = (Chromosome)selCompetitors.get(size-1).clone();
			
			nextPopulation.add(selected);
			
			selCompetitors.clear();
		}
		return  nextPopulation;
	}

}