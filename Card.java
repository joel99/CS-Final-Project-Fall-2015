/**********************
 Class Card
 **********************/
 
public class Card //implements Ownable
{
	
	private int countryID;
	private int type; //soldier, horse, cannnon - 0, 1, 2
	private int ownerID; //initialize to 0
	
	public Card(int id, int type){
		countryID = id;
		this.type = type;
		ownerID = 0;
	}
	
} 