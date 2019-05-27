import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONExtractor 
{
	
	// -------------------------- EXTRACTION DU JSON ----------------------
	// -------------------------- recupere le json ------------------------
	public static Tournoi ExtractJSON(String path)
	{
		JSONParser parser = new JSONParser();
		JSONObject json = new JSONObject();
		try
		{
			Object obj = parser.parse(new FileReader(path)) ;
			json = (JSONObject) obj ;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		// ------- cr�� le tournoi ---------
		// recupere les infos du tournoi
		String nomTournoi = (String)json.get("nom") ;
		String date = (String)json.get("date") ;
		// cr�� le tournoi
		Tournoi tournoi = new Tournoi(nomTournoi,date) ;
		
		
		// recup la liste de classe
		JSONArray listeClasse = (JSONArray)json.get("classe") ;
		// on parcourt le json pour recuperer les eleves et les classes	
		// pour chaque classe
		for (Object c : listeClasse)
		{
			// ------- cr�� la classe -----------
			// recup l'objet classe
			JSONObject classe = (JSONObject) c ;
			// recup les infos de la classe
			int idClasse = Integer.parseInt((String)classe.get("id") );
			String prof = (String)classe.get("prof") ;
			// cr�� la classe
			Classe cl = new Classe(idClasse, prof) ;
			
			// recup la liste des eleves et la parcourt
			JSONArray listeEleve = (JSONArray)classe.get("eleve") ;
			for(Object e : listeEleve)
			{
				// ----- cr�� l'eleve ----------
				// recup l'objet
				JSONObject eleve = (JSONObject) e ;
				// recup les infosd e l'eleve
				int idEleve = Integer.parseInt((String)eleve.get("id")) ;
				String nomEleve = (String)eleve.get("nom") ;
				int niveau = Integer.parseInt((String)eleve.get("niveau"));
				// cr�� l'eleve
				Eleve el = new Eleve(idEleve, nomEleve, idClasse, niveau) ;
				// ajoute � la classe
				cl.addEleve(el);
			}
			// ajoute la classe au tournoi
			tournoi.addClasse(cl);
		}
		
		return tournoi ;
		
	}
	
	

}