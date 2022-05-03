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
		PizzaSolution sol = new PizzaSolution(inst, false); // initialize a (random) solution
		randomIterativeImprovement(inst, sol, 100000); // perform iterative improvement
		//sol = tabuSearch(inst, sol, 100000); // perform tabu search
		//simAnnealing(inst, sol, 1000000); // perform simulated annealing
		//bestSimAnnealing(inst, sol, 1000000); // perform improved simulated annealing
		System.out.println("Cost = " + sol.getCost()); // output the cost
		sol.output("output/" + dataset + ".out"); // output the solution
		sol.visualize("figures/" + dataset + ".png", true); // visualize the solution
	}

	
	
	
	// perform random iterative improvement for [nIter] iterations (solution will be changed!)
	public static void randomIterativeImprovement(PizzaInstance inst, PizzaSolution sol, int nIter) {
		
		// initialize
		double curCost = sol.getCost();
		Random rand = new Random();

		// keep applying local move
		for (int k = 0; k < nIter; k++) {
			int a = rand.nextInt(inst.M);
			sol.swapIngredient(a);
			double newCost = sol.getCost();
			if (newCost > curCost) {
				curCost = newCost;
			}
			else {
				sol.swapIngredient(a);
			}
		}
		
		// TODO: Change to use smoothed cost function

	}
	
	
	
	
	// Execute a tabu search, where [nIter] is the number of iterations; must return the best found solution
	// Solutions should be copied using the PizzaSolution.copy() function!
	public static PizzaSolution tabuSearch(PizzaInstance inst, PizzaSolution sol, int nIter) {
		
		PizzaSolution bestSol = sol.copy();

		// TODO
		
		return bestSol;
		
	}

	
	
	
	
	// perform simulated annealing, where [nIter] is the number of iterations
	// you may add more parameters if you wish (e.g. temperature settings, etc.)
	public static void simAnnealing(PizzaInstance inst, PizzaSolution sol, int nIter) {
		
		// TODO
		
	}
	
	
	
	// perform improved (!) simulated annealing, where [nIter] is the number of iterations
	// you may add more parameters if you wish (e.g. temperature settings, etc.)
	public static void bestSimAnnealing(PizzaInstance inst, PizzaSolution sol, int nIter) {
		
		// TODO
		
	}
	
	
	
}
