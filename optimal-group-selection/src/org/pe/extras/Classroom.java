package org.pe.extras;

import java.util.ArrayList;

import org.pe.ea.chromosome.Gen;

public class Classroom {

	public static Student[] student;
	
	public static int originalLength;
	
	public static double avg;
	
	/**
	 * Calcula la media
	 * 
	 * */
	public static void computeAVG(){
		avg = 0;
		for(int i = 0; i < student.length; i++)
			avg += student[i].getMark(); 
		
		avg = avg / student.length;
	}
	
	/**
	 * Rellena el array de estudiantes con alumnos vacíos si es necesario para que sea su número divisible por el entero "m"
	 * 
	 * */
	public static void resizeStudents(int m){
		Student[] auxStudent = new Student[originalLength];
		int remainder;
		// Borramos redimensiones previas i.e. alumnos vacíos
		System.arraycopy(student, 0, auxStudent, 0, originalLength);
		
		remainder = originalLength % m;
		
		if(remainder != 0){
			int numStudents = (originalLength - remainder) + m;
			
			auxStudent = new Student[numStudents];
			
			// Borramos redimensiones previas i.e. alumnos vacíos
			System.arraycopy(student, 0, auxStudent, 0, originalLength);
			
			// Añade los alumnos vacíos necesarios
			for(int i = originalLength; i < numStudents; i++){
				auxStudent[i] = new Student( -i , 0, new ArrayList<Integer>());
			}
		}
		
		student = auxStudent;
		
		computeAVG();
	}

	public static String translate(Gen<Integer>[] bestGenes, int gs) {
		String result = "";
		int id;
		int j = 0;
		
		for (int i = 0; i < bestGenes.length; i++) {
			if (j % gs == 0) 
				result += "{";
			
			id = Classroom.student[bestGenes[i].get(0)].getId();
			if (id < 0)
				result += "vacío";
			else
				result += id;
			
			if (j % gs < gs - 1)
				result += ", ";
			else
				result += "} ";
			
			j = (j+1) % gs;
		}
		return result;
	}
}
