
public class Player {

	private int id;
	private int classeId;
	private String prenom;
	private String nom;
	
	public Player(int id, int classeId) {
		this.id = id;
		this.classeId = classeId;
	}
	
	public Player(int id, int classeId, String prenom, String nom) {
		this.id = id;
		this.classeId = classeId;
		this.prenom = prenom;
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

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	
	
	
	
}
