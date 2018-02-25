package org.pe.GeneticAlgorithm;

import org.pe.GeneticAlgorithm.GeneticAlgorithm.SelectionType;

public class Params {
	private int function;
	private int popSize;
	private int genNumber;
	private double pC;
	private double pM;
	private double tol;
	private int n;
	private boolean isMax;
	private double eliteSize;
	
	private SelectionType st;
	
	public Params(int function, int popSize, int genNumber, double pC, double pM, double tol, int n, SelectionType st, double eliteSize) {
		this.function = function;
		this.popSize = popSize;
		this.genNumber = genNumber;
		this.pC = pC;
		this.pM = pM;
		this.tol = tol;
		this.n = n;
		this.st = st;
		isMax = function == 1 || function == 2;
		this.eliteSize = eliteSize;
	}

	public int getFunction() {
		return function;
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

	public int getN() {
		return n;
	}
	
	public SelectionType getSt() {
		return st;
	}

	public boolean getMax() {
		return isMax;
	}

	public void setFunction(int function) {
		this.function = function;
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
		this.n = n;
	}
	
	public void setSt(SelectionType st) {
		this.st = st;
	}
}
