package org.pe.functions;

public interface Function {
	
	public double evaluate(double[] variables); // evaluaci�n de la funci�n

	public int getVariablesN(); // n�mero de variables de la funci�n

	public double[][] getRanges(); // rangos en los se considera cada variable de la funci�n
								// la primera dimensi�n distingue m�ximos y m�nimos es decir
								// 		ranges[0] son todos los m�nimos
								//      ranges[1] son todos los m�ximos
								// la segunda entre variables

	public boolean isMaximize();
}
