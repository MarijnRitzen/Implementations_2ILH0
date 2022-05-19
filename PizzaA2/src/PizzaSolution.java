import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.TreeSet;

import javax.imageio.ImageIO;

public class PizzaSolution implements Comparable<PizzaSolution> {

	// static functions
	public static PizzaSolution crossover(PizzaSolution sol1, PizzaSolution sol2) {
		PizzaSolution sol = new PizzaSolution(sol1.instance, false);
		
		// TODO
		
		//sol.recomputeConflicts(); Fix the nConflicts array, if necessary
		
		return sol;
	}
	
	
	PizzaInstance instance; // instance
	boolean[] onPizza; // solution representation (bit vector for which ingredients are on the pizza)
	int N; // a shorthand for the number of preferences
	int M; // a shorthand for the number of ingredients
	double cost; // cost of most recent call to getCost
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
    
    
    
    // greedy algorithm that iteratively picks the most popular choice for an ingredient
    public void computeGreedy() {
    	
    	int[][] ingrVotes = new int[M][2]; // how many customers (weighed by nr of orders) like (second index 0) or hate (second index 1) the ingredient
    	for (int i = 0; i < M; i++) {
    		for (Integer k: instance.lovers.get(i)) ingrVotes[i][0] += instance.prefs.get(k).nOrder;
    		for (Integer k: instance.haters.get(i)) ingrVotes[i][1] += instance.prefs.get(k).nOrder;
    	}
    	
    	boolean[] eliminated = new boolean[N]; // keep track of which customers have already been "eliminated"
    	TreeSet<Integer> remIngrs = new TreeSet<Integer>(); // the set of ingredients for which we have not made a decision yet
    	for (int i = 0; i < M; i++) remIngrs.add(i);
    	
    	for (int i = 0; i < M; i++) onPizza[i] = false; // reset to empty pizza

		Random rand = new Random();
    	// main loop of greedy algorithm
    	while (remIngrs.size() > 0) {
    		
    		// fitness proportionate selection
    		int ingr = 0;
    		boolean val = true;
    		double totalWeight = 0;

    		// Calculate the sum of the weights
    		for (Integer k: remIngrs) {
    			totalWeight += ingrVotes[k][0] + ingrVotes[k][1];
    		}

			if (totalWeight <= 0) break; // stop if the remaining customers don't care about the remaining ingredients

    		// Calculate cumulative probabilities and when we find that x < a_j, we pick that option.
			double uniformly_random = rand.nextDouble();
			double cumulativeProbability = 0;
			for (Integer k: remIngrs) {
				// Add chance of adding ingredient k
				cumulativeProbability += ingrVotes[k][0] / totalWeight;
				if (uniformly_random < cumulativeProbability) {
					val = true;
					ingr = k;
					break;
				};
				// Add chance of not adding ingredient k
				cumulativeProbability += ingrVotes[k][1] / totalWeight;
				if (uniformly_random < cumulativeProbability) {
					val = false;
					ingr = k;
					break;
				};
			}

    		
    		onPizza[ingr] = val; // set value according to greedy choice
    		remIngrs.remove(ingr); // remove ingredient from set	
    		
			for (Integer k: (val ? instance.haters.get(ingr) : instance.lovers.get(ingr))) { // eliminate all customers that hate/love ingredient and update ingredient counts
				if (eliminated[k]) continue;
				eliminated[k] = true;
				for (Integer x: instance.prefs.get(k).likes) ingrVotes[x][0] -= instance.prefs.get(k).nOrder;
				for (Integer x: instance.prefs.get(k).hates) ingrVotes[x][1] -= instance.prefs.get(k).nOrder;
			}    		

    	}
    	
    	recomputeConflicts(); // recompute the conflicts
    	
    }
    
    
    
