import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GUI {

	private JFrame frame;
	public JTextPane console;
	
	private Tournament tournament;
	
	final int NB_SOLUTIONS = 1;
	final int NB_PLAYERS = 200;
	final int NB_ROUNDS = 6;
	
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
		
		tournament = new Tournament(NB_PLAYERS, NB_ROUNDS);
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
		initTournament();
		
		frame = new JFrame();
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		frame.setBounds(100, 100, 631, 385);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {300, 300};
		gridBagLayout.rowHeights = new int[] {300, 100};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0};
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
		tabbedPane.addTab("New tab", null, panel_1, null);
		panel_1.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane);
		
		JList<String> list = new JList<String>();
		scrollPane.setViewportView(list);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.PINK);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 0;
		frame.getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JButton btnChargerJoueurs = new JButton("Charger joueurs");
		btnChargerJoueurs.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				DefaultListModel<String> playerNames = new DefaultListModel<String>();
				
				for(Player p : tournament.getPlayers()) {
					playerNames.addElement(p.getId() + " [" + p.getClasseId() + "]");
				}
				
				list.setModel(playerNames);
				
				writeConsole("Joueurs chargés.");
			}
		});
		GridBagConstraints gbc_btnChargerJoueurs = new GridBagConstraints();
		gbc_btnChargerJoueurs.insets = new Insets(0, 0, 5, 0);
		gbc_btnChargerJoueurs.gridx = 0;
		gbc_btnChargerJoueurs.gridy = 0;
		panel.add(btnChargerJoueurs, gbc_btnChargerJoueurs);
		
		JButton btnMatcher = new JButton("Matcher");
		btnMatcher.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tournament.createMatches();
			}
		});
		GridBagConstraints gbc_btnMatcher = new GridBagConstraints();
		gbc_btnMatcher.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnMatcher.gridx = 0;
		gbc_btnMatcher.gridy = 1;
		panel.add(btnMatcher, gbc_btnMatcher);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.YELLOW);
		panel_2.setMaximumSize(new Dimension(100,100));
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.anchor = GridBagConstraints.PAGE_END;
		gbc_panel_2.gridwidth = 2;
		gbc_panel_2.insets = new Insets(0, 0, 0, 5);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 1;
		frame.getContentPane().add(panel_2, gbc_panel_2);
		panel_2.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panel_2.add(scrollPane_1);
		
		console = new JTextPane();
		console.setEditable(false);
		console.setEnabled(false);
		scrollPane_1.setViewportView(console);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFichier = new JMenu("Fichier");
		menuBar.add(mnFichier);
		
		JMenuItem mntmQuitter = new JMenuItem("Quitter");
		mnFichier.add(mntmQuitter);
		
		tournament.setGUI(this);
	}
	
	void writeConsole(String message) {
		
		// Format de la date
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String date = dateFormat.format(new Date());
		
		String oldText = console.getText();
		
		console.setText(oldText + "[" + date + "] " + message + "\n");
		
	}
}
