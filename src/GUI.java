import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;


public class GUI {

	private JFrame frmOrganisationDeTournois;
	public JTextPane console;
	public JTable matchsTable1;
	public JTable matchsTable2;
	public JTable matchsTable3;

	private JList<String> listAllPlayers = new JList<String>();
	private JList listPlayers1;
	private JList listPlayers2;
	private JList listPlayers3;
	
	JMenuItem mntmCrerLesMatchs;
	JButton btnCreerMatchs;
	JSpinner spinner;
	JLabel lblNombreDeSolutions;
	JCheckBox chkSamePlayers;
	JLabel lblMatchsDeNiveau1, lblMatchsDeNiveau2, lblMatchsDeNiveau3;
	
	JMenuItem mntmCrerLesFiches;
	JButton btnCreerFiches;
	public JLabel lblResultProg;
	JSpinner spinnerMinClassesNumber;
	
	
	// Tournois
	private Tournament tournament1;
	private Tournament tournament2;
	private Tournament tournament3;
	
	// Matchs gardés (meilleures solutions)
	int matches1[][];
	int matches2[][];
	int matches3[][];
	
	// Joueurs
	private static ArrayList<Player> allPlayers = new ArrayList<>();
	private static ArrayList<Player> players1 = new ArrayList<>();
	private static ArrayList<Player> players2 = new ArrayList<>();
	private static ArrayList<Player> players3 = new ArrayList<>();
	
	// Fichier de données
	File playersFile;
	
//	static final int NB_SOLUTIONS = 100;
//	final int NB_PLAYERS = 200;
	static final int NB_ROUNDS = 6;
	
	private float progress = 0;
	
//	private static final float FORGOTTEN_PERCENT = (float) 30/100;
//	private static final int FORGET_TURNS_NUMBER = 0;
	
	final boolean saveSolution = false;
	
	
	public int getProgress() {
		return Math.min(100, Math.round(progress));
	}
	
	public void setLblProgress() {
		lblResultProg.setText("R\u00E9sultats de la programmation : " + getProgress());
	}
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		// Gestion de la fenêtre
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmOrganisationDeTournois.setVisible(true);
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
	
