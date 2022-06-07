import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class PizzaInstance {

	final List<PizzaPref> prefs; // the preferences of the potential customers
	final int N; // the number of preferences
	final int M; // the number of ingredients
	ArrayList<ArrayList<Integer>> lovers; // for each ingredient, the set of customers that like that ingredient
	ArrayList<ArrayList<Integer>> haters; // for each ingredient, the set of customers that hate that ingredient
	
	public PizzaInstance(String filename) {
		// initialize local variable to get around try-catch/final issue
		int n = 0, m = 0;
		ArrayList<PizzaPref> preferences = new ArrayList<PizzaPref>();
		
		File file = new File(filename);
		try {
			Scanner scan = new Scanner(file);
			
			n = scan.nextInt(); // number of customers
			for (int i = 0; i < n; i++) {
				int count, nLikes, nHates;
				count = scan.nextInt(); // the number of orders by this customer
				PizzaPref pp = new PizzaPref(count);
				nLikes = scan.nextInt(); // ingredients the customer likes
				for (int j = 0; j < nLikes; j++) {
					int x = scan.nextInt();
					m = Math.max(m, x);
					pp.addLike(x);
				}
				nHates = scan.nextInt(); // ingredients the customer hates
				for (int j = 0; j < nHates; j++) {
					int x = scan.nextInt();
					m = Math.max(m, x);
					pp.addHate(x);
				}
				preferences.add(pp);
			}
			
			scan.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not read file!");
			e.printStackTrace();
		}
		
		// determine number of ingredients and initialize indicators
		m++;
		for (PizzaPref pp: preferences) pp.initIndicator(m);
		
		// collect lovers and haters
		lovers = new ArrayList<ArrayList<Integer>>();
		haters = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < m; i++) {
			ArrayList<Integer> L = new ArrayList<Integer>();
			ArrayList<Integer> H = new ArrayList<Integer>();
			for (int j = 0; j < n; j++) {
				int r = preferences.get(j).feelsAboutIngr(i);
				if (r == 1) L.add(j);
				else if (r == -1) H.add(j);
			}
			lovers.add(L);
			haters.add(H);
		}
		
		
		// set final variables
		N = n;
		M = m;
		prefs = Collections.unmodifiableList(preferences);
	}
	
}
