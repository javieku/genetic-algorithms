package org.pe.ea.selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.pe.ea.chromosome.Chromosome;
import org.pe.ui.Main;

/**
 * Método propio:
 * 
 * 	1 - Ordena la población según su adaptación.
 * 	2 - La divide en dos conjuntos h y l
 *  3 - Se toma al azar un individuo de una u otro conjunto según una probabilidad que depende del tamaño de h
 * 
 * */

public class HighLow implements Selector {

	private double m;
	
	public HighLow(double m) {
		this.m = m;
	}
	
	@Override
	public List<Chromosome> selection(List<Chromosome> population) {
		if(m != 0 && m != 1){
			List<Chromosome> h,l;
			int division;
			
			Collections.sort(population);
			
			division = (int)(population.size()*m);
			
			h = population.subList(0, division);
			l = population.subList(division, population.size());
			
			double p = 0.5;
			
			List<Chromosome> nextPopulation = new ArrayList<Chromosome>();
			
			int nh = 0;
			int nl = 0;
			for(int i = 0; i < population.size(); i++){
				double prob = Main.randomGenerator.nextDouble();
				if( prob < p ){
					nextPopulation.add((Chromosome) h.get(nh).clone());
					nh = (nh + 1) % h.size();
				}else{
					nextPopulation.add((Chromosome) l.get(nl).clone());
					nl = (nl + 1) % l.size();
				}
			}
			
			return nextPopulation;
		}else
			return population;
	}

}
