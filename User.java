/**********************
 Class User - Superclass for players and ai
**********************/ 

import java.util.ArrayList;

public class User{

    private int id;
    private ArrayList <Card> cards;
    private ArrayList <Country> countriesOwned;
    private String name;
    private char nickname;

    public User(int id){
	name = Integer.toString(id);
	nickname = name.charAt(0);
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
    
    public int getID() {
    	return id;
    }
	
    public void add(Card c){
	cards.add(c);
    }
	
    public void add(Country c){
	countriesOwned.add(c);
    }
	
    public ArrayList<Country> getCountries(){
	return countriesOwned;
    }

    public char getNick(){
	return nickname;
    }
  
    public char setNick(char nick){
	char temp = getNick();
	nickname = nick;
	return temp;
    }
}
