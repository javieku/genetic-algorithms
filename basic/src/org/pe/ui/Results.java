package org.pe.ui;

import java.util.Collection;

import org.pe.GeneticAlgorithm.Chromosome;

public class Results {
	private Chromosome[] bestOnes;
	private double[] bestApt;
	private double[] avgApt;
	private int numGen;
	
	public Results() {}

	public Chromosome[] getBestOnes() {
		return bestOnes;
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
