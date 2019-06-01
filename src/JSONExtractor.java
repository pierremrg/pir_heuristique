import java.io.File;
import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.itextpdf.text.BaseColor;

public class JSONExtractor 
{
	// -------------------------- EXTRACTION DU JSON ----------------------
	// -------------------------- recupere le json ------------------------
	public static Tournoi ExtractJSON(File file)
	{
		JSONParser parser = new JSONParser();
		JSONObject json = new JSONObject();
		try
		{
			Object obj = parser.parse(new FileReader(file)) ;
			json = (JSONObject) obj ;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		// ------------- Couleur des classes  -------------
		BaseColor[] tabCoul = {BaseColor.BLUE, BaseColor.ORANGE,BaseColor.GREEN, BaseColor.MAGENTA, BaseColor.CYAN, BaseColor.PINK, BaseColor.YELLOW, BaseColor.RED, BaseColor.LIGHT_GRAY}; 
		

		// ------- créé le tournoi ---------
		// recupere les infos du tournoi
		String nomTournoi = (String)json.get("nom") ;
		String date = (String)json.get("date") ;
		// créé le tournoi
		Tournoi tournoi = new Tournoi(nomTournoi,date) ;
		
		
		// recup la liste de classe
		JSONArray listeClasse = (JSONArray)json.get("classe") ;
		// on parcourt le json pour recuperer les eleves et les classes	
		// pour chaque classe
		for (Object c : listeClasse)
		{
			// ------- créé la classe -----------
			// recup l'objet classe
			JSONObject classe = (JSONObject) c ;
			// recup les infos de la classe
			int idClasse = Integer.parseInt((String)classe.get("id") );
			String prof = (String)classe.get("prof") ;
			// attribue une couleur
			BaseColor couleur = tabCoul[idClasse] ;
						
			// créé la classe
			Classe cl = new Classe(idClasse, prof, couleur) ;
			
			// recup la liste des eleves et la parcourt
			JSONArray listeEleve = (JSONArray)classe.get("eleve") ;
			for(Object e : listeEleve)
			{
				// ----- créé l'eleve ----------
				// recup l'objet
				JSONObject eleve = (JSONObject) e ;
				// recup les infosd e l'eleve
				int idEleve = Integer.parseInt((String)eleve.get("id")) ;
				String nomEleve = (String)eleve.get("nom") ;
				int niveau = Integer.parseInt((String)eleve.get("niveau"));
				// créé l'eleve
				Eleve el = new Eleve(idEleve, nomEleve, idClasse, niveau) ;
				// ajoute à la classe
				cl.addEleve(el);
			}
			// ajoute la classe au tournoi
			tournoi.addClasse(cl);
		}
		
		return tournoi ;
		
	}
	
	

}
