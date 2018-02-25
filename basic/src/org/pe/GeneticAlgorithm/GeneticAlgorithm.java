package org.pe.GeneticAlgorithm;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.pe.ui.Main;
import org.pe.ui.Results;


public class GeneticAlgorithm {

	public enum SelectionType { ROULETTE, TOURNAMENT }
	
	private SelectionType selection; // tipo de selección en reproducción
	
	private List<Chromosome> population; // población de cromosomas
	
	private int gen_max_num;
	private int gen_num;
	
	private Chromosome[] bestOnes; 
	private Chromosome theBest;
	
	private boolean isMaximize;
	
	// Datos para las gráficas
	private double[] bestApt;	// Evolución de la aptitud del mejor absoluto
	private double[] avgApt;	// Evolución de la aptitud media
	
	private double crossover_prob;
	private double mutation_prob;
	private double tolerance; // tolerancia para discretización del intervalo en el que se estudia cada función
	
	
	public GeneticAlgorithm(List<Chromosome> population) {
		gen_max_num = 10;
		crossover_prob = 0.6f;
		mutation_prob = 0.03f;
		tolerance = 0.001f;
		this.population = population;
		selection = SelectionType.ROULETTE;
		isMaximize = false;
		
		bestOnes = new Chromosome[gen_max_num];
		bestApt = new double[gen_max_num];
		avgApt = new double[gen_max_num];
	}

	public GeneticAlgorithm(int gen_max_num, SelectionType selection, double crossover_prob, double mutation_prob,
			double tolerance, boolean isMaximize, List<Chromosome> population) {
		this.gen_max_num = gen_max_num;
		this.crossover_prob = crossover_prob;
		this.mutation_prob = mutation_prob;
		this.tolerance = tolerance;
		this.population = population;
		this.selection = selection;
		this.isMaximize = isMaximize;

		bestOnes = new Chromosome[gen_max_num];
		bestApt = new double[gen_max_num];
		avgApt = new double[gen_max_num];
	}
	
	public GeneticAlgorithm(Params params, List<Chromosome> population) {
		gen_max_num = params.getGenNumber();
		crossover_prob = params.getpC();
		mutation_prob = params.getpM();
		tolerance = params.getTol();
		selection = params.getSt();
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

	public Chromosome getTheBest() {
		return theBest;
	}
	
	public double getTolerance(){
		return tolerance;
	}
	
	public void incNextGen() {
		gen_num++;
	}
	
	public Chromosome[] getTheBestOnes() {
		return bestOnes;
	}

	public void selection() {
		switch(selection){
		case ROULETTE:
			rouletteSelection();
			break;
		case TOURNAMENT:
			tournamentSelection();
			break;
		default:
			rouletteSelection();
			break;
		}
	}
	
	/**
	 *  Ruleta clásica 
	 *  
	 *  Necesario que las puntuaciones de los individuos sean positivas para que funcione
	 * */
	public void rouletteSelection() {
		Chromosome survivor_sel[];	//seleccionados para sobrevivir
		double prob; 				// probabilidad de seleccion
		int survivor_pos;	 		// posición del superviviente
		Chromosome chro;
		List<Chromosome> nextPopulation;
		int i = 0;
		
		survivor_sel = new Chromosome[population.size()];
		Iterator<Chromosome> it = population.iterator();
		while(it.hasNext()){
			chro = it.next();
			prob = Main.randomGenerator.nextDouble();
			survivor_pos = 0;
			while (survivor_pos < population.size() && prob > population.get(survivor_pos).getAccumulatedPunt() ) {
				survivor_pos++;
			}
			survivor_sel[i] = population.get(survivor_pos);
			i++;
		}

		nextPopulation = new ArrayList<Chromosome>();
		// se genera la poblacion intermedia
		for(i = 0; i < population.size(); i++){
			chro = (Chromosome) survivor_sel[i].clone();
			nextPopulation.add(chro);
		}
		
		population = nextPopulation;
	}
	
	/**
	 * Torneo determinístico se itera N veces la toma de 3 cromosomas de la población al azar 
	 * y se elige el mejor.
	 *  
	 * */
	public void tournamentSelection() {
		int competitor;	
		List<Chromosome> selCompetitors =  new ArrayList<Chromosome>();//seleccionados para competir
		List<Chromosome> nextPopulation = new ArrayList<Chromosome>();
		for(int i = 0; i < population.size(); i++){
			for(int j = 0; j < 3; j++){
				competitor = Main.randomGenerator.nextInt(population.size());
				selCompetitors.add(population.get(competitor));
			}
			Collections.sort(selCompetitors);
			nextPopulation.add((Chromosome)selCompetitors.get(0).clone());
		}
		population = nextPopulation;
	}
	
	/** 
	 * Se realiza un cruce monopunto dos a dos entre los individuos seleccionados por la ruleta.
	 * A la hora de realizar el reemplazo se realiza por inclusi�n de los mejores, esto es, se juntan
	 *  padres e hijos y se toman los N mejores. 
	 */
	public void reproduction() {
		// seleccionados para reproducir
		Chromosome crossover_sel[]; 
		// contador seleccionados
		int crossover_sel_num; 
		int crossover_point; 
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

		// se cruzan los individuos elegidos en un punto al azar
		crossover_point = Main.randomGenerator.nextInt(population.get(0).getSize());
		for(int j = 0; j < crossover_sel_num; j += 2) {
			crossover_sel[j].crossover(crossover_sel[j+1], crossover_point);
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
		for (Chromosome c : population)
			c.mutation(mutation_prob);
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
		double P = d;
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
			if(c.getAdaptation() < 0){
				int j = 0;
				j++;
			}
			c.setAdaptation( (a*c.getAdaptation()) + b);  
			if(c.getAdaptation() < 0){
				int j = 0;
				j++;
			}
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
			if(theBest == null || best_aptitude > theBest.getAptitude()) {
					bestApt[gen_num] = theBestNextPopulation.getAptitude();
					if (theBestNextPopulation != null)
						theBest = (Chromosome) theBestNextPopulation.clone();
			}else
				bestApt[gen_num] = theBest.getAptitude();
		}else{
			if(theBest == null || best_aptitude < theBest.getAptitude()) {
				bestApt[gen_num] = theBestNextPopulation.getAptitude();
				if (theBestNextPopulation != null)
					theBest = (Chromosome) theBestNextPopulation.clone();
			}else
				bestApt[gen_num] = theBest.getAptitude(); 
		}
		bestOnes[gen_num] = theBestNextPopulation; 
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
		r.setBestApt(bestApt);
		r.setAvgApt(avgApt);
		r.setNumGen(gen_max_num);
		
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
}
