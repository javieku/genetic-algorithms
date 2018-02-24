package org.pe.ea.mutation;

import java.util.List;

import org.pe.ea.chromosome.Chromosome;
import org.pe.extra.Function;
import org.pe.ui.Main;
import org.pe.util.Tree;

/**
 * Mutación por terminal - Reemplaza un terminal al azar de un programa por otro generado también aleatoriamente.
 * 
 * */
public class TerminalMutator implements Mutator {

	@Override
	public boolean mutation(Chromosome c) {
		List<Integer> terminalNodes = c.getTerminals();
		Tree<Function> t = c.getTree();
		c.reviewFunctions();
		
		// Se toma aleatoriamente la posición en el árbol de un terminal de la lista del programa
		int selectedNode = Main.randomGenerator.nextInt(terminalNodes.size());
		// Se busca ese nodo en el programa
		Tree<Function> selterminalTree = t.getNode(terminalNodes.get(selectedNode));
		// Se genera un terminal al azar
		Function terminal = Function.randomTerminal();
		// Se realiza la sustitución
		selterminalTree.setData(terminal);
		
		c.reviewFunctions();
		return true;
	}

}
