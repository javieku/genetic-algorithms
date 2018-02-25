package org.pe.functions;

public class Function5 implements Function{

	private int variablesN;
	
	public Function5(int variablesN){
		this.variablesN = variablesN;
	}
	
	@Override
	public double evaluate(double[] variables) {
		double x;
		double result = 0;
		for(int i = 1; i <=  variablesN; i++){
			x = variables[i-1];
			result += Math.sin(x) * Math.pow(Math.sin( ((i+1) * (x*x)) / Math.PI), 20);
		}
		return -result;
	}

	@Override
	public int getVariablesN() {
		return 	variablesN;
	}

	@Override
	public double[][] getRanges() {
		double[][] ranges = new double[2][variablesN];
		for(int i = 0; i < variablesN; i++){
			ranges[0][i] = 0;
			ranges[1][i] = Math.PI;
		}
		return ranges;
	}
	
	public boolean isMaximize() { return false; }
}
