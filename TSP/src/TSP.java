import java.util.Locale;

public class TSP {

	public static void main(String[] args) {
		Locale.setDefault(new Locale("en", "US"));	
		String dataset = "Burma"; // choose the dataset
		TSPInstance inst = new TSPInstance("data/" + dataset + ".txt"); // load the problem instance
		TSPSolution sol = new TSPSolution(inst, true); // initialize a (random) solution
		System.out.println("Running greedy algorithm...");
		sol.computeGreedy(); // run the greedy algorithm
		System.out.println("Finished.");
		System.out.println("Running First iterative improvement swap...");
		firstIterativeImprovementSwap(inst, sol); // perform iterative improvement
		System.out.println("Finished.");
		System.out.println("Running First iterative improvement 2-OPT...");
		firstIterativeImprovement2OPT(inst, sol); // perform iterative improvement
		System.out.println("Finished.");
		System.out.println("Cost = " + sol.getCost()); // output the cost
		sol.output("output/" + dataset + ".out"); // output the solution
		sol.visualize("figures/" + dataset + ".ipe"); // visualize the solution
	}
	

	public static TSPSolution getFirstImprovementSwap(TSPSolution previousSol) {
		double oldCost = previousSol.getCost();

		//loop over all pairs of elements
		for (int i = 0; i < previousSol.N; i++)
			for (int j = 0; j < previousSol.N; j++) {

				if (i != j) {
					previousSol.applySwap(i, j);

					if (previousSol.getCost() < oldCost) {
						return previousSol;
					} else {
						previousSol.undoSwap(i, j);
					}
				}
			}
		return previousSol;
	}
	
	// perform first iterative improvement using the swap local move (solution should be changed!)
	public static void firstIterativeImprovementSwap(TSPInstance inst, TSPSolution sol) {	
		
		// TODO
		TSPSolution bestSol;
		TSPSolution previousSol = sol.copy();
		double oldCost = sol.getCost();
		int M = 100000; //max iteration

		bestSol = getFirstImprovementSwap(previousSol).copy();
		int count = 0;

		while (previousSol.perm != bestSol.perm && count < M ) {//No improvement or max iterations has been reached
			previousSol = bestSol.copy(); //previousSol becomes best solution of last loop
			bestSol = getFirstImprovementSwap(previousSol).copy(); //try and find improvement of previousSol
			count += 1;
		}
	}
	
	

	public static TSPSolution getFirstImprovement2OPT(TSPSolution previousSol) {
		double oldCost = previousSol.getCost();

		//loop over all pairs of elements
		for (int i = 0; i < previousSol.N; i++)
			for (int j = 0; j < previousSol.N; j++) {

				if (i != j) {
					previousSol.apply2OPT(i, j);

					if (previousSol.getCost() < oldCost) {
						return previousSol;
					} else {
						previousSol.undo2OPT(i, j);
					}
				}
			}
		return previousSol;
	}

	// perform first iterative improvement using the 2-OPT local move (solution should be changed!)
	public static void firstIterativeImprovement2OPT(TSPInstance inst, TSPSolution sol) {	

		// TODO
		TSPSolution bestSol;
		TSPSolution previousSol = sol.copy();
		double oldCost = sol.getCost();
		int M = 100000; //max iteration

		bestSol = getFirstImprovement2OPT(previousSol).copy();
		int count = 0;

		while (previousSol.perm != bestSol.perm && count < M ) {//No improvement or max iterations has been reached
			previousSol = bestSol.copy(); //previousSol becomes best solution of last loop
			bestSol = getFirstImprovement2OPT(previousSol).copy(); //try and find improvement of previousSol
			count += 1;
		}

	}
	

}
