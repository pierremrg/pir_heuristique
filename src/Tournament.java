import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

public class Tournament {
	
	private GUI gui;
	private int level; // Niveau du tournoi (pour le message d'erreur si nombre impair)
	
	// Pour deux élèves de la même classe
	private static final int NO_POSSIBLE_MATCH = -1;
	
	// Influe sur la durée du traitement
	private static final int MAX_TRIES = 2000;
	
	// On oublie les N moins bons joueurs et on refait un match
//	private static final float FORGOTTEN_PERCENT = (float) 30/100;
//	private static final int FORGET_TURNS_NUMBER = 1;
//	private final float forgotten_percent;
//	private final int forget_turns_number;
	
	private final int classesNumber;
	private final int roundsNumber;
	private final int playersNumber;
	private int[][] matches;
	
	// Un tableau d'ArrayList => contient la liste de tous les matches prévus pour chaque joueur
	// Permet que si A joue contre B au round R, on ne cherche pas d'adversaire ensuite pour B au round R
	ArrayList<Integer>[] doneMatches;
	
	private int[] scores;
	
	// Pour chaque classe
//	private final int[] STUDENTS_NUMBER = {3, 4, 4, 4, 3, 3, 2,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,100};
//	private final int[] STUDENTS_NUMBER = {25,25,25,25,25,25,25,25,25,25,25,25,25};
	private final ArrayList<Integer> classeSize = new ArrayList<Integer>();//(Arrays.asList(15,20,18,21,28,25,16,27,30));
//	private final ArrayList<Integer> classeSize = new ArrayList<Integer>(Arrays.asList(6,2,8,4,10,8,7,6));
//	private final ArrayList<Integer> classeSize = new ArrayList<Integer>(Arrays.asList(30,20,25,25,25,25,25,25));
	
	private ArrayList<Player> players;
	private ArrayList<Integer>[] classPlayersId;
	private ArrayList<Integer>[] possibleOpponentsId;
	
	// Exception pour les groupes impairs
	@SuppressWarnings("serial")
	public static class OddClassException extends Exception {
		int level;

		public OddClassException(int level) {
			this.level = level;
		}
		
		public int getLevel() {
			return this.level;
		}
	};
	
	@SuppressWarnings("unchecked")
 	public Tournament(ArrayList<Player> ps, int roundsNumber, GUI gui, int level) throws OddClassException {
		this.gui = gui;
		this.level = level;
		
		this.players = ps;
		
		playersNumber = players.size();
		
		// Nombre impair de joueurs, erreur
		if(players.size() % 2 != 0) {
			throw new OddClassException(level);
//			System.out.println("Erreur : nombre impair de joueur dans le niveau " + level);
//			return;
		}
		
		
		
		doneMatches = new ArrayList[playersNumber];
		
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
		
		sortPlayers();
		
		// createPlayers();
		
		generatePossibleOpponents();
		
		createBaseMatches();
		
		// Scores
		scores = new int[playersNumber];
		
		// Amélioration
//		this.forgotten_percent = forgotten_percent;
//		this.forget_turns_number = forget_turns_number;
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
				if(matches[i][j] > 0) {
					sum_ligne += matches[i][j];
				}
					
			}
			
			if(sum_ligne != goal) {
				System.out.println("Erreur joueur " + i + " (" + sum_ligne + ")");
				correct = false;
			}
				
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
		
		possibleOpponentsId = new ArrayList[players.size()];
		
		// Tableau qui contient tous les IDs
		ArrayList<Integer> allPlayersIds = new ArrayList<Integer>();
		
