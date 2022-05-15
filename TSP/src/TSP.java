import java.util.Locale;

public class TSP {

	public static void main(String[] args) {
		Locale.setDefault(new Locale("en", "US"));	
		String dataset = "Beergardens"; // choose the dataset
		TSPInstance inst = new TSPInstance("data/" + dataset + ".txt"); // load the problem instance
		TSPSolution sol = new TSPSolution(inst, true); // initialize a (random) solution
		System.out.println("Running greedy algorithm...");
		sol.computeGreedy(); // run the greedy algorithm
		System.out.println("Finished.");
		System.out.println("Running First iterative improvement swap...");
		//firstIterativeImprovementSwap(inst, sol); // perform iterative improvement
		System.out.println("Finished.");
		System.out.println("Running First iterative improvement 2-OPT...");
		firstIterativeImprovement2OPT(inst, sol); // perform iterative improvement
		System.out.println("Finished.");
		System.out.println("Cost = " + sol.getCost()); // output the cost
		sol.output("output/" + dataset + ".out"); // output the solution
		sol.visualize("figures/" + dataset + ".ipe"); // visualize the solution
	}
	

	// Returns null if no improvement found, otherwise returns new solution
	// with swap applied
	public static TSPSolution applyFirstImprovementSwap(TSPSolution solution) {
		double oldCost = solution.getCost();

		//loop over all pairs of elements
		for (int i = 0; i < solution.N; i++) {
			for (int j = i + 1; j < solution.N; j++) {
				solution.applySwap(i, j);

				if (solution.getCost() < oldCost) {
					return solution;
				} else {
					solution.undoSwap(i, j);
				}
			}
		}

		return null;
	}

	// perform first iterative improvement using the swap local move (solution should be changed!)
	public static void firstIterativeImprovementSwap(TSPInstance inst, TSPSolution sol) {
		while (applyFirstImprovementSwap(sol) != null) {}
	}

	public static TSPSolution getFirstImprovement2OPT(TSPSolution solution) {
		double oldCost = solution.getCost();

		//loop over all pairs of elements
		for (int i = 0; i < solution.N; i++) {
			for (int j = i + 1; j < solution.N; j++) {
				solution.apply2OPT(i, j);

				if (solution.getCost() < oldCost) {
					return solution;
				} else {
					solution.undo2OPT(i, j);
				}
			}
		}

		return null;
	}

	// perform first iterative improvement using the 2-OPT local move (solution should be changed!)
	public static void firstIterativeImprovement2OPT(TSPInstance inst, TSPSolution sol) {
		while (getFirstImprovement2OPT(sol) != null) {}
	}
	

}
