package org.pe.ea.chromosome;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.pe.ea.mutation.Mutator;

public class ChromosomePermutation extends Chromosome<Integer>{

	private int n; // numero de elementos de la permutacion
	private int m; // define el tamaño de las n-tuplas
	private int g; // numero de tuplas
	
	private FEvaluation fEv;
	
	private double alpha;
	
	public ChromosomePermutation(FEvaluation fEv, Mutator<Integer> mutator, int n, int m, double alpha) {
		super(mutator);
		 this.n = n;
		 this.m = m;
		 this.alpha = alpha;
		 this.fEv = fEv;
		 g = n/m;
	}

	@Override
	public double[] phenotype() {
		double[] result = new double[1];
		return result;
	}

	/**
	 * Utiliza el método evaluate de la función de evaluación seleccionada en el interfaz
	 * */
	@Override
	public double evaluate() {
		if(fEv != null)
			return fEv.evaluate(this);
		else
			return -1;
	}

	public int phenotype(int i, int j) {
		return genes[i*m + j].get(0);
	}


	@Override
	public Object clone() {
		ChromosomePermutation chro = new ChromosomePermutation(fEv, mutator, n, m, alpha);
		chro.aptitude = this.aptitude;
		chro.adaptation = this.adaptation;
		chro.punctuation = this.punctuation; 
		chro.accum_punct = this.accum_punct; 
		chro.chromosomeLenght = this.chromosomeLenght;
		chro.genes = Arrays.copyOf(genes, genes.length);
		
		return chro;
	}

	/**
	 * Crea un array de enteros cuyos elementos coinciden con el indice y aplicamos Fisher&Yates shuffle
	 * 
	 * */
	@SuppressWarnings("unchecked")
	@Override
	public void initialize() {
		
		genes = (Gen<Integer>[]) Array.newInstance(Gen.class, n);
		
		for(int i = 0; i < n; i++){
			genes[i] = new Gen<Integer>();
			genes[i].addElement(i);
			chromosomeLenght++;
		}
		
		shuffle(genes);
		
		aptitude = evaluate();
	}
	
	public int getM() { return m; }
	
	public int getG() { return g; }
	
	public double getAlpha() { return alpha; }
}
