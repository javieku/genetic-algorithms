package org.pe.ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

import org.pe.extra.Map;

public class MapCanvas extends Canvas {
	private static final long serialVersionUID = 1L;
	
	private float w = 300;
	private float h = 200;
	
	private int nrow;
	private int ncol;
	
	private Map m;
	
	public MapCanvas() {
		super();
	}
	
	public void setMap(Map m) {
		this.m = m;
		
		nrow = m.getHeight();	//c.getFilas();
		ncol = m.getWidth();	//c.getColumnas();
	
		w = 550;
		h = 340;
		setSize((int) w, (int) h);
	}
	
	public void paint(Graphics g) {
		paintCells(g);
		paintWireFrame(g);
	}
	
	private void paintWireFrame(Graphics g) {
		g.setColor(Color.BLACK);
		for (int i = 0; i <= nrow; i++)
			g.drawLine(0, (int) (i*((h-1)/nrow)), 
				(int) (w-1), (int) (i*((h-1)/nrow)));
		
		for (int i = 0; i <= ncol; i++)
			g.drawLine((int) (i*((w-1)/ncol)), 0, 
				(int) (i*((w-1)/ncol)), (int) (h-1));
	}
	
	private void paintCells(Graphics g) {
		float cw = w / ncol;
		float ch = h / nrow;
		
		for (int i = 0; i < m.getWidth(); i++) {
			for (int j = 0; j < m.getHeight(); j++) {
				switch (m.getPos(j, i)) {
					case FREE:
						g.setColor(Color.WHITE);
						break;
					case FOOD:
						g.setColor(Color.ORANGE);
						break;
					case VISITED:
						g.setColor(Color.GREEN);
						break;
				}
				g.fillRect((int) (i*cw), (int) (j*ch), (int) ((i+1)*cw), (int) ((j+1)*ch));
			}
		}
	}
}
