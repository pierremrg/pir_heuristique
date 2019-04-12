
public class Player {

	private int id;
	private int classeId;
	
	public Player(int id, int classeId) {
		this.id = id;
		this.classeId = classeId;
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
	
	
	
}
