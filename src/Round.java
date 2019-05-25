import java.util.ArrayList;
import java.util.Iterator;

public class Round 
{
	private int id ;
	private ArrayList<Match> listeMatch ;
	
	public Round(int id)
	{
		this.id = id ;
		this.listeMatch = new ArrayList<Match>() ;
	}
	
	// ---- getter
	public ArrayList<Match> getMatches()
	{
		return this.listeMatch ;
	}
	public int getId()
	{
		return this.id ;
	}
	public Match getMatchFromTable(int table)
	{
		Iterator<Match> it = this.listeMatch.iterator() ;
		while(it.hasNext())
		{
			Match m = it.next() ;
			if (m.getTable()==table)
			{
				return m ;
			}
		}
		return null ;
	}
	
	// ---- fonction
	public void addMatch(Match m)
	{
		this.listeMatch.add(m) ;
	}

	public boolean matchExists(Eleve e1, Eleve e2)
	{
		Iterator<Match> it = this.listeMatch.iterator() ;
		while(it.hasNext())
		{
			Match m = it.next() ;
			if((m.getEleve1().equals(e1) && m.getEleve2().equals(e2)) || (m.getEleve2().equals(e1) && m.getEleve1().equals(e2)))
			{
				return true ;
			}
		}
		return false ;
	}
	
}
