package org.pe.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.pe.ea.EvolutionaryAlgorithm;
import org.pe.ea.Params;
import org.pe.ea.chromosome.Chromosome;
import org.pe.ea.chromosome.EvaluationAntProgram;
import org.pe.ea.mutation.Mutator;
import org.pe.extra.Map;

public class Main {
	
	public static Random randomGenerator;
	
	public static List<Chromosome> initialize(Params params) {
		List<Chromosome> population = new ArrayList<Chromosome>();
		Chromosome chro ;

		Mutator mutator = params.getMutator();
		
		for (int i = 0; i < params.getPopSize(); i++) {
				chro = new Chromosome(params.getFunction(), mutator);
				chro.initialize(params.getMinDepth(), params.getMaxDepth());
				population.add(chro);
		}
			
		return population;
	}
	
	public static Results compute(Params params) {
		List<Chromosome> elite = null;
		List<Chromosome> population = initialize(params);
		EvolutionaryAlgorithm ea = new EvolutionaryAlgorithm(params, population);
		boolean elitism = params.getEliteSize() >= 0;
		
		ea.evaluatePopulation();
		ea.incNextGen();
		
		while(!ea.isFinished()){
			
			if(elitism){
				elite = new ArrayList<Chromosome>();
				elite = ea.getElite((int) (population.size()*params.getEliteSize()));
			}
			
			ea.selection();
			ea.reproduction();
			ea.mutation();
			
			if(elitism)
				ea.addElite(elite);
		
			ea.evaluatePopulation();

			ea.incNextGen();
		}
		
		Chromosome c = ea.getTheBestOne();
		c.evaluate();
		EvaluationAntProgram ep = (EvaluationAntProgram) c.getFEv();
		Map m = ep.getResults();
		m.print(new File("res.txt"));
		System.out.println(c);
		
		return ea.getResults();
	}
}
