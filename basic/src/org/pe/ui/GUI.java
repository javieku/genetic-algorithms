package org.pe.ui;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;

import org.math.plot.Plot2DPanel;
import org.pe.GeneticAlgorithm.Chromosome;
import org.pe.GeneticAlgorithm.GeneticAlgorithm.SelectionType;
import org.pe.GeneticAlgorithm.Params;

@SuppressWarnings("serial")
public class GUI extends JFrame {
	private JButton okButton;
    
	private JComboBox funCombo;
    private JComboBox selCombo;
    private JComboBox crCombo;
    
    private JSpinner minTamPob;
    private JSpinner maxTamPob;
    private JSpinner incTamPob;
    private JSpinner minNumGen;
    private JSpinner maxNumGen;
    private JSpinner incNumGen;
    private JSpinner minPC;
    private JSpinner maxPC;
    private JSpinner incPC;
    private JSpinner minPM;
    private JSpinner maxPM;
    private JSpinner incPM;
    
    private JCheckBox eliteCB;
    private JCheckBox tamPobCB;
    private JCheckBox numGenCB;
    private JCheckBox pcCB;
    private JCheckBox pmCB;
    
    private JTextField tolField;
    private JSpinner nSpinner;
    
    private JSpinner eliteSpinner;
    
    private JTabbedPane tabbedPanel;
    private Plot2DPanel plotPanel;
    private JPanel paramPanel;
	
	public static void main(String args[]) {
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		      e.printStackTrace();
		}
		
