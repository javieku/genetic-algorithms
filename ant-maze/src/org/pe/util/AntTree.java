package org.pe.util;

import org.pe.extra.Function;
import org.pe.ui.Main;

public class AntTree {
	
	public static void initializeTree(Tree<Function> tree, int minDepth, int maxDepth) {
		if (minDepth > 0) {
			generateSubtree(tree, minDepth, maxDepth);
		} else {
			if (maxDepth == 0) {
				generateLeaf(tree);
			} else {
				int n = Main.randomGenerator.nextInt(2);
				if (n == 0) 
					generateSubtree(tree, minDepth, maxDepth);
				 else 
					generateLeaf(tree);
			}
		}
	}
	
	private static void generateSubtree(Tree<Function> tree, int minDepth, int maxDepth) {
		int max = 0;
		Function operator = Function.randomOperator();
		tree.data = operator;
		tree.numNodes++;
		tree.depth++;
		
		Tree<Function> hi = new Tree<Function>();
		initializeTree(hi, minDepth - 1, maxDepth - 1); 	
		tree.children.add(hi);
		tree.numNodes += hi.numNodes;
		
		max = Math.max(max, hi.depth);
		
		if (operator.getArity() == 3) {
			
			Tree<Function> hc = new Tree<Function>();
			initializeTree(hc, minDepth - 1, maxDepth - 1); 	
			tree.children.add(hc);
			tree.numNodes += hc.numNodes;
			
			max = Math.max(max, hc.depth);
			
		} 
		
		Tree<Function> hd = new Tree<Function>();
		initializeTree(hd, minDepth - 1, maxDepth - 1); 	
		tree.children.add(hd);
		tree.numNodes += hd.numNodes;
		
		max = Math.max(max, hd.depth);
		tree.depth += max;
	}
	
	private static void generateLeaf(Tree<Function> tree) {
		Function terminal = Function.randomTerminal();
		tree.data = terminal;
		tree.numNodes++;
	}
}
