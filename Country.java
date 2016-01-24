/**********************
 Class Country
**********************/ 

public class Country {

    //no need to store continents, tracked in id.
    private int id;
    private String name;	
    private int troops;
    private int borders[];	//holds ids of bordering countries
    private int[] mapLoc; 	//stored in proper y,x format.
	private int ownerId;
	
    public Country(int id, String name, int[] borders, int[] mapLoc){
	this.id = id;
	this.name = name;
	this.borders = borders; 
	this.mapLoc = mapLoc;
	ownerId = 0;
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
	
    public int getId(){
	return id;
    }
	
    public int[] getBorders(){
	return borders;
    }
	
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
	return Integer.toString(getOwnerId()) + Integer.toString(getTroops());
    }
	
    public int compareTo(Country other){ //not actually a comparable class :)
	if (other == null)
	    throw new NullPointerException();
	return id - other.getId();
    }
	
    public boolean equals(String str){
	return name.toLowerCase().equals(str.toLowerCase());
    }
	
	
}
