
public class Player {

	private int id;
	private int classeId; // Utilisé pour "fusionner" deux classes si besoin
	private int realClasseId; // Cette valeur est fixe et ne doit pas changer
//	private String prenom;
	private String nom;
	
	public Player(int id, int classeId) {
		this.id = id;
		this.classeId = classeId;
	}
	
	public Player(int id, int classeId, String nom) {
		this.id = id;
		this.classeId = classeId;
		this.realClasseId = classeId; 
//		this.prenom = prenom;
		this.nom = nom;
	}
	
	public Player(Player original) {
		this.id = original.getId();
		this.classeId = original.getClasseId();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getClasseId() {
		return classeId;
	}

	public void setClasseId(int classeId) {
		this.classeId = classeId;
	}
	
	

//	public String getPrenom() {
//		return prenom;
//	}
//
//	public void setPrenom(String prenom) {
//		this.prenom = prenom;
//	}

	public int getRealClasseId() {
		return realClasseId;
	}

	public void setRealClasseId(int realClasseId) {
		this.realClasseId = realClasseId;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public String getNom() {
		return nom;
	}
	
}
