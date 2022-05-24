import java.util.Random;

public class Pizza {

	public static void main(String[] args) {
		String dataset = "difficult"; // choose the dataset
		PizzaInstance inst = new PizzaInstance("data/" + dataset + ".txt"); // load the problem instance
		//PizzaSolution sol = new PizzaSolution(inst, true); // initialize a (random) solution
		//sol.computeGreedy(); // run the original greedy algorithm
		AntColonyOpt ants = new AntColonyOpt(inst.N, 1, 1, 0.5, 0.1, 1.0 / inst.M); // make object for Ant Colony Optimization
		//sol.computeGreedy(ants); // run the ant colony greedy algorithm
		PizzaSolution sol = antColony(inst, 10, 1000, ants); // perform ant colony optimization
		//PizzaSolution sol = antColonyRank(inst, ..., ..., ants); // perform ant colony optimization with rank-based pheromone depositing
		//PizzaSolution sol = geneticAlg(inst, ..., ..., ...); // perform the genetic algorithm
		System.out.println("Cost = " + sol.getCost()); // output the cost
		sol.output("output/" + dataset + ".out"); // output the solution
		sol.visualize("figures/" + dataset + ".png", true); // visualize the solution
	}

	
	
	
	// perform ant colony optimization with [nAnts] ants and [nIter] iterations/rounds
	public static PizzaSolution antColony(PizzaInstance inst, int nAnts, int nIter, AntColonyOpt ants) {
		// Solution to run all the iterations
		PizzaSolution sol = new PizzaSolution(inst, true);

		// Keep track of best solution
		PizzaSolution bestSol = sol.copy();
		double bestCost = bestSol.getCost();
		
		for (int round = 0; round < nIter; round++) {

			ants.initRound();

			for (int ant = 0; ant < nAnts; ant++) {
				sol.computeGreedy(ants); // run the ant colony greedy algorithm
			}

			ants.concludeRound();

			if (sol.getCost() > bestCost) {
				bestCost = sol.cost;
				bestSol = sol.copy();
			}

		}
		
		return bestSol;
	}
	
	
	
	// perform ant colony optimization with [nAnts] ants and [nIter] iterations/rounds and rank-based pheromone depositing
	public static PizzaSolution antColonyRank(PizzaInstance inst, int nAnts, int nIter, AntColonyOpt ants) {
		
		PizzaSolution bestSol = new PizzaSolution(inst, true);
		
		// TODO
		
		return bestSol;
	}
	
	
	
	
	// perform genetic algorithm with [select] solutions after selection, and [offSpring] solutions after crossovers/mutations
	// The number of iterations/generations must be [nIter]
	// You may choose the type of selection yourself
	public static PizzaSolution geneticAlg(PizzaInstance inst, int select, int offSpring, int nIter) {
		
		PizzaSolution bestSol = new PizzaSolution(inst, true);
		
		// TODO
		
		return bestSol;
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

	}
	
	
}
