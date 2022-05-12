import java.util.Locale;

public class TSP {

	public static void main(String[] args) {
		Locale.setDefault(new Locale("en", "US"));	
		String dataset = "Burma"; // choose the dataset
		TSPInstance inst = new TSPInstance("data/" + dataset + ".txt"); // load the problem instance
		TSPSolution sol = new TSPSolution(inst, true); // initialize a (random) solution
		sol.computeGreedy(); // run the greedy algorithm
		//firstIterativeImprovementSwap(inst, sol); // perform iterative improvement
		//firstIterativeImprovement2OPT(inst, sol); // perform iterative improvement
		System.out.println("Cost = " + sol.getCost()); // output the cost
		sol.output("output/" + dataset + ".out"); // output the solution
		sol.visualize("figures/" + dataset + ".ipe"); // visualize the solution
	}
	
	
	
	// perform first iterative improvement using the swap local move (solution should be changed!)
	public static void firstIterativeImprovementSwap(TSPInstance inst, TSPSolution sol) {	
		
		// TODO
		
	}
	
	
	
	// perform first iterative improvement using the 2-OPT local move (solution should be changed!)
	public static void firstIterativeImprovement2OPT(TSPInstance inst, TSPSolution sol) {	

		// TODO

	}
	

}
