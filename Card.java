/**********************
 Class Card
**********************/
 
public class Card //implements Ownable
{
    private Country country;
    private int type; //soldier, horse, cannnon - 0, 1, 2
    //wildcard - 3 (To be implemented)
    private int ownerId; //initialize to -1 (no one owns)
	
	public String toString(){
		return country.toString() + "\t: " + Util.cardTypes[type]; 
	}
	
    public Card(Country c, int type){
	country = c;
	this.type = type;
	ownerId = -1;
    }
	
    public Country getCountry(){
	return country;
    }
    
    public int getType(){
	return type;
    }

    public void setOwnerId(int newId){
	ownerId = newId;
    }

} 
