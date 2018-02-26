package org.pe.extras;

import java.util.ArrayList;

public class Student {
	
	private int id;
	private double mark;
	private ArrayList<Integer> enemy;
	
	public Student() {
		id = -1;
		mark = 0;
		enemy = new ArrayList<Integer>();
	}
	
	public Student(int id, double mark, ArrayList<Integer> enemy) {
		this.id = id;
		this.mark = mark;
		this.enemy = enemy;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public double getMark() {
		return mark;
	}
	
	public void setMark(double mark) {
		this.mark = mark;
	}
	
	public ArrayList<Integer> getEnemy() {
		return enemy;
	}
	
	public void setEnemy(ArrayList<Integer> enemy) {
		this.enemy = enemy;
	}
	
	public String toString(){
		return "ID:" + id + "  Mark:" + mark + " Enemies:" + enemy.toString();
	}
}
