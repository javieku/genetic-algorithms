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


public class EvolutionaryAlgorithm<T> {

	private Crosser<T> crosser;
	
	private Selector<T> selector;
	
	private List<Chromosome<T>> population; // población de cromosomas
	
	private int gen_max_num;
	private int gen_num;
	
	private Chromosome<T>[] bestOnes; 
	private Chromosome<T> theBest;
	
	private boolean isMaximize;
	
	// Datos para las gráficas
	private double[] bestApt;	// Evolución de la aptitud del mejor absoluto
	private double[] avgApt;	// Evolución de la aptitud media
	
	private double crossover_prob;
	private double mutation_prob;
	private double tolerance; // tolerancia para discretización del intervalo en el que se estudia cada función
	
	
	@SuppressWarnings("unchecked")
	public EvolutionaryAlgorithm(List<Chromosome<T>> population) {
		gen_max_num = 10;
		crossover_prob = 0.6f;
		mutation_prob = 0.05f;
		crosser = null;
		tolerance = 0.001f;
		this.population = population;
		selector = new Roulette<T>();
		isMaximize = false;
		gen_num = 0;
		
		bestOnes = new Chromosome[gen_max_num];
		bestApt = new double[gen_max_num];
		avgApt = new double[gen_max_num];
	}

	@SuppressWarnings("unchecked")
	public EvolutionaryAlgorithm(int gen_max_num, double crossover_prob, double mutation_prob,
			double tolerance, boolean isMaximize, Crosser<T> crosser, List<Chromosome<T>> population) {
		this.gen_max_num = gen_max_num;
		this.crossover_prob = crossover_prob;
		this.mutation_prob = mutation_prob;
		this.crosser = crosser;
		this.tolerance = tolerance;
		this.population = population;
		this.isMaximize = isMaximize;
		gen_num = 0;

		bestOnes = new Chromosome[gen_max_num];
		bestApt = new double[gen_max_num];
		avgApt = new double[gen_max_num];
	}
	
	@SuppressWarnings("unchecked")
	public EvolutionaryAlgorithm(Params params, List<Chromosome<T>> population) {
		gen_max_num = params.getGenNumber();
		crossover_prob = params.getpC();
		mutation_prob = params.getpM();
		gen_num = 0;

		crosser = (Crosser<T>) params.getCr();
		selector = (Selector<T>) params.getSel();
		tolerance = params.getTol();
		isMaximize = params.getMax();
		this.population = population;
		
		bestOnes = new Chromosome[gen_max_num];
		bestApt = new double[gen_max_num];
		avgApt = new double[gen_max_num];
	}

	/**
	 * gen_num + 1 porque considero la población inicial generada aleatoriamente como primera generación
	 * 
	 * */
	public boolean isFinished() {
		return (gen_num >= gen_max_num);
	}

	public Chromosome<T> getTheBest() {
		return theBest;
	}
	
	public double getTolerance(){
		return tolerance;
	}
	
	public void incNextGen() {
		gen_num++;
	}
	
