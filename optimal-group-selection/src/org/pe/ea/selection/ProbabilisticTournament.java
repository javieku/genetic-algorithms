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
public class ProbabilisticTournament<T> implements Selector<T>{

	private double p;
	private int size;
	
	public ProbabilisticTournament(double p, int size){
		this.p = p;
		this.size = size;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Chromosome<T>> selection(List<Chromosome<T>> population) {
		int competitor;	
		Chromosome<T> selected;
		List<Chromosome<T>> selCompetitors = new ArrayList<Chromosome<T>>();//seleccionados para competir
		List<Chromosome<T>> nextPopulation = new ArrayList<Chromosome<T>>();
		for(int i = 0; i < population.size(); i++){
			for(int j = 0; j < size; j++){
				competitor = Main.randomGenerator.nextInt(population.size());
				selCompetitors.add(population.get(competitor));
			}
			Collections.sort(selCompetitors);
			
			// Genera n�mero al azar entre [0,1] si es mayor que p a�ade el mejor sino el peor a la poblaci�n intermedia
			int prob = Main.randomGenerator.nextInt();
			if(prob > p)
				selected = (Chromosome<T>)selCompetitors.get(0).clone();
			else
				selected = (Chromosome<T>)selCompetitors.get(size-1).clone();
			
			nextPopulation.add(selected);
		}
		return  nextPopulation;
	}

}