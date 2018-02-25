package org.pe.functions;

public interface Function {
	
	public double evaluate(double[] variables); // evaluación de la función

	public int getVariablesN(); // número de variables de la función

	public double[][] getRanges(); // rangos en los se considera cada variable de la función
								// la primera dimensión distingue máximos y mínimos es decir
								// 		ranges[0] son todos los mínimos
								//      ranges[1] son todos los máximos
								// la segunda entre variables

	public boolean isMaximize();
}
