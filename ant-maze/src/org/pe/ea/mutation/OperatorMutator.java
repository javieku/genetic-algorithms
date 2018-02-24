package org.pe.ea.mutation;

import java.util.List;

import org.pe.ea.chromosome.Chromosome;
import org.pe.extra.Function;
import org.pe.ui.Main;
import org.pe.util.Tree;

/**
 * Mutación de operadores - Reemplaza un operador al azar de un programa por otro generado 
 * también aleatoriamente de su misma aridad.
 * 
 * */
public class OperatorMutator implements Mutator {

	@Override
	public boolean mutation(Chromosome c) {
		List<Integer> operatorNodes = c.getOperators();
		Tree<Function> t = c.getTree();
		c.reviewFunctions();
		
		// Se toma aleatoriamente la posición en el árbol de un operador de la lista del programa
		int selectedNode = Main.randomGenerator.nextInt(operatorNodes.size());
		// Se busca ese nodo en el programa
		Tree<Function> selterminalTree = t.getNode(operatorNodes.get(selectedNode));
		
		// Se genera un operador al azar
		Function operator = Function.randomOperator(selterminalTree.getData().getArity());
		// Se realiza la sustitución
		selterminalTree.setData(operator);
		
		c.reviewFunctions();
		return true;
	}

}