    // greedy algorithm for ant colony optimization
    public void computeGreedy(AntColonyOpt ants) {

		int[][] ingrVotes = new int[M][2]; // how many customers (weighed by nr of orders) like (second index 0) or hate (second index 1) the ingredient
		for (int i = 0; i < M; i++) {
			for (Integer k: instance.lovers.get(i)) ingrVotes[i][0] += instance.prefs.get(k).nOrder;
			for (Integer k: instance.haters.get(i)) ingrVotes[i][1] += instance.prefs.get(k).nOrder;
		}

		boolean[] eliminated = new boolean[N]; // keep track of which customers have already been "eliminated"
		TreeSet<Integer> remIngrs = new TreeSet<Integer>(); // the set of ingredients for which we have not made a decision yet
		for (int i = 0; i < M; i++) remIngrs.add(i);

		for (int i = 0; i < M; i++) onPizza[i] = false; // reset to empty pizza

		Random rand = new Random();
		// main loop of greedy algorithm
		while (remIngrs.size() > 0) {

			// fitness proportionate selection
			int ingr = 0;
			boolean val = true;
			double totalWeight = 0;

			// Calculate the sum of the weights
			for (Integer k: remIngrs) {
				totalWeight += ants.getPreference(k, 0, ingrVotes[k][0]) + ants.getPreference(k, 1, ingrVotes[k][0]);
			}

			if (totalWeight <= 0) break; // stop if the remaining customers don't care about the remaining ingredients

			// Calculate cumulative probabilities and when we find that x < a_j, we pick that option.
			double uniformly_random = rand.nextDouble();
			double cumulativeProbability = 0;
			for (Integer k: remIngrs) {
				// Add chance of adding ingredient k
				cumulativeProbability += ants.getPreference(k, 0, ingrVotes[k][0]) / totalWeight;
				if (uniformly_random < cumulativeProbability) {
					val = true;
					ingr = k;
					break;
				};
				// Add chance of not adding ingredient k
				cumulativeProbability += ants.getPreference(k, 1, ingrVotes[k][1]) / totalWeight;
				if (uniformly_random < cumulativeProbability) {
					val = false;
					ingr = k;
					break;
				};
			}


			onPizza[ingr] = val; // set value according to greedy choice
			remIngrs.remove(ingr); // remove ingredient from set

			for (Integer k: (val ? instance.haters.get(ingr) : instance.lovers.get(ingr))) { // eliminate all customers that hate/love ingredient and update ingredient counts
				if (eliminated[k]) continue;
				eliminated[k] = true;
				for (Integer x: instance.prefs.get(k).likes) ingrVotes[x][0] -= instance.prefs.get(k).nOrder;
				for (Integer x: instance.prefs.get(k).hates) ingrVotes[x][1] -= instance.prefs.get(k).nOrder;
			}

		}

		recomputeConflicts(); // recompute the conflicts

		addPheromones(ants, ants.Q * getCost());
    	
    }
    
    
    
	// add pheromones
	public void addPheromones(AntColonyOpt ants, double amount) {
		for (int i = 0; i < M; i++) {
			ants.addPheromone(i, (onPizza[i] ? 0 : 1), amount);
		}
	}
	
	
	
	// mutate current solution
	public void mutate() {
		
		// TODO
		
	}
    
    
    
    // recompute the number of conflicts for each customer
    public void recomputeConflicts() {
		for (int j = 0; j < N; j++) {
			int r = 0; 
			PizzaPref pp = instance.prefs.get(j);
			for (int k: pp.getLikes()) if (!onPizza[k]) r++;
			for (int k: pp.getHates()) if (onPizza[k]) r++;
			nConflicts[j] = r;
		}
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
		for (int j: instance.lovers.get(k)) nConflicts[j]--;
		for (int j: instance.haters.get(k)) nConflicts[j]++;
	}
	
	
	// remove an ingredient from the pizza
	public void removeFromPizza(int k) {
		onPizza[k] = false;
		for (int j: instance.lovers.get(k)) nConflicts[j]++;
		for (int j: instance.haters.get(k)) nConflicts[j]--;		
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
	
	
	// comparisons based on cost -- you must first call getCost() on all solutions and then you can simply sort them (best solution first)
	public int compareTo(PizzaSolution o) {
		return Double.compare(o.cost, cost);
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
