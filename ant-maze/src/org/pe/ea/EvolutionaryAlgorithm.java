package org.pe.ea;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.pe.ea.chromosome.Chromosome;
import org.pe.ea.crossover.Crosser;
import org.pe.ea.selection.Roulette;
import org.pe.ea.selection.Selector;
import org.pe.ui.Main;
import org.pe.ui.Results;


public class EvolutionaryAlgorithm {

	public enum TBloating { TARPEIAN, WELL_FOUNDED };
	
	private Crosser crosser;
	
	private Selector selector;
	
	private List<Chromosome> population; // población de cromosomas
	
	private int gen_max_num;
	private int gen_num;
	
	private Chromosome[] bestOnes; 
	private Chromosome theBestOne;
	
	private boolean isMaximize;
	
	// Datos para las gráficas
	private double[] bestApt;	// Evolución de la aptitud del mejor absoluto
	private double[] avgApt;	// Evolución de la aptitud media
	private double[] avgPSize;  // Evolución del tamaño medio de los programas
	
	private double crossover_prob;
	private double mutation_prob;
	
	private TBloating typeBloating; // Tipo de bloating a aplicar
	private double avg_psize; // Media de tamaño de los programas
	private double avg_aptitude; // Media de aptitudes 
	private int n; // Bloating método tarpeian

	public EvolutionaryAlgorithm(List<Chromosome> population) {
		gen_max_num = 10;
		crossover_prob = 0.6f;
		mutation_prob = 0.05f;
		crosser = null;
		this.population = population;
		selector = new Roulette();
		isMaximize = false;
		gen_num = 0;
		typeBloating = TBloating.TARPEIAN; 
		avg_psize = 0; 
		n = 2; 
		
		bestOnes = new Chromosome[gen_max_num];
		bestApt = new double[gen_max_num];
		avgApt = new double[gen_max_num];
		avgPSize = new double[gen_max_num];
	}

	public EvolutionaryAlgorithm(int gen_max_num, double crossover_prob, double mutation_prob,
			 boolean isMaximize, Crosser crosser, List<Chromosome> population, TBloating typeBloating, int nTarpeian) {
		this.gen_max_num = gen_max_num;
		this.crossover_prob = crossover_prob;
		this.mutation_prob = mutation_prob;
		this.crosser = crosser;
		this.population = population;
		this.isMaximize = isMaximize;
		gen_num = 0;
		this.typeBloating = typeBloating; 
		avg_psize = 0; 
		n = nTarpeian; 

		bestOnes = new Chromosome[gen_max_num];
		bestApt = new double[gen_max_num];
		avgApt = new double[gen_max_num];
		avgPSize = new double[gen_max_num];
	}
	
	public EvolutionaryAlgorithm(Params params, List<Chromosome> population) {
		gen_max_num = params.getGenNumber();
		crossover_prob = params.getpC();
		mutation_prob = params.getpM();
		gen_num = 0;
		typeBloating = params.getTBloating(); 
		avg_psize = 0; 
		n = params.getNTarpeian(); 

		crosser = params.getCr();
		selector = params.getSel();
		isMaximize = params.getMax();
		this.population = population;
		
		bestOnes = new Chromosome[gen_max_num];
		bestApt = new double[gen_max_num];
		avgApt = new double[gen_max_num];
		avgPSize = new double[gen_max_num];
	}

	/**
	 * gen_num + 1 porque considero la población inicial generada aleatoriamente como primera generación
	 * 
	 * */
	public boolean isFinished() {
		return (gen_num >= gen_max_num);
	}

	public Chromosome getTheBestOne() {
		return theBestOne;
	}

	public void incNextGen() {
		gen_num++;
	}
	
	public Chromosome[] getTheBestOnes() {
		return bestOnes;
	}

	public void selection() {
		population = selector.selection(population);
	}

