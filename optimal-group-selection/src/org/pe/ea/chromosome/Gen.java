package org.pe.ea.chromosome;

import java.util.ArrayList;
import java.util.List;

public class Gen<T> implements Cloneable {

	private List<T> element;	

	public Gen(){
		element = new ArrayList<T>();
	}
	
	public List<T> getBits(){
		return element;
	}

	public int getSize() {
		return element.size();
	}
	
	public T get(int i) {
		return element.get(i);
	}
	
	public void set(int i, T value) {
		element.set(i, value);
	}
	
	public Object clone(){
		Gen<T> g = new Gen<T>();
		for (int i = 0; i < element.size(); i++) {	
			g.element.add(element.get(i));
		}
		return g;
	}
	
	public String toString(){
		String result = "";
		for(T el:element){
				result += " " + el.toString();
		}
		return result;
	}

	public void addElement(T value) {
		if(element != null)
			element.add(value);
	}
	
	@SuppressWarnings("unchecked")
	public boolean equals(Object o){
		Gen<T> gen = (Gen<T>) o;
		// si ambos son vacíos son iguales
		if(this.element.isEmpty() && gen.getBits().isEmpty()){
			return true;
		}else{
			// si uno es vacío y otro no no lo son
			if((this.element.isEmpty() && !gen.getBits().isEmpty()) || (!this.element.isEmpty() && gen.getBits().isEmpty()))
				return false;
			else{
				// sino comparamos 
				boolean equals = true;
				int i = 0;
				while(equals && i < this.getSize()){
					equals = this.get(i).equals(gen.get(i));
					i++;
				}
				return equals;
			}
		}
	}
}
