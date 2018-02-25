package org.pe.GeneticAlgorithm;

import java.util.Iterator;
import java.util.List;

import org.pe.functions.Function;
import org.pe.ui.Main;
	
public class ChromosomeFunction extends Chromosome{

	private double[] xmin; // máximos y mínimos de cada variable.
	private double[] xmax; 
	
	private int variablesN; // número de variables de la función
	
	private Function function; // función a aplicar 
	
	private double tolerance; // discretización del intervalo(s) en el que se define la función
	
	private int[] genesLength; // longitud de cada segmento asociado a cada variable en todo el cromosoma
	
	private int chromosomeLength; // longitud total del cromosoma
	
	public ChromosomeFunction(Function function, double tolerance) {
		super();
		double aux;
		this.variablesN = function.getVariablesN();
		this.xmax = function.getRanges()[1];
		this.xmin = function.getRanges()[0];
		this.chromosomeLength = 0;
		this.genesLength = new int[variablesN];
		for(int i = 0; i < variablesN; i++){
			aux = Math.log(1 + ((xmax[i] - xmin[i]) / tolerance)) / Math.log(2); 
			this.genesLength[i] = (int)aux + 1;
		}
		for(int i = 0; i < variablesN; i++){
			chromosomeLength += genesLength[i];
		}
		this.function = function;
		this.tolerance = tolerance;
	}

	private double bin2dec(List<Boolean> genes, int initialPos, int segmentLength) {
		Boolean gen;
		double result = 0;
		int j = 0;
		for(int i = initialPos; i < initialPos + segmentLength; i++){
			gen = genes.get(i);
			if(gen)
				result += Math.pow(2, j);  
			j++;
		}
		return result;
	}

	@Override
	public double[] phenotype() {
		double[] value = new double[variablesN];
		int acc_range = 0;
		for(int i = 0; i < variablesN; i++){ 
			value[i] = (xmin[i] + (((xmax[i] - xmin[i]) *  bin2dec(genes, acc_range, genesLength[i])) / (Math.pow(2,genesLength[i]) - 1)));
			acc_range += genesLength[i]; 
		}
		return value;
	}
	
	@Override
	public double evaluate() {
		// calcula el fenotipo de la función, decodificamos el genotipo, la secuencia de bits
		double[] x = phenotype();
		
		// calculamos la función de evaluación del problema, la aptitud bruta
		return function.evaluate(x);
		
	}
	
	@Override
	public Object clone() {
		ChromosomeFunction chro = new ChromosomeFunction(function, tolerance);
		chro.aptitude = this.aptitude;
		chro.adaptation = this.adaptation;
		chro.punctuation = this.punctuation; 
		chro.accum_punct = this.accum_punct; 
		Iterator<Boolean> it = this.genes.iterator();
		Boolean gen;
		while(it.hasNext()){
			gen = it.next();
			chro.genes.add(gen.booleanValue());
		}	
		return chro;
	}

	@Override
	public void initialize() {
		for (int i = 0; i < chromosomeLength; i++) {
			 genes.add(Main.randomGenerator.nextBoolean());
		}
		this.aptitude = this.evaluate();
	}
	
	public String toString() {
		String result = super.toString();
		double[] x; 
		x = phenotype();
		for(int i = 0; i < variablesN; i++){ 
			result += " x"+ i + ":" + x[i]; 
		}
		return result;
	}
}