	private void initTournaments() throws Tournament.OddClassException {
		
//		readFromJSON();
		
//		tournament = new Tournament(players1, NB_ROUNDS, FORGOTTEN_PERCENT, FORGET_TURNS_NUMBER, this);
		
		int canFightSamePlayers = Tournament.CANNOT_FIGHT_SAME_PLAYER_TWICE;
		if(chkSamePlayers.isSelected())
			canFightSamePlayers = Tournament.CAN_FIGHT_SAME_PLAYER_TWICE;
			
		tournament1 = new Tournament(players1, NB_ROUNDS, this, 1, canFightSamePlayers);
		tournament2 = new Tournament(players2, NB_ROUNDS, this, 2, canFightSamePlayers);
		tournament3 = new Tournament(players3, NB_ROUNDS, this, 3, canFightSamePlayers);

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
		
		frmOrganisationDeTournois = new JFrame();
	//	frmOrganisationDeTournois.setIconImage(Toolkit.getDefaultToolkit().getImage(GUI.class.getResource("/icon.png")));
		frmOrganisationDeTournois.setBackground(Color.WHITE);
		frmOrganisationDeTournois.setTitle("Organisation de tournois scolaires d'\u00E9checs");
	//	frmOrganisationDeTournois.setIconImage(Toolkit.getDefaultToolkit().getImage(GUI.class.getResource("/icon.png")));
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		frmOrganisationDeTournois.setBounds(100, 100, 700, 500);
		frmOrganisationDeTournois.setLocationRelativeTo(null);
		frmOrganisationDeTournois.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {300};
		gridBagLayout.rowHeights = new int[] {330, 100};
		gridBagLayout.columnWeights = new double[]{1.0};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0};
		frmOrganisationDeTournois.getContentPane().setLayout(gridBagLayout);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.WHITE);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		frmOrganisationDeTournois.getContentPane().add(tabbedPane, gbc_tabbedPane);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBackground(Color.WHITE);
		tabbedPane.addTab("Gestion du tournoi", null, panel_6, "Vue d'ensemble du tournoi");
		GridBagLayout gbl_panel_6 = new GridBagLayout();
		gbl_panel_6.rowWeights = new double[]{0.1, 0.9};
		gbl_panel_6.columnWeights = new double[]{0.33, 0.33, 0.33};
		gbl_panel_6.columnWidths = new int[] {1000, 1000, 1000};
		panel_6.setLayout(gbl_panel_6);
		
		JButton btnNewButton = new JButton("1. Charger les joueurs");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				actOpenPlayers();
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.weightx = 0.33;
		gbc_btnNewButton.weighty = 0.1;
		gbc_btnNewButton.insets = new Insets(35, 0, 10, 0);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 0;
		panel_6.add(btnNewButton, gbc_btnNewButton);
		
		btnCreerMatchs = new JButton("2. Cr\u00E9er les matchs");
		btnCreerMatchs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				readFromJSON(null);
				actCreateMatches();
			}
		});
		GridBagConstraints gbc_btnCreerMatchs = new GridBagConstraints();
		gbc_btnCreerMatchs.weightx = 0.33;
		gbc_btnCreerMatchs.weighty = 0.1;
		gbc_btnCreerMatchs.insets = new Insets(35, 0, 10, 0);
		gbc_btnCreerMatchs.gridx = 1;
		gbc_btnCreerMatchs.gridy = 0;
		panel_6.add(btnCreerMatchs, gbc_btnCreerMatchs);
		
		btnCreerFiches = new JButton("3. Cr\u00E9er les fiches");
		btnCreerFiches.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				actCreateFiles();
			}
		});
		btnCreerFiches.setEnabled(false);
		GridBagConstraints gbc_btnCreerFiches = new GridBagConstraints();
		gbc_btnCreerFiches.weightx = 0.33;
		gbc_btnCreerFiches.weighty = 0.1;
		gbc_btnCreerFiches.insets = new Insets(35, 0, 10, 0);
		gbc_btnCreerFiches.gridx = 2;
		gbc_btnCreerFiches.gridy = 0;
		panel_6.add(btnCreerFiches, gbc_btnCreerFiches);
		
		JPanel panel_7 = new JPanel();
		panel_7.setBackground(Color.WHITE);
		GridBagConstraints gbc_panel_7 = new GridBagConstraints();
		gbc_panel_7.weighty = 0.9;
		gbc_panel_7.gridwidth = 3;
		gbc_panel_7.fill = GridBagConstraints.BOTH;
		gbc_panel_7.gridx = 0;
		gbc_panel_7.gridy = 1;
		panel_6.add(panel_7, gbc_panel_7);
		GridBagLayout gbl_panel_7 = new GridBagLayout();
		gbl_panel_7.columnWeights = new double[] {1.0, 0.1, 0.1};
		gbl_panel_7.rowHeights = new int[] {20, 20, 20, 20, 100};
		gbl_panel_7.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0};
		panel_7.setLayout(gbl_panel_7);
		
		JLabel lblConfigurationDuTournoi = new JLabel("Configuration du tournoi");
		lblConfigurationDuTournoi.setHorizontalAlignment(SwingConstants.CENTER);
		lblConfigurationDuTournoi.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblConfigurationDuTournoi = new GridBagConstraints();
		gbc_lblConfigurationDuTournoi.fill = GridBagConstraints.BOTH;
		gbc_lblConfigurationDuTournoi.gridwidth = 3;
		gbc_lblConfigurationDuTournoi.insets = new Insets(20, 0, 5, 0);
		gbc_lblConfigurationDuTournoi.gridx = 0;
		gbc_lblConfigurationDuTournoi.gridy = 0;
		panel_7.add(lblConfigurationDuTournoi, gbc_lblConfigurationDuTournoi);
		
		JPanel panel_8 = new JPanel();
		panel_8.setBackground(Color.WHITE);
		panel_8.setBorder(new LineBorder(new Color(192, 192, 192), 1, true));
		GridBagConstraints gbc_panel_8 = new GridBagConstraints();
		gbc_panel_8.gridwidth = 3;
		gbc_panel_8.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_8.insets = new Insets(0, 5, 5, 5);
		gbc_panel_8.gridx = 0;
		gbc_panel_8.gridy = 1;
		panel_7.add(panel_8, gbc_panel_8);
		panel_8.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblNombreDeSolutions = new JLabel("Nombre de solutions \u00E0 g\u00E9n\u00E9rer");
		lblNombreDeSolutions.setEnabled(false);
		lblNombreDeSolutions.setHorizontalAlignment(SwingConstants.CENTER);
		panel_8.add(lblNombreDeSolutions);
		
		spinner = new JSpinner();
		spinner.setEnabled(false);
		spinner.setModel(new SpinnerNumberModel(100, 1, 1000, 100));
		panel_8.add(spinner);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		panel_8.add(horizontalStrut);
		
		JLabel lblNombreDeClasses = new JLabel("Nombre de classes diff\u00E9rentes \u00E0 affronter au minimum");
		panel_8.add(lblNombreDeClasses);
		
		spinnerMinClassesNumber = new JSpinner();
		spinnerMinClassesNumber.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		panel_8.add(spinnerMinClassesNumber);
		
		JPanel panel_9 = new JPanel();
		panel_9.setBackground(Color.WHITE);
		panel_9.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
		GridBagConstraints gbc_panel_9 = new GridBagConstraints();
		gbc_panel_9.gridwidth = 3;
		gbc_panel_9.insets = new Insets(0, 5, 0, 5);
		gbc_panel_9.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_9.gridx = 0;
		gbc_panel_9.gridy = 2;
		panel_7.add(panel_9, gbc_panel_9);
		
		chkSamePlayers = new JCheckBox("Autoriser les joueurs \u00E0 s'affronter plusieurs fois");
		chkSamePlayers.setSelected(true);
		chkSamePlayers.setBackground(Color.WHITE);
		panel_9.add(chkSamePlayers);
		
		lblResultProg = new JLabel("R\u00E9sultats de la programmation");
		lblResultProg.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblResultProg = new GridBagConstraints();
		gbc_lblResultProg.insets = new Insets(20, 0, 5, 0);
		gbc_lblResultProg.gridwidth = 3;
		gbc_lblResultProg.gridx = 0;
		gbc_lblResultProg.gridy = 3;
		panel_7.add(lblResultProg, gbc_lblResultProg);
		
		JPanel panel_10 = new JPanel();
		panel_10.setBackground(Color.WHITE);
		panel_10.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
		GridBagConstraints gbc_panel_10 = new GridBagConstraints();
		gbc_panel_10.gridwidth = 3;
		gbc_panel_10.insets = new Insets(0, 5, 5, 5);
		gbc_panel_10.fill = GridBagConstraints.BOTH;
		gbc_panel_10.gridx = 0;
		gbc_panel_10.gridy = 4;
		panel_7.add(panel_10, gbc_panel_10);
		GridBagLayout gbl_panel_10 = new GridBagLayout();
		gbl_panel_10.columnWidths = new int[]{20,20,20};
		gbl_panel_10.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel_10.columnWeights = new double[]{0.1,0.1,0.1};
		gbl_panel_10.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_10.setLayout(gbl_panel_10);
		
		lblMatchsDeNiveau1 = new JLabel("Matchs de niveau 1 : pas encore tir\u00E9s");
		lblMatchsDeNiveau1.setEnabled(false);
		GridBagConstraints gbc_lblMatchsDeNiveau1 = new GridBagConstraints();
		gbc_lblMatchsDeNiveau1.insets = new Insets(5, 0, 5, 5);
		gbc_lblMatchsDeNiveau1.gridx = 1;
		gbc_lblMatchsDeNiveau1.gridy = 0;
		panel_10.add(lblMatchsDeNiveau1, gbc_lblMatchsDeNiveau1);
		
		lblMatchsDeNiveau2 = new JLabel("Matchs de niveau 2 : pas encore tir\u00E9s");
		lblMatchsDeNiveau2.setEnabled(false);
		GridBagConstraints gbc_lblMatchsDeNiveau2 = new GridBagConstraints();
		gbc_lblMatchsDeNiveau2.insets = new Insets(0, 0, 5, 5);
		gbc_lblMatchsDeNiveau2.gridx = 1;
		gbc_lblMatchsDeNiveau2.gridy = 1;
		panel_10.add(lblMatchsDeNiveau2, gbc_lblMatchsDeNiveau2);
		
		lblMatchsDeNiveau3 = new JLabel("Matchs de niveau 3 : pas encore tir\u00E9s");
		lblMatchsDeNiveau3.setEnabled(false);
		GridBagConstraints gbc_lblMatchsDeNiveau3 = new GridBagConstraints();
		gbc_lblMatchsDeNiveau3.insets = new Insets(0, 0, 0, 5);
		gbc_lblMatchsDeNiveau3.gridx = 1;
		gbc_lblMatchsDeNiveau3.gridy = 2;
		panel_10.add(lblMatchsDeNiveau3, gbc_lblMatchsDeNiveau3);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Tous les joueurs", null, panel_1, "Liste de tous les joueurs du tournoi");
		panel_1.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane);
		
		listAllPlayers = new JList<String>();
		scrollPane.setViewportView(listAllPlayers);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Joueurs Niveau 1", null, panel, "Liste des joueurs du niveau 1");
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JScrollPane scrollPane_2 = new JScrollPane();
		panel.add(scrollPane_2);
		
		listPlayers1 = new JList();
		scrollPane_2.setViewportView(listPlayers1);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Joueurs Niveau 2", null, panel_3, "Liste des joueurs du niveau 2");
		panel_3.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPane_3 = new JScrollPane();
		panel_3.add(scrollPane_3);
		
		listPlayers2 = new JList();
		scrollPane_3.setViewportView(listPlayers2);
		
		JPanel panel_5 = new JPanel();
		tabbedPane.addTab("Joueurs Niveau 3", null, panel_5, "Liste des joueurs du niveau 3");
		panel_5.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPane_4 = new JScrollPane();
		panel_5.add(scrollPane_4);
		
		listPlayers3 = new JList();
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
		gbc_panel_2.anchor = GridBagConstraints.PAGE_END;
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 1;
		frmOrganisationDeTournois.getContentPane().add(panel_2, gbc_panel_2);
		panel_2.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panel_2.add(scrollPane_1);
		
		console = new JTextPane();
		console.setBackground(SystemColor.control);
		console.setEditable(false);
		scrollPane_1.setViewportView(console);

		JMenuBar menuBar = new JMenuBar();
		frmOrganisationDeTournois.setJMenuBar(menuBar);
		
		JMenu mnFichier = new JMenu("Fichier");
		menuBar.add(mnFichier);
		
		JMenuItem mntmQuitter = new JMenuItem("Quitter");
		mntmQuitter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		mntmQuitter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frmOrganisationDeTournois.dispatchEvent(new WindowEvent(frmOrganisationDeTournois, WindowEvent.WINDOW_CLOSING));
			}
		});
		mnFichier.add(mntmQuitter);
		
		JMenu mnTournoi = new JMenu("Tournoi");
		menuBar.add(mnTournoi);
		
		JMenuItem mntmChargerLesJoueurs = new JMenuItem("Charger les joueurs");
		mntmChargerLesJoueurs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
		mntmChargerLesJoueurs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				actOpenPlayers();
			}
		});
		mnTournoi.add(mntmChargerLesJoueurs);
		
		mntmCrerLesMatchs = new JMenuItem("Cr\u00E9er les matchs");
		mntmCrerLesMatchs.setEnabled(false);
		mntmCrerLesMatchs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_MASK));
		mntmCrerLesMatchs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				actCreateMatches();
			}
		});
		mnTournoi.add(mntmCrerLesMatchs);
		
		mntmCrerLesFiches = new JMenuItem("Cr\u00E9er les fiches");
		mntmCrerLesFiches.setEnabled(false);
		mntmCrerLesFiches.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mntmCrerLesFiches.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				actCreateFiles();
			}
		});
		mnTournoi.add(mntmCrerLesFiches);
		
		JMenu mnAide = new JMenu("Aide");
		menuBar.add(mnAide);
		
		JMenuItem mntmPropos = new JMenuItem("\u00C0 propos");
		mntmPropos.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK));
		mntmPropos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				displayPopUp("Ce logiciel a été conçu et développé par Maxence Biers, Pierre Marigo,\nLoïca Marotte, et Rémi Négrier,"
						+ " avec l'aide de Marie-José Huguet.\n\n"
						+ "INSA Toulouse (2019)", "À propos", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnAide.add(mntmPropos);
		
//		tournament.setGUI(this);
	}
	
	void actOpenPlayers() {
		playersFile = openPlayersFile();
		
		if(playersFile == null)
			return;
		
		if(readFromJSON(playersFile) == -1)
			return;
		
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
		
		
		// Active tout ce qu'il faut
		mntmCrerLesMatchs.setEnabled(true);
		btnCreerMatchs.setEnabled(true);
		lblNombreDeSolutions.setEnabled(true);
		spinner.setEnabled(true);
		chkSamePlayers.setEnabled(true);
		lblMatchsDeNiveau1.setEnabled(true);
		lblMatchsDeNiveau2.setEnabled(true);
		lblMatchsDeNiveau3.setEnabled(true);
		
		writeConsole("Les joueurs ont correctement été chargés.");
	}