	public Chromosome<T>[] getTheBestOnes() {
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
	@SuppressWarnings("unchecked")
	public void reproduction() {
		// seleccionados para reproducir
		Chromosome<T> crossover_sel[]; 
		// contador seleccionados
		int crossover_sel_num; 
		double prob;
		
		crossover_sel_num = 0;
		crossover_sel = new Chromosome[population.size()];
		for (Chromosome<T> c : population){
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
		
		List<Chromosome<T>> bothGenerations = new ArrayList<Chromosome<T>>();
		for (Chromosome<T> c: population) {
			bothGenerations.add((Chromosome<T>) c.clone());
		}

		// se aplica cruce
		for(int j = 0; j < crossover_sel_num; j += 2) {
			crosser.crossover(crossover_sel[j],crossover_sel[j+1]);
			bothGenerations.add(crossover_sel[j]);
			bothGenerations.add(crossover_sel[j+1]);
		}
		
		// Ordenamos bothGenerations
		Collections.sort(bothGenerations);
		
		// Filtramos los n mejores
		bothGenerations = bothGenerations.subList(0, population.size());
		
		// Fijamos la nueva población
		population = bothGenerations;
	}

	/**
	 * Mutación clásica bit a bit
	 * */
	public void mutation() {
		double p;
		for (Chromosome<T> c : population){
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
		for (Chromosome<T> c : population){
			if (c.getAptitude() > cmax) 
				cmax = c.getAptitude();
		}

		cmax = cmax * 1.05; //margen para evitar que la suma de adaptaciones sea = 0 
		
		for (Chromosome<T> c : population){
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
		for (Chromosome<T> c : population){
			if (c.getAptitude() < cmin) 
				cmin = c.getAptitude();
		}

		//cmin = cmin * 1.05; //margen para evitar que la suma de adaptaciones sea = 0 
		
		for (Chromosome<T> c : population){
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
		for (Chromosome<T> c : population){
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
		
		for (Chromosome<T> c : population){
			c.setAdaptation( (a*c.getAdaptation()) + b);  
		}	
	}

	/**
	 *  Se revisan las adaptaciones y se comprueban las aptitudes (función de evaluación) guardando
	 *  la mejor en total y la de cada generación. Esta comentada arriba la versión en la que se distingue 
	 *  entre minimizar y maximizar a nivel algoritmo. Puede que en algún caso sea tan fácil hacer esa distinción(lo del cambio de signo).
	 * */
	public void evaluatePopulation() {
		 // se desplaza la funcion de evaluación (inicializando la adaptación de cada cromosoma)
		if(isMaximize)
			reviewAdaptationMaximize(); 
		else
			reviewAdaptationMinimize();
	
		// calcula puntuaciones para la ruleta
		computePunctuation(); 
		
		computeResults();
	}
	
	@SuppressWarnings("unchecked")
	private void computeResults() {
		double best_aptitude;
		double accu_aptitude = 0;
		Chromosome<T> theBestNextPopulation = null;
		
		if(isMaximize)
			best_aptitude = -Double.MAX_VALUE;
		else
			best_aptitude = Double.MAX_VALUE;
		
		for(Chromosome<T> c: population){
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
			if(theBest == null || best_aptitude > theBest.getAptitude()) {
					bestApt[gen_num] = theBestNextPopulation.getAptitude();
					if (theBestNextPopulation != null)
						theBest = (Chromosome<T>) theBestNextPopulation.clone();
			}else
				bestApt[gen_num] = theBest.getAptitude();
		}else{
			if(theBest == null || best_aptitude < theBest.getAptitude()) {
				bestApt[gen_num] = theBestNextPopulation.getAptitude();
				if (theBestNextPopulation != null)
					theBest = (Chromosome<T>) theBestNextPopulation.clone();
			}else
				bestApt[gen_num] = theBest.getAptitude(); 
		}
		bestOnes[gen_num] = theBestNextPopulation; 
	}

	private void computePunctuation(){
		double accu_punct = 0;
		double accu_adaptation = 0;
		for (Chromosome<T> c : population){
			accu_adaptation += c.getAdaptation();
		}
		
		for(Chromosome<T> c : population){
			c.setPunctuation(c.getAdaptation()/accu_adaptation);
			c.setAccPunct(c.getPunctuation() + accu_punct);
			accu_punct += c.getPunctuation();
		}
	}
	
	public Results getResults() {
		Results r = new Results();
		r.setBestOnes(bestOnes);
		r.setBestOne(theBest);
		r.setBestApt(bestApt);
		r.setAvgApt(avgApt);
		r.setNumGen(gen_max_num);
		
		return r;
	}

	@SuppressWarnings("unchecked")
	public List<Chromosome<T>> getElite(int eliteSize) {
		Collections.sort(population);
		List<Chromosome<T>> result = new ArrayList<Chromosome<T>>();

		for(Chromosome<T> c: population.subList(0, eliteSize)){
				result.add((Chromosome<T>) c.clone());
		}	
	
		population.subList(0, eliteSize).clear();
		
		computePunctuation();
		
		return result;
	}

	public void addElite(List<Chromosome<T>> elite) {
		population.addAll(elite);
		
		computePunctuation();
	}
}
