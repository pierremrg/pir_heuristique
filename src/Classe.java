import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.itextpdf.text.BaseColor;

public class Classe 
{
	private int id ;
	private String prof ;
	private ArrayList<Eleve> listeEleves ;
	private BaseColor couleur ;
	
	public Classe(int id, String prof)
	{
		this.id = id ;
		this.prof = prof ;
		this.listeEleves = new ArrayList<Eleve>() ;
		
		// couleur en rvb random
		int r = new Random().nextInt(200)+50;
		int v = new Random().nextInt(200)+50;
		int b = new Random().nextInt(200)+50;
		
		this.couleur = new BaseColor(r, v, b) ;
	}
	
	// ----- getter
	public String getProf()
	{
		return this.prof ;
	}
	public ArrayList<Eleve> getListe()
	{
		return this.listeEleves ;
	}
	public BaseColor getCouleur()
	{
		return this.couleur ;
	}
	public int getNbEleves()
	{
		return this.listeEleves.size() ;
	}
	public int getId()
	{
		return this.id ;
	}
	public int getNbElevesNiveau(int niveau)
	{
		return this.getGroupeNiveau(niveau).size() ;
	}
	public Eleve getEleveFromId(int id)
	{
		Iterator<Eleve> it = this.listeEleves.iterator() ;
		while(it.hasNext())
		{
			Eleve e = it.next() ;
			if(e.getId()==id)
			{
				return e ;
			}
		}
		return null ;
	}
	
	// ----- fonctions
	// ajoute un élève a la classe
	public void addEleve(Eleve e)
	{
		this.listeEleves.add(e) ;
	}
	
	// renvoi la liste d'un niveau donné
	public ArrayList<Eleve> getGroupeNiveau(int niveau)
	{
		ArrayList<Eleve> groupe = new ArrayList<Eleve>() ;
		Iterator<Eleve> it = this.listeEleves.iterator() ;
		while(it.hasNext())
		{
			Eleve e = it.next() ;
			if (e.getNiveau()==niveau)
			{
				groupe.add(e);
			}
		}
		return groupe ;
	}
}
