package org.pe.ea;

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
	private Selector<Integer> sel;
	private Crosser<Integer> crosser;
	private Mutator<Integer> mutator;
	
	// Parámetros propios del problema
	private double alpha;
	private int groupSize;
	private double beta;

	public Params(FEvaluation function, int popSize, int genNumber, double pC, double pM,double tol, Selector<Integer> sel, Mutator<Integer> mutator,
					Crosser<Integer> crosser, boolean isMax, double eliteSize, double alpha, int groupSize, double beta) {
		this.fEv = function;
		this.popSize = popSize;
		this.genNumber = genNumber;
		this.pC = pC;
		this.pM = pM;
		this.tol = tol;
		this.alpha = alpha;
		this.sel = sel;
		this.crosser = crosser;
		this.mutator = mutator;
		this.isMax = isMax;
		this.eliteSize = eliteSize;
		this.groupSize = groupSize;
		this.beta = beta;
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

	public double getAlpha() {
		return alpha;
	}
	
	public Selector<Integer> getSel() {
		return sel;
	}
	
	public Crosser<Integer> getCr() {
		return crosser;
	}

	public Mutator<Integer> getMutator() {
		return mutator;
	}
	
	public boolean getMax() {
		return isMax;
	}
	
	public int getGS() {
		return groupSize;
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

	public void setN(int n) {
		this.alpha = n;
	}
	
	public void setSel(Selector<Integer> sel) {
		this.sel = sel;
	}
}
