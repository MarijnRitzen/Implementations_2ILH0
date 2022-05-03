
public class NQueens {

	public static void main(String[] args) {
		String dataset = "8"; // choose the dataset
		NQueenInstance inst = new NQueenInstance("data/" + dataset + ".txt"); // load the problem instance
		NQBasicSol sol = new NQBasicSol(inst, true); // initialize a (random) solution
		//NQPermSol sol = new NQPermSol(inst, true); // initialize a (random) solution
		bestIterativeImprovement(inst, sol);
		System.out.println("Cost = " + sol.getCost());
		sol.visualize("figures/" + dataset + ".png");
		sol.output("output/" + dataset + ".txt");
	}
	
	

	// perform best iterative improvement (solution should be changed directly)
	public static void bestIterativeImprovement(NQueenInstance inst, NQBasicSol sol) {
		
		// TODO

	}
	
	

	// perform best iterative improvement (solution should be changed directly)
	public static void bestIterativeImprovement(NQueenInstance inst, NQPermSol sol) {
		
		// TODO
		
	}
	
}
