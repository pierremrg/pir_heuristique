import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Heuristique {
	
	private static ArrayList<Player> players = new ArrayList<>();
	
	public static void main(String[] args) {
				
		final int NB_SOLUTIONS = 1;

		// final int NB_PLAYERS = 30;
		final int NB_ROUNDS = 6;
		
		final boolean saveSolution = false;
		
		readFromJSON();
			
		Tournament tournament = new Tournament(players, NB_ROUNDS);
		
		// Moyenne de temps pour NB_SOLUTIONS solutions
		long sum_duration = 0;
		
		// Score moyen et minimum
		double sum_score = 0;
		int sum_min_score = 0;
		int sum_max_score = 0;
		
		for(int i=0; i<NB_SOLUTIONS; i++) {
			
			// Solution
			long startTime = System.nanoTime();
			
//			System.out.println(tournament.getMatchesTable());
			
			tournament.createMatches();
			
			long timeElapsed = System.nanoTime() - startTime;
			sum_duration += timeElapsed;
			
			// Calcul des scores
			tournament.computeScores();
			
			double score = tournament.getAverageScore();
			int min_score = tournament.getMinScore();
			int max_score = tournament.getMaxScore();
			
			sum_score += score;
			sum_min_score += min_score;
			sum_max_score += max_score;
			
			boolean correct = tournament.checkSolution();
			
			System.out.println("Solution " + (i+1) + " trouvée en " + (float)timeElapsed/1000000.0 + " ms"
					+ " / Score moyen : " + score + " / Score minimum : " + min_score + " / Score maximum : " + max_score + " / Correct : " + correct);
			
			if(saveSolution) {
				try {
					System.out.print("Sauvegarde de la solution " + (i+1) + "... ");
					tournament.saveSolution("solution" + (i+1) + ".txt");
					System.out.println("Solution " + (i+1) + " sauvegardée.");
				} catch (IOException e) {
					System.err.println("Erreur lors de la sauvegarde du fichier.");
				}
			}
		}
		
		
		System.out.println("------------------------------------------------------");
		System.out.println("Durée moyenne pour " + NB_SOLUTIONS + " solutions : " + (float)sum_duration/(float)NB_SOLUTIONS/1000000.0 + "ms");
		System.out.println("Score moyen pour " + NB_SOLUTIONS + " solutions : " + sum_score/(float)NB_SOLUTIONS
				+ " / Score minimal moyen pour " + NB_SOLUTIONS + " solutions : " + (float)sum_min_score/(float)NB_SOLUTIONS
				+ " / Score maximal moyen : " + (float)sum_max_score/(float)NB_SOLUTIONS);
		System.out.println("------------------------------------------------------");
		
		
		System.out.println(tournament.getMatchesTable());		
	}
	
	/**
	 * Permet de lire des données depuis un fichier JSON
	 * 
	 * @return le nombre d'élèves lus
	 */
	public static int readFromJSON() {
		int nbEleves = 0;
		
		
		
		
		// Création du JSONPArser
		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(new FileReader("./donnees_eleves.json"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
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
				
				Player p = new Player(Integer.parseInt((String)eleve.get("id")), Integer.parseInt((String)classe.get("id")));
				players.add(p);
				nbEleves++;
			}
		}
		return nbEleves;
	}

}
