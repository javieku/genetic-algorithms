package org.pe.extra;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Tablero o mapa donde la hormiga realiza todos los movimientos. Carga desde fichero especificado por GUI
 * En caso de error buscará el fichero map.txt en el directorio actual de la práctica.
 * En cada evaluación de un cromosoma se realiza una copia de esta clase para poner a prueba el programa.
 * 
 * */

public class Map implements Cloneable{

	private static final String DEFAULT_PATH_INI = "map.txt";
	private static final String DEFAULT_PATH_RES = "resMap.txt";
	private static Map map = null;
	
	public enum TCell { FREE, FOOD, VISITED };
	
	private TCell[][] matrix;
	private int nFood;
	private int width;
	private int height;
	
	private Map() {
		matrix = null;
		nFood = 0;
		width = 0;
		height = 0;
	}
	
	public static Map getInstance() {
		if (map == null) {
			map = new Map();
		}
		return map;
	}

	public TCell getPos(int posR, int posC) {
		return matrix[posR][posC];
	}

	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}

	public int getNFood() {
		return nFood;
	}
	
	public void visit(int posR, int posC) {
        matrix[posR][posC] = TCell.VISITED; 
	}
	
	public void load(File file) {
		nFood = 0;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			System.err.println("No se ha encontrado el archivo: " + file.getAbsolutePath());
			e.printStackTrace();
		}
		if (br == null) {
			try {
				br = new BufferedReader(new FileReader(new File(DEFAULT_PATH_INI)));
			} catch (FileNotFoundException e) {
				System.err.println("No se ha encontrado el archivo: " + file.getAbsolutePath());
				e.printStackTrace();
			}
		}
		if (br != null) {
			try {
				String[] line;
				String[] first_line = br.readLine().split(" ");
				width = Integer.parseInt(first_line[0]);
				height = Integer.parseInt(first_line[1]);
				matrix = new TCell[height][width];
				for (int r = 0; r < height; r++) {
					line = br.readLine().split(" ");
					for (int c = 0; c < width; c++){
						matrix[r][c] = intToCell(Integer.parseInt(line[c]));
						if (matrix[r][c] == TCell.FOOD)
							nFood++;
					}
				}
			} catch (IOException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	public void print(File file) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (bw == null) {
			try {
				bw = new BufferedWriter(new FileWriter(new File(DEFAULT_PATH_RES)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (bw != null) {
			try {
				bw.write(String.valueOf(remainedFood()));
				bw.newLine();
				for (int r = 0; r < height; r++) {
					for (int c = 0; c < width; c++){
						bw.write(cellToInt(matrix[r][c]) + " ");
					}
					bw.newLine();
				}
				bw.close();
			} catch (IOException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private TCell intToCell(int cell) {
		TCell res;
		switch(cell){
		case 0:
			res = TCell.FREE;
			break;
		case 1:
			res = TCell.FOOD;
			break;
		case 2:
			res = TCell.VISITED;
			break;
		default:
			res = TCell.FREE;
			break;
		}
		return res;
	}
	
	private int cellToInt(TCell cell) {
		int res;
		switch(cell){
		case FREE:
			res = 0;
			break;
		case FOOD:
			res = 1;
			break;
		case VISITED:
			res = 2;
			break;
		default:
			res = -1;
			break;
		}
		return res;
	}
	
	public String toString() {
		String res = "Comida: " + nFood + "\n";
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++){
				res += matrix[r][c].toString() + " ";
			}
			res += "\n";
		}
		return res;
	}
	
	public Object clone() {
		Map map = new Map();
		TCell[][] m = new TCell[matrix.length][];
		int aLength;
		for(int i = 0; i < matrix.length; i++) {
			TCell[] aMatrix = matrix[i];
			aLength = aMatrix.length;
			m[i] = new TCell[aLength];
			System.arraycopy(aMatrix, 0, m[i], 0, aLength);
		}
		map.matrix = m;
		map.nFood = nFood;
		map.height = height;
		map.width = width;
		return map;
	}
	
	public int remainedFood() {
		int counter = 0;
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++){
				if (matrix[r][c] == TCell.FOOD) {
					counter++;
				}
			}
		}
		return (nFood - counter);
	}
}
