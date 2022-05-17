import java.util.ArrayList;

class Node {
	ArrayList<Integer> adj;
	int match;
	boolean visited;
	double p;
	public Node() {
		adj = new ArrayList<Integer>();
	}
}

public class Hungarian {

	int N;
	double[][] W;
	Node[] A, B;
	
	public Hungarian(int N) {
		this.N = N;
		W = new double[N][N];
		A = new Node[N];
		B = new Node[N];
		for (int i = 0; i < N; i++) {
			A[i] = new Node();
			B[i] = new Node();
		}
	}
	
	private boolean aug(int x) {
		if (A[x].visited) return false;
		A[x].visited = true;
		
		for (Integer k: A[x].adj) {
			B[k].visited = true;
			if (B[k].match == -1 || aug(B[k].match)) {
				B[k].match = x;
				return true;
			}
		}
		return false;
	}
	
	private int checkMatch() {
		for (int i = 0; i < N; i++) {
			A[i].match = -1; B[i].match = -1;
		}
		
		int S = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) A[j].visited = false;
			if (aug(i)) S++;
		}
		
		for (int i = 0; i < N; i++) {
			if (B[i].match == -1) continue;
			A[B[i].match].match = i;
		}
		
		for (int i = 0; i < N; i++) {
			A[i].visited = false;
			B[i].visited = false; 
		}
		
		for (int i = 0; i < N; i++) {
			if (A[i].match == -1) aug(i);
		}
		
		double X = Double.MAX_VALUE;
		for (int i = 0; i < N; i++) {
			if (!A[i].visited) continue;
			for (int j = 0; j < N; j++) {
				if (B[j].visited) continue;
				X = Math.min(X, W[i][j] - A[i].p - B[j].p);
			}
		}
		
		for (int i = 0; i < N; i++) {
			if (A[i].visited) A[i].p += X;
			if (B[i].visited) B[i].p -= X;
		}
		
		return S;
	}
	
	private void matchIt() {
		
		for (int i = 0; i < N; i++) {
			A[i].p = Double.MAX_VALUE; B[i].p = 0.0;
			for (int j = 0; j < N; j++) A[i].p = Math.min(A[i].p, W[i][j]);
		}
		
		int S = 0;
		while (S < N) {
			for (int i = 0; i < N; i++) {
				A[i].adj.clear();
				for (int j = 0; j < N; j++) {
					if (Math.abs(A[i].p + B[j].p - W[i][j]) < 0.00001) A[i].adj.add(j);
				}
			}
			S = checkMatch();
		}
		
	}
	
	
	public ArrayList<Pos> align(ArrayList<Pos> P1, ArrayList<Pos> P2) {
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				W[i][j] = Pos.distsqr(P1.get(i), P2.get(j));
			}
		}
		
		matchIt();
		ArrayList<Pos> R = new ArrayList<Pos>();
		for (int i = 0; i < N; i++) {
			R.add(P2.get(A[i].match));
		}
		
		return R;
	}
	
	
}
