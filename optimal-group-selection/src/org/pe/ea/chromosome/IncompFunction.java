package org.pe.ea.chromosome;

import java.util.List;

import org.pe.extras.Classroom;

public class IncompFunction implements FEvaluation{

	private int incompatibilities;
	private double imbalance;
	
	@Override
	public double evaluate(Chromosome c) {
		ChromosomePermutation chro;
		double result = 0;
		
		if(c instanceof ChromosomePermutation)
			chro = (ChromosomePermutation) c;
		else
			return -1;
		
		imbalance(chro);
		
		incompatibilities(chro);
		
		result = (chro.getAlpha() * imbalance) + ((1-chro.getAlpha()) * incompatibilities);
		
		return result;
	}
	
	/**
	 * Calcula el desequilibrio por cada grupo de estudiantes
	 * 
	 * */
	private void imbalance(ChromosomePermutation c) {
		imbalance = 0;
		double partSum = 0;
		for(int i = 0; i < c.getG(); i++){
			partSum = 0;
			for(int j = 0; j < c.getM(); j++){
				partSum +=  (Classroom.student[c.phenotype(i,j)].getMark() - Classroom.avg); 
			}
			imbalance += partSum*partSum;
		}
	}
	
	/**
	 * Cuenta el numero de enemigos en los grupos de este cromosoma
	 * 
	 * */
	private void incompatibilities(ChromosomePermutation c) {
		incompatibilities = 0;
		for(int i = 0; i < c.getG(); i++){
			for(int j = 0; j < c.getM(); j++){
				incompatibilities += countEnemies(i, j, c);
			}
		}
	}

	/**
	 * Cuenta el numero de enemigos para un determinado estudiante en un grupo
	 * 
	 * */
	private int countEnemies(int group, int student, ChromosomePermutation c) {
		int result = 0;
		List<Integer> enemies =  Classroom.student[c.phenotype(group,student)].getEnemy();
		for(int i = 0; i < c.getM(); i++){
			for(Integer e : enemies){
				if (e.intValue() == Classroom.student[c.phenotype(group,i)].getId() && i != student)
					result++;
			}
		}
		return result;
	}
}
