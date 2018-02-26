package org.pe.ea.crossover;

import java.util.Iterator;

import org.pe.ea.chromosome.Chromosome;
import org.pe.ea.chromosome.ChromosomePermutation;
import org.pe.ea.chromosome.Gen;
import org.pe.extras.Classroom;

/**
 * Asigna los genes de los padres a los hijos teniendo en cuenta los genes que ya están asignados 
 * tratando de minimizar el número de alumnos que no desean estar juntos.
 * Ver memoria para explicación más detallada.
 * 
 * */
public class AntiEnemies implements Crosser<Integer>{

	private int nGroup;
	private int groupSize;
	
	public AntiEnemies(int groupSize, int nGroup){
		this.groupSize = groupSize;
		this.nGroup = nGroup;
	}
	
	@Override
	public void crossover(Chromosome<Integer> h1, Chromosome<Integer> h2) {
		if(h1 instanceof ChromosomePermutation){
			ChromosomePermutation p1 = (ChromosomePermutation) h1.clone();
			ChromosomePermutation p2 = (ChromosomePermutation) h2.clone();
			Gen<Integer>[] genh1 = h1.getGenes();
			Gen<Integer>[] genh2 = h2.getGenes();
			Gen<Integer>[] genp1 = p1.getGenes();
			Gen<Integer>[] genp2 = p2.getGenes();
			
			initializeH(genh1);initializeH(genh2);
			
			int cp1 = 0;
			int cp2 = 0;
			int chroSize = genp1.length;
			// Para generar un hijo se recorren en sentido ascendente 
			for(int i = 0; i < 2*chroSize; i++){
				if(i % 2 == 0){
					selectGen(genh1,genp1[cp1]);
					cp1++;
				}else{
					selectGen(genh1,genp2[cp2]);
					cp2++;
				}
			}
			
			cp1 = chroSize - 1;
			cp2 = chroSize - 1;
			for(int i = 0; i < 2*chroSize; i++){
				if(i % 2 == 0){
					selectGen(genh2,genp1[cp1]);
					cp1--;
				}else{
					selectGen(genh2,genp2[cp2]);
					cp2--;
				}
			}
			
			for(int i = 0; i < h1.getNumGenes(); i++){
				if (h1.getGenes()[i].getBits().isEmpty()){
					int j = 0;
					j++;
				}
			}
			
			h1.setAptitude(h1.evaluate());
			h2.setAptitude(h2.evaluate());
		}
	}

	private void selectGen(Gen<Integer>[] genes, Gen<Integer> gen) {
		if(isCompatible(genes, gen)){
			int pos = checkEnemies(genes, gen);
			if(pos != -1)
				genes[pos] = gen;
		}
	}


	private int checkEnemies(Gen<Integer>[] genesh, Gen<Integer> genp) {
		int pos = findGroup(genesh, genp, true);
		if(pos != -1)
			return pos;
		else{
			pos = findGroup(genesh, genp, false);
			return pos;
		}
	}

	/**
	 *  Encuentra el primer grupo libre 
	 *  si flag es true tiene en cuenta las enemistades si añadieramos genp
	 *  
	 * */
	private int findGroup(Gen<Integer>[] genes, Gen<Integer> gen, boolean flag) {
		int i = 0;
		int pos = -1;
		boolean found = false;
		while(i < nGroup && !found){
			pos = findSpace(genes, gen, i, flag);
			found = pos != -1; 
			i++;
		}
		if(found){
			i--;
			return i*groupSize + pos;
		}
		else
			return -1;
	}

	/**
	 *  Comprueba is un grupo tiene plazas libres
	 *  si flag es true tiene en cuenta las enemistades si añadieramos genp
	 * 
	 * */
	private int findSpace(Gen<Integer>[] genes, Gen<Integer> gen, int g, boolean flag) {
		int j = 0;
		boolean foundSpace = false;
		boolean foundEnemy = false;
		while(j < groupSize && !foundSpace && !foundEnemy){
			foundSpace = genes[g*groupSize + j].getBits().isEmpty();
			if(flag)
				foundEnemy = isEnemy(genes[g*groupSize  + j], gen)
						 	|| isEnemy(gen, genes[g*groupSize + j]);
			
			j++;
		}
		if(foundSpace && !foundEnemy){
			j--;
			return j;
		}
		else
			return -1;
	}
	
	/**
	 * ¿Es enemigo g1 de g2? 
	 * 
	 * */
	private boolean isEnemy(Gen<Integer> g1, Gen<Integer> g2) {
		if(g1.getBits().isEmpty() || g2.getBits().isEmpty()){
			return false;
		}else{
			int pos1 = g1.getBits().get(0);
			int pos2 = g2.getBits().get(0);
			Iterator<Integer> it = Classroom.student[pos1].getEnemy().iterator();
			int enemyId; 
			int possEnemy = Classroom.student[pos2].getId();
			boolean found = false;
			while(it.hasNext() && !found){
				enemyId = it.next();
				found = (enemyId == possEnemy);
			}
			return found;
		}
	}

	private void initializeH(Gen<Integer>[] genes) {
		for(int i = 0; i < genes.length; i++){
			genes[i] = new Gen<Integer>(); 
		}
	}

	/**
	 * Comprobamos si no esta el gen del padre en el hijo ya
	 * 
	 * */
	private boolean isCompatible(Gen<Integer>[] genes, Gen<Integer> gen) {
		boolean found = false;
		int i = 0 ;
		while (i < genes.length && !found){
			found = genes[i].equals(gen);
			i++;
		}
		return !found;
	}
}
