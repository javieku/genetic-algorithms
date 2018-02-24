package org.pe.ea;

import org.pe.ea.EvolutionaryAlgorithm.TBloating;
import org.pe.ea.chromosome.FEvaluation;
import org.pe.ea.crossover.Crosser;
import org.pe.ea.mutation.Mutator;
import org.pe.ea.selection.Selector;


public class Params {
	
	// Parámetros generales del algoritmo
	private FEvaluation fEv;
	private int popSize;
	private int genNumber;
	private double pC;
	private double pM;
	private double tol;
	private boolean isMax;
	private double eliteSize;
	private Selector sel;
	private Crosser crosser;
	private Mutator mutator;
	private double beta;
	private TBloating tBloating;
	private int nTarpeian;
	
	// Problema concreto
	private int minDepth;
	private int maxDepth;

	public Params(FEvaluation function, int popSize, int genNumber, double pC, double pM,double tol, Selector sel, Mutator mutator,
					Crosser crosser, boolean isMax, double eliteSize, double beta, TBloating tBloating, int nTarpeian, int minDepth, int maxDepth) {
		this.fEv = function;
		this.popSize = popSize;
		this.genNumber = genNumber;
		this.pC = pC;
		this.pM = pM;
		this.tol = tol;
		this.sel = sel;
		this.crosser = crosser;
		this.mutator = mutator;
		this.isMax = isMax;
		this.eliteSize = eliteSize;
		this.beta = beta;
		this.tBloating = tBloating;
		this.nTarpeian = nTarpeian;
		
		this.minDepth = minDepth;
		this.maxDepth = maxDepth;
	}

	public FEvaluation getFunction() {
		return fEv;
	}

	public int getPopSize() {
		return popSize;
	}

	public double getEliteSize() {
		return eliteSize;
	}

	public void setEliteSize(double eliteSize) {
		this.eliteSize = eliteSize;
	}

	public int getGenNumber() {
		return genNumber;
	}

	public double getpC() {
		return pC;
	}

	public double getpM() {
		return pM;
	}

	public double getTol() {
		return tol;
	}
	
	public Selector getSel() {
		return sel;
	}
	
	public Crosser getCr() {
		return crosser;
	}

	public Mutator getMutator() {
		return mutator;
	}
	
	public boolean getMax() {
		return isMax;
	}
	
	public double getBeta() {
		return beta;
	}

	public void setFunction(FEvaluation function) {
		this.fEv = function;
	}

	public void setPopSize(int popSize) {
		this.popSize = popSize;
	}

	public void setGenNumber(int genNumber) {
		this.genNumber = genNumber;
	}

	public void setpC(double pC) {
		this.pC = pC;
	}

	public void setpM(double pM) {
		this.pM = pM;
	}

	public void setTol(double tol) {
		this.tol = tol;
	}
	
	public void setSel(Selector sel) {
		this.sel = sel;
	}

	public int getMinDepth() {
		return minDepth;
	}
	
	public int getMaxDepth() {
		return maxDepth;
	}

	public int getNTarpeian() {
		return nTarpeian;
	}

	public TBloating getTBloating() {
		return tBloating;
	}
}
