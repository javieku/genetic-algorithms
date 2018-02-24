package org.pe.ea.chromosome;

import org.pe.extra.Ant;
import org.pe.extra.Function;
import org.pe.extra.Map;
import org.pe.extra.Function.TFunction;
import org.pe.extra.Map.TCell;
import org.pe.util.Tree;

public class EvaluationAntProgram implements FEvaluation {

	private Ant ant;
	
	public EvaluationAntProgram(int steps) {
		ant = new Ant(steps);
	}
	
	@Override
	public double evaluate(Chromosome c) {
		ant.reset();
	    while (ant.getSteps() < ant.getHP() &&
	    		ant.getEaten() < Map.getInstance().getNFood())  
			  executeTree(c.getTree());
		return ant.getEaten();
	}

	public void executeTree(Tree<Function> tree) { 
	    // Mientras no se haya acabado el tiempo ni la comida 
	 if (ant.getSteps() < ant.getHP() && ant.getEaten() < Map.getInstance().getNFood()) { 
	       
		 // Si estamos encima de comida comemos 
	    if (ant.getCurrentPos() == TCell.FOOD) { 
	    		ant.eat();
	    } 
	    // Acciones a realizar en función del nodo en el que estemos  
	    if (tree.getData().getType().equals(TFunction.PROGN3)){ 
	    		executeTree(tree.getChildAt(0)); 
	    		executeTree(tree.getChildAt(1)); 
	    		executeTree(tree.getChildAt(2)); 
	    } 	
	    else if (tree.getData().getType().equals(TFunction.PROGN2)){ 
    			executeTree(tree.getChildAt(0)); 
    			executeTree(tree.getChildAt(1)); 
	      } 
	    else if(tree.getData().getType().equals(TFunction.SIG)){ 
		   	 if (ant.isThereFood()) 
		   		 	executeTree(tree.getChildAt(0)); 
		   	 else 
		   		 executeTree(tree.getChildAt(1)); 
	    } 
	    else if (tree.getData().getType().equals(TFunction.STEP)) ant.step(); 
	  	else if (tree.getData().getType().equals(TFunction.RIGHT)) ant.right(); 
	  	else if(tree.getData().getType().equals(TFunction.LEFT)) ant.left();
	    } 
	}
	
	public Map getResults() {
		return ant.getMap();
	}
}
