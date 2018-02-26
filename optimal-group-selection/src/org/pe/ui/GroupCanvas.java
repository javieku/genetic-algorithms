package org.pe.ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import org.pe.ea.chromosome.ChromosomePermutation;
import org.pe.extras.Classroom;
import org.pe.extras.Group;
import org.pe.extras.GroupGroup;

public class GroupCanvas extends Canvas {
	private static final long serialVersionUID = 1L;
	
	private int m;				// Número de individuos por grupo
	private int g;				// Número de grupos
	
	private float w = 300;		// Ancho del canvas
	private float h = 200;		// Alto del canvas
	
	private float cw;			// Ancho de las columnas
	private float ch;			// Alto de las columnas
	
	private float hw; 			// Ancho de los huecos
	
	private GroupGroup gg;
	
	private ChromosomePermutation c = null;

	public GroupCanvas() {
		super();
	}
	
	public void setChromosome(ChromosomePermutation c, GUI gui) {
		this.c = c;
		
		m = c.getM();
		g = c.getG();
		
		int xgap = 50;
		int ygap = 150;
		
		w = gui.getWidth() - xgap;
		h = gui.getHeight() - ygap;
		setSize(gui.getWidth() - xgap, gui.getHeight() - ygap);
		
		ch = (h-2) / m;
		
		cw = 2*(w-2) / (3*g);
		hw = (w-2) / (3*(g+1));
	}
	
	public void paint(Graphics g) {
		paintColumns(g);
		paintNumbers(g);
	}
	
	private void paintColumns(Graphics g) {
		int x = (int) hw + 1;
		
		g.setColor(Color.white);
		g.fillRect(0, 0, (int) (w-1), (int) (h-1));
		g.setColor(Color.black);
		g.drawRect(0, 0, (int) (w-1), (int) (h-1));
		
		if (c != null) {
			gg = new GroupGroup();
			
			for (int i = 0; i < c.getG(); i++) {
				gg.newGroup();
				for (int j = 0; j < c.getM(); j++) {
					int gen = i*c.getM() + j;
					int student = c.getGenes()[gen].getBits().get(0);
					double mark = Classroom.student[student].getMark();
					
					gg.addToGroup(mark);
				}
				
				gg.sortGroup();
			}
			
			gg.addLastGroup();
			gg.sort();
			
			for (Group group: gg.getArray()) {
				Double[] val = group.getValues();
				
				for (int j = 0; j < m; j++)
					val[j] *= ch / 10;
				
				for (int j = m-1; j >= 0; j--) {
					g.setColor(new Color(((float) j) / m, ((float) j) / m, ((float) j) / m));
					
					double rectangleH = 0;
					for (int k = j; k >= 0; k--)
						rectangleH += val[k];
					
					g.fillRect(x, (int) (h - rectangleH), (int) cw, (int) rectangleH);
				}
				x += cw + hw; 
			}
			
		}
	}
	
	private void paintNumbers(Graphics g) {
		int margin = 2;
		
		int x = (int) (hw + cw/2 - margin);
		int i = 0;
		int y = 0;
				
		for (Group group: gg.getArray()) {
			y = (int) (h - group.getTotal()*ch / 10 - margin);
			g.drawString(String.valueOf(i+1), x, y);
			x += hw + cw;
			i++;
			
			if (i == 9) x -= 6;
		}
	}

	public void setDimensions(int width, int height) {
		w = width;
		h = height;
	}
}
