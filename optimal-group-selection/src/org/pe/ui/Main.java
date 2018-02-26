package org.pe.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.pe.ea.EvolutionaryAlgorithm;
import org.pe.ea.Params;
import org.pe.ea.chromosome.Chromosome;
import org.pe.ea.chromosome.ChromosomePermutation;
import org.pe.ea.mutation.Mutator;
import org.pe.extras.Classroom;

public class Main {
	
	public static Random randomGenerator;
	
	public static List<Chromosome<Integer>> initialize(Params params) {
		List<Chromosome<Integer>> population = new ArrayList<Chromosome<Integer>>();
		Chromosome<Integer> chro ;

		Mutator<Integer> mutator = params.getMutator();
		
		for (int i = 0; i < params.getPopSize(); i++) {
				chro = new ChromosomePermutation(params.getFunction(),mutator, Classroom.student.length, params.getGS(), params.getAlpha());
				chro.initialize();
				population.add(chro);
		}
			
		return population;
	}
	
	public static Results compute(Params params) {
		List<Chromosome<Integer>> elite = null;
		List<Chromosome<Integer>> population = initialize(params);
		EvolutionaryAlgorithm<Integer> ea = new EvolutionaryAlgorithm<Integer>(params, population);
		boolean elitism = params.getEliteSize() >= 0;
		
		ea.evaluatePopulation();
		ea.incNextGen();
		
		while(!ea.isFinished()){
			
			if(elitism){
				elite = new ArrayList<Chromosome<Integer>>();
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
		
		String result = Classroom.translate(ea.getTheBest().getGenes(), params.getGS());
		System.out.println(result);
		
		return ea.getResults();
	}
}
