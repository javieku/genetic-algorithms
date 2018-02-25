package org.pe.functions;

public class Function3 implements Function {

	@Override
	public double evaluate(double[] variables) {
		double x = variables[0];
		double num = Math.sin(x);
		double den = 1 + Math.sqrt(x) + (Math.cos(x)/(1+x));
		return num/den;
	}

	@Override
	public int getVariablesN() {
		return 1;
	}

	@Override
	public double[][] getRanges() {		
		double[][] ranges = new double[2][1];
		ranges[0][0] = 0;
	    ranges[1][0] = 25;	
		return ranges;
	}

	public boolean isMaximize() { return false; }
}
