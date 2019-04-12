import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

public class Tournament {
	
	// Influe sur la durée du traitement
	private static final int MAX_TRIES = 2000;
	
	private final int classesNumber;
	private final int roundsNumber;
	private final int playersNumber;
	private int[][] matches;
	private int[] scores;
	
	// Pour chaque classe
	// TODO Gérer si classes trop grandes par rapport au nombre d'élèves --> blocage
//	private final int[] STUDENTS_NUMBER = {3, 4, 4, 4, 3, 3, 2,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,100};
//	private final int[] STUDENTS_NUMBER = {25,25,25,25,25,25,25,25,25,25,25,25,25};
	private final ArrayList<Integer> classeSize = new ArrayList<Integer>();//(Arrays.asList(15,20,18,21,28,25,16,27,30));
//	private final ArrayList<Integer> classeSize = new ArrayList<Integer>(Arrays.asList(6,2,8,4,10,8,7,6));
//	private final ArrayList<Integer> classeSize = new ArrayList<Integer>(Arrays.asList(30,20,25,25,25,25,25,25));
	
	private ArrayList<Player> players;
	private ArrayList<Integer>[] classPlayersId;
	private ArrayList<Integer>[] possibleOpponentsId;
	
	@SuppressWarnings("unchecked")
 	public Tournament(ArrayList<Player> ps, int roundsNumber) {
		this.players = ps;
		playersNumber = players.size();
		
		
		this.roundsNumber = roundsNumber;
		
		// Tableau contenant les IDs des élèves pour chaque classe
		// Aide pour le random (évite de tomber sur une personne de la même classe)
		int idCurrentClass = players.get(0).getClasseId();
		int nbClasses = 1;
		for (Player p : players) {
			if (p.getClasseId() != idCurrentClass) {
				nbClasses++;
				idCurrentClass = p.getClasseId();
			}
		}
		classesNumber = nbClasses;
		
		classPlayersId = new ArrayList[classesNumber];
		for(int i=0; i<classesNumber; i++) {
			classPlayersId[i] = new ArrayList<Integer>();
		}
		for (Player p : players) {
			classPlayersId[p.getClasseId()].add(p.getId());
		}
		for(int i=0; i < classesNumber; i++) {
			classeSize.add(classPlayersId[i].size());
		}
		
		// createPlayers();
		
		generatePossibleOpponents();
		
		// Scores
		scores = new int[playersNumber];
	}
	
	// Indique si la solution est correcte
	public boolean checkSolution() {
		
		int goal = 0;
		for(int i=1; i<=roundsNumber; i++) {
			goal += i;
		}
		
		boolean correct = true;
		int sum_ligne;
		
		for(int i=0; i<playersNumber; i++) {
			sum_ligne = 0;
			
			for(int j=0; j<playersNumber; j++) {
				if(matches[i][j] > 0)
					sum_ligne += matches[i][j];
			}
			
			if(sum_ligne != goal)
				correct = false;
		}
		
		return correct;
	}
	
	/* private int getPlayerClasse(int id) {
		int sum = 0;
		int classe = 0;
		
		
		
		while(sum + classeSize.get(classe)-1 < id) {
			sum += classeSize.get(classe);
			classe++;
		}
		
		// Ajoute l'ID du joueur à la classe
		classPlayersId[classe].add(id);
		
		return classe;
	} */

	/*
	 * private void createPlayers() {
		// Si pas suffisamment de places disponibles dans les classes
		int placesNumber = classeSize.stream().mapToInt(a -> a).sum();
		if(placesNumber < playersNumber)
			throw new RuntimeException("Pas suffisamment de places dans les classes (" + playersNumber + " joueurs / " + placesNumber + " places)");
		
		players = new ArrayList<Player>();
		
		for (int i = 0; i < playersNumber; i++) {
			Player player = new Player(i, getPlayerClasse(i));
			players.add(player);
		}
	
		sortPlayers();
	}*/
	
	@SuppressWarnings("unchecked")
	private void generatePossibleOpponents() {
		
		possibleOpponentsId = new ArrayList[classesNumber];
		
		// Tableau qui contient tous les IDs
		ArrayList<Integer> allPlayersIds = new ArrayList<Integer>();
		
		for(int i=0; i<playersNumber; i++)
			allPlayersIds.add(i);
		
		for(int classe=0; classe<classesNumber; classe++) {
			possibleOpponentsId[classe] = new ArrayList<Integer>(allPlayersIds);

			for(int i=0; i<classPlayersId[classe].size(); i++) {
				possibleOpponentsId[classe].remove(classPlayersId[classe].get(i));
			}
			
//			System.out.println(possibleOpponents.toString());
//			System.out.println(allPlayersIds.toString());
		}
		
	}
	
	// Classe les joueurs selon leur classe
	// Les joueurs de la classe la plus grande en premiers
	private void sortPlayers() {
		
		ArrayList<Integer> tempClasseSize = new ArrayList<Integer>(classeSize);
		ArrayList<Player> tempPlayers = new ArrayList<Player>();
		Player tempPlayer;
		
		for(int i=0; i<classeSize.size(); i++) {
			
			int maxClasseId = tempClasseSize.indexOf(Collections.max(tempClasseSize)); // index change !! du coup la même classe revient
			
			for(Player p : players) {
				if(p.getClasseId() != maxClasseId)
					continue;
				
				// On change la classe du joueur
				tempPlayer = new Player(p);
				tempPlayer.setClasseId(i);
				
				// On ajoute le joueur a la nouvelle liste
				tempPlayers.add(tempPlayer);
			}
			
			tempClasseSize.remove(maxClasseId);
			tempClasseSize.add(maxClasseId, -1); // Permet de ne pas changer les index
		}
		
		players = new ArrayList<Player>(tempPlayers);
		
		// Debug : affichage des joueurs triés
//		for(int i=0; i<players.size(); i++)
//			System.out.println("i: " + i + " / player: " + players.get(i).getId() + " / classe:" + players.get(i).getClasseId());
	}
	
