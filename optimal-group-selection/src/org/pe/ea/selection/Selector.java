package org.pe.ea.selection;

import java.util.List;

import org.pe.ea.chromosome.Chromosome;

/**
 * Cualquier técnica de selección que se desee añadir al algoritmo tiene que implementar esta interfaz.
 * 
 * */
public interface Selector<T> {
	
	public List<Chromosome<T>> selection(List<Chromosome<T>> population);
	
}
