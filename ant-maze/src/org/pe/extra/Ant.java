package org.pe.extra;

import org.pe.extra.Map.TCell;

/***
 * Clase que contiene la información y los métodos para los movimientos de la hormiga, la cantidad que ha comido y el tiempo
 * que le queda para seguir explorando el tablero.
 * 
 * */

public class Ant {
	
	private enum TSense {
		LEFT, RIGHT, UP, DOWN
	}

	private int hp; // tiempo para comer 400 por defecto
	private Map antMap; // Referencia a la matriz que contiene las posiciones con comida y las visitadas
	private int steps; // Pasos dados por la hormiga
	private int eaten; 
	
	private int posC, posR;
	private TSense sense;
	
	public Ant() {
		antMap = (Map) Map.getInstance().clone();
		steps = 0;
		eaten = 0;
		posC = 0;
		posR = 0;
		this.hp = 400;
		sense = TSense.RIGHT;
	}
	
	public Ant(int hp) {
		antMap = (Map) Map.getInstance().clone();
		steps = 0;
		eaten = 0;
		posC = 0;
		posR = 0;
		this.hp = hp;
		sense = TSense.RIGHT;
	}
	
	public TCell getCurrentPos() {
		return antMap.getPos(posR, posC);
	}

	public Map getMap() {
		return antMap;
	}
	
	public int getSteps() {
		return steps;
	}

	public int getEaten() {
		return eaten;
	}

	public int getHP() {
		return hp;
	}

	public void step() {
		switch (sense) {
			case LEFT:
				posC = mod(posC - 1, antMap.getWidth());
				break;
			case RIGHT:
				posC = mod(posC + 1, antMap.getWidth());
				break;
			case UP:
				posR = mod(posR - 1,  antMap.getHeight());
				break;
			case DOWN:
				posR = mod(posR + 1, antMap.getHeight());
				break;
		}
		if(antMap.getPos(posR, posC) != TCell.FOOD)
			antMap.visit(posR, posC);
		steps++;
	}
	
	private int mod(int a, int b) {
		if(a < 0)
			return a + b;
		else 
			return a % b;
	}
	

	public void right() {
		switch (sense) {
			case LEFT:
				sense = TSense.UP;
				break;
			case RIGHT:
				sense = TSense.DOWN;
				break;
			case UP:
				sense = TSense.RIGHT;
				break;
			case DOWN:
				sense = TSense.LEFT;
				break;
		}
		steps++;
	}
	
	/**
	 * Si es negativo se calcula el módulo manualmente. El operador módulo no existe en java sólo el resto.
	 * 
	 * */
	public void left() {
		switch (sense) {
			case LEFT:
				sense = TSense.DOWN;
				break;
			case RIGHT:
				sense = TSense.UP;
				break;
			case UP:
				sense = TSense.LEFT;
				break;
			case DOWN:
				sense = TSense.RIGHT;
				break;
		}
		steps++;
	}

	public boolean isFinishedFood() {
		return eaten == antMap.getNFood();
	}

	public void eat() {
		antMap.visit(posR, posC);
		eaten++;
	}

	public void reset() {
		antMap = (Map) Map.getInstance().clone();
		steps = 0;
		eaten = 0;
		posC = 0;
		posR = 0;
		sense = TSense.RIGHT;
	}

	public boolean isThereFood() {
		boolean res = false;
		int pos;
		switch (sense) {
			case LEFT:
				pos = mod(posC - 1, antMap.getWidth());
				res = antMap.getPos(posR, pos) == TCell.FOOD;
				break;
			case RIGHT:
				pos = mod(posC + 1, antMap.getWidth());
				res = antMap.getPos(posR, pos) == TCell.FOOD;
				break;
			case UP:
				pos = mod(posR - 1,  antMap.getHeight());
				res = antMap.getPos(pos, posC) == TCell.FOOD;
				break;
			case DOWN:
				pos = mod(posR + 1, antMap.getHeight());
				res = antMap.getPos(pos, posC) == TCell.FOOD;
				break;
		}
		return res;
	}
}