	private void createBaseMatches() {
		
		matches = new int[playersNumber][playersNumber];
		
		for(int i=0; i<playersNumber; i++) {
			for(int j=0; j<playersNumber; j++) {
				
				if(players.get(i).getClasseId() == players.get(j).getClasseId())
					matches[i][j] = -1;
				
			}
		}
		
	}
	
	public void createMatches() {
		
		Random rand = new Random();
		
		// Score de la solution
		//int score = -1;
		int roundOKCount = 0;
		
		while(roundOKCount < playersNumber * roundsNumber) {
			
			roundOKCount = 0;
			createBaseMatches();
		
			// Un tableau d'ArrayList => contient la liste de tous les matches prévus pour chaque joueur
			// Permet que si A joue contre B au round R, on ne cherche pas d'adversaire ensuite pour B au round R
			@SuppressWarnings("unchecked")
			ArrayList<Integer>[] doneMatches = new ArrayList[playersNumber];
			
			for(int i=0; i<playersNumber; i++) {
				doneMatches[i] = new ArrayList<Integer>();
			}
			
			// Pour chaque joueur (chaque ligne)
			for(int i=0; i<playersNumber; i++) {
				
				int classeId = players.get(i).getClasseId();
				int opponentsSize = possibleOpponentsId[classeId].size();
				
				// On essaie d'attribuer tous les rounds sur la ligne
				for(int round=1; round<=roundsNumber; round++) {
					
					// Si le joueur a déjà un match prévu ce round, on passe
					if(doneMatches[i].contains(round)) {
						roundOKCount++;
						continue;
					}
					
					boolean roundOK = false;
					int loopCount = 0;
				
					// Tant qu'on n'a pas trouvé d'adversaire
					while(!roundOK) {
						
						loopCount++;
						if(loopCount == MAX_TRIES) {
							//System.out.println(getMatchesTable());
							//System.out.println("STOP (Joueur : " + i + ")");
							round = 1000;
							i = 1000;
							break;
						}
						
						// Choix de l'adversaire au hasard
//						int randomNum = rand.nextInt((max - min) + 1) + min;
//						int advId = rand.nextInt((players.size() - 1) + 1);
						int advId = possibleOpponentsId[classeId].get(rand.nextInt(opponentsSize));
						
						// Si les deux joueurs pas dans la même classe et qu'aucun match n'est prévu
						if(matches[i][advId] == 0 && !doneMatches[advId].contains(round)) {
							matches[i][advId] = round;
							matches[advId][i] = round;
							doneMatches[i].add(round);
							doneMatches[advId].add(round);
							roundOK = true;
							roundOKCount++;
						}
						
					}
	
				}
			}
			
		}

		//System.out.println((float) timeElapsed/1000000.0 + " ms pour " + playersNumber + " joueurs");
	}
	
	
	public void computeScores() {
		
		for(int i=0; i<playersNumber; i++) {
			
			ArrayList<Integer> differentClasses = new ArrayList<Integer>();
			
			for(int j=0; j<playersNumber; j++) {
				
				if(matches[i][j] > 0) {
					if(!differentClasses.contains(players.get(j).getClasseId()))
						differentClasses.add(players.get(j).getClasseId());
				}
				
			}
			
			scores[i] = differentClasses.size();
		}
		
	}
	
	public double getAverageScore() {
		return Arrays.stream(scores).average().getAsDouble();
	}
	
	public int getMinScore() {
		return Arrays.stream(scores).min().getAsInt();
	}
	
	public int getMaxScore() {
		return Arrays.stream(scores).max().getAsInt();
	}
	
	public String getMatchesTable() {
		
		String str = "     |";
		
		for(int j=0; j<playersNumber; j++) {
			str += format(players.get(j).getId() + "/" + players.get(j).getClasseId()) + " ";
		}
		str += "\n";
		
		for(int i=0; i<playersNumber; i++) {
			str += format(players.get(i).getId() + "/" + players.get(i).getClasseId()) + "|";
			
			for(int j=0; j<playersNumber; j++) {
				if(matches[i][j] == -1)
					str += format("_ ") + " ";
				
				else if(matches[i][j] > 0)
					str += format(matches[i][j]) + " ";
				
				else
					str += format("") + " ";
			}

			str += format(scores[i]);
			
			str += "\n";
		}
		
		return str;
	}
	
	private static String format(int number) {
		String str = Integer.toString(number);
		
		while(str.length() < 5)
			str = " " + str;
		
		return str;
	}
	
	private static String format(String str) {
		while(str.length() < 5)
			str = " " + str;
		
		return str;
	}
	
	public void saveSolution(String filePath) throws IOException {		
		PrintWriter out = null;
		
		try {
			out = new PrintWriter(new FileWriter(filePath));
			
			Locale.setDefault(Locale.ENGLISH);
			
			out.println("------------------------------------------------------");
			out.println("Jeu de données : " + playersNumber + " joueurs / " + roundsNumber + " rounds");
			out.println("Taille des classes : " + classeSize.toString());
			out.println("------------------------------------------------------");
			out.println();
			out.print(getMatchesTable());

		} finally {
			if (out != null)
				out.close();
		}
	}

}
