import java.util.ArrayList;
import java.util.Random;

// a basic solution class
public class NQBasicSol extends NQueenSol {

	// the local moves (includes diagonal moves); for move i, move (i+4)%8 is opposite move 
	final static int[] dx = {1, 1, 0, -1, -1, -1, 0, 1};
	final static int[] dy = {0, 1, 1, 1, 0, -1, -1, -1};
	
	ArrayList<Pos> queens; // positions of all queens
	
	
	// standard constructor
	public NQBasicSol(NQueenInstance inst, boolean random) {
		super(inst);
		
		queens = new ArrayList<Pos>(); // queen positions
		
		if (!random) {
			// Place all queens on the diagonal
			for (int i = 0; i < N; i++) {
				queens.add(new Pos(i, i));
			}
		}
		else {
			// Place queens randomly (we allow two queens on the same position)
			Random rand = new Random();
			for (int i = 0; i < N; i++) {
				int x = rand.nextInt(N);
				int y = rand.nextInt(N);
				queens.add(new Pos(x, y));
			}			
		}
	}
	
	
	// get position of a queen
	public Pos getPosition(int i) {
		return queens.get(i);
	}
	
	
	
	// Moves queen [qID] in direction (dx[moveID], dy[moveID]); returns true if resulting position is valid, otherwise no change is made
	public boolean applyLocalMove(int qID, int moveID) {
		Pos p = new Pos(queens.get(qID).x + dx[moveID], queens.get(qID).y + dy[moveID]);
		if (p.x >= 0 && p.y >= 0 && p.x < N && p.y < N) {
			queens.get(qID).x = p.x;
			queens.get(qID).y = p.y;
			return true;
		}
		else return false;
	}
	
	// Undo a local move (must be executed after performing the same local move for correct behavior)
	public void undoLocalMove(int qID, int moveID) {
		applyLocalMove(qID, (moveID+4)%8);
	}
	
}
