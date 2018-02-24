package org.pe.extra;

import org.pe.ui.Main;
/**
 * Clase que modela los operadores y operandos(Operadores de aridad 0). 
 * Contiene métodos de conversión a string, generación de operandos y terminales
 * de forma aleatoria...
 * 
 * */

public class Function {

	private final static int nOperators = 3;
	private final static int nTerminals = 3;
	
	public enum TFunction {
		SIG, PROGN2, PROGN3, LEFT, RIGHT, STEP
	}
	
	private int arity;
	private TFunction type;
	
	public Function(TFunction type){
		this.type = type;
		initializeArity(type);
	}
	
	private void initializeArity(TFunction type2) {
		switch(type){
		case SIG:
		case PROGN2:
			arity = 2;
			break;
		case PROGN3:
			arity = 3;
			break;
		case LEFT:
		case RIGHT:
		case STEP:
			arity = 0;
			break;
		default:
			arity = 0;
			break;
	}
	}

	public int getArity() {
		return arity;
	}

	public TFunction getType() {
		return type;
	}

	public String toString(){
		String res;
		switch(type){
			case SIG:
				res = "SIG";
				break;
			case PROGN2:
				res = "PROGN2";
				break;
			case PROGN3:
				res = "PROGN3";
				break;
			case LEFT:
				res = "LEFT";
				break;
			case RIGHT:
				res = "RIGHT";
				break;
			case STEP:
				res = "STEP";
				break;
			default:
				res = ""; 
				break;
		}
		return res;
	}
	
	public static Function randomOperator(){
		int n = Main.randomGenerator.nextInt(nOperators);
		Function f = null;
		switch(n){
			case 0: 
				f = new Function(TFunction.SIG);
				break; 
			case 1: 
				f = new Function(TFunction.PROGN2);
				break;
			case 2:	
				f = new Function(TFunction.PROGN3);
				break;
		}
		return f;
	}
	
	public static Function randomOperator(int arity){
		Function f = null;
		switch(arity){
			case 2: 
				int n = Main.randomGenerator.nextInt(nOperators - 1);
				if (n == 0) 
					f = new Function(TFunction.SIG);
				else 
					f = new Function(TFunction.PROGN2);
				break; 
			case 3: 
				f = new Function(TFunction.PROGN3);
				break;
		}
		return f;
	}
	
	public static Function randomTerminal(){
		int n = Main.randomGenerator.nextInt(nTerminals);
		Function f = null;
		switch(n){
			case 0: 
				f = new Function(TFunction.LEFT);
				break;
			case 1:
				f = new Function(TFunction.RIGHT);
				break;
			case 2:
				f = new Function(TFunction.STEP);
				break;
		}
		return f;
	}
}
