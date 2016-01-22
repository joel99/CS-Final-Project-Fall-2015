/**********************
 Class Continent
**********************/
 
public class Continent{
	
    private String name;
    private int bonus;
    private int idLow;
    private int idHigh; //Stores highest ID + 1. LIKE AN ARRAY!
	
    public Continent(String name, int bonus, int idLow, int idHigh){
	this.name = name;
	this.bonus = bonus;
	this.idLow = idLow;
	this.idHigh = idHigh;
    }

    public String toString() {
	return name;
    }
	
    public int getIdLow(){
	return idLow;
    }
	
    public int getIdHigh(){
	return idHigh;
    }
	
    public int getBonus(){
	return bonus;
    }

}
