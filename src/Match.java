
public class Match 
{
	private int table ;
	private int round ;
	private Eleve eleve1 ;
	private Eleve eleve2 ;
	
	public Match(Eleve e1,Eleve e2, int round)
	{
		this.eleve1 = e1 ;
		this.eleve2 = e2 ;
		this.round = round ;

	}
	
	// ---- getter
	public int getTable()
	{
		return this.table ;
	}
	public Eleve getEleve1()
	{
		return this.eleve1 ;
	}
	public Eleve getEleve2()
	{
		return this.eleve2 ;
	}
	
	// ---- setter
	public void setTable(int table)
	{
		this.table = table ;
	
	}
	
}
