package org.pe.extras;

import java.util.ArrayList;
import java.util.Collections;

public class Group implements Comparable<Group> {
	private ArrayList<Double> array;
	private double total;
	
	public Group() {
		array = new ArrayList<Double>();
		total = 0;
	}
	
	public void add(Double d) {
		array.add(d);
		total += d;
	}
	
	public void sort() {
		Collections.sort(array);
	}

	@Override
	public int compareTo(Group other) {
		if (other.total > total)
			return 1;
		if (other.total < total)
			return -1;
		return 0;
	}
	
	public Double[] getValues() {
		Object[] obj = array.toArray();
		Double[] val = new Double[obj.length];
		for (int i = 0; i < obj.length; i++)
			val[i] = (Double) obj[i];
		
		return val;
	}
	
	public double getTotal() { return total; }
}
