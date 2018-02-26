package org.pe.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;

import org.math.plot.Plot2DPanel;
import org.pe.ea.Params;
import org.pe.ea.chromosome.Chromosome;
import org.pe.ea.chromosome.ChromosomePermutation;
import org.pe.ea.chromosome.IncompFunction;
import org.pe.ea.crossover.AntiEnemies;
import org.pe.ea.crossover.Crosser;
import org.pe.ea.crossover.OXAltCrosser;
import org.pe.ea.crossover.OXCrosser;
import org.pe.ea.crossover.OrdinalCrosser;
import org.pe.ea.crossover.PMXCrosser;
import org.pe.ea.mutation.HeuristicMutator;
import org.pe.ea.mutation.InsertionMutator;
import org.pe.ea.mutation.InterchangeMutator;
import org.pe.ea.mutation.Mutator;
import org.pe.ea.mutation.ReverseMutator;
import org.pe.ea.selection.DeterministicTournament;
import org.pe.ea.selection.HighLow;
import org.pe.ea.selection.ProbabilisticTournament;
import org.pe.ea.selection.Ranking;
import org.pe.ea.selection.Roulette;
import org.pe.ea.selection.Selector;
import org.pe.extras.Classroom;
import org.pe.extras.Student;

@SuppressWarnings("serial")
public class GUI extends JFrame {
	private JButton okButton;
    
	private JComboBox funCombo;
    private JComboBox selCombo;
    private JComboBox crCombo;
    private JComboBox mutCombo;
    
    private JMenuBar menu;
    
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
    
    private JSpinner tournamentP;
    private JSpinner tournamentS;
    private JSpinner betaRanking;
    private JSpinner alphaEval;
    private JSpinner oxTrials;
    private JSpinner permHeurMut;
    private JSpinner javiParameter;
    
    private JLabel tournamentPL;
    private JLabel tournamentSL;
    private JLabel betaRankingL;
    private JLabel alphaEvalL;
    private JLabel oxTrialsL;
    private JLabel permHeurMutL;
    private JLabel javiParameterL;
    
    private JSpinner mSpinner;
    
    private JSpinner eliteSpinner;
    
    private JTabbedPane tabbedPanel;
    private Plot2DPanel plotPanel;
    private JPanel paramPanel;
    private JPanel groupPanel;
    
    private JCheckBox eliteCB;
    
    private JLabel fileLabel;
    
    private GroupCanvas gc;
    
    
    private final int MODE_NORMAL = 0;
    private final int MODE_POPSIZE = 1;
    private final int MODE_GENNUM = 2;
    private final int MODE_CP = 3;
    private final int MODE_MP = 4;
    
    private int mode = MODE_NORMAL;
	
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
        
