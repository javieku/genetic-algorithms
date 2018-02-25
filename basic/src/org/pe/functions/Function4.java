package org.pe.functions;

public class Function4 implements Function{

	@Override
	public double evaluate(double[] variables) {
		double aux1 = 0;
		double aux2 = 0;
		double x1 = variables[0]; 
		double x2 = variables[1];
		for(int i = 1; i <= 5; i++){
			aux1 += (i * Math.cos( ((i + 1) * x1) + i));
			aux2 += (i * Math.cos( ((i + 1) * x2) + i));
		}
		return aux1*aux2;
	}

	@Override
	public int getVariablesN() {
		return 2;
	}

	@Override
	public double[][] getRanges() {
		double[][] ranges = new double[2][2];
		ranges[0][0] = -10;
		ranges[0][1] = -10;
		ranges[1][0] = 10;
		ranges[1][1] = 10;
		return ranges;
	}
	
	public boolean isMaximize() { return false; }
}
