import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeSet;

public class Pizza {

	public static void main(String[] args) {
		String dataset = "large"; // choose the dataset
		PizzaInstance inst = new PizzaInstance("data/" + dataset + ".txt"); // load the problem instance
		PizzaSolution sol = new PizzaSolution(inst, true); // initialize a (random) solution
		//randomIterativeImprovement(inst, sol, 100000); // perform iterative improvement
		//sol = tabuSearch(inst, sol, 100000); // perform tabu search
//		for (int i = 0; i < inst.M; i++) {
//			System.out.println(i + ": " + sol.onPizza[i]);
//		}
//		for (int j = 0; j < inst.N; j++) {
//			System.out.print(" " + sol.nConflicts[j]);
//		}
		//simAnnealing(inst, sol, 1000000); // perform simulated annealing
		bestSimAnnealing(inst, sol, 1000000); // perform improved simulated annealing
		System.out.println("Cost = " + sol.getCost()); // output the cost
		sol.output("output/" + dataset + ".out"); // output the solution
		sol.visualize("figures/" + dataset + ".png", true); // visualize the solution
	}

	
	
	
	// perform random iterative improvement for [nIter] iterations (solution will be changed!)
	public static void randomIterativeImprovement(PizzaInstance inst, PizzaSolution sol, int nIter) {
		
		// initialize
		double curCost = sol.getSmoothCost();
		Random rand = new Random();

		// keep applying local move
		for (int k = 0; k < nIter; k++) {
			int a = rand.nextInt(inst.M);
			sol.swapIngredient(a);
			double newCost = sol.getSmoothCost();
			if (newCost > curCost) {
				curCost = newCost;
			}
			else {
				sol.swapIngredient(a);
			}
		}

	}
	
	
	
	
	// Execute a tabu search, where [nIter] is the number of iterations; must return the best found solution
	// Solutions should be copied using the PizzaSolution.copy() function!
	public static PizzaSolution tabuSearch(PizzaInstance inst, PizzaSolution sol, int nIter) {
		
		PizzaSolution bestSol = sol.copy();
		double bestOverallCost = sol.getCost();

		int[] tabuList = new int[inst.M];
		int i = 0;

		while (i < nIter) {
			if (i % 500 == 0)
				System.out.println(i);
			// Get the best solution that is not in the tabulist
			double curCost = 0;
			int bestSwap = -1;
			for (int k = 0; k < inst.M; k++) {
				if (tabuList[k] <= i) {
					sol.swapIngredient(k);
					double newCost = sol.getSmoothCost();
					if (newCost > curCost) {
						curCost = newCost;
						bestSwap = k;
					}
					sol.swapIngredient(k);
				}
			}

			// Add the move to the tabulist
			tabuList[bestSwap] = i + inst.M / 2;
			sol.swapIngredient(bestSwap);

			// Only if the solution is the best we have seen, update bestSol
			if (sol.getCost() > bestOverallCost) {
				bestSol = sol.copy();
				bestOverallCost = sol.getCost();
			}

			i++;
		}
		
		return bestSol;
		
	}

	
	
	
	
	// perform simulated annealing, where [nIter] is the number of iterations
	// you may add more parameters if you wish (e.g. temperature settings, etc.)
	public static void simAnnealing(PizzaInstance inst, PizzaSolution sol, int nIter) {
		
		double temperature = 1000;
		double c = 0.99999;
		Random rand = new Random();
		double curCost = sol.getCost();

		int i = 0;
		while (temperature > 0.048) {
			int a = rand.nextInt(inst.M);
			sol.swapIngredient(a);

			double newCost = sol.getCost();

			double metropolisValue = Math.min(1, Math.exp((newCost - curCost) / temperature));
			if (rand.nextDouble() > metropolisValue) { // In this case, we don't make the move
				sol.swapIngredient(a);
			} else { // In this case, we do
				curCost = newCost;
			}

			i++;
			temperature *= c;
		}

		
	}
	
	
	
	// perform improved (!) simulated annealing, where [nIter] is the number of iterations
	// you may add more parameters if you wish (e.g. temperature settings, etc.)
	public static void bestSimAnnealing(PizzaInstance inst, PizzaSolution sol, int nIter) {

		double temperature = 850;
		double c = 0.999992;
		Random rand = new Random();
		double curCost = sol.getCost();

		int i = 0;
		while (i < nIter) {
			int a = rand.nextInt(inst.M);
			sol.swapIngredient(a);

			double newCost = sol.getCost();

			double metropolisValue = Math.min(1, Math.exp((newCost - curCost) / temperature));
			if (rand.nextDouble() > metropolisValue) { // In this case, we don't make the move
				sol.swapIngredient(a);
			} else { // In this case, we do
				curCost = newCost;
			}

			i++;
			temperature *= c;
		}
		
	}
	
	
	
}
