import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;

public class GUI {

	private JFrame frame;
	public JTextPane console;
	public JTable matchsTable1;
	public JTable matchsTable2;
	public JTable matchsTable3;

	
	private Tournament tournament1;
	private Tournament tournament2;
	private Tournament tournament3;
	
	private static ArrayList<Player> allPlayers = new ArrayList<>();
	private static ArrayList<Player> players1 = new ArrayList<>();
	private static ArrayList<Player> players2 = new ArrayList<>();
	private static ArrayList<Player> players3 = new ArrayList<>();
	
	File playersFile ;
	
	final int NB_SOLUTIONS = 1;
//	final int NB_PLAYERS = 200;
	final int NB_ROUNDS = 6;
	
	private static final float FORGOTTEN_PERCENT = (float) 30/100;
	private static final int FORGET_TURNS_NUMBER = 0;
	
	final boolean saveSolution = false;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		// Gestion de la fenêtre
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
//		
//		
//		
//		
//		Tournament tournament = new Tournament(NB_PLAYERS, NB_ROUNDS);
//		
//		// Moyenne de temps pour NB_SOLUTIONS solutions
//		long sum_duration = 0;
//		
//		// Score moyen et minimum
//		double sum_score = 0;
//		int sum_min_score = 0;
//		int sum_max_score = 0;
//		
//		for(int i=0; i<NB_SOLUTIONS; i++) {
//			
//			// Solution
//			long startTime = System.nanoTime();
//			
////			System.out.println(tournament.getMatchesTable());
//			
//			startTournament();
//			
//			tournament.createMatches();
//			
//			long timeElapsed = System.nanoTime() - startTime;
//			sum_duration += timeElapsed;
//			
//			// Calcul des scores
//			tournament.computeScores();
//			
//			double score = tournament.getAverageScore();
//			int min_score = tournament.getMinScore();
//			int max_score = tournament.getMaxScore();
//			
//			sum_score += score;
//			sum_min_score += min_score;
//			sum_max_score += max_score;
//			
//			boolean correct = tournament.checkSolution();
//			
//			System.out.println("Solution " + (i+1) + " trouvée en " + (float)timeElapsed/1000000.0 + " ms"
//					+ " / Score moyen : " + score + " / Score minimum : " + min_score + " / Score maximum : " + max_score + " / Correct : " + correct);
//			
//			if(saveSolution) {
//				try {
//					System.out.print("Sauvegarde de la solution " + (i+1) + "... ");
//					tournament.saveSolution("solution" + (i+1) + ".txt");
//					System.out.println("Solution " + (i+1) + " sauvegardée.");
//				} catch (IOException e) {
//					System.err.println("Erreur lors de la sauvegarde du fichier.");
//				}
//			}
//		}
//		
//		
//		System.out.println("------------------------------------------------------");
//		System.out.println("Durée moyenne pour " + NB_SOLUTIONS + " solutions : " + (float)sum_duration/(float)NB_SOLUTIONS/1000000.0 + "ms");
//		System.out.println("Score moyen pour " + NB_SOLUTIONS + " solutions : " + sum_score/(float)NB_SOLUTIONS
//				+ " / Score minimal moyen pour " + NB_SOLUTIONS + " solutions : " + (float)sum_min_score/(float)NB_SOLUTIONS
//				+ " / Score maximal moyen : " + (float)sum_max_score/(float)NB_SOLUTIONS);
//		System.out.println("------------------------------------------------------");
	}
	
	private void initTournament() {
		
//		readFromJSON();
		
//		tournament = new Tournament(players1, NB_ROUNDS, FORGOTTEN_PERCENT, FORGET_TURNS_NUMBER, this);
		tournament1 = new Tournament(players1, NB_ROUNDS, this);
		tournament2 = new Tournament(players2, NB_ROUNDS, this);
		tournament3 = new Tournament(players3, NB_ROUNDS, this);

		//tournament.createMatches();
		
	}

	/**
	 * Create the application.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public GUI() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	private void initialize() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		
		// Initialise le tournoi
//		initTournament();
		
		frame = new JFrame();
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		frame.setBounds(100, 100, 700, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {300};
		gridBagLayout.rowHeights = new int[] {330, 100};
		gridBagLayout.columnWeights = new double[]{1.0};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 5);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		frame.getContentPane().add(tabbedPane, gbc_tabbedPane);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Joueurs", null, panel_1, null);
		panel_1.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane);
		
		JList<String> listAllPlayers = new JList<String>();
		scrollPane.setViewportView(listAllPlayers);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Niveau 1", null, panel, null);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JScrollPane scrollPane_2 = new JScrollPane();
		panel.add(scrollPane_2);
		
		JList listPlayers1 = new JList();
		scrollPane_2.setViewportView(listPlayers1);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Niveau 2", null, panel_3, null);
		panel_3.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPane_3 = new JScrollPane();
		panel_3.add(scrollPane_3);
		
		JList listPlayers2 = new JList();
		scrollPane_3.setViewportView(listPlayers2);
		
		JPanel panel_5 = new JPanel();
		tabbedPane.addTab("Niveau 3", null, panel_5, null);
		panel_5.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPane_4 = new JScrollPane();
		panel_5.add(scrollPane_4);
		
		JList listPlayers3 = new JList();
		scrollPane_4.setViewportView(listPlayers3);
		
		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("Matchs", null, panel_4, null);
		panel_4.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPane_5 = new JScrollPane();
		panel_4.add(scrollPane_5);
		
		matchsTable1 = new JTable();
		matchsTable1.setRowSelectionAllowed(false);
		matchsTable1.setEnabled(false);
		scrollPane_5.setViewportView(matchsTable1);
		
		
		
		
//		displayMatchsTable();
//		matchsTable.setDefaultRenderer(String.class, new MyRenderer());
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.YELLOW);
		panel_2.setMaximumSize(new Dimension(100,100));
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.anchor = GridBagConstraints.PAGE_END;
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 1;
		frame.getContentPane().add(panel_2, gbc_panel_2);
		panel_2.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panel_2.add(scrollPane_1);
		
		console = new JTextPane();
		console.setBackground(SystemColor.control);
		console.setEditable(false);
		scrollPane_1.setViewportView(console);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFichier = new JMenu("Fichier");
		menuBar.add(mnFichier);
		
		JMenuItem mntmQuitter = new JMenuItem("Quitter");
		mntmQuitter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});
		mnFichier.add(mntmQuitter);
		
		JMenu mnTournoi = new JMenu("Tournoi");
		menuBar.add(mnTournoi);
		
		JMenuItem mntmChargerLesJoueurs = new JMenuItem("Charger les joueurs");
		mntmChargerLesJoueurs.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent arg0) {
				playersFile = openPlayersFile();
				
				if(playersFile == null)
					return;
				
				readFromJSON(playersFile);
				
					
				DefaultListModel<String> playerNames = new DefaultListModel<String>();
				
				for(Player p : allPlayers)
					playerNames.addElement(p.getNom());
				listAllPlayers.setModel(playerNames);
				
				playerNames = new DefaultListModel<String>();
				for(Player p : players1)
					playerNames.addElement(p.getNom());
				listPlayers1.setModel(playerNames);
				
				playerNames = new DefaultListModel<String>();
				for(Player p : players2)
					playerNames.addElement(p.getNom());
				listPlayers2.setModel(playerNames);
				
				playerNames = new DefaultListModel<String>();
				for(Player p : players3)
					playerNames.addElement(p.getNom());
				listPlayers3.setModel(playerNames);
				
				writeConsole("Joueurs chargés");
			}
		});
		mnTournoi.add(mntmChargerLesJoueurs);
		
		JMenuItem mntmCrerLesMatchs = new JMenuItem("Cr\u00E9er les matchs");
		mntmCrerLesMatchs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0));
		mntmCrerLesMatchs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				readFromJSON(null);
				initTournament();

				tournament1.createMatches();
				tournament2.createMatches();
				tournament3.createMatches();
			
			/// PDF
				// ------------- creation du tournoi
				String path = "./donnees_eleves.json" ;
				Tournoi tournoi = JSONExtractor.ExtractJSON(path);
				//---------------  ajoute les rounds
				tournoi.addNbRound(tournament1);
				tournoi.addAllRoundNiv(tournament1,1) ;
				tournoi.addAllRoundNiv(tournament2,2) ;
				tournoi.addAllRoundNiv(tournament3,3) ;
				// -------------- ajoute les tables
				tournoi.addTables() ;

				try
				{
					PDFGen.createPDF(tournoi);

				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				displayMatchsTable();
			}
		});
		mnTournoi.add(mntmCrerLesMatchs);
		
		JMenuItem mntmTest = new JMenuItem("Test");
		mntmTest.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0));
		mntmTest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				readFromJSON(null);
				initTournament();
				tournament1.divideBiggestClass();
				tournament2.divideBiggestClass();
				tournament3.divideBiggestClass();
			}
		});
		mnTournoi.add(mntmTest);
		
//		tournament.setGUI(this);
	}
	
	void writeConsole(String message) {
		
		// Format de la date
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String date = dateFormat.format(new Date());
		
		String oldText = console.getText();
		
		console.setText(oldText + "[" + date + "] " + message + "\n");
		
	}
	
	public File openPlayersFile() {
		final JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
		
		int result = fc.showOpenDialog(frame);
		
		if(result == JFileChooser.APPROVE_OPTION)
			return fc.getSelectedFile();
		else
			return null;
	}
	
	/**
	 * Permet de lire des données depuis un fichier JSON
	 * 
	 * @return le nombre d'élèves lus
	 */
	public static int readFromJSON(File playersFile) {
		int nbEleves = 0;
		
		players1.clear();
		players2.clear();
		players3.clear();
		
		// TODO Gérer enabled/disabled
		
		
		// Création du JSONPArser
		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(new FileReader("./donnees_eleves.json"));
			//		obj = (JSONObject) parser.parse(new FileReader(playersFile));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		// Récupération de toutes les classes
		JSONArray classes = (JSONArray) obj.get("classe");
		
		// Pour chaque classe
		for(Object o : classes) {
			
			JSONObject classe = (JSONObject) o;
			
			// On récupère chaque eleve
			JSONArray eleves = (JSONArray) classe.get("eleve");
			
			for (Object e : eleves) {
				JSONObject eleve = (JSONObject) e;
				
				Player p = new Player(
						Integer.parseInt((String) eleve.get("id")),
						Integer.parseInt((String) classe.get("id")),
						(String) eleve.get("nom")
				);
				
				allPlayers.add(p);
				
				int niveau = Integer.parseInt((String) eleve.get("niveau"));
				
				if(niveau == 1)
					players1.add(p);
				
				else if(niveau == 2)
					players2.add(p);
				
				else
					players3.add(p);
				
				nbEleves++;
			}
		}
		return nbEleves;
	}
	
	@SuppressWarnings("rawtypes")
	public void displayMatchsTable() {
		
//		Object[][] donnees = {
//                {"Johnathan", "Sykes", Color.red, true},
//                {"Nicolas", "Van de Kampf", Color.black, true},
//                {"Damien", "Cuthbert", Color.cyan, true},
//                {"Corinne", "Valance", Color.blue, false},
//                {"Emilie", "Schrödinger", Color.magenta},
//                {"Delphine", "Duke", Color.yellow, false},
//                {"Eric", "Trump", Color.pink, true},
//        };
		
        DefaultTableModel dtm = new DefaultTableModel(0, 0);
        
//        String[] headers = {"Prénom", "Nom", "Couleur favorite", "Homme"};
//        dtm.setColumnIdentifiers(headers);
        matchsTable1.setTableHeader(null);
        matchsTable1.setModel(dtm);
        
        players1 = tournament1.getPlayers();
        
        String[] headers = new String[players1.size() + 1];
        headers[0] = "";
        for(int i=0; i<players1.size(); i++)
        	headers[i+1] = players1.get(i).getId() + " [" + players1.get(i).getClasseId() + "]";
        
        dtm.setColumnIdentifiers(headers); // Doit etre mis avant d'ajouter les donnees !
        
//        dtm.addRow(new Object[] {"toto", "toto", "toto", "toto"});
        
        int[][] matches = tournament1.getMatches();
        
        for(int i=0; i<players1.size(); i++) {
        	
        	Object[] data = new Object[players1.size() + 1];
        	
//        	Object[] data = ArrayUtil.toObject(matches[i]);
//        	DefaultTableModel.con
//			@SuppressWarnings("unchecked")
//			Vector<Integer> data = new Vector(Arrays.asList(matches[i]));
//        	dtm.addRow(data);
        	
        	data[0] = players1.get(i).getId() + " [" + players1.get(i).getClasseId() + "]";
        	
        	for(int j=0; j<players1.size(); j++) {
        		data[j+1] = (matches[i][j] != 0) ? matches[i][j] : "";
        		
        		
        	}
        	
        	dtm.addRow(data);
        }
	
//		class MyRenderer extends DefaultTableCellRenderer {
//
//			private static final long serialVersionUID = 1L;
//
//			Color backgroundColor = Color.LIGHT_GRAY;
//	
//	        @Override
//	        public Component getTableCellRendererComponent(
//	            JTable table, Object value, boolean isSelected,
//	            boolean hasFocus, int row, int column) {
//	        	
//	            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//	            
////	            MyModel model = (MyModel) table.getModel();
//	            DefaultTableModel model = (DefaultTableModel) matchsTable.getModel();
//	            
//	            if(model.getValueAt(row, column) != null){
//	            	System.out.println((String) model.getValueAt(row, column));
//	            }
//	            
////	            if (model.getState(row)) {
////	                c.setBackground(Color.green.darker());
////	            } else if (!isSelected) {
////	                c.setBackground(backgroundColor);
////	            }
//	            
//	            return c;
//	        }
//	    }
	
	}
	
}
