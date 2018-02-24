package org.pe.ea.chromosome;
import java.util.ArrayList;
import java.util.List;

import org.pe.ea.mutation.Mutator;
import org.pe.extra.Function;
import org.pe.util.AntTree;
import org.pe.util.Tree;

/**
 * Clase que representa un programa cualquiera. Dispone de información sobre la puntuación y aptitud de ese programa así 
 * como referencias a objetos para llevar a cabo la mutación y la evaluación de la aptitud del mismo.
 * 
 * */
public class Chromosome implements Cloneable, Comparable<Chromosome> {

	protected Tree<Function> tree; 
	protected List<Integer> terminals; // terminales del programa 
	protected List<Integer> operators; // funciones del programa	
	private int nodeID; // Para realizar la asignación de identificadores a terminales y funciones durante la generación del árbol.

	protected int chromosomeLenght;
	
	protected Mutator mutator;
	
	protected FEvaluation fEv;
	
	protected double aptitude;
	protected double aptitude_pen;
	protected double adaptation;
	protected double punctuation; 
	protected double accum_punct; 
	
	public Chromosome(FEvaluation fEv, Mutator mutator) {
		aptitude = 0;
		aptitude_pen = 0;
		adaptation = 0; 
		punctuation = 0;
		accum_punct = 0;
		chromosomeLenght = 0;
		tree = new Tree<Function>();
		terminals = new ArrayList<Integer>();
		operators = new ArrayList<Integer>();
		this.fEv = fEv;
		this.mutator = mutator;
	}

	public double getAccumulatedPunt() {
		return accum_punct;
	}

	public int getSize() {
		return chromosomeLenght;
	}
	
	public int getNumGenes() {
		return 1;
	}

	public Tree<Function> getTree() {
		return tree;
	}
	
	public List<Integer> getTerminals() {
		return terminals;
	}
	
	public List<Integer> getOperators() {
		return operators;
	}
	
	public double getAptitude() {
		return aptitude;
	}
	
	public double getAptitudePen() {
		return aptitude_pen;
	}
	
	public double getAdaptation() {
		return adaptation;
	}
	
	public double getPunctuation() {
		return punctuation;
	}
	
	public FEvaluation getFEv() {
		return fEv;
	}
	
	public void setTree(Tree<Function> tree){
		this.tree = tree;
	}
	
	public void setAptitude(double aptitude) {
		this.aptitude = aptitude;
	}
	
	public void setAptitudePen(double aptitude_pen) {
		this.aptitude_pen = aptitude_pen;
	}
	
	public void setAdaptation(double adaptation) {
		this.adaptation = adaptation;
	}
	
	public void setPunctuation(double punctuation) {
		this.punctuation = punctuation;
	}
	
	public void setAccPunct(double accum_punct) {
		this.accum_punct = accum_punct;
	}

	public void mutation() {
		if(mutator.mutation(this)){
			this.aptitude = fEv.evaluate(this);
		}
	}
	
	public double evaluate() {
		return fEv.evaluate(this);
	}

	public String toString() {
		String result = new String();
		result += "============================\n";
		result += "f:" + aptitude;
		result += " prof:" + tree.getDepth();
		result += " nodos:" + tree.getNumberOfNodes();
		result += "\n";
		result += tree.toString();
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Object clone() {
		Chromosome chro = new Chromosome(fEv, mutator);
		chro.aptitude = aptitude;
		chro.aptitude_pen = aptitude_pen;
		chro.adaptation = adaptation;
		chro.punctuation = punctuation; 
		chro.accum_punct = accum_punct; 
		chro.chromosomeLenght = chromosomeLenght;
		chro.terminals = new ArrayList<Integer>(terminals);
		chro.operators = new ArrayList<Integer>(operators);
		chro.tree = (Tree<Function>) tree.clone();
		return chro;
	}
	
	public void initialize(int minDepth, int maxDepth) {
		nodeID = 0;
		AntTree.initializeTree(tree, minDepth, maxDepth);
		aptitude = evaluate();
	}
	
	public void reviewFunctions() {
		nodeID = 0;
		terminals.clear();
		operators.clear();
		reviewFunctions(tree);
	}
	
	private void reviewFunctions(Tree<Function> tree) {
		Tree<Function> child; 
		if(!tree.hasChildren()){
			terminals.add(nodeID);
			nodeID++;
		} else {
			operators.add(nodeID);
			nodeID++;
			for (int i = 0; i < tree.getChildren().size(); i++) {
				child = tree.getChildAt(i);
				reviewFunctions(child);
			}
		}
	}
	
	public int compareTo(Chromosome c) {
		double apt = c.getAdaptation() - getAdaptation();
		if (apt == 0)
			return 0;
		
		if (apt < 0)
			return -1;
		
		return 1;
	}
}