	/** 
	 * Se realiza un cruce monopunto dos a dos entre los individuos seleccionados por la ruleta.
	 * A la hora de realizar el reemplazo se realiza por inclusión de los mejores, esto es, se juntan
	 *  padres e hijos y se toman los N mejores. 
	 */
	public void reproduction() {
		// seleccionados para reproducir
		Chromosome crossover_sel[]; 
		// contador seleccionados
		int crossover_sel_num; 
		double prob;
		
		crossover_sel_num = 0;
		crossover_sel = new Chromosome[population.size()];
		for (Chromosome c : population){
			prob = Main.randomGenerator.nextDouble();
			// se eligen los individuos de las posiciones i si prob < crossover_prob
			if(prob < crossover_prob){
				crossover_sel[crossover_sel_num] = c; 
				crossover_sel_num++;
			}
		}
		// el numero de seleccionados se hace par
		if ((crossover_sel_num % 2) == 1)
				crossover_sel_num--;
		
		List<Chromosome> bothGenerations = new ArrayList<Chromosome>();
		for (Chromosome c: population) {
			bothGenerations.add((Chromosome) c.clone());
		}

		// se aplica cruce
		for(int j = 0; j < crossover_sel_num; j += 2) {
			crosser.crossover(crossover_sel[j], crossover_sel[j+1]);
		
			crossover_sel[j].setAptitude(crossover_sel[j].evaluate());
			crossover_sel[j+1].setAptitude(crossover_sel[j+1].evaluate());
			
			bothGenerations.add(crossover_sel[j]);
			bothGenerations.add(crossover_sel[j+1]);
		}
		
		// Ordenamos bothGenerations
		Collections.sort(bothGenerations);
		
		// Filtramos los n mejores
		bothGenerations = bothGenerations.subList(0, population.size());
		
		// Fijamos la nueva población
		if(typeBloating != TBloating.TARPEIAN)
			population = bothGenerations;
	}

	/**
	 * Mutación clásica bit a bit
	 * */
	public void mutation() {
		double p;
		for (Chromosome c : population){
			p = Main.randomGenerator.nextDouble();
			if(p < mutation_prob)
				c.mutation();
		}
	}

	/**
	 * Introduce la distinción entre adaptación y función de evaluación (Ver teoría). 
	 * Convierte un problema de minimización en maximización. Gráficamente es equivalente a desplazar la función 
	 * a optimizar la cantidad necesaria para que sus valores sean positivos.
	 * */
	public void reviewAdaptationMinimize(){
		double cmax = -Double.MAX_VALUE; 
		
		// un valor por debajo de cualquiera que pueda
		// tomar la función objetivo
		for (Chromosome c : population){
			if (c.getAptitude() > cmax) 
				cmax = c.getAptitude();
		}

		cmax = cmax * 1.05; //margen para evitar que la suma de adaptaciones sea = 0 
		
		for (Chromosome c : population){
			c.setAdaptation(cmax - c.getAptitude());  
		}
	}
	
	/**
	 * Para eludir posibles valores negativos en la aptitud de funciones a maximizar. 
	 * Gráficamente es equivalente a desplazar la función a optimizar la cantidad necesaria para que sus valores sean positivos.(Condición
	 * necesaria para que la ruleta funcione)
	 * */
	public void reviewAdaptationMaximize(){
		
		double cmin = Double.MAX_VALUE; 
		
		// un valor por debajo de cualquiera que pueda
		// tomar la función objetivo
		for (Chromosome c : population){
			if (c.getAptitude() < cmin) 
				cmin = c.getAptitude();
		}

		//cmin = cmin * 1.05; //margen para evitar que la suma de adaptaciones sea = 0 
		
		for (Chromosome c : population){
			c.setAdaptation(c.getAptitude() + cmin);  
		}
	}
	
	public void scaleAdaptation2(double d){
		double a = 0;
		double b = 0;
	//	double P = d;
		double cmax = -Double.MAX_VALUE; 
		double cmin = Double.MAX_VALUE; 
		double avgAdaptation = 0;
		
		// un valor por debajo de cualquiera que pueda
		// tomar la función objetivo
		for (Chromosome c : population){
			avgAdaptation += c.getAdaptation();
			
			if (c.getAdaptation() > cmax){
				cmax = c.getAdaptation();
			}
			
			if (c.getAdaptation() < cmin) 
				cmin = c.getAdaptation();
		}
		
		avgAdaptation = avgAdaptation/population.size();

		//if(isMaximize)
		//	a = ((P-1)*avgAdaptation)/(cmax - avgAdaptation); 
		//else
		a = avgAdaptation/(avgAdaptation - cmin);
		b = (1 - a)/avgAdaptation;
		
		for (Chromosome c : population){
			c.setAdaptation( (a*c.getAdaptation()) + b);  
		}	
	}

	/**
	 *  Se revisan las adaptaciones y se comprueban las aptitudes (función de evaluación) guardando
	 *  la mejor en total y la de cada generación. Esta comentada arriba la versión en la que se distingue 
	 *  entre minimizar y maximizar a nivel algoritmo.
	 * */
	public void evaluatePopulation() {
		// Se calcula el tamaño medio de programa de la población
		computeAvgPSize();
		// Se calcula la aptitud media de la población
		computeAvgAptitude();
		
		// Se aplica el método de bloating que corresponda
		bloating();
	
		 // se desplaza la funcion de evaluación (inicializando la adaptación de cada cromosoma)
		if(isMaximize)
			reviewAdaptationMaximize(); 
		else
			reviewAdaptationMinimize();
		
		// calcula puntuaciones para la ruleta
		computePunctuation(); 
		
		computeResults();
	}

