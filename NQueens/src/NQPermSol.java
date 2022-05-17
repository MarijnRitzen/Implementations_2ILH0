import java.util.Random;

public class NQPermSol extends NQueenSol {

	// Solution representation. Looking at the columns from left to right,
	// this array holds in order the row coordinates of the queens.
	// Of course this presumes the queens all have their own columns, but
	// we will make sure of that in the instantiation.
	int[] rowPositions;


	public NQPermSol(NQueenInstance inst, boolean random) {
		super(inst);

		// Initialize to -1 (needed when we want randomization).
		rowPositions = new int[inst.N];
		for (int col = 0; col < inst.N; col++) {
			rowPositions[col] = -1;
		}

		if (!random) {
			// Place all queens on the diagonal
			for (int i = 0; i < N; i++) {
				rowPositions[i] = i;
			}
		}
		else {
			// Place queens randomly (we allow two queens on the same position)
			Random rand = new Random();
			for (int i = 0; i < N; i++) {
				int rowPosition = rand.nextInt(N);
				while (rowTaken(rowPosition)) {
					rowPosition = rand.nextInt(N);
				}
				rowPositions[i] = rowPosition;
			}
		}
	}

	private boolean rowTaken(int row) {
		for (int i = 0; i < rowPositions.length; i++) {
			if (rowPositions[i] == row) return true;
		}

		return false;
	}

	// get the position of a queen
	public Pos getPosition(int i) { return new Pos(i, rowPositions[i]);	}

	// Swaps the columns of two queens indicated by their column positions firstQueenColumn and secondQueenColumn.
	// If both queen columns are the same, return false. Otherwise return true.
	public boolean swapQueenColumns(int firstQueenColumn, int secondQueenColumn) {
		if (firstQueenColumn == secondQueenColumn) return false;

		int temp = rowPositions[firstQueenColumn];
		rowPositions[firstQueenColumn] = rowPositions[secondQueenColumn];
		rowPositions[secondQueenColumn] = temp;

		return true;
	}

	// Undo a local move (must be executed after performing the same local move for correct behavior)
	public void undoLocalSwap(int firstQueenColumn, int secondQueenColumn) {
		int temp = rowPositions[firstQueenColumn];
		rowPositions[firstQueenColumn] = rowPositions[secondQueenColumn];
		rowPositions[secondQueenColumn] = temp;
	}
	
}
