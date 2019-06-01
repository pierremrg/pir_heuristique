import java.util.ArrayList;
import java.util.Iterator;

public class Tournoi 
{
	private String nom ;
	private String date ;
	private ArrayList<Classe> listeClasse;
	private ArrayList<Eleve> groupeCouleur1 ;
	private ArrayList<Round> listeRound ;
	
	public Tournoi (String nom, String date)
	{
		this.nom = nom ;
		this.date = date ;
		this.listeClasse = new ArrayList<Classe>() ;
		this.groupeCouleur1 = new ArrayList<Eleve>() ; // eleves pairs
		this.listeRound = new ArrayList<Round>() ;
	}
	
	// --------------- getter
	public String getNom()
	{
		return this.nom ;
	}
	public String getDate()
	{
		return this.date ;
	}
	public ArrayList<Classe> getClasses()
	{
		return this.listeClasse ;
	}
	public int getNbClasses()
	{
		return this.listeClasse.size() ;
	}
	public ArrayList<Eleve> getGroupeCouleur1()
	{
		return this.groupeCouleur1 ;
	}

	public ArrayList<Round> getRounds()
	{
		return this.listeRound ;
	}
	public Eleve getEleveFromId(int id)
	{
		Iterator<Classe> it = this.listeClasse.iterator() ;
		while(it.hasNext())
		{
			Classe c = it.next();
			Eleve e = c.getEleveFromId(id);
			if(e!=null)
			{
				return e ;
			}
		}
		return null ;
	}
	public int getNbEleves()
	{
		int total = 0 ;
		Iterator<Classe> it = this.listeClasse.iterator() ;
		while(it.hasNext())
		{
			Classe c = it.next() ;
			total = total + c.getNbEleves() ;
		}
		return total ;
	}
	public Classe getClasseFromId(int id)
	{
		Iterator<Classe> it = this.listeClasse.iterator() ;
		while(it.hasNext())
		{
			Classe c = it.next();
			if(c.getId()==id)
			{
				return c ;
			}
		}
		return null ;
	}
	public ArrayList<ArrayList<Eleve>> getGroupeNiveau(int niv)
	{
		// creation groupe niveau
		ArrayList<ArrayList<Eleve>> groupeNiveau = new ArrayList<ArrayList<Eleve>>() ;
		// parcours des classes
		Iterator<Classe> it = this.listeClasse.iterator() ;
		while(it.hasNext())
		{
			Classe c = it.next();
			// ajout du groupe de niveau de la classe
			groupeNiveau.add(c.getGroupeNiveau(niv)) ;	
		}
		return groupeNiveau ;
	}
	
	public Match getMatchFromEleveRound(Eleve eleve, Round round)
	{
		// on parcourt les rounds pour trouver ses matchs
		Iterator<Round> itR = this.listeRound.iterator() ;
		while(itR.hasNext())
		{
			// recup le round
			Round r = itR.next() ;
			// on verifie que c'est le bon
			if(round.getId()==(r.getId()))
			{
				// on le parcourt pour trouver son match
				Iterator<Match> itM = round.getMatches().iterator() ;
				while(itM.hasNext())
				{
					// on recup le match
					Match match = itM.next() ;
					// si c'est le bon match, 
					if(match.getEleve1().equals(eleve) || match.getEleve2().equals(eleve))
					{
						return match ;		
					}
				}
			}

		}
		return null ;
	}
	
	// -------------- fonctions
	// remplissent les listes

	public void addClasse(Classe c)
	{
		// ajoute la classe
		this.listeClasse.add(c) ;
	}


	public void addNbRound(Tournament t)
	{
		for(int i=1;i<=t.getNbRounds();i++)
		{
			this.listeRound.add(new Round(i)) ;
		}
	}
	
	public boolean matchExists(Eleve e1, Eleve e2, int round)
	{
		Round r = this.listeRound.get(round-1);
		return r.matchExists(e1, e2) ;

	}
	
	public boolean dansGroupCoul1(Eleve e)
	{
		Iterator<Eleve> it = this.groupeCouleur1.iterator() ;
		while(it.hasNext())
		{
			Eleve eleve = it.next() ;
			if(eleve.equals(e))
			{
				return true ;
			}
		}
		return false ;
	}
	
