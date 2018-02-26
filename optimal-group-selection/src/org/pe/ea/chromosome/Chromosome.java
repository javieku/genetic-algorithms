package org.pe.ea.chromosome;
import org.pe.ea.mutation.Mutator;
import org.pe.ui.Main;


public abstract class Chromosome<T> implements Cloneable, Comparable<Chromosome<T>> {

	protected Gen<T>[] genes; 
	protected int chromosomeLenght;
	
	protected Mutator<T> mutator;

	protected double aptitude;
	protected double adaptation;
	protected double punctuation; 
	protected double accum_punct; 
	
	public Chromosome(Mutator<T> mutator) {
		aptitude = 0;
		punctuation = 0;
		accum_punct = 0;
		chromosomeLenght = 0;
		this.mutator = mutator;
	}

	public double getAccumulatedPunt() {
		return accum_punct;
	}

	public int getSize() {
		return chromosomeLenght;
	}
	
	public int getNumGenes() {
		return genes.length;
	}

	public Gen<T>[] getGenes() {
		return genes;
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
	
	public void setGenes(Gen<T>[] genes){
		this.genes = genes;
	}
	
	public void setAptitude(double aptitude) {
		this.aptitude = aptitude;
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

	public void mutation() {
		if(mutator.mutation(this)){
			this.aptitude = this.evaluate();
		}
	}

	public String toString() {
		String result = new String();
		for (int i = 0; i < genes.length; i++){
			result += genes[i];
		}
		result += " f:" + aptitude;
		return result;
	}
	
	public abstract double[] phenotype();
	public abstract double evaluate();
	public abstract Object clone();
	public abstract void initialize();
	
	public int compareTo(Chromosome<T> c) {
		double apt = c.getAdaptation() - getAdaptation();
		if (apt == 0)
			return 0;
		
		if (apt < 0)
			return -1;
		
		return 1;
	}
	
	/**
	* Desordena el array 'genes'. 
	* Para generar una permutación aleatoria de enteros, bastaría con que 
	* 'a' contuviese todos los enteros del 0 al N-1.
	*/
	public void shuffle(Gen<T>[] genes) {
		for (int i = genes.length-1; i >= 1; i--) {
			int j = Main.randomGenerator.nextInt(i+1); // un numero entre 0 e i, inclusive
			// intercambia a[i], a[j]
			Gen<T> aux = genes[j];
			genes[j] = genes[i];
			genes[i] = aux;
		}
	}
}
