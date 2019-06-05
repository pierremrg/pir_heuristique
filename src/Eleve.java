
public class Eleve 
{
	private int id ;
	private String nom ;
	private int idClasse ;
	private int niveau ;
	
	public Eleve(int id, String nom, int idClasse, int niveau)
	{
		this.id = id ;
		this.nom = nom ;
		this.idClasse = idClasse ;
		this.niveau = niveau ;
	}
	
	// -- Getter
	public int getId()
	{
		return this.id ;
	}
	public String getNom()
	{
		return this.nom ;
	}
	public int getClasse()
	{
		return this.idClasse ;
	}
	public int getNiveau()
	{
		return this.niveau ;
	}
	public void setNiveau(int niv) {
		this.niveau = niv;
	}


}

