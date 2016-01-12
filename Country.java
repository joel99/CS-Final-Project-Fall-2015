/**********************
 Class Country
 **********************/ 

public class Country{

	//no need to store continents, tracked in id.
	private int id;
	private String name;
	private int ownerId;
	
	private User owner;
	
	private int troops;
	private int borders[];	//holds ids of bordering countries
	private int[] mapLoc;
	
	/*
	public Country(int id){
		this.id = id;
	}
	*/
	public Country(int id, String name, int[] borders, int[] mapLoc){
		this.id = id;
		this.name = name;
		this.borders = borders; 
		this.mapLoc = mapLoc;
		ownerId = 0;
		owner = null;
		troops = 0;
	}
	
	public String getName(){
		return name;
	}
	
	public String toString(){
		return name;
	}
	
	public int getOwnerId(){
		return ownerId;
	}
	
	/*
	public User getOwner(){
		return owner;
	}
	
	public User setOwner(User newOwner){
		User temp = owner;
		owner = newOwner;
		return temp;
	}	
	*/
	
	public int setOwnerId(int newOwnerId){
		int temp = ownerId;
		ownerId = newOwnerId;
		return temp;
	}
	
	
	
	public void addTroops(int num){
		troops += num;
	}
	
	
	public int getTroops(){
		return troops;
	}
	
	//map updating done in global b/c map is a global kinda thing???
	public int[] getMapLoc(){
		return mapLoc;
	}
	
	//for update function
	public String status(){
		return Integer.toString(ownerId) + Integer.toString(getTroops());
	}
	
}