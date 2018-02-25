package org.pe.GeneticAlgorithm;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.pe.ui.Main;


public abstract class Chromosome implements Cloneable, Comparable<Chromosome> {

	protected List<Boolean> genes; 

	protected double aptitude;
	protected double adaptation;
	protected double punctuation; 
	protected double accum_punct; 
	
	public Chromosome() {
		aptitude = 0;
		punctuation = 0;
		accum_punct = 0;
		genes = new ArrayList<Boolean>();
	}

	public double getAccumulatedPunt() {
		return accum_punct;
	}

	public int getSize() {
		return genes.size();
	}

	public double getAptitude() {
		return aptitude;
	}
	
	public double getAdaptation() {
		return adaptation;
	}
	
	public double getPunctuation() {
		return punctuation;
	}
	
	public void setAdaptation(double adaptation) {
		this.adaptation = adaptation;
	}
	
	public void setPunctuation(double punctuation) {
		this.punctuation = punctuation;
	}
	
	public void setAccPunct(double accum_punct) {
		this.accum_punct = accum_punct;
	}

	public void mutation(double mutation_prob) {
		boolean mutated = false;
		double prob;
		Boolean gen;
		
		Iterator<Boolean> it = genes.iterator();
		int i = 0; 
		while(it.hasNext()){
			gen = it.next();
			// se genera un numero aleatorio en [0 1)
			prob = Main.randomGenerator.nextDouble();
	
			// mutan los genes con prob < mutation_prob
			if(prob < mutation_prob){
				genes.set(i,!gen);
				
				mutated = true;
				
				if(mutated){
					this.aptitude = this.evaluate();
				}
			}
			i++;
		}
	}

	public void crossover(Chromosome chromosome, int crossover_point) {
		// realiza el cruce de los genes 
		// solo la parte que cambia con respecto a sus padres (que son ellos mismos... paradójico)
		for (int i = crossover_point; i < genes.size(); i++){
			// swap
			boolean aux1 = this.genes.get(i).booleanValue();
			this.genes.set(i, chromosome.genes.get(i));
			chromosome.genes.set(i, aux1);
		}
		this.aptitude = this.evaluate();
		chromosome.aptitude = chromosome.evaluate();
	}
	
	public String toString() {
		String result = new String();
		Iterator<Boolean> it = genes.iterator();
		Boolean gen;
		/*while(it.hasNext()){
			gen = it.next();
			if(gen)
				result += " 1";
			else
				result += " 0";
		}*/
		result += " f:" + aptitude;
		return result;
	}
	
	public abstract double[] phenotype();
	public abstract double evaluate();
	public abstract Object clone();
	public abstract void initialize();
	
	public int compareTo(Chromosome c) {
		double apt = c.getAdaptation() - getAdaptation();
		if (apt == 0)
			return 0;
		
		if (apt < 0)
			return -1;
		
		return 1;
	}
}
