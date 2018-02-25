package org.pe.functions;

public class DummyFunction implements Function{
	
   // f(x) = x : x [0,10]

	@Override
	public double evaluate(double[] variables) {
		double x = variables[0];
		double result = x;
		return (double) result;
	}

	@Override
	public int getVariablesN() {
		return 1;
	}

	@Override
	public double[][] getRanges() {
		double[][] ranges = new double[2][2];
		ranges[0][0] = 0;
		ranges[1][0] = 10;
		return ranges;
	}

	public boolean isMaximize() { return true; }
}