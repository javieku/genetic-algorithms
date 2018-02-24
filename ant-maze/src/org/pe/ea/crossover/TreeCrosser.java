package org.pe.ea.crossover;

import org.pe.ea.chromosome.Chromosome;
import org.pe.extra.Function;
import org.pe.ui.Main;
import org.pe.util.Tree;

public class TreeCrosser implements Crosser {

	@Override
	public void crossover(Chromosome chro1, Chromosome chro2) {
		 Tree<Function> subtree1, subtree2; 
		 Tree<Function> childTree1, childTree2; 
		 int nNodes;
		 int crossoverPoint;
		  
		 childTree1 = chro1.getTree();
		 childTree2 = chro2.getTree(); 
		  
		 nNodes = Math.min(childTree1.getNumberOfNodes(), childTree2.getNumberOfNodes());
		 
		
		 crossoverPoint = 1 + Main.randomGenerator.nextInt(nNodes - 1); 
		
		subtree1 = childTree1.getNode(crossoverPoint); 
		subtree2 = childTree2.getNode(crossoverPoint); 
		childTree1.setNode(crossoverPoint, subtree2); 
	    childTree2.setNode(crossoverPoint, subtree1); 
		 
	}

}
