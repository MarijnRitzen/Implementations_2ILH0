import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Pizza {

	public static void main(String[] args) {
		String dataset = "groups"; // choose the dataset
		PizzaInstance inst = new PizzaInstance("data/" + dataset + ".txt"); // load the problem instance
		double best = 0;
		//PizzaSolution sol = new PizzaSolution(inst, true); // initialize a (random) solution
		//sol.computeGreedy(); // run the original greedy algorithm
		//AntColonyOpt ants = new AntColonyOpt(inst.N, 1, 1, 0.1, 0.1, 1.0 / inst.M); // make object for Ant Colony Optimization
		//sol.computeGreedy(ants); // run the ant colony greedy algorithm
		//PizzaSolution sol = antColonyRank(inst, 10, 500, ants, 2.0); // perform ant colony optimization
		//PizzaSolution sol = antColonyRank(inst, ..., ..., ants); // perform ant colony optimization with rank-based pheromone depositing
		while (true) {
			PizzaSolution sol = geneticAlg(inst, 10, 20, 10000, 0.3, 0.1); // perform the genetic algorithm
			System.out.println("Cost = " + sol.getCost()); // output the cost
			if (sol.getCost() > best) {
				best = sol.getCost();
				sol.output("output/" + dataset + ".out"); // output the solution
				sol.visualize("figures/" + dataset + ".png", true); // visualize the solution
			}

		}

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

				if (sol.getCost() > bestCost) {
					bestCost = sol.cost;
					bestSol = sol.copy();
				}

				sol.addPheromones(ants, ants.Q * sol.cost);
			}

			ants.concludeRound();

		}
		
		return bestSol;
	}
	
	
	
	// perform ant colony optimization with [nAnts] ants and [nIter] iterations/rounds and rank-based pheromone depositing
	public static PizzaSolution antColonyRank(PizzaInstance inst, int nAnts, int nIter, AntColonyOpt ants, double beta) {
		// Solution to run all the iterations
		PizzaSolution sol = new PizzaSolution(inst, true);

		// Keep track of best solution
		PizzaSolution bestSol = sol.copy();
		double bestCost = bestSol.getCost();

		ArrayList<PizzaSolution> solutions = new ArrayList<>();

		for (int round = 0; round < nIter; round++) {

			ants.initRound();

			for (int ant = 0; ant < nAnts; ant++) {
				sol.computeGreedy(ants); // run the ant colony greedy algorithm
				PizzaSolution copy = sol.copy();
				copy.getCost();

				if (copy.cost > bestCost) {
					bestCost = copy.cost;
					bestSol = copy.copy();
				}

				solutions.add(copy);
			}

			Collections.sort(solutions);

			// Rank-based scheme
			for (int rank = 0; rank < nAnts; rank++) {
				solutions.get(rank).addPheromones(ants, 1.0 / nAnts * (beta - 2 * (beta - 1) * (rank - 1) / (nAnts - 1)) * ants.Q);
			}

			ants.concludeRound();

			solutions.clear();

		}

		return bestSol;
	}
	
	
	
	
	// perform genetic algorithm with [select] solutions after selection, and [offSpring] solutions after crossovers/mutations
	// The number of iterations/generations must be [nIter]
	// You may choose the type of selection yourself
	public static PizzaSolution geneticAlg(PizzaInstance inst, int select, int offSpring, int nIter, double pCross, double pMut) {
		
		PizzaSolution bestSol = new PizzaSolution(inst, true);
		double bestCost = bestSol.getCost();

		Random rand = new Random();

		// Creating starting population
		PizzaSolution[] population = new PizzaSolution[offSpring];

		for (int individual = 0; individual < offSpring; individual++) {
			PizzaSolution indv = new PizzaSolution(inst, true);
			population[individual] = indv;

			if (indv.getCost() > bestCost) {
				bestCost = indv.cost;
				bestSol = indv.copy();
			}
		}

		PizzaSolution[] afterSelection = new PizzaSolution[select];

		for (int i = 0; i < nIter; i++) {
			// Tournament based selection

			for (int ind = 0; ind < select; ind++) {
				// Get two distinct parents
				int first = rand.nextInt(offSpring);
				int second;

				do {
					second = rand.nextInt(offSpring);
				} while (first == second);

				afterSelection[ind] = population[first].cost > population[second].cost ? population[first] : population[second];
			}

			// crossover, mutation
			for (int child = 0; child < offSpring; child++) {

				// possibly crossover, otherwise pick random parent
				PizzaSolution sol;
				if (rand.nextDouble() < pCross) {
					sol = PizzaSolution.crossover(afterSelection[rand.nextInt(select)], afterSelection[rand.nextInt(select)]);
				} else {
					sol = afterSelection[rand.nextInt(select)];
				}

				// Mutate the new individual
				if (rand.nextDouble() < pMut)
					sol.mutate();

				// Keep track of best found so far
				if (sol.getCost() > bestCost) {
					bestCost = sol.cost;
					bestSol = sol.copy();
				}

				// Make new population
				population[child] = sol;
			}
		}
		
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
