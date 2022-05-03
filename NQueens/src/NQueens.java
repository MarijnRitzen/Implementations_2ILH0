
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

		while (true) {
			int lowestCost = sol.getCost();
			int bestMoveQueen = -1;
			int bestMove = -1;

			for (int queen = 0; queen < inst.N; queen++) { // For every queen
				for (int move = 0; move < 8; move++) { // For every move
					if (sol.applyLocalMove(queen, move)) {
						// If applyLocalMove returns True, move was valid and
						// we check new cost and have to inverse the move
						int currentCost = sol.getCost();
						if (currentCost < lowestCost) {
							lowestCost = currentCost;
							bestMoveQueen = queen;
							bestMove = move;
						}

						sol.undoLocalMove(queen, move);
					}
				}
			}

			// If we have reached a local minimum, the best improvement is as good as the current solution and thus
			// we never got into the if statement that modifies bestMove, so we quit the while loop
			if (bestMove == -1)
				break;

			sol.applyLocalMove(bestMoveQueen, bestMove);
		}

	}
	
	

	// perform best iterative improvement (solution should be changed directly)
	public static void bestIterativeImprovement(NQueenInstance inst, NQPermSol sol) {
		
		// TODO
		
	}
	
}
