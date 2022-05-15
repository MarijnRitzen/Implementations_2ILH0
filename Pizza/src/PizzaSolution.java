import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.imageio.ImageIO;

public class PizzaSolution {

	PizzaInstance instance; // instance
	boolean[] onPizza; // solution representation (bit vector for which ingredients are on the pizza)
	int N; // a shorthand for the number of preferences
	int M; // a shorthand for the number of ingredients
	double cost; // cost of most recent call to getCost
	ArrayList<ArrayList<Integer>> lovers; // for each ingredient, the set of customers that like that ingredient
	ArrayList<ArrayList<Integer>> haters; // for each ingredient, the set of customers that hate that ingredient
	int[] nConflicts; // the number of conflicts per customer
	
	
	// constructor
	public PizzaSolution(PizzaInstance inst, boolean random) {
		instance = inst;
		N = instance.N;
		M = instance.M;
		
		// initialize with an empty pizza
		onPizza = new boolean[M];
		
		if (random) {
			// randomly add ingredients or not
			Random rand = new Random();
			for (int i = 0; i < M; i++) {
				if (rand.nextDouble() > 0.5) onPizza[i] = true;
			}
		}
		
		// initialize extra data structures
		lovers = new ArrayList<ArrayList<Integer>>();
		haters = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < M; i++) {
			ArrayList<Integer> L = new ArrayList<Integer>();
			ArrayList<Integer> H = new ArrayList<Integer>();
			for (int j = 0; j < N; j++) {
				int r = instance.prefs.get(j).feelsAboutIngr(i);
				if (r == 1) L.add(j);
				else if (r == -1) H.add(j);
			}
			lovers.add(L);
			haters.add(H);
		}
		