void oddTournamentHandler() { //%TODO

		
		//trouver qui est odd
		boolean odd1 = false,odd2 = false,odd3 =false;
		ArrayList<ArrayList<Player>> lOdd = new ArrayList<ArrayList<Player>>();
		
		//intOdd contient les numeros des tournois qui sont odd
		int[] intOdd = new int[3];
		int indIntOdd =0; //auxiliaire, pas important
		
		if (players1.size()%2 != 0) {
			odd1 = true;
			lOdd.add(players1);
			System.out.println("t1 impair");
			intOdd[indIntOdd]=1;
			indIntOdd++;
		}
		if (players2.size()%2 != 0) {
			odd2 = true;
			lOdd.add(players2);
			System.out.println("t2 impair");
			intOdd[indIntOdd]=2;
			indIntOdd++;
		}
		if (players3.size()%2 != 0) {
			odd3 = true;
			lOdd.add(players3);
			System.out.println("t3 impair");
			intOdd[indIntOdd]=3;
			indIntOdd++;
		}
		
		if (lOdd.size()!=0) { // s'il y a des impairs...
			
			JDialog frame = new JDialog();
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			
			//creation du string et de son label pour la fenetre/////////////////////////////
			String texteLabel = "Les niveaux ";		
			for (int i=0;i<lOdd.size();i++) {
				texteLabel = texteLabel + intOdd[i];
				if (i!=lOdd.size()) {
					texteLabel = texteLabel + " ,";
				}
			}
			texteLabel = texteLabel + " ont un nombre de joueurs impair.";
			JLabel label1 = new JLabel(texteLabel);
			frame.add(label1);
			
			//preparation d'un tableau contenant des tableaux de string contenant des strings decrivant les joueurs de chaque tournoi
			ArrayList<ArrayList<Player>> tabListePlayers = new ArrayList<ArrayList<Player>>();
			tabListePlayers.add(players1);
			tabListePlayers.add(players2);
			tabListePlayers.add(players3);
			
			String[][] tabNoms = new String[3][];
			tabNoms[0] = new String[players1.size()];
			int ind =0;
			for (Player p : players1) {
				tabNoms[0][ind]=p.getNom() + " de la classe " + p.getClasseId();
				ind++;
			}
			
			tabNoms[1] = new String[players2.size()];
			ind =0;
			for (Player p : players2) {
				tabNoms[1][ind]=p.getNom() + " de la classe " + p.getClasseId();
				ind++;
			}
			
			tabNoms[2] = new String[players3.size()];
			ind =0;
			for (Player p : players3) {
				tabNoms[2][ind]=p.getNom() + " de la classe " + p.getClasseId();
				ind++;
			}
			
			// affichage fenetre pour ajouter un joueur
			if (lOdd.size() == 1) { 
				String textButton = "Ajouter un joueur Complément au tournoi pour équilibrer" + intOdd[0];
				JButton button1 = new JButton(textButton);
				frame.add(button1);
				button1.addActionListener(new ActionListener(){
					int t = intOdd[0];
					public void actionPerformed(ActionEvent e) {
					playerAdder(t);
					frame.setVisible(false);
					}
				});
			}
			
			// affichage fenetre pour bouger un joueur
			else if (lOdd.size()==2) { 
				String[] stab1 = new String[lOdd.size()]; //selection de tournoi de qui le joueur part
				String[] stab2 = new String[lOdd.size()]; //selection de tournoi vers qui le joueur part
				
				for (int i=0;i<lOdd.size();i++) {
					stab1[i]= "Prendre un élève du niveau " + intOdd[i];
					stab2[i]= "pour le placer au niveau " + intOdd[i];
				}
				
				JComboBox<String> cbniv1 = new JComboBox<String>(stab1); //selection de tournoi de qui le joueur part
				JComboBox<String> cbniv2 = new JComboBox<String>(stab2); //selection de tournoi vers qui le joueur part
				JComboBox<String> cbelv = new JComboBox<String>(tabNoms[intOdd[cbniv1.getSelectedIndex()]-1]); //selection du joueur qui part
				
				cbniv1.addActionListener(new ActionListener() {
					int currInd = cbniv1.getSelectedIndex();
					public void actionPerformed(ActionEvent e) {
						int indexcb1 =cbniv1.getSelectedIndex();
						if (indexcb1!=currInd) {
							currInd = indexcb1;
							// permet de changer les valeurs de la selection de joueur 
							cbelv.setModel(new DefaultComboBoxModel<String>(tabNoms[intOdd[indexcb1]-1]));	
						}
					}
				});
				
				JButton buttonConfirm = new JButton("Confirmer le changement");
						buttonConfirm.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e) {
								int ind1 = cbniv1.getSelectedIndex();
								int ind2 = cbniv2.getSelectedIndex();
								int indNiveau = intOdd[ind1];
								ArrayList<Player> alp1 = null;
								if(ind1!= ind2) {
									if (indNiveau==1) {
										alp1=players1;
									}
									else if (indNiveau==2) {
										alp1=players2;
									}
									else if (indNiveau==3) {
										alp1=players3;
									}
									else {
										System.out.println("oddTournamentHandler erreur 8");
									}
									playerMover(intOdd[ind1],intOdd[ind2],alp1.get(cbelv.getSelectedIndex()));
									frame.setVisible(false);
								}
								else {
									displayPopUp("Le niveau d'où vient l'élève doit être différent de celui ou il finit","Attention",JOptionPane.WARNING_MESSAGE);
								}
							}
						});
					frame.add(cbniv1);
					frame.add(cbniv2);
					frame.add(cbelv);
					frame.add(buttonConfirm);
				}
			else if (lOdd.size()==3) {
				
			
				String[] stabAdd = new String [3]; //tournoi ou on ajoute joueur complément
				String[] stab1 = new String[3]; //selection de tournoi de qui le joueur part
				String[] stab2 = new String[3]; //selection de tournoi vers qui le joueur part
				
				for (int i=0;i<3;i++) {
					stabAdd[i] = "Ajouter un élève de complément dans le niveau" + (i+1);
					stab1[i]= "Prendre un élève du niveau " + i;
					stab2[i]= "pour le placer au niveau " + i;
				}
				
				JComboBox<String> cbAdd = new JComboBox<String>(stabAdd);
				JComboBox<String> cbniv1 = new JComboBox<String>(stab1); //selection de tournoi de qui le joueur part
				JComboBox<String> cbniv2 = new JComboBox<String>(stab2); //selection de tournoi vers qui le joueur part
				JComboBox<String> cbelv = new JComboBox<String>(tabNoms[intOdd[cbniv1.getSelectedIndex()]-1]); //selection du joueur qui part
				
				cbniv1.addActionListener(new ActionListener() {
					int currInd = cbniv1.getSelectedIndex();
					public void actionPerformed(ActionEvent e) {
						int indexcb1 =cbniv1.getSelectedIndex();
						if (indexcb1!=currInd) {
							currInd = indexcb1;
							// permet de changer les valeurs de la selection de joueur 
							cbelv.setModel(new DefaultComboBoxModel<String>(tabNoms[intOdd[indexcb1]-1]));	
						}
					}
				});
				
				JButton buttonConfirm = new JButton("Confirmer le changement");
						buttonConfirm.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e) {
								int indAdd = cbAdd.getSelectedIndex();
								int ind1 = cbniv1.getSelectedIndex();
								int ind2 = cbniv2.getSelectedIndex();
								int indNiveau = intOdd[ind1];
								ArrayList<Player> alp1 = null;
								if(ind1!= ind2 && ind1 != indAdd && ind2 != indAdd) {
									if (indNiveau==1) {
										alp1=players1;
									}
									else if (indNiveau==2) {
										alp1=players2;
									}
									else if (indNiveau==3) {
										alp1=players3;
									}
									else {
										System.out.println("oddTournamentHandler erreur 8");
									}
									playerMover(intOdd[ind1],intOdd[ind2],alp1.get(cbelv.getSelectedIndex()));
									playerAdder(indAdd +1);
									frame.setVisible(false);
								}
								else {
									displayPopUp("Le niveau d'où vient l'élève doit être différent de celui ou il finit","Attention",JOptionPane.WARNING_MESSAGE);
								}
							}
						});
						frame.add(cbAdd);
					frame.add(cbniv1);
					frame.add(cbniv2);
					frame.add(cbelv);
					frame.add(buttonConfirm);				
			}
			
			frame.setLayout(new FlowLayout());
			frame.pack();
			frame.setModal(true);
			frame.setVisible(true);
		}
	}
	
	void playerMover(int l1, int l2, Player e) {//gros nom
		ArrayList<Player> alp1 = null;
		ArrayList<Player> alp2 = null;
		
		if (l1==1) {
			alp1=players1;
		}
		else if (l1==2) {
			alp1=players2;
		}
		else if (l1==3) {
			alp1=players3;
		}
		else {
			System.out.println("probleme arguments playerMover");
		}
		if (l2==1) {
			alp2=players1;
		}
		else if (l2==2) {
			alp2=players2;
		}
		else if (l2==3) {
			alp2=players3;
		}
		else {
			System.out.println("probleme arguments playerMover");
		}
		alp1.remove(e);
		alp2.add(e);
	}
	
	void playerAdder(int tournoi) {
		
		//trouver le dernier id joueur et le dernier id classse pour plus tard
		int idc=0,idp=0;
		
		for (Player p:allPlayers) {
			if (p.getId()>idp) {
				idp = p.getId();
			}
			if (p.getClasseId()>idc) {
				idc = p.getClasseId();
			}
		}
		ArrayList<Player> lp;
		if(tournoi == 1) {
			lp = players1;
		}
		else if(tournoi == 2) {
			lp = players2;
		}
		else if(tournoi == 3) {
			lp = players3;
		}
		else {
			System.out.println("bug ajout joueur");
			return;
		}
		lp.add(new Player(idp+1, idc+1, "Complement")); 
	}
	
	
	void actCreateMatches() {
		
		try {
			progress = 0;
			oddTournamentHandler();
			initTournaments();
			
//			WaitThread waitThread = new WaitThread(this);
//			waitThread.start();
			
			long startTime = System.nanoTime();
			
			int minClassesNumber = (Integer) spinnerMinClassesNumber.getValue();
			
			// Calcul des meilleures solutions
			boolean foundMatches1 = tournament1.createMatches();
			matches1 = tournament1.getMatches();
			float bestScore1 = tournament1.getSolutionScore(minClassesNumber);
			
			boolean foundMatches2 = tournament2.createMatches();
			matches2 = tournament2.getMatches();
			float bestScore2 = tournament2.getSolutionScore(minClassesNumber);
			
			boolean foundMatches3 = tournament3.createMatches();
			matches3 = tournament3.getMatches();
			float bestScore3 = tournament3.getSolutionScore(minClassesNumber);
			
			for(int i=1; i < (Integer) spinner.getValue(); i++) {
				
				progress += (float) i/(Integer) spinner.getValue();
				
				if(foundMatches1)
					foundMatches1 = foundMatches1 && tournament1.createMatches();
				else
					foundMatches1 = foundMatches1 && tournament1.createMatches(Tournament.CAN_FIGHT_SAME_PLAYER_TWICE_ALREADY_DONE);
					
				float score1 = tournament1.getSolutionScore(minClassesNumber);
				if(score1 > bestScore1) {
					matches1 = tournament1.getMatches();
					bestScore1 = score1;
				}

				if(foundMatches2)
					foundMatches2 = foundMatches2 && tournament2.createMatches();
				else
					foundMatches2 = foundMatches2 && tournament2.createMatches(Tournament.CAN_FIGHT_SAME_PLAYER_TWICE_ALREADY_DONE);
				
				float score2 = tournament2.getSolutionScore(minClassesNumber);	
				if(score2 > bestScore2) {
					matches2 = tournament2.getMatches();
					bestScore2 = score2;
				}
				
				if(foundMatches3)
					foundMatches3 = foundMatches3 && tournament3.createMatches();
				else
					foundMatches3 = foundMatches3 && tournament3.createMatches(Tournament.CAN_FIGHT_SAME_PLAYER_TWICE_ALREADY_DONE);
				
				float score3 = tournament3.getSolutionScore(minClassesNumber);
				if(score3 > bestScore3) {
					matches3 = tournament3.getMatches();
					bestScore3 = score3;
				}
				
			}
//			System.out.println("--------- " + bestScore1 + " " + bestScore2 + " " + bestScore3);
			
			
			long timeElapsed = System.nanoTime() - startTime;
			writeConsole("Matches créés pour " + allPlayers.size() + " joueurs et " + NB_ROUNDS + " rounds en " + (float)timeElapsed/1000000.0 + " ms");
			
			if(bestScore1 < 0)
				writeConsole("La contrainte du nombre minimum de classes différentes a dû être ignoré pour le groupe 1.");
			
			if(bestScore2 < 0)
				writeConsole("La contrainte du nombre minimum de classes différentes a dû être ignoré pour le groupe 2.");
			
			if(bestScore3 < 0)
				writeConsole("La contrainte du nombre minimum de classes différentes a dû être ignoré pour le groupe 3.");
				
	
			// Messages 1er onglet
			lblMatchsDeNiveau1.setText("Matchs de niveau 1 : " + (Math.round(tournament1.getAverageScore()*100)/100.0) +
					" classes différentes par élève en moyenne (Maximum théorique : " + (Math.min(tournament1.getClassesNumber()-1, NB_ROUNDS)) + ")");
			
			lblMatchsDeNiveau2.setText("Matchs de niveau 2 : " + (float) (Math.round(tournament2.getAverageScore()*100)/100.0) +
					" classes différentes par élève en moyenne (Maximum théorique : " + (Math.min(tournament2.getClassesNumber()-1, NB_ROUNDS)) + ")");
			
			lblMatchsDeNiveau3.setText("Matchs de niveau 3 : " + (float) (Math.round(tournament3.getAverageScore()*100)/100.0) +
					" classes différentes par élève en moyenne (Maximum théorique : " + (Math.min(tournament3.getClassesNumber()-1, NB_ROUNDS)) + ")");
			
			// Active tout ce qu'il faut
			mntmCrerLesFiches.setEnabled(true);
			btnCreerFiches.setEnabled(true);
			
			
			displayMatchsTable(); // TODO à supprimer
		}
		catch(Tournament.OddClassException e) {
			displayPopUp("Le nombre de joueurs dans le groupe de niveau " + e.getLevel() + " est impair.\n"
					+ "Ajoutez un joueur à l'aide de l'application web, puis recommencez la création des matchs.",
					"Nombre de joueurs impairs",
					JOptionPane.WARNING_MESSAGE);
		}
		catch(Tournament.NeedSamePlayersException e) {
			displayPopUp("Impossible de trouver une solution pour les élèves du niveau " + e.getLevel() + ".\n"
					+ "Modifiez les groupes ou autorisez les joueurs à s'affronter plusieurs fois pour obtenir une solution.",
					"Impossible de trouver une solution",
					JOptionPane.WARNING_MESSAGE);
		}
		catch(Tournament.NoSolutionFoundException e) {
			displayPopUp("Impossible de trouver une solution pour les élèves du niveau " + e.getLevel() + ".\n"
					+ "Modifiez les groupes pour obtenir une solution.",
					"Impossible de trouver une solution",
					JOptionPane.WARNING_MESSAGE);
		}
		catch(Exception e) {
			displayPopUp("Une erreur inconnue est survenue.", "Erreur inconnue", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		finally {
			
			int[][] otherMatches = tournament3.getOtherMatches();
			
			for(int i=0; i<players3.size(); i++) {
				System.out.print("Player " + i + "(" + players3.get(i).getId() + "): ");
				
				for(int j=0; j<NB_ROUNDS; j++)
					System.out.print(otherMatches[i][j] + " ");
				
				System.out.println();
			}
			
			
			progress = -1;
		}

	}
	
	void actCreateFiles() {
		/// PDF
		// ------------- creation du tournoi
		
//		String path = "./donnees_eleves.json"; // remplacé par playersFile (qui est le fichier chargé au début)
		Tournoi tournoi = JSONExtractor.ExtractJSON(playersFile);
		
		
		//---------------  ajoute les rounds
		tournoi.addNbRound(tournament1);
		
		tournament1.setMatches(matches1);
		tournoi.addAllRoundNiv(tournament1, 1);
		
		tournament2.setMatches(matches2);
		tournoi.addAllRoundNiv(tournament2, 2);
		
		tournament3.setMatches(matches3);
		tournoi.addAllRoundNiv(tournament3, 3);
		
		
		// -------------- ajoute les tables
		tournoi.addTables() ;

		try{
			PDFGen.createPDF(tournoi);

			writeConsole("Les fiches ont correctement été créées.");
		}
		catch(Exception e){
			displayPopUp("Une erreur inconnue est survenue lors de la création des fiches.\n"
					+ "Si une fiche est déjà ouverte dans un document, fermez cette dernière avant d'en créer une nouvelle.",
					"Erreur inconnue", JOptionPane.WARNING_MESSAGE);
//			e.printStackTrace();
		}
	}
	
	void writeConsole(String message) {
		
		// Format de la date
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String date = dateFormat.format(new Date());
		
		String oldText = console.getText();
		
		console.setText(oldText + "[" + date + "] " + message + "\n");
		
	}
	
	public void displayPopUp(String message, String title, int type) {
		JOptionPane.showMessageDialog(this.frmOrganisationDeTournois, message, title, type);
	}
	
	public File openPlayersFile() {
		final JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
		fc.setDialogTitle("Sélectionnez le fichier de données généré par l'application web...");
		
		int result = fc.showOpenDialog(frmOrganisationDeTournois);
		
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
	public int readFromJSON(File playersFile) {
		int nbEleves = 0;
		
		allPlayers.clear();
		players1.clear();
		players2.clear();
		players3.clear();
		
		// Création du JSONPArser
		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		try {
//			obj = (JSONObject) parser.parse(new FileReader("./donnees_eleves.json"));
			obj = (JSONObject) parser.parse(new FileReader(playersFile));
		} catch (IOException | ParseException e) {
			displayPopUp("Le fichier de données est incorrect.\n"
					+ "Il est préférable de toujours utiliser l'application web pour le générer.",
					"Erreur lors du chargement des joueurs", JOptionPane.WARNING_MESSAGE);
//			e.printStackTrace();
			return -1;
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
		
//		System.out.println(players1);
//		System.out.println(players1.size());
//		System.out.println(players2);
//		System.out.println(players2.size());
//		System.out.println(players3);
//		System.out.println(players3.size());
		
		return nbEleves;
	}
	
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
        
        players3 = tournament3.getPlayers();
        
        String[] headers = new String[players3.size() + 1];
        headers[0] = "";
        for(int i=0; i<players3.size(); i++)
        	headers[i+1] = players3.get(i).getId() + " [" + players3.get(i).getClasseId() + "]";
        
        dtm.setColumnIdentifiers(headers); // Doit etre mis avant d'ajouter les donnees !
        
//        dtm.addRow(new Object[] {"toto", "toto", "toto", "toto"});
        
        int[][] matches = tournament3.getMatches();
        
        for(int i=0; i<players3.size(); i++) {
        	
        	Object[] data = new Object[players3.size() + 1];
        	
//        	Object[] data = ArrayUtil.toObject(matches[i]);
//        	DefaultTableModel.con
//			@SuppressWarnings("unchecked")
//			Vector<Integer> data = new Vector(Arrays.asList(matches[i]));
//        	dtm.addRow(data);
        	
        	data[0] = players3.get(i).getId() + " [" + players3.get(i).getClasseId() + "]";
        	
        	for(int j=0; j<players3.size(); j++) {
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
