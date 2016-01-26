/**********************
 Class User - Superclass for players and ai
**********************/ 

import java.util.ArrayList;

public class User{

    private int id;
    private ArrayList <Card> cards;
    private ArrayList <Country> countriesOwned;
    private String name;
    //private boolean alive;	//because actually removing the user from users is a lil annoying, I think...

    public User(int id){
	name = Integer.toString(id);
	cards = new ArrayList<Card>();
	countriesOwned = new ArrayList<Country>();
	this.id = id;
    }
	
    public String toString(){
	return name;
    }
	
    public String getName(){
	return name;
    }
	
    public void setName(String newName){
	name = newName;
    }
    public int getId() {
    	return id;
    }
	
    public void add(Card c){
	cards.add(c);
    }
	
    public ArrayList<Card> getCards(){
	return cards;
    }
	
	//NO WILDCARDS
	public Card getCard(String name){
		name = name.toLowerCase().trim();
		for (Card c: cards){
			if (c.getCountry().toString().toLowerCase().equals(name))
				return c;
		}
		return null;
	}
	
    public void add(Country c){ //Add in order so that we can check continent ownership easier
	for (int i = 0; i < countriesOwned.size(); i++)
	    if (c.compareTo(countriesOwned.get(i)) < 0){
		countriesOwned.add(i,c);
		return;
	    }
	countriesOwned.add(c);	//if input country has highest id.
    }
	
    public ArrayList<Country> getCountries(){
	return countriesOwned;
    }
	
    public int numTroops(){
	int n = 0;
	for (Country c: countriesOwned)
	    n += c.getTroops();
	return n;
    }
  
    public boolean owns(Country c){//check if own a certain country.
	return c.getOwnerId() == id;
	}

	//inefficient but mad work to fix :(
	
	public boolean owns(int id){//check if own a certain country.
	for (Country c: countriesOwned)
	    if (c.getId() == id)
		return true;
	return false;
    }
	
    public int calcReinforcements(){//aw, who needs .contains anyways...
	//calculate number of continents owned
	int continentBonus = 0;
	for (Continent c: Util.continents){
	    boolean ownsThis = true;
	    for (int i = c.getIdLow(); i < c.getIdHigh(); i++){
		if (!owns(i)){
		    ownsThis = false;
		    break;
		}
	    }
	    if (ownsThis) 
		continentBonus += c.getBonus(); 
	}
	return (Math.max(3,countriesOwned.size() / 3) + continentBonus);
    }
}
