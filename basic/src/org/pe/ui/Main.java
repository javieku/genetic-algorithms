package org.pe.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.pe.GeneticAlgorithm.Chromosome;
import org.pe.GeneticAlgorithm.ChromosomeFunction;
import org.pe.GeneticAlgorithm.GeneticAlgorithm;
import org.pe.GeneticAlgorithm.Params;
import org.pe.functions.Function;
import org.pe.functions.Function1;
import org.pe.functions.Function2;
import org.pe.functions.Function3;
import org.pe.functions.Function4;
import org.pe.functions.Function5;

public class Main {
	
	public static Random randomGenerator;
	
	public static List<Chromosome> initialize(Params params) {
		List<Chromosome> population = new ArrayList<Chromosome>();
		Chromosome chro;
		
		Function f;
		switch (params.getFunction()) {
			case 1:
				f = new Function1();
				break;
			case 2:
				f = new Function2();
				break;
			case 3:
				f = new Function3();
				break;
			case 4:
				f = new Function4();
				break;
			case 5:
				f = new Function5(params.getN());
				break;
			default:
				f = null;
				break;
		}
		
		if (f != null) {
			for (int i = 0; i < params.getPopSize(); i++) {
				chro = new ChromosomeFunction(f, params.getTol());
				chro.initialize();
				population.add(chro);
			}
			
			return population;
		}
		else
			return null;
	}
	
	
	public static Results compute(Params params) {
		List<Chromosome> elite = null;
		List<Chromosome> population = initialize(params);
		GeneticAlgorithm ga = new GeneticAlgorithm(params, population);
		boolean elitism = params.getEliteSize() >= 0;
		
		ga.evaluatePopulation();
		ga.incNextGen();
		
		while(!ga.isFinished()){
			
			if(elitism){
				elite = new ArrayList<Chromosome>();
				elite = ga.getElite((int) (population.size()*params.getEliteSize()));
			}
			
			ga.selection();
			ga.reproduction();
			ga.mutation();
			
			if(elitism)
				ga.addElite(elite);
		
			ga.evaluatePopulation();

			ga.incNextGen();
		}
		
		System.out.println(ga.getTheBest());
		
		return ga.getResults();
	}
}
