/**********************
 Class Country
 **********************/ 

public class Country{

	//no need to store continents, tracked in id.
	private int id;
	private String name;
	private int ownerId;
	private int troops;
	private int borders[];	//holds ids of bordering countries
	
	/*
	public Country(int id){
		this.id = id;
	}
	*/
	public Country(int id, String name, int[] borders){
		this.id = id;
		this.name = name;
		this.borders = borders;
		ownerId = 0;
		troops = 0;
	}
	
	/*
	//Should only be used @ intialization.
	public setName(String name){
		this.name = name;
	}
	*/
	public setOwner(int newOwner){
		ownerId = newOwner;
	}
	
	
}