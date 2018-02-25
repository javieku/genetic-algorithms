package org.pe.functions;

public class Function1 implements Function{
	
   // f(x) = x + abs(sen(32PIx))   : x [0,1]

	@Override
	public double evaluate(double[] variables) {
		double x = variables[0];
		double result = (x + Math.abs(Math.sin(32*Math.PI*x)));
		return result;
	}

	@Override
	public int getVariablesN() {
		return 1;
	}

	@Override
	public double[][] getRanges() {
		double[][] ranges = new double[2][1];
		ranges[0][0] = 0;
		ranges[1][0] = 1;
		return ranges;
	}

	public boolean isMaximize() { return true; }
}