        setJMenuBar(getMenu());
        setTitle("Algoritmo Evolutivo");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setSize(640, 480);
	    //pack();
	    setLocationRelativeTo(null);
    }
    
    private JMenuBar getMenu() {
    	
    	JMenu file = new JMenu("Archivo");
    	JMenu exec = new JMenu("Ejecución");
    	
    	JMenuItem load = new JMenuItem("Cargar");
    	JMenuItem exit = new JMenuItem("Salir");
    	JMenuItem normal = new JMenuItem("Normal");
    	
    	JMenuItem tpItem = new JMenuItem("Variar tamaño población");
    	JMenuItem ngItem = new JMenuItem("Variar número de generaciones");
    	JMenuItem pcItem = new JMenuItem("Variar probabilidad de cruce");
    	JMenuItem pmItem = new JMenuItem("Variar probabilidad de mutación");
    	
    	load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(".");
		        if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(null)) {
		        	File file = fileChooser.getSelectedFile();
		        	load(file);
		        	okButton.setEnabled(true);
		        	fileLabel.setText("Archivo cargado: " + file.getName());
		        }
			}
		});
    	
    	exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
    	
    	normal.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				maxTamPob.setEnabled(false);
				incTamPob.setEnabled(false);
				maxNumGen.setEnabled(false);
				incNumGen.setEnabled(false);
				maxPC.setEnabled(false);
				incPC.setEnabled(false);
				maxPM.setEnabled(false);
				incPM.setEnabled(false);
				
				mode = MODE_NORMAL;
			}
    	});
    	
    	tpItem.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				maxTamPob.setEnabled(true);
				incTamPob.setEnabled(true);
				maxNumGen.setEnabled(false);
				incNumGen.setEnabled(false);
				maxPC.setEnabled(false);
				incPC.setEnabled(false);
				maxPM.setEnabled(false);
				incPM.setEnabled(false);
				
				mode = MODE_POPSIZE;
			}
    	});
    	
    	ngItem.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				maxTamPob.setEnabled(false);
				incTamPob.setEnabled(false);
				maxNumGen.setEnabled(true);
				incNumGen.setEnabled(true);
				maxPC.setEnabled(false);
				incPC.setEnabled(false);
				maxPM.setEnabled(false);
				incPM.setEnabled(false);
				
				mode = MODE_GENNUM;
			}
    	});
    	
    	pcItem.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				maxTamPob.setEnabled(false);
				incTamPob.setEnabled(false);
				maxNumGen.setEnabled(false);
				incNumGen.setEnabled(false);
				maxPC.setEnabled(true);
				incPC.setEnabled(true);
				maxPM.setEnabled(false);
				incPM.setEnabled(false);
				
				mode = MODE_CP;
			}
    	});
    	
    	pmItem.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				maxTamPob.setEnabled(false);
				incTamPob.setEnabled(false);
				maxNumGen.setEnabled(false);
				incNumGen.setEnabled(false);
				maxPC.setEnabled(false);
				incPC.setEnabled(false);
				maxPM.setEnabled(true);
				incPM.setEnabled(true);
				
				mode = MODE_MP;
			}
		});

    	file.add(load);
    	file.add(new JSeparator());
    	file.add(exit);
    	
    	exec.add(normal);
    	exec.add(new JSeparator());
    	exec.add(tpItem);
    	exec.add(ngItem);
    	exec.add(pcItem);
    	exec.add(pmItem);
    	
    	menu = new JMenuBar();
    	
    	menu.add(file);
    	menu.add(exec);
    	
		return menu;
	}

	// Comprueba que los campos tienen valores válidos (por ejemplo, tolerancia negativa)
    boolean check_params() {
    	boolean b1 = ((Integer) mSpinner.getValue()) > 1;
    	/*boolean b2;
    	try {
    		b2 = Float.parseFloat(tolField.getText()) > 0;
    	}
    	catch (NumberFormatException e) {
    		b2 = false;
    	}*/
    	
    	return ((Integer) minTamPob.getValue()) > 0 &&
    		   ((Integer) minNumGen.getValue()) > 0 && b1; // && b2;
    }
    
    boolean check_ranges() {
    	boolean b1 = mode == MODE_GENNUM && ((Integer) minNumGen.getValue()) > ((Integer) maxNumGen.getValue());
    	boolean b2 = mode == MODE_POPSIZE && ((Integer) minTamPob.getValue()) > ((Integer) maxTamPob.getValue());
    	boolean b3 = mode == MODE_CP && ((Double) minPC.getValue()) > ((Double) maxPC.getValue());
    	boolean b4 = mode == MODE_MP && ((Double) minPM.getValue()) > ((Double) maxPM.getValue());
    	
    	return !(b1 || b2 || b3 || b4);
    }
    
    // Ejecuta el programa con los parámetros actuales
    @SuppressWarnings("unchecked")
	void compute_results() {
    	// Calculamos cuántas ejecuciones del algoritmo hay que hacer
    	int executions;
    	executions = mode == MODE_POPSIZE ? (Integer) incTamPob.getValue() : 0;
    	executions = mode == MODE_GENNUM ? (Integer) incNumGen.getValue() : executions;
    	executions = mode == MODE_CP ? (Integer) incPC.getValue() : executions;
    	executions = mode == MODE_MP ? (Integer) incPM.getValue() : executions;
    	executions++;
    	
    	// Borramos los diagramas anteriores si los hubiera
    	plotPanel.removeAllPlots();
    	
    	// Ejecutamos el algoritmo las veces que se hayan pedido
    	Params params = null;
    	Results results = null;
    
    	long seed = (long) (Math.random() * 100);
    	
    	for (int i = 0; i < executions; i++) {
    		
    		if(mode == MODE_GENNUM)
    			seed = (long) (Math.random() * 100);
    		
    		Main.randomGenerator = new Random(seed);
    		
    		// Calculamos los parmetros para la ejecución
    		params = gen_params(i);
    		// Ejecutamos el algoritmo con dichos parámetros
    		results = Main.compute(params);

    		// Creamos los datos para mostrar que faltan
    		double[] gen = new double[results.getNumGen()];
        	double[] best = new double[results.getNumGen()];
        	Chromosome<Double> c = null;
        	for (int j = 0; j < results.getNumGen(); j++) {
        		c =  results.getBestOnes()[j];
        		gen[j] = j;
        		best[j] = c.getAptitude();
        	}
        	
        	// Añadimos las gráficas a representar
        	plotPanel.addLinePlot("Mejor de la generación (paso " + i + ")", Color.blue, gen, best);
        	plotPanel.addLinePlot("Mejor absoluto (paso " + i + ")", Color.red, gen, results.getBestApt());
        	plotPanel.addLinePlot("Media de la generación (paso " + i + ")", Color.green, gen, results.getAvgApt());
        	
        	gc.setChromosome((ChromosomePermutation) results.getBestOne(), this);
        	gc.repaint();
    	}
    	
    	// Refrescamos el panel
    	plotPanel.repaint();
    	
    	// Seleccionamos la pestaña con el panel de resultados
    	tabbedPanel.setSelectedIndex(1);
    }
    
    private void load(File file) {
    	BufferedReader br = null;
    	try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			System.err.println("No se ha encontrado el archivo: " + file.getAbsolutePath());
			e.printStackTrace();
		}
    	
    	if (br != null) {
	    	// Hacemos cosas
	    	try {
				String[] first_line = br.readLine().split(" ");
				int N = Integer.valueOf(first_line[0]);	// Número de alumnos totales
				int R = Integer.valueOf(first_line[1]);	// Número de restricciones
				
				Classroom.student = new Student[N];
				
				String[] line;
				for (int i = 0; i < N; i++) {
					line = br.readLine().split(" ");
					Classroom.student[i] = new Student();
					Classroom.student[i].setId(Integer.valueOf(line[0]));
					Classroom.student[i].setMark(Double.valueOf(line[1]));
				}
				
				for (int i = 0; i < R; i++) {
					line = br.readLine().split(" ");
					
					boolean found = false;
					int j = 0;
					while (!found && j < N) {
						found = (Classroom.student[j].getId() == Integer.valueOf(line[0]));
						j++;
					}
					
					if (found)
						Classroom.student[j-1].getEnemy().add(Integer.valueOf(line[1]));
				}
				
				Classroom.originalLength = Classroom.student.length;
				
			} catch (IOException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
    	}
    	
    }
    
    private Params gen_params(int i) {
    	Selector<Integer> sel = null;
    	
    	// Redimensiona el array de estudiantes según el valor de m
    	Classroom.resizeStudents((Integer) mSpinner.getValue());
    	
    	switch (selCombo.getSelectedIndex()) {
	    	case 0:
	    		sel = new Roulette<Integer>();
	    		break;
	    	case 1:
	    		sel = new DeterministicTournament<Integer>( (Integer) tournamentS.getValue());
	    		break;
	    	case 2:
	    		sel = new ProbabilisticTournament<Integer>((Double) tournamentP.getValue(), (Integer) tournamentS.getValue());
	    		break;
	    	case 3:
	    		sel = new Ranking<Integer>((Double) betaRanking.getValue());
	    		break;
	    	case 4:
	    		sel = new HighLow<Integer>((Double) javiParameter.getValue());
	    		break;
	    	default:
    			sel = new Roulette<Integer>();
    			break;
    	}
    	
    	Crosser<Integer> crosser = null;
    	switch (crCombo.getSelectedIndex()) {
	    	case 0:
	    		crosser = new PMXCrosser<Integer>();
	    		break;
	    	case 1:
	    		crosser = new OXCrosser<Integer>();
	    		break;
	    	case 2:
	    		crosser =  new OXAltCrosser<Integer>((Integer) oxTrials.getValue());
	    		break;
	    	case 3:
	    		crosser =  new OrdinalCrosser<Integer>();
	    		break;
	    	case 4:
	    		crosser = new AntiEnemies((Integer) mSpinner.getValue(), Classroom.student.length/(Integer) mSpinner.getValue());
	    		break;	
	    	default:
	    		crosser = new PMXCrosser<Integer>();
	    	break;
    	}
    	
    	Mutator<Integer> mutator;
    	switch (mutCombo .getSelectedIndex()) {
	    	case 0:
	    		mutator = new InterchangeMutator<Integer>();
	    		break;
	    	case 1:
	    		mutator = new InsertionMutator<Integer>();
	    		break;
	    	case 2:
	    		mutator = new ReverseMutator<Integer>();
	    		break;
	    	case 3:
	    		mutator = new HeuristicMutator<Integer>((Integer) permHeurMut.getValue(), false);
	    		break;
	    	default:
	    		mutator = new InterchangeMutator<Integer>();
	    	break;
    	}
    	
    	Double eliteSize = (double) -1;
    	if(eliteCB.isSelected())
    		eliteSize = (Double) eliteSpinner.getValue();
    	
    	int tp = (Integer) minTamPob.getValue();
    	int ng = (Integer) minNumGen.getValue();
    	double pc = (Double) minPC.getValue();
    	double pm = (Double) minPM.getValue();
    	
    	if (mode == MODE_POPSIZE)
    		tp += (((Integer) maxTamPob.getValue()) - tp) * i / ((Integer) incTamPob.getValue());
    	if (mode == MODE_GENNUM)
    		ng += (((Integer) maxNumGen.getValue()) - ng) * i / ((Integer) incNumGen.getValue());
    	if (mode == MODE_CP)
    		pc += (((Double) maxPC.getValue()) - pc) * i / ((Integer) incPC.getValue());
    	if (mode == MODE_MP)
    		pm += (((Double) maxPM.getValue()) - pm) * i / ((Integer) incPM.getValue());
    	
    	
    	return new Params(new IncompFunction(), 
    				tp, ng, pc, pm, 
    				0, sel,
	    			mutator, crosser, false, 
	    			eliteSize, (Double) alphaEval.getValue(), (Integer) mSpinner.getValue(), 0.5);
    }
                      
    private void initComponents() {
    	Dimension compSize = new Dimension(100, 20);
    	
        // Comboboxes
        funCombo = new JComboBox(new DefaultComboBoxModel(new String[] { "Función de desequilibrio"}));
        selCombo = new JComboBox(new DefaultComboBoxModel(new String[] { "Ruleta", "Torneo determinista", "Torneo probabilístico", "Ranking", "High-Low" }));
        crCombo = new JComboBox(new DefaultComboBoxModel(new String[] { "PMX", "OX", "OX alternativo", "Ordinal", "Antienemies" }));
        mutCombo = new JComboBox(new DefaultComboBoxModel(new String[] { "Intercambio", "Inserción", "Inversión", "Heurístico" }));
        
        funCombo.setToolTipText("Función de evaluación");
        selCombo.setToolTipText("Método de selección");
        crCombo.setToolTipText("Método de cruce");
        mutCombo.setToolTipText("Método de mutación");
        
        funCombo.setPreferredSize(compSize);
        selCombo.setPreferredSize(compSize);
        crCombo.setPreferredSize(compSize);
        mutCombo.setPreferredSize(compSize);
        
        funCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			    alphaEval.setVisible(true);
			    alphaEvalL.setVisible(true);
			}
		});
        
		selCombo.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				switch (selCombo.getSelectedIndex()) {
					case 1:
						tournamentP.setVisible(false);
					    tournamentS.setVisible(true);
					    tournamentPL.setVisible(false);
					    tournamentSL.setVisible(true);
					    betaRanking.setVisible(false);
					    betaRankingL.setVisible(false);
					    javiParameter.setVisible(false);
					    javiParameterL.setVisible(false);
						break;
					case 2:
						tournamentP.setVisible(true);
					    tournamentS.setVisible(true);
					    tournamentPL.setVisible(true);
					    tournamentSL.setVisible(true);
					    betaRanking.setVisible(false);
					    betaRankingL.setVisible(false);
					    javiParameter.setVisible(false);
					    javiParameterL.setVisible(false);
						break;
					case 3:
						betaRanking.setVisible(true);
						betaRankingL.setVisible(true);
						tournamentP.setVisible(false);
					    tournamentS.setVisible(false);
					    tournamentPL.setVisible(false);
					    tournamentSL.setVisible(false);
					    javiParameter.setVisible(false);
					    javiParameterL.setVisible(false);
						break;
					case 4:
						javiParameter.setVisible(true);
						javiParameterL.setVisible(true);
						tournamentP.setVisible(false);
					    tournamentS.setVisible(false);
					    tournamentPL.setVisible(false);
					    tournamentSL.setVisible(false);
					    betaRanking.setVisible(false);
					    betaRankingL.setVisible(false);
						break;
					default:
						tournamentP.setVisible(false);
					    tournamentS.setVisible(false);
					    tournamentPL.setVisible(false);
					    tournamentSL.setVisible(false);
					    betaRanking.setVisible(false);
					    betaRankingL.setVisible(false);
					    javiParameter.setVisible(false);
					    javiParameterL.setVisible(false);
				}
			}
		});
		
		crCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (crCombo.getSelectedIndex() == 2) {
					oxTrials.setVisible(true);
					oxTrialsL.setVisible(true);
				} else {
					oxTrials.setVisible(false);
					oxTrialsL.setVisible(false);
				}
			}
		});
		
		mutCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mutCombo.getSelectedIndex() == 3) {
					permHeurMut.setVisible(true);
					permHeurMutL.setVisible(true);
				} else {
					permHeurMut.setVisible(false);
					permHeurMutL.setVisible(false);
				}
			}
		});
        
        // Labels
        fileLabel = new JLabel("(Ningún archivo cargado)");
        
        // Buttons
        okButton = new JButton();
        okButton.setText("Calcular"); 
        okButton.setName("okButton");
        okButton.setEnabled(false);
        
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
        
        /*tolField = new JTextField("0.0001");
        tolField.setPreferredSize(compSize);*/
        mSpinner = new JSpinner();
        mSpinner.setPreferredSize(compSize);
        
        eliteSpinner = new JSpinner(eliteNM);
        eliteSpinner.setPreferredSize(compSize);

        minTamPob.setValue(100);
        maxTamPob.setValue(100);
        minNumGen.setValue(100);
        maxNumGen.setValue(100);
        //tolField.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        mSpinner.setValue(2);
        eliteSpinner.setEnabled(false);       
        
        eliteCB = new JCheckBox("");
        eliteCB.setName("eliteCheckBox");
        
        eliteCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				eliteSpinner.setEnabled(eliteCB.isSelected());
			}
		});
        
        // Panels
        plotPanel = new Plot2DPanel();
        plotPanel.addLegend("SOUTH");
        
        paramPanel = getParamPanel();
        groupPanel = new JPanel();
        
        gc = new GroupCanvas();
        groupPanel.add(gc);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        tabbedPanel = new JTabbedPane();       
        tabbedPanel.addTab("Parámetros", paramPanel);
        tabbedPanel.addTab("Función", plotPanel);
        tabbedPanel.addTab("Grupos", groupPanel);
        
        statusBar.add(fileLabel);
        statusBar.add(okButton);
        
        mainPanel.add(tabbedPanel, BorderLayout.CENTER);
        mainPanel.add(statusBar, BorderLayout.SOUTH);
        setContentPane(mainPanel);
    }

	private JPanel getParamPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		
		JPanel horPanel = new JPanel();
		horPanel.setLayout(new BoxLayout(horPanel, BoxLayout.X_AXIS));
		
		JLabel lab;
		Dimension labdim = new Dimension(50, 20);
		
		JPanel pop = new JPanel();
		JPanel popmin = new JPanel();
		JPanel popmax = new JPanel();
		JPanel popinc = new JPanel();
		popmin.setLayout(new BoxLayout(popmin, BoxLayout.X_AXIS));
		popmax.setLayout(new BoxLayout(popmax, BoxLayout.X_AXIS));
		popinc.setLayout(new BoxLayout(popinc, BoxLayout.X_AXIS));
		pop.setLayout(new BoxLayout(pop, BoxLayout.Y_AXIS));
		lab = new JLabel("Min:");
		lab.setPreferredSize(labdim);
		popmin.add(lab);
		lab = new JLabel("Max:");
		lab.setPreferredSize(labdim);
		popmax.add(lab);
		lab = new JLabel("Pasos:");
		lab.setPreferredSize(labdim);
		popinc.add(lab);
		popmin.add(minTamPob);
		popmax.add(maxTamPob);
		popinc.add(incTamPob);
		pop.add(popmin);
		pop.add(popmax);
		pop.add(popinc);
		pop.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.black), "Tamaño población:"));
		
		JPanel gen = new JPanel();
		JPanel genmin = new JPanel();
		JPanel genmax = new JPanel();
		JPanel geninc = new JPanel();
		genmin.setLayout(new BoxLayout(genmin, BoxLayout.X_AXIS));
		genmax.setLayout(new BoxLayout(genmax, BoxLayout.X_AXIS));
		geninc.setLayout(new BoxLayout(geninc, BoxLayout.X_AXIS));
		gen.setLayout(new BoxLayout(gen, BoxLayout.Y_AXIS));
		lab = new JLabel("Min:");
		lab.setPreferredSize(labdim);
		genmin.add(lab);
		lab = new JLabel("Max:");
		lab.setPreferredSize(labdim);
		genmax.add(lab);
		lab = new JLabel("Pasos:");
		lab.setPreferredSize(labdim);
		geninc.add(lab);
		genmin.add(minNumGen);
		genmax.add(maxNumGen);
		geninc.add(incNumGen);
		gen.add(genmin);
		gen.add(genmax);
		gen.add(geninc);
		gen.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.black), "Nº generaciones:"));
		
		JPanel cro = new JPanel();
		JPanel cromin = new JPanel();
		JPanel cromax = new JPanel();
		JPanel croinc = new JPanel();
		cromin.setLayout(new BoxLayout(cromin, BoxLayout.X_AXIS));
		cromax.setLayout(new BoxLayout(cromax, BoxLayout.X_AXIS));
		croinc.setLayout(new BoxLayout(croinc, BoxLayout.X_AXIS));
		cro.setLayout(new BoxLayout(cro, BoxLayout.Y_AXIS));
		lab = new JLabel("Min:");
		lab.setPreferredSize(labdim);
		cromin.add(lab);
		lab = new JLabel("Max:");
		lab.setPreferredSize(labdim);
		cromax.add(lab);
		lab = new JLabel("Pasos:");
		lab.setPreferredSize(labdim);
		croinc.add(lab);
		cromin.add(minPC);
		cromax.add(maxPC);
		croinc.add(incPC);
		cro.add(cromin);
		cro.add(cromax);
		cro.add(croinc);
		cro.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.black), "P. cruce:"));
		
		JPanel mut = new JPanel();
		JPanel mutmin = new JPanel();
		JPanel mutmax = new JPanel();
		JPanel mutinc = new JPanel();
		mutmin.setLayout(new BoxLayout(mutmin, BoxLayout.X_AXIS));
		mutmax.setLayout(new BoxLayout(mutmax, BoxLayout.X_AXIS));
		mutinc.setLayout(new BoxLayout(mutinc, BoxLayout.X_AXIS));
		mut.setLayout(new BoxLayout(mut, BoxLayout.Y_AXIS));
		lab = new JLabel("Min:");
		lab.setPreferredSize(labdim);
		mutmin.add(lab);
		lab = new JLabel("Max:");
		lab.setPreferredSize(labdim);
		mutmax.add(lab);
		lab = new JLabel("Pasos:");
		lab.setPreferredSize(labdim);
		mutinc.add(lab);
		mutmin.add(minPM);
		mutmax.add(maxPM);
		mutinc.add(incPM);
		mut.add(mutmin);
		mut.add(mutmax);
		mut.add(mutinc);
		mut.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.black), "P. mutación:"));
		
		Dimension dimension = new Dimension(120, 20);
		
		JPanel eval = new JPanel();
		lab = new JLabel("Función de evaluación:");
		lab.setPreferredSize(dimension);
		eval.setLayout(new BoxLayout(eval, BoxLayout.X_AXIS));
		eval.add(lab);
		eval.add(funCombo);
		
		JPanel selection = new JPanel();
		lab = new JLabel("Función de selección:");
		lab.setPreferredSize(dimension);
		selection.setLayout(new BoxLayout(selection, BoxLayout.X_AXIS));
		selection.add(lab);
		selection.add(selCombo);
		
		JPanel cross = new JPanel();
		lab = new JLabel("Operador de cruce:");
		lab.setPreferredSize(dimension);
		cross.setLayout(new BoxLayout(cross, BoxLayout.X_AXIS));
		cross.add(lab);
		cross.add(crCombo);
		
		JPanel mutation = new JPanel();
		lab = new JLabel("Operador de mutación:");
		lab.setPreferredSize(dimension);
		mutation.setLayout(new BoxLayout(mutation, BoxLayout.X_AXIS));
		mutation.add(lab);
		mutation.add(mutCombo);
		
		JPanel eme = new JPanel();
		lab = new JLabel("Tamaño de los grupos:");
		lab.setPreferredSize(dimension);
		eme.setLayout(new BoxLayout(eme, BoxLayout.X_AXIS));
		eme.add(lab);
		eme.add(mSpinner);
		
		JPanel eli = new JPanel();
		lab = new JLabel("Elitismo:");
		lab.setPreferredSize(dimension);
		eli.setLayout(new BoxLayout(eli, BoxLayout.X_AXIS));
		eli.add(lab);
		eli.add(eliteCB);
		eli.add(eliteSpinner);		
		
		/* Spinners del final */
		
		JPanel sp1 = new JPanel();
		tournamentPL = new JLabel("Probabilidad del torneo:");
		tournamentPL.setPreferredSize(dimension);
		tournamentPL.setVisible(false);
		sp1.setLayout(new BoxLayout(sp1, BoxLayout.X_AXIS));
		sp1.add(tournamentPL);
		tournamentP = new JSpinner(new SpinnerNumberModel(0.1, 0, 1, 0.1));
		tournamentP.setVisible(false);
		sp1.add(tournamentP);
		
		JPanel sp2 = new JPanel();
		tournamentSL = new JLabel("Tamaño del torneo:");
		tournamentSL.setPreferredSize(dimension);
		tournamentSL.setVisible(false);
		sp2.setLayout(new BoxLayout(sp2, BoxLayout.X_AXIS));
		sp2.add(tournamentSL);
		tournamentS = new JSpinner(new SpinnerNumberModel(2, 2, 1000, 1));
		tournamentS.setVisible(false);
		sp2.add(tournamentS);
		
		JPanel sp3 = new JPanel();
		betaRankingL = new JLabel("Valor de beta:");
		betaRankingL.setPreferredSize(dimension);
		betaRankingL.setVisible(false);
		sp3.setLayout(new BoxLayout(sp3, BoxLayout.X_AXIS));
		sp3.add(betaRankingL);
		betaRanking = new JSpinner(new SpinnerNumberModel(1, 1, 2, 0.1));
		betaRanking.setVisible(false);
		sp3.add(betaRanking);
		
		JPanel sp4 = new JPanel();
		alphaEvalL = new JLabel("Valor de alfa:");
		alphaEvalL.setPreferredSize(dimension);
		sp4.setLayout(new BoxLayout(sp4, BoxLayout.X_AXIS));
		sp4.add(alphaEvalL);
		alphaEval = new JSpinner(new SpinnerNumberModel(0.1, 0, 1, 0.1));
		sp4.add(alphaEval);
		
		JPanel sp5 = new JPanel();
		oxTrialsL = new JLabel("Nº de intentos:");
		oxTrialsL.setPreferredSize(dimension);
		oxTrialsL.setVisible(false);
		sp5.setLayout(new BoxLayout(sp5, BoxLayout.X_AXIS));
		sp5.add(oxTrialsL);
		oxTrials = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
		oxTrials.setVisible(false);
		sp5.add(oxTrials);
		
		JPanel sp6 = new JPanel();
		permHeurMutL = new JLabel("Permutaciones mutación:");
		permHeurMutL.setPreferredSize(dimension);
		permHeurMutL.setVisible(false);
		sp6.setLayout(new BoxLayout(sp6, BoxLayout.X_AXIS));
		sp6.add(permHeurMutL);
		permHeurMut = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
		permHeurMut.setVisible(false);
		sp6.add(permHeurMut);
		
		JPanel sp7 = new JPanel();
		javiParameterL = new JLabel("Parámetro high-low:");
		javiParameterL.setPreferredSize(dimension);
		javiParameterL.setVisible(false);
		sp7.setLayout(new BoxLayout(sp7, BoxLayout.X_AXIS));
		sp7.add(javiParameterL);
		javiParameter = new JSpinner(new SpinnerNumberModel(0, 0, 1, 0.1));
		javiParameter.setVisible(false);
		sp7.add(javiParameter);
		
		horPanel.add(pop);
		horPanel.add(gen);
		horPanel.add(cro);
		horPanel.add(mut);
		
		p.add(eval);
		p.add(selection);
		p.add(cross);
		p.add(mutation);
		p.add(horPanel);
		p.add(eme);
		p.add(sp1);
		p.add(sp2);
		p.add(sp3);
		p.add(sp4);
		p.add(sp5);
		p.add(sp6);
		p.add(sp7);
		p.add(eli);
	
		return p;
	} 
}