		GUI g = new GUI();
		g.setEnabled(true);
		g.setVisible(true);
	}

    public GUI() {
        super();

        initComponents();
        
        setTitle("Algoritmo Genético Simple");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setSize(640, 480);
	    setLocationRelativeTo(null);
    }
    
    // Comprueba que los campos tienen valores válidos (por ejemplo, tolerancia negativa)
    boolean check_params() {
    	boolean b1 = !nSpinner.isEnabled() || ((Integer) nSpinner.getValue()) > 0;
    	boolean b2;
    	try {
    		b2 = Float.parseFloat(tolField.getText()) > 0;
    	}
    	catch (NumberFormatException e) {
    		b2 = false;
    	}
    	
    	return ((Integer) minTamPob.getValue()) > 0 &&
    		   ((Integer) minNumGen.getValue()) > 0 && b1 && b2;
    }
    
    boolean check_ranges() {
    	boolean b1 = numGenCB.isSelected() && ((Integer) minNumGen.getValue()) > ((Integer) maxNumGen.getValue());
    	boolean b2 = tamPobCB.isSelected() && ((Integer) minTamPob.getValue()) > ((Integer) maxTamPob.getValue());
    	boolean b3 = pcCB.isSelected() && ((Double) minPC.getValue()) > ((Double) maxPC.getValue());
    	boolean b4 = pmCB.isSelected() && ((Double) minPM.getValue()) > ((Double) maxPM.getValue());
    	
    	return !(b1 || b2 || b3 || b4);
    }
    
    // Ejecuta el programa con los parámetros actuales
    void compute_results() {
    	// Calculamos cuántas ejecuciones del algoritmo hay que hacer
    	int executions;
    	executions = tamPobCB.isSelected() ? (Integer) incTamPob.getValue() : 0;
    	executions = numGenCB.isSelected() ? (Integer) incNumGen.getValue() : executions;
    	executions = pcCB.isSelected() ? (Integer) incPC.getValue() : executions;
    	executions = pmCB.isSelected() ? (Integer) incPM.getValue() : executions;
    	executions++;
    	
    	// Borramos los diagramas anteriores si los hubiera
    	plotPanel.removeAllPlots();
    	
    	// Ejecutamos el algoritmo las veces que se hayan pedido
    	Params params = null;
    	Results results = null;
    
    	
    	long seed = (long) (Math.random() * 100);
    	
    	for (int i = 0; i < executions; i++) {
    		if (numGenCB.isSelected())
    			// Si cambiamos el número de generaciones sí que cambiamos la semilla
    			seed = (long) (Math.random() * 100);
    		Main.randomGenerator = new Random(seed);
    		
    		// Calculamos los parámetros para la ejecución i
    		params = gen_params(i);
    		// Ejecutamos el algoritmo con dichos parámetros
    		results = Main.compute(params);

    		// Creamos los datos para mostrar que faltan
    		double[] gen = new double[results.getNumGen()];
        	double[] best = new double[results.getNumGen()];
        	Chromosome c = null;
        	for (int j = 0; j < results.getNumGen(); j++) {
        		c =  results.getBestOnes()[j];
        		gen[j] = j;
        		best[j] = c.getAptitude();
        	}
        	
        	// Añadimos las gráficas a representar
        	plotPanel.addLinePlot("Mejor de la generación (paso " + i + ")", Color.blue, gen, best);
        	plotPanel.addLinePlot("Mejor absoluto (paso " + i + ")", Color.red, gen, results.getBestApt());
        	plotPanel.addLinePlot("Media de la generación (paso " + i + ")", Color.green, gen, results.getAvgApt());
    	}
    	
    	// Refrescamos el panel
    	plotPanel.repaint();
    	
    	// Seleccionamos la pestaña con el panel de resultados
    	tabbedPanel.setSelectedIndex(1);
    }
    
    private Params gen_params(int i) {
    	SelectionType st = null;
    	switch (selCombo.getSelectedIndex()) {
	    	case 0:
	    		st = SelectionType.ROULETTE;
	    		break;
	    	case 1:
	    		st = SelectionType.TOURNAMENT;
	    		break;
    	}
    	
    	Double eliteSize = (double) -1;
    	if(eliteCB.isSelected())
    		eliteSize = (Double) eliteSpinner.getValue();
    	
    	boolean btp = tamPobCB.isSelected();
    	boolean bng = numGenCB.isSelected();
    	boolean bpc = pcCB.isSelected();
    	boolean bpm = pmCB.isSelected();
    	
    	int tp = (Integer) minTamPob.getValue();
    	int ng = (Integer) minNumGen.getValue();
    	double pc = (Double) minPC.getValue();
    	double pm = (Double) minPM.getValue();
    	
    	if (btp)
    		tp += (((Integer) maxTamPob.getValue()) - tp) * i / ((Integer) incTamPob.getValue());
    	if (bng)
    		ng += (((Integer) maxNumGen.getValue()) - ng) * i / ((Integer) incNumGen.getValue());
    	if (bpc)
    		pc += (((Double) maxPC.getValue()) - pc) * i / ((Integer) incPC.getValue());
    	if (bpm)
    		pm += (((Double) maxPM.getValue()) - pm) * i / ((Integer) incPM.getValue());
    	
    	return new Params(funCombo.getSelectedIndex() + 1, 
    				tp, ng, pc, pm, 
	    			Double.parseDouble(tolField.getText()), (Integer) nSpinner.getValue(), 
	    			st, eliteSize);
    }
                      
    private void initComponents() {
    	Dimension compSize = new Dimension(100, 20);
    	
        // Comboboxes
        funCombo = new JComboBox(new DefaultComboBoxModel(new String[] { "Función 1", "Función 2", "Función 3", "Función 4", "Función 5"}));
        selCombo = new JComboBox(new DefaultComboBoxModel(new String[] { "Ruleta", "Torneo" }));
        crCombo = new JComboBox(new DefaultComboBoxModel(new String[] { "Monopunto" }));
        
        funCombo.setPreferredSize(compSize);
        selCombo.setPreferredSize(compSize);
        crCombo.setPreferredSize(compSize);
        
        funCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (funCombo.getSelectedIndex() == 4)
					nSpinner.setEnabled(true);
				else
					nSpinner.setEnabled(false);
			}
		});
       
        
        // Buttons
        okButton = new JButton();
        okButton.setText("Calcular"); 
        okButton.setName("okButton");
        
        okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (check_params())
					if (check_ranges())
						compute_results();
					else
						JOptionPane.showMessageDialog(null, 
								"Alguno de los intervalos está vacío.", 
								"¡Error!", JOptionPane.ERROR_MESSAGE);
				else
					JOptionPane.showMessageDialog(null, 
							"Alguno de los campos contiene valores incorrectos.", 
							"¡Error!", JOptionPane.ERROR_MESSAGE);
			}
		});
        
        // Spinners
        SpinnerNumberModel eliteNM = new SpinnerNumberModel(0.02, 0, 1, 0.01);
        
        minTamPob = new JSpinner();
        maxTamPob = new JSpinner();
        incTamPob = new JSpinner(new SpinnerNumberModel(2, 2, 10, 1));
        minTamPob.setPreferredSize(compSize);
        maxTamPob.setPreferredSize(compSize);
        incTamPob.setPreferredSize(compSize);
        maxTamPob.setEnabled(false);
        incTamPob.setEnabled(false);
        
        minNumGen = new JSpinner();
        maxNumGen = new JSpinner();
        incNumGen = new JSpinner(new SpinnerNumberModel(2, 2, 10, 1));
        minNumGen.setPreferredSize(compSize);
        maxNumGen.setPreferredSize(compSize);
        incNumGen.setPreferredSize(compSize);
        maxNumGen.setEnabled(false);
        incNumGen.setEnabled(false);
        
        minPC = new JSpinner(new SpinnerNumberModel(0.6, 0, 1, 0.1));
        maxPC = new JSpinner(new SpinnerNumberModel(0.6, 0, 1, 0.1));
        incPC = new JSpinner(new SpinnerNumberModel(2, 2, 10, 1));
        minPC.setPreferredSize(compSize);
        maxPC.setPreferredSize(compSize);
        incPC.setPreferredSize(compSize);
        maxPC.setEnabled(false);
        incPC.setEnabled(false);
        
        minPM = new JSpinner(new SpinnerNumberModel(0.05, 0, 1, 0.01));
        maxPM = new JSpinner(new SpinnerNumberModel(0.05, 0, 1, 0.01));
        incPM = new JSpinner(new SpinnerNumberModel(2, 2, 10, 1));
        minPM.setPreferredSize(compSize);
        maxPM.setPreferredSize(compSize);
        incPM.setPreferredSize(compSize);
        maxPM.setEnabled(false);
        incPM.setEnabled(false);
        
        tolField = new JTextField("0.0001");
        tolField.setPreferredSize(compSize);
        nSpinner = new JSpinner();
        nSpinner.setPreferredSize(compSize);
        
        eliteSpinner = new JSpinner(eliteNM);
        eliteSpinner.setPreferredSize(compSize);

        minTamPob.setValue(100);
        maxTamPob.setValue(100);
        minNumGen.setValue(100);
        maxNumGen.setValue(100);
        tolField.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        nSpinner.setValue(1);
        nSpinner.setEnabled(false);
        eliteSpinner.setEnabled(false);
       
        // Check boxes
        eliteCB = new JCheckBox("Elitismo");
        tamPobCB = new JCheckBox("Tamaño población");
        numGenCB = new JCheckBox("Número de generaciones");
        pcCB = new JCheckBox("Probabilidad de cruce");
        pmCB = new JCheckBox("Probabilidad de mutación");
        
        eliteCB.setName("eliteCheckBox");
        
        eliteCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				eliteSpinner.setEnabled(eliteCB.isSelected());
			}
		});
        
        tamPobCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!tamPobCB.isSelected()) {
					maxTamPob.setEnabled(false);
					incTamPob.setEnabled(false);
				} else {	
					maxTamPob.setEnabled(true);
					incTamPob.setEnabled(true);
					maxNumGen.setEnabled(false);
					incNumGen.setEnabled(false);
					maxPC.setEnabled(false);
					incPC.setEnabled(false);
					maxPM.setEnabled(false);
					incPM.setEnabled(false);
					
					numGenCB.setSelected(false);
					pcCB.setSelected(false);
					pmCB.setSelected(false);
				}
			}
		});
        
        numGenCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!numGenCB.isSelected()) {
					maxNumGen.setEnabled(false);
					incNumGen.setEnabled(false);
				} else {	
					maxTamPob.setEnabled(false);
					incTamPob.setEnabled(false);
					maxNumGen.setEnabled(true);
					incNumGen.setEnabled(true);
					maxPC.setEnabled(false);
					incPC.setEnabled(false);
					maxPM.setEnabled(false);
					incPM.setEnabled(false);
					
					tamPobCB.setSelected(false);
					pcCB.setSelected(false);
					pmCB.setSelected(false);
				}
			}
		});
        
        pcCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!pcCB.isSelected()) {
					maxPC.setEnabled(false);
					incPC.setEnabled(false);
				} else {	
					maxTamPob.setEnabled(false);
					incTamPob.setEnabled(false);
					maxNumGen.setEnabled(false);
					incNumGen.setEnabled(false);
					maxPC.setEnabled(true);
					incPC.setEnabled(true);
					maxPM.setEnabled(false);
					incPM.setEnabled(false);
					
					numGenCB.setSelected(false);
					tamPobCB.setSelected(false);
					pmCB.setSelected(false);
				}
			}
		});
        
        pmCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!pmCB.isSelected()) {
					maxPM.setEnabled(false);
					incPM.setEnabled(false);
				} else {	
					maxTamPob.setEnabled(false);
					incTamPob.setEnabled(false);
					maxNumGen.setEnabled(false);
					incNumGen.setEnabled(false);
					maxPC.setEnabled(false);
					incPC.setEnabled(false);
					maxPM.setEnabled(true);
					incPM.setEnabled(true);
					
					numGenCB.setSelected(false);
					pcCB.setSelected(false);
					tamPobCB.setSelected(false);
				}
			}
		});
        
        // Panels
        plotPanel = new Plot2DPanel();
        plotPanel.addLegend("SOUTH");
        
        paramPanel = getParamPanel();
        
        tabbedPanel = new JTabbedPane();       
        tabbedPanel.addTab("Parámetros", paramPanel);
        tabbedPanel.addTab("Función", plotPanel);
       
        setContentPane(tabbedPanel);
    }

	private JPanel getParamPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		
		JPanel cmb = new JPanel(new FlowLayout(FlowLayout.CENTER));
		cmb.add(funCombo);
		cmb.add(selCombo);
		cmb.add(crCombo);
		
		JPanel pop = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pop.add(new JLabel("Min:"));
		pop.add(minTamPob);
		pop.add(new JLabel("Max:"));
		pop.add(maxTamPob);
		pop.add(new JLabel("Pasos:"));
		pop.add(incTamPob);
		pop.add(tamPobCB);
		
		JPanel gen = new JPanel(new FlowLayout(FlowLayout.LEFT));
		gen.add(new JLabel("Min:"));
		gen.add(minNumGen);
		gen.add(new JLabel("Max:"));
		gen.add(maxNumGen);
		gen.add(new JLabel("Pasos:"));
		gen.add(incNumGen);
		gen.add(numGenCB);
		
		JPanel cro = new JPanel(new FlowLayout(FlowLayout.LEFT));
		cro.add(new JLabel("Min:"));
		cro.add(minPC);
		cro.add(new JLabel("Max:"));
		cro.add(maxPC);
		cro.add(new JLabel("Pasos:"));
		cro.add(incPC);
		cro.add(pcCB);
		
		JPanel mut = new JPanel(new FlowLayout(FlowLayout.LEFT));
		mut.add(new JLabel("Min:"));
		mut.add(minPM);
		mut.add(new JLabel("Max:"));
		mut.add(maxPM);
		mut.add(new JLabel("Pasos:"));
		mut.add(incPM);
		mut.add(pmCB);
		
		JPanel tol = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tol.add(new JLabel("Tolerancia:"));
		tol.add(tolField);
		
		JPanel ene = new JPanel(new FlowLayout(FlowLayout.LEFT));
		ene.add(nSpinner);
		ene.add(new JLabel("Valor de n"));
		
		JPanel eli = new JPanel(new FlowLayout(FlowLayout.LEFT));
		eli.add(eliteSpinner);
		eli.add(eliteCB);
		
		p.add(cmb);
		p.add(pop);
		p.add(gen);
		p.add(cro);
		p.add(mut);
		p.add(tol);
		p.add(eli);
		p.add(ene);
		
		p.add(new JPanel().add(okButton));
		
		return p;
	}                        
}
