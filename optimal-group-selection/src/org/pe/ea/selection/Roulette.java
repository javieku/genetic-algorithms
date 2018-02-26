package org.pe.ea.selection;

import java.util.ArrayList;
import java.util.List;

import org.pe.ea.chromosome.Chromosome;
import org.pe.ui.Main;

/**
 *  Ruleta clásica 
 *  
 *  Necesario que las puntuaciones de los individuos sean positivas para que funcione correctamente
 * */

public class Roulette<T> implements Selector<T>{

	@SuppressWarnings("unchecked")
	@Override
	public List<Chromosome<T>> selection(List<Chromosome<T>> population) {
		Chromosome<T> survivor_sel[];	//seleccionados para sobrevivir
		double prob; 				// probabilidad de seleccion
		int survivor_pos;	 		// posici�n del superviviente
		Chromosome<T> chro;
		List<Chromosome<T>> nextPopulation;
		int i = 0;
		
		survivor_sel = new Chromosome[population.size()];
		for(int j = 0; j < population.size(); j++){
			prob = Main.randomGenerator.nextDouble();
			survivor_pos = 0;
			while (survivor_pos < population.size() && prob > population.get(survivor_pos).getAccumulatedPunt() ) {
				survivor_pos++;
			}
			survivor_sel[i] = population.get(survivor_pos);
			i++;
		}

		nextPopulation = new ArrayList<Chromosome<T>>();
		// se genera la poblacion intermedia
		for(i = 0; i < population.size(); i++){
			chro = (Chromosome<T>) survivor_sel[i].clone();
			nextPopulation.add(chro);
		}
		
		return nextPopulation;
	}

}
