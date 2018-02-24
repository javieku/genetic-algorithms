package org.pe.ea.mutation;

import org.pe.ea.chromosome.Chromosome;
import org.pe.extra.Function;
import org.pe.ui.Main;
import org.pe.util.AntTree;
import org.pe.util.Tree;

/**
 *  Mutación de árbol. Toma un nodo aleatoriamente y lo reemplaza por otro árbol generado al azar.
 * 
 * */
public class TreeMutator implements Mutator {

	@Override
	public boolean mutation(Chromosome c) {
		Tree<Function> t = c.getTree();
		
		Tree<Function> substitute = new Tree<Function>();
		
		int selNode = Main.randomGenerator.nextInt(t.getNumberOfNodes()); 
		if (selNode == 0) {
			 int maxDepth = t.getDepth();
			 // No generamos una hoja para que reemplaze al árbol. No es posible generar programas con sólo
			 // un nodo hoja por lo que la profundidad no se ve incrementada
			 AntTree.initializeTree(substitute, 1, maxDepth); 
			 c.setTree(substitute);
		}else{ 	
			 int maxDepth = t.getNode(selNode).getDepth();
			 // Cualquier árbol entre la profundidad del sustituido y nodo hoja.
			 AntTree.initializeTree(substitute, 0, maxDepth);
			 t.setNode(selNode, substitute); 
		}
		return true;
	}

}
