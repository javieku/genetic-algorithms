package org.pe.util;

import java.io.File;
import java.util.Random;

import org.pe.ea.chromosome.Chromosome;
import org.pe.ea.chromosome.EvaluationAntProgram;
import org.pe.ea.mutation.TerminalMutator;
import org.pe.extra.Function;
import org.pe.extra.Function.TFunction;
import org.pe.extra.Map;
import org.pe.ui.Main;


public class Test {

	public static void main(String[] args) {
    	long seed = (long) (Math.random() * 100);
		
    	Main.randomGenerator = new Random(seed);
    	
		TerminalMutator mut = new TerminalMutator();
		Map.getInstance().load(new File("map.txt"));
		EvaluationAntProgram f = new EvaluationAntProgram(800);
		Chromosome c = new Chromosome(f, mut);
	
		Tree<Function> t = createTree();
		
		c.setTree(t);
		
		c.evaluate();
		
		f.getResults().print(new File("hola.txt"));
		
		//System.out.println("f:" + f.ant.getEaten());
		
		
	}
	//(PROGN3 (DERECHA) (PROGN2  (AVANZA) (IZQUIERDA)) (PROGN2 (AVANZA) (AVANZA))
	private static Tree<Function> createTree() {
		Tree<Function> t = new Tree<Function>(new Function(TFunction.PROGN3));
		Tree<Function> ct1, ct2, ct3, cct21, cct22, cct31, cct32;
		ct1 = new Tree<Function>(new Function(TFunction.RIGHT));
		ct2 = new Tree<Function>(new Function(TFunction.PROGN2));
		ct3 = new Tree<Function>(new Function(TFunction.PROGN2));
		
		cct21 = new Tree<Function>(new Function(TFunction.STEP));
		cct22 = new Tree<Function>(new Function(TFunction.LEFT));
		cct31 = new Tree<Function>(new Function(TFunction.STEP));
		cct32 = new Tree<Function>(new Function(TFunction.STEP));
		
		ct2.addChild(cct21);
		ct2.addChild(cct22);
		ct3.addChild(cct31);
		ct3.addChild(cct32);
		
		
		t.addChild(ct1);
		t.addChild(ct2);
		t.addChild(ct3);
		
		return t;
	}
	
}
