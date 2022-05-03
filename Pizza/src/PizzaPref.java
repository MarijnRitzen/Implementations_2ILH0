import java.util.ArrayList;

public class PizzaPref {
	
	int nOrder; // number of orders from this customer
	int[] indicator; // indicates per ingredient whether it is liked (1), disliked (-1), or neutral (0)
	ArrayList<Integer> likes; // the set of ingredients liked by this customer
	ArrayList<Integer> hates; // the set of ingredients hated by this customer
	
	public PizzaPref(int nOrder) {
		this.nOrder = nOrder;
		likes = new ArrayList<Integer>();
		hates = new ArrayList<Integer>();
	}
	
	// add ingredient that customer likes
	public void addLike(int k) {
		likes.add(k);
	}
	
	// add ingredient that customer hates
	public void addHate(int k) {
		hates.add(k);
	}
	
	// get the liked ingredients list
	public ArrayList<Integer> getLikes() {
		return likes;
	}
	
	// get the hated ingredients list
	public ArrayList<Integer> getHates() {
		return hates;
	}
	
	// get the number of orders for this customer
	public int getNrOrders() {
		return nOrder;
	}
	
	// initialize the indicator vector (1 if customer likes ingredient, -1 if customer hates ingredient, 0 otherwise)
	public void initIndicator(int M) {
		indicator = new int[M];
		for (int k: likes) indicator[k] = 1;
		for (int k: hates) indicator[k] = -1;
	}
	
	// returns how customer feels about ingredient (1 if customer likes ingredient, -1 if customer hates ingredient, 0 otherwise)
	public int feelsAboutIngr(int k) {
		return indicator[k];
	}
	
}
