package org.pe.ui;

import org.pe.ea.chromosome.Chromosome;

public class Results {
	
	private Chromosome[] bestOnes;
	private Chromosome bestOne;
	private double[] bestApt;
	private double[] avgApt;
	private int numGen;
	
	public Results() {}

	public Chromosome[] getBestOnes() {
		return bestOnes;
	}
	
	public Chromosome getBestOne() {
		return bestOne;
	}

	public double[] getBestApt() {
		return bestApt;
	}

	public double[] getAvgApt() {
		return avgApt;
	}
	
	public int getNumGen() {
		return numGen;
	}

	public void setBestOnes(Chromosome[] bestOnes) {
		this.bestOnes = bestOnes;
	}
	
	public void setBestOne(Chromosome bestOne) {
		this.bestOne = bestOne;
	}

	public void setBestApt(double[] bestApt) {
		this.bestApt = bestApt;
	}

	public void setAvgApt(double[] avgApt) {
		this.avgApt = avgApt;
	}
	
	public void setNumGen(int numGen) {
		this.numGen = numGen;
	}
}