		for(int i=0; i<playersNumber; i++)
			allPlayersIds.add(i);
		
//		for(int classe=0; classe<classesNumber; classe++) {
//			possibleOpponentsId[classe] = new ArrayList<Integer>(allPlayersIds);
//
//			for(int i=0; i<classPlayersId[classe].size(); i++) {
//				possibleOpponentsId[classe].remove(classPlayersId[classe].get(i));
//			}
//			
////			System.out.println(possibleOpponents.toString());
////			System.out.println(allPlayersIds.toString());
//		}m
		
		
		for(int i=0; i<playersNumber; i++) {
			
			possibleOpponentsId[i] = new ArrayList<Integer>(allPlayersIds);
			
			for(int j=0; j<classPlayersId[players.get(i).getClasseId()].size(); j++) {
				possibleOpponentsId[i].remove(classPlayersId[players.get(i).getClasseId()].get(j));
			}
			
			ArrayList<Integer> tmp = new ArrayList<Integer>(possibleOpponentsId[i]);
			
//			for(int j=0; j<playersNumber; j++) {
			for(int j : tmp) {
				if(!colorsMatch(i, j))
					possibleOpponentsId[i].remove(possibleOpponentsId[i].indexOf(j));
			}
			
		}

//		System.out.println(possibleOpponentsId[1]);
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
					matches[i][j] = NO_POSSIBLE_MATCH;
				
			}
		}
		
	}
	
	private boolean colorsMatch(int playerId1, int playerId2) {
		return (playerId1 % 2 != playerId2 % 2);
	}
	
	public void createMatches() throws OddClassException {

//		System.out.println("Génération des matches (" + playersNumber + " joueurs / " + classesNumber + " classes / " + roundsNumber + " rounds)...");
		
		
		
		int playerOKCount = 0;
		
		int tries = 0;
		
		while(playerOKCount < playersNumber) {
			
			tries++;
//			System.out.println(tries);
			if(tries > 5000) {
				// TODO Gérer message d'erreur
//				System.out.println("PAS DE MATCH POSSIBLE !");
				gui.writeConsole("Pas de solution trouvée. Division de la plus grande classe.");
				
				gui.displayMatchsTable();
				
				divideBiggestClass();
				
				tries = 0;
				
				return;
			}
			
			playerOKCount = 0;
			createBaseMatches();
			
			for(int i=0; i<playersNumber; i++) {
				doneMatches[i] = new ArrayList<Integer>();
			}
			
			// Pour chaque joueur (chaque ligne)
			for(int i=0; i<playersNumber; i++) {
				
				if(foundMatches(i) >= 0)
					playerOKCount++;
				else
					break;
			
			}
			
		}
		
//		System.out.println("Tries: " + tries);
		
		/*computeScores();
		double oldAverage = getAverageScore();
//		System.out.println(getMatchesTable());
		
//		int oldMatches[][] = copyBidimensionalArray(matches);
		
		// Amélioration du résultat : on supprime N fois les moins bons tirages
		for(int t=0; t<forget_turns_number; t++) {
			
			int oldMatches[][] = copyBidimensionalArray(matches);
			ArrayList<Integer> oldDoneMatches[] = copyArrayOfArrayList(doneMatches);
			ArrayList<Integer> playerIdsToForget = getPlayerIdsToForget();
			System.out.println(playerIdsToForget.toString());
			
			playerOKCount = 0;
			
			while(playerOKCount < playersNumber) {
				
				tries++;
				
				matches = copyBidimensionalArray(oldMatches);
				doneMatches = copyArrayOfArrayList(oldDoneMatches);
				
				// Pour chaque joueur pour lesquels on recommence le tirage, on efface ses matches
				for(int i : playerIdsToForget) {
					resetMatches(i);
				}
				
//				System.out.println("here: " + playerOKCount);
//				System.out.println(getMatchesTable());
				
				playerOKCount = playersNumber - getNotCompletedPlayersNumber();
				
				// Pour chaque joueur, on refait les matchs si besoin
				for(int i=0; i<playersNumber; i++) {
					
					// changed = -1 si pas de match, 0 si pas de changement, 1 si changement
					int changed = foundMatches(i);
					System.out.println("changed:" + changed);
					
					if(changed == 1) {
						playerOKCount++;
						
						System.out.println(getMatchesTable());
					}
						
					else if(changed == -1)
						break;
				
				}
				
			}
		
		}
		
		
//		computeScores();
//		double newAverage = getAverageScore();
//		double diffAverage = newAverage - oldAverage;
//		
//		System.out.println(newAverage + " / " + oldAverage);
//		
//		System.out.println(diffAverage);
		*/

		//System.out.println((float) timeElapsed/1000000.0 + " ms pour " + playersNumber + " joueurs");
		//System.out.println(timeElapsed);
	}
	
	/**
	 * Trouve les matches pour un joueur
	 * @param playerId ID du joueur
	 * @return 0 si pas de changement pour ce joueur, 1 si changement, -1 si MAX_TRIES atteint
	 */
	private int foundMatches(int playerId) {
		
		int changed = 0;
		
		int i = playerId;
		
		Random rand = new Random();
		
//		int classeId = players.get(i).getClasseId();
//		int opponentsSize = possibleOpponentsId[classeId].size();
		int opponentSize = possibleOpponentsId[i].size();
		
		// On essaie d'attribuer tous les rounds sur la ligne
		for(int round=1; round<=roundsNumber; round++) {
			
			// Si le joueur a déjà un match prévu ce round, on passe
			if(doneMatches[i].contains(round)) {
				//roundOKCount++;
				continue;
			}
			
			boolean roundOK = false;
			int loopCount = 0;
		
			// Tant qu'on n'a pas trouvé d'adversaire
			while(!roundOK) {
				
				loopCount++;
				if(loopCount == MAX_TRIES) {
//					System.out.println(checkSolution());
//					System.out.println(getMatchesTable());
//					System.out.println("STOP (Joueur : " + i + ")");
//					round = 1000;
//					i = 1000;
//					break;
					return -1;
				}
				
				// Choix de l'adversaire au hasard
//				int randomNum = rand.nextInt((max - min) + 1) + min;
//				int advId = rand.nextInt((players.size() - 1) + 1);
//				int advId = possibleOpponentsId[classeId].get(rand.nextInt(opponentsSize));
//				int advId = possibleOpponentsId.get(rand.nextInt(opponentSize));
				int advId = possibleOpponentsId[i].get(rand.nextInt(opponentSize));
				
				// Si les deux joueurs pas dans la même classe et qu'aucun match n'est prévu
//				if(matches[i][advId] == 0 && colorsMatch(i, advId) && !doneMatches[advId].contains(round)) {
				if(matches[i][advId] == 0 && !doneMatches[advId].contains(round)) {
					matches[i][advId] = round;
					matches[advId][i] = round;
					doneMatches[i].add(round);
					doneMatches[advId].add(round);
					roundOK = true;
					//changed = 1;
					//roundOKCount++;
				}
				
			}

		}
		
		return changed;
	}
	
	/*/**
	 * Retourne les N plus mauvais tirages (selon FORGOTTEN_PERCENT)
	 *
	private ArrayList<Integer> getPlayerIdsToForget() {
		
		ArrayList<Integer> playerIdsToForget = new ArrayList<Integer>();
		
		computeScores();
//		System.out.println(getAverageScore());
		
		int tmpScores[] = Arrays.copyOf(scores, scores.length);
		
		int nbPlayersToForget = (int) (forgotten_percent * playersNumber);
		
		// TODO Faire un "shuffle" de tmpScores (sinon ce sont toujours les 1ers moins bons élèves qui sont modifiés !)
		
		for(int i=0; i<nbPlayersToForget; i++) {
			int min = Arrays.stream(tmpScores).min().getAsInt();
			int indexMin = Arrays.stream(tmpScores).boxed().collect(Collectors.toList()).indexOf(min);
			
			playerIdsToForget.add(indexMin);
			tmpScores[indexMin] = roundsNumber + 1; // Impossible d'avoir plus que ça
		}
		
		return playerIdsToForget;
	}
	
	private void resetMatches(int playerId) {
		
		for(int i=0; i<playersNumber; i++) {
			for(int j=0; j<playersNumber; j++) {
			
				if(matches[i][j] > 0 && (i == playerId || j == playerId)) {
					
					int oldRound = matches[i][j];
					
//					System.out.println(doneMatches[i].toString() + " / " + oldRound);
					doneMatches[i].remove(doneMatches[i].indexOf(oldRound));

					matches[i][j] = 0;
				}
				
			}
		}
		
	}
	
	private int getNotCompletedPlayersNumber() {
		
		int notCompleted = 0;
		
		int goal = 0;
		for(int i=1; i<=roundsNumber; i++) {
			goal += i;
		}

		int sum_ligne;
		
		for(int i=0; i<playersNumber; i++) {
			sum_ligne = 0;
			
			for(int j=0; j<playersNumber; j++) {
				if(matches[i][j] > 0) {
					sum_ligne += matches[i][j];
				}
			}
			
			if(sum_ligne != goal) {
				notCompleted++;
			}
				
		}
		
		return notCompleted;
	}*/
	
	/**
	 * Divise la plus grande classe en deux (la classe d'origine + rajoute une nouvelle classe)
	 * @throws OddClassException 
	 */
	private void divideBiggestClass() throws OddClassException {
		
		ArrayList<Integer> tempClasseSize = new ArrayList<Integer>(classeSize);
		int maxClasseId = tempClasseSize.indexOf(Collections.max(tempClasseSize));
		
		int newClasseId = classeSize.size();
		
		ArrayList<Player> newPlayers = new ArrayList<Player>();
		
		ArrayList<Player> tmpPlayers = new ArrayList<Player>();
		int i = 0;
		
		for(Player p : players) {	
			
			if(p.getClasseId() == maxClasseId) {
				
				if(i % 2 == 0) {
					p.setClasseId(newClasseId);
					tmpPlayers.add(p);
				}
				else {
					newPlayers.add(p);
				}
				
				i++;
			}
			else {
				newPlayers.add(p);
			}
		}
		
		for(Player p : tmpPlayers)
			newPlayers.add(p);
		
		this.players = newPlayers;
		
		Tournament newTournament = new Tournament(players, roundsNumber, gui, level);
		newTournament.createMatches();
		
		this.matches = newTournament.getMatches();
		
//		System.out.println(newTournament.getMatchesTable());
		
	}
	
	
	/**
	 * Retourne le global_score de la solution actuelle
	 * @return global_score(s) = average_score(s) - 2 * weight_lowest_score(s)
	 */
	public float getSolutionScore() {
		computePlayersScores();
		
		double averageScore = getAverageScore();
		
		// Calcul du weight_lowest_score
		double lowestScore = getMinScore();
		double weightLowestScore = 0;
		
		for(int i=0; i<playersNumber; i++) {
			if(scores[i] <= lowestScore)
				weightLowestScore++;
		}
		weightLowestScore /= playersNumber;
		
		// Average score entre 1 et 6 (souvent 4-5)
		// weightLowestScore entre 0 et 1 (souvent 0.5)
		return (float) (averageScore - 2 * weightLowestScore);
	}
	
	private void computePlayersScores() {
		
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
	
	private double getAverageScore() {
		return Arrays.stream(scores).average().getAsDouble();
	}
	
	private int getMinScore() {
		return Arrays.stream(scores).min().getAsInt();
	}
	
	private int getMaxScore() {
		return Arrays.stream(scores).max().getAsInt();
	}
	
	
	
	
	public String getMatchesTable() {
		
		String str = "           |";
		
		for(int j=0; j<playersNumber; j++) {
			str += format(players.get(j).getId() + "[" + players.get(j).getClasseId()) + "]";
		}
		str += "\n";
		
		for(int i=0; i<playersNumber; i++) {
			str += (i % 2 == 1) ? "(B)" : "(W)";
			str += format(players.get(i).getId() + "[" + players.get(i).getClasseId()) + "] |";
			
			for(int j=0; j<playersNumber; j++) {
				if(matches[i][j] == -1)
					str += format("_") + " ";
				
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
		
		while(str.length() < 6)
			str = " " + str;
		
		return str;
	}
	
	private static String format(String str) {
		while(str.length() < 6)
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
	
	private static int[][] copyBidimensionalArray(int origin[][]) {
		
		int copy[][] = new int[origin.length][];
		for(int i = 0; i < origin.length; i++) {
			copy[i] = new int[origin[i].length];
			
			for(int j=0; j<origin[i].length; j++) {
				copy[i][j] = origin[i][j];
			}
		}
		
		return copy;
	}
	
	private ArrayList<Integer>[] copyArrayOfArrayList(ArrayList<Integer>[] origin){
		
		@SuppressWarnings("unchecked")
		ArrayList<Integer>[] copy = new ArrayList[origin.length];
		
		for(int i=0; i<origin.length; i++)
			copy[i] = new ArrayList<Integer>(origin[i]);
		
		return copy;
	}
	
	
	/************************* Getters / Setters *************************/
	
	public ArrayList<Player> getPlayers(){
		return players;
	}
	
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	
	public int[][] getMatches(){
		return matches;
	}
	
	public void setMatches(int[][] matches) {
		this.matches = matches;
	}
	
	public int getNbRounds()
	{
		return this.roundsNumber ;
	}
	
	public void setGUI(GUI gui) {
		this.gui = gui;
	}

}