	private void computeResults() {
		double best_aptitude;
		double accu_aptitude = 0;
		Chromosome theBestNextPopulation = null;
		
		if(isMaximize)
			best_aptitude = -Double.MAX_VALUE;
		else
			best_aptitude = Double.MAX_VALUE;
		
		for(Chromosome c: population){
			accu_aptitude += c.getAptitude();
			
			if(isMaximize){
				if(c.getAptitude() > best_aptitude){
					theBestNextPopulation = c;
					best_aptitude = c.getAptitude();
				}
			}else{
				if(c.getAptitude() < best_aptitude){
					theBestNextPopulation = c;
					best_aptitude = c.getAptitude();
				}
			}
		}
		
		avgApt[gen_num] = accu_aptitude / population.size(); 
		
		if(isMaximize){
			if(theBestOne == null || best_aptitude > theBestOne.getAptitude()) {
					bestApt[gen_num] = theBestNextPopulation.getAptitude();
					if (theBestNextPopulation != null)
						theBestOne = (Chromosome) theBestNextPopulation.clone();
			}else
				bestApt[gen_num] = theBestOne.getAptitude();
		}else{
			if(theBestOne == null || best_aptitude < theBestOne.getAptitude()) {
				bestApt[gen_num] = theBestNextPopulation.getAptitude();
				if (theBestNextPopulation != null)
					theBestOne = (Chromosome) theBestNextPopulation.clone();
			}else
				bestApt[gen_num] = theBestOne.getAptitude(); 
		}
		
		bestOnes[gen_num] = theBestNextPopulation; 
		
		avgPSize[gen_num] = avg_psize;
	}

	private void computePunctuation(){
		double accu_punct = 0;
		double accu_adaptation = 0;
		for (Chromosome c : population){
			accu_adaptation += c.getAdaptation();
		}
		
		for(Chromosome c : population){
			c.setPunctuation(c.getAdaptation()/accu_adaptation);
			c.setAccPunct(c.getPunctuation() + accu_punct);
			accu_punct += c.getPunctuation();
		}
	}
	
	public Results getResults() {
		Results r = new Results();
		r.setBestOnes(bestOnes);
		r.setBestOne(theBestOne);
		r.setBestApt(bestApt);
		r.setAvgApt(avgApt);
		r.setNumGen(gen_max_num);
		r.setAvgPSize(avgPSize);
		
		return r;
	}

	public List<Chromosome> getElite(int eliteSize) {
		Collections.sort(population);
		List<Chromosome> result = new ArrayList<Chromosome>();

		for(Chromosome c: population.subList(0, eliteSize)){
				result.add((Chromosome) c.clone());
		}	
	
		population.subList(0, eliteSize).clear();
		
		computePunctuation();
		
		return result;
	}

	public void addElite(List<Chromosome> elite) {
		population.addAll(elite);
		
		computePunctuation();
	}
	
	public void bloating() {
		if(typeBloating != null){
			if (typeBloating == TBloating.TARPEIAN) {
				bloatingTarpeian();
			} else {
				bloatingWF();
			}
		}
	}
	
	private void bloatingTarpeian() {
		for (Chromosome c: population) {
			if (c.getTree().getNumberOfNodes() > avg_psize && 
						Main.randomGenerator.nextInt() % n == 0) 
				c.setAptitude(1);
			else 
				c.setAptitude(c.evaluate());
		}
	} 
	
	private void bloatingWF() {
		double varPS = 0;
		double covPSAp = 0;
		int n = population.size();
		for (Chromosome chro: population) {
			covPSAp += (chro.getAptitude() - avg_aptitude) * (chro.getTree().getNumberOfNodes() - avg_psize);
			varPS += (chro.getTree().getNumberOfNodes() - avg_psize) * (chro.getTree().getNumberOfNodes() - avg_psize);
		}
		varPS = varPS/n;
		covPSAp = covPSAp/n;
		if(varPS > 0) {
			for (Chromosome c: population) {
					c.setAptitude(c.getAptitude() - (covPSAp/varPS * c.getTree().getNumberOfNodes()));
			}
		}
	} 
	
	private void computeAvgAptitude() {
		avg_aptitude = 0;
		double acc_size = 0;
		for (Chromosome c: population) {
			acc_size += c.getAptitude();
		}
		avg_aptitude = acc_size/population.size();
	}
	
	private void computeAvgPSize() {
		avg_psize = 0;
		double acc_size = 0;
		for (Chromosome c: population) {
			acc_size += c.getTree().getNumberOfNodes();
		}
		avg_psize = acc_size/population.size();
	}
}