	// ajoute tous les matches en parcourant les 2 tableaux, pour 1 niveau
	public void addAllRoundNiv(Tournament t, int niv)
	{
		// recuperation des tableaux des matches
		int[][] tabMatch = t.getMatches() ;
		int [][] tabOtherMatch = t.getOtherMatches() ;
		
		
		//parcourt des rounds
		for (int i=1;i<=this.listeRound.size();i++)
		{
			// ---------------------- Matches Tableau 1 ------------------

			//parcourt du tableau de matches
			// les lignes et les colonnes sont les eleves, les cases le numero du round
			for(int j=0;j<tabMatch.length;j++)
			{
				 for(int k=0;k<tabMatch.length;k++)
				 {
					if(tabMatch[j][k]==i)
					{											
						// on recupere l'id des players associés à cette case
						int id1 = t.getPlayers().get(j).getId() ;
						int id2 = t.getPlayers().get(k).getId() ;
						
						// on recupere les eleves grace aux id
						Eleve e1 = this.getEleveFromId(id1) ;
						Eleve e2 = this.getEleveFromId(id2) ;
						
						// on ajoute au groupe couleur
						if(j%2==0 && !dansGroupCoul1(e1))
						{
							this.groupeCouleur1.add(e1) ;
						}
						if(k%2==0 && !dansGroupCoul1(e2))
						{
							this.groupeCouleur1.add(e2) ;
						}
						// on verifie que le match n'existe pas
						if(!this.matchExists(e1, e2, i))
						{
							// a l'ajoute
							this.listeRound.get(i-1).addMatch(new Match(e1,e2,i));
						}
					 }
				 }
			}
			

			// ---------------------- Matches Tableau 2
			// parcourt le tableau [player][round]
			// les colonnes sont les rounds, les lignes les eleves, les cases les eleves adversaires
			// parcours des lignes pour CE round i
			for(int l=0;l<tabOtherMatch.length;l++)
			{
				 // on recup l'adversaire potentiel
				int adv = tabOtherMatch[l][i-1] ;
				
				
				// si il s'agit d'un match
				if(adv != -1)
				{
					System.out.println("ligne "+ l + " col " + i + " adv " +adv) ;

					// on recupere l'id des players associés à cette case
					int id1 = t.getPlayers().get(l).getId() ;
					int id2 = t.getPlayers().get(adv).getId() ;
	
					// on recupere les eleves grace aux id
					Eleve e1 = this.getEleveFromId(id1) ;
					Eleve e2 = this.getEleveFromId(id2) ;
				
					// on ajoute pas au groupe couleur car il s'agit de matchs en plus, normalement l'eleve a déja été ajouté
					
					System.out.println("match bis") ;

					
					// on verifie que le match n'existe pas
					if(!this.matchExists(e1, e2, i))
					{
						// a l'ajoute
						this.listeRound.get(i-1).addMatch(new Match(e1,e2,i));
						System.out.println("ajout other match" + e1.getId()+" vs "+e2.getId()) ;
					}			
				}	
			}
		}		
	}
	
	// gere les numeros de tables
	public void addTables()
	{
		// numero de table
		int numTable = 0 ;
		
		// parcourt les eleves statiques
		Iterator<Eleve> it = this.groupeCouleur1.iterator() ;
		while(it.hasNext())
		{
			//recup eleve
			Eleve eleve = it.next() ;
			// change de table
			numTable++ ;
			
			// parcourt les rounds
			Iterator<Round> itR = this.listeRound.iterator() ;
			while(itR.hasNext())
			{
				//recup le round
				Round round = itR.next() ;
				// on le parcourt pour trouver son match
				Iterator<Match> itM = round.getMatches().iterator() ;
				while(itM.hasNext())
				{
					// on recup le match
					Match match = itM.next() ;
					// si c'est le bon match, 
					if(match.getEleve1().equals(eleve) || match.getEleve2().equals(eleve))
					{
						// ajoute la table au match
						Match m = this.getMatchFromEleveRound(eleve, round) ;
						m.setTable(numTable); 
					//	System.out.println("Round "+round.getId()+" Table "+m.getTable()+": "+m.getEleve1().getId()+" vs "+m.getEleve2().getId());
					}
				}
			}
			
		}
		
		
	}
}
