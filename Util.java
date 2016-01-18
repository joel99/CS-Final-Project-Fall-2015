/*******************
 class Util - holds constants and driver's static methods 
 ******************/


public class Util{
    
	public static final Continent nAm = new Continent("North America", 5, 1, 10);
	public static final Continent sAm = new Continent ("South America", 2, 10, 14);
	public static final Continent africa = new Continent("Africa", 3, 14, 20);
	public static final Continent europe = new Continent("Europe", 5, 20, 27);
	public static final Continent australia = new Continent("Australia", 2, 27, 31);
	public static final Continent asia = new Continent("Asia", 7, 31, 43);

	public static final Continent[] continents = {nAm, sAm, africa, europe, australia, asia};
	
	
    public final int[] cardBonus = {4, 6, 8, 10, 12, 15};

    public int cardBonusCtr = 0; //num sets traded in so far

    public int getCardReinforce(){
	cardBonusCtr++;
	if (cardBonusCtr > cardBonus.length)
	    return (cardBonusCtr - cardBonus.length) * 5 + cardBonus[cardBonus.length - 1];
	return cardBonus[cardBonusCtr - 1];
    }

    //Wait function, for that authentic feel.
    public static void wait(int ms){try {Thread.sleep(ms);} catch (InterruptedException ie) {}}
	
    //@William - it'd be nice to make this generic, but I'm not sure how
    private static void swap( int i, int j , Country[] arr) {
	Country temp = arr[i];
	arr[i] = arr[j];
	arr[j] = temp;
    }
	
    public static void shuffle(Country[] arr) {
	int len = arr.length;
	//len - i. Elements to choose from.
	for (int i = 0; i < len; i++){
	    swap(i, i + (int)(Math.random()*(len-i)), arr);
	}
    }

}
