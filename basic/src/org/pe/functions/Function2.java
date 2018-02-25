package org.pe.functions;

public class Function2 implements Function {

	//  f(x,y) = 21.5 + x.sen(4pix)+y.sen(20pi y)  
	
	@Override
	public double evaluate(double[] variables) {
		double x = variables[0];
		double y = variables[1];
		double result = 21.5f + (x * Math.sin(4 * Math.PI * x)) + (y * Math.sin(20 * Math.PI * y));
		return result;
	}

	@Override
	public int getVariablesN() {return 2;}

	@Override
	public double[][] getRanges() {
		double[][] ranges = new double[2][2];
		ranges[0][0] = -3.0;
		ranges[0][1] = 4.1;
		ranges[1][0] = 12.1;
		ranges[1][1] = 5.8;
		return ranges;
	}

	public boolean isMaximize() { return true; }
}
