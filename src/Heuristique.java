public class Heuristique {
	
	public static void main(String[] args) {
		
		final int NB_SOLUTIONS = 1;
		final int NB_PLAYERS = 30;
		final int NB_ROUNDS = 6;
		
		Tournament tournament = new Tournament(NB_PLAYERS, NB_ROUNDS);
		
		// Moyenne de temps pour NB_SOLUTIONS solutions
		long sum_duration = 0;
		
		// Score moyen et minimum
		double sum_score = 0;
		int sum_min_score = 0;
		int sum_max_score = 0;
		
		for(int i=0; i<NB_SOLUTIONS; i++) {
			
			// Solution
			long startTime = System.nanoTime();
			
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
			
//			try {
//				System.out.print("Sauvegarde de la solution " + (i+1) + "... ");
//				tournament.saveSolution("solution" + (i+1) + ".txt");
//				System.out.println("Solution " + (i+1) + " sauvegardée.");
//			} catch (IOException e) {
//				System.err.println("Erreur lors de la sauvegarde du fichier.");
//			}
		}
		
		
		System.out.println("------------------------------------------------------");
		System.out.println("Durée moyenne pour " + NB_SOLUTIONS + " solutions : " + (float)sum_duration/(float)NB_SOLUTIONS/1000000.0 + "ms");
		System.out.println("Score moyen pour " + NB_SOLUTIONS + " solutions : " + sum_score/(float)NB_SOLUTIONS
				+ " / Score minimal moyen pour " + NB_SOLUTIONS + " solutions : " + (float)sum_min_score/(float)NB_SOLUTIONS
				+ " / Score maximal moyen : " + (float)sum_max_score/(float)NB_SOLUTIONS);
		System.out.println("------------------------------------------------------");
		
		
//		System.out.println(tournament.getMatchesTable());		
	}

}