		nConflicts = new int[N];
		for (int j = 0; j < N; j++) {
			int r = 0; 
			PizzaPref pp = instance.prefs.get(j);
			for (int k: pp.getLikes()) if (!onPizza[k]) r++;
			for (int k: pp.getHates()) if (onPizza[k]) r++;
			nConflicts[j] = r;
		}
		
	}
	
	
	// copy the solution (if you want to keep track of the best solution found)
	public PizzaSolution copy() {
		PizzaSolution sol = new PizzaSolution(instance, false);
		for (int i = 0; i < N; i++) {
			sol.nConflicts[i] = nConflicts[i];
		}
		for (int i = 0; i < M; i++) {
			sol.onPizza[i] = onPizza[i];
		}
		return sol;
	}
	
	
	// returns the number of ingredients on the pizza
	public int getNrOnPizza() {
		int r = 0;
		for (boolean b: onPizza) if (b) r++;
		return r;
	}
	
	
	// add an ingredient to the pizza
	public void addToPizza(int k) {
		onPizza[k] = true;
		for (int j: lovers.get(k)) nConflicts[j]--;
		for (int j: haters.get(k)) nConflicts[j]++;
	}
	
	
	// remove an ingredient from the pizza
	public void removeFromPizza(int k) {
		onPizza[k] = false;
		for (int j: lovers.get(k)) nConflicts[j]++;
		for (int j: haters.get(k)) nConflicts[j]--;		
	}
	
	
	// change status of ingredient (add to pizza if it was not on, otherwise remove from pizza) 
	public void swapIngredient(int k) {
		onPizza[k] = !onPizza[k];
		if (onPizza[k]) addToPizza(k);
		else removeFromPizza(k);
	}
	
	// undo swap ingredient
	public void undoSwapIngredient(int k) {
		swapIngredient(k);
	}	
	
	
	// compute the cost of a solution
	public double getCost() {
		cost = 0;
		for (int j = 0; j < N; j++) {
			if (nConflicts[j] == 0) cost += instance.prefs.get(j).getNrOrders();
		}
		return cost;
	}
	
	
	// Computes the smoothed cost of a solution. The returned cost will be the number of orders if there are no conflicts.
	// Otherwise it will be between 0 and < 0.5 times the number of orders depending on the fraction of ingredients that
	// are wrong
	public double getSmoothCost() {
		cost = 0;

		for (int j = 0; j < N; j++) {
			double fractionIncorrect = nConflicts[j] / (double)(instance.prefs.get(j).getHates().size() + instance.prefs.get(j).getLikes().size());
			cost += nConflicts[j] == 0 ? instance.prefs.get(j).getNrOrders() :
					(int)(instance.prefs.get(j).getNrOrders() * (1 - fractionIncorrect) / 3);
		}
		
		return cost;
	}
	
	
	
	
	
	// -------------------------------- OUTPUT STUFF ----------------------------------------------------
	
	class IntPair implements Comparable<IntPair> {
		int a, b;
		public IntPair(int a, int b) {this.a = a; this.b = b;};
		public int compareTo(PizzaSolution.IntPair o) {
			return Integer.compare(b, o.b);
		}
	}
	
	// visualize solution as ipe file
	public void visualize(String filename, boolean reorder) {
		
		if (N > 1000) {
			System.out.println("Solution too large too visualize!");
			return;
		}
		
		int sqSize = (int)Math.ceil(1900.0 / (N + 3.0));
		if (sqSize > 32) sqSize = 32;
		BufferedImage image = new BufferedImage((N + 3) * sqSize, (2 * M + 2) * sqSize, BufferedImage.TYPE_INT_RGB);
		
		// background in gray
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				image.setRGB(x, y, Color.lightGray.getRGB());
			}
		}
		
		// first the solution itself
		for (int i = 0; i < M; i++) {
			Color col = new Color(255, 255, 255);
			if (onPizza[i]) col = new Color(0, 0, 220);
			for (int x = 0; x < sqSize; x++) {
				for (int y = 0; y < sqSize; y++) {
					image.setRGB(x, i * sqSize + y, col.getRGB());
				}
			}
		}
		
		// reorder if necessary
		ArrayList<IntPair> perm = new ArrayList<IntPair>();
		
		for (int i = 0; i < N; i++) {
			int conflicts = 0;
			PizzaPref pp = instance.prefs.get(i);
			for (int r: pp.getLikes()) if (!onPizza[r]) conflicts++;
			for (int r: pp.getHates()) if (onPizza[r]) conflicts++;
			perm.add(new IntPair(i, conflicts));
		}
		
		if (reorder) Collections.sort(perm);
		
		
		double maxCount = 0;
		for (int i = 0; i < N; i++) maxCount = Math.max(maxCount, instance.prefs.get(i).getNrOrders());
		
		// then the customers
		for (int j = 0; j < N; j++) {
			for (int i = 0; i < M; i++) {
				Color col = new Color(255, 255, 255);
				int k = instance.prefs.get(perm.get(j).a).feelsAboutIngr(i);
				if (k == 1) {
					if (onPizza[i]) col = new Color(0, 200, 0);
					else col = new Color(200, 200, 0);
				}
				if (k == -1) {
					if (onPizza[i]) col = new Color(220, 0, 0);
					else col = new Color(150, 150, 150);
				}
				for (int x = 0; x < sqSize; x++) {
					for (int y = 0; y < sqSize; y++) {
						image.setRGB((j + 3) * sqSize + x, i * sqSize + y, col.getRGB());
					}
				}
			}
			boolean happy = true;
			PizzaPref pp = instance.prefs.get(perm.get(j).a);
			for (int r: pp.getLikes()) if (!onPizza[r]) happy = false;
			for (int r: pp.getHates()) if (onPizza[r]) happy = false;
			
			Color col = new Color(150, 150, 250);
			if (happy) col = new Color(0, 0, 220);
			
			double frac = pp.getNrOrders() / maxCount;
			int nPixels = (int)Math.floor(M * sqSize * frac);
			for (int x = 0; x < sqSize; x++) {
				for (int y = 0; y < nPixels; y++) {
					image.setRGB((j + 3) * sqSize + x, (M + 2) * sqSize + y, col.getRGB());
				}
			}			
		}
		
		// Write file
		File file = new File(filename);
		try {
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			System.out.println("Could not output image!");
			e.printStackTrace();
		}		
		
	}
	
	
	
	// write solution to file
	public void output(String filename) {
		
		File file = new File(filename);
		try {
			PrintStream ps = new PrintStream(file);
			ps.println(getNrOnPizza()); // number of ingredients on pizza
			// write IDs of pizza ingredients
			for (int i = 0; i < M; i++) {
				if (onPizza[i]) ps.println(i);
			}
			ps.close();
		} catch (IOException e) {
			System.out.println("Could not write to output file!");
			e.printStackTrace();
		}
		
	}
	
	
	
}
