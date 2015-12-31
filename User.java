/**********************
 Class User - Superclass for players and ai
 **********************/ 

public class User{

	public int id;
	private ArrayList <Card> cards;
	private ArrayList <Country> userCountries;
	private String name;

	public User(int id){
		name = Integer.toString(id);
		this.id = id;
	}
	
	public String toString(){
		return name;
	}
	
	public String getName(){
		return name;
	}
	
	public void add(Card c){
		cards.add(c);
	}
	public void add(Country c){
		countries.add(c);
	}
}