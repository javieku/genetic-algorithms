package org.pe.util;


public class Test {

	public static void main(String[] args){
		
		Integer[] lAux;
		Integer[] buffer = new Integer[3];
		buffer[0] = 5;
		buffer[1] = 2;
		buffer[2] = 4;
		PermUtil<Integer> permutator = new PermUtil<Integer>(buffer);
		
		// Generamos los candidatos a genes mutados
		for(int i = 0; i < UsefulMethods.factorial(3); i++) {
			lAux = permutator.nextwoRep();
			for(int j = 0; j < 3; j++)
				System.out.print(lAux[j] + " ");
			System.out.println();
		}
	}
}
