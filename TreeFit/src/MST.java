import java.util.ArrayList;

class IDDistPair implements Comparable<IDDistPair> {
	int id, parent;
	double dist;

	public IDDistPair(int id, int parent, double dist) {
		this.id = id; this.parent = parent; this.dist = dist;
	}
	
	public int compareTo(IDDistPair o) {
		return Double.compare(dist, o.dist);
	}
}


public class MST {
	
	// compute the geometric MST from a set of points (no comments, just ignore)
	public static ArrayList<Segment> computeMST(ArrayList<Pos> P) {
		
		boolean[] in = new boolean[P.size()];
		in[0] = true;
		double[] dist = new double[P.size()];
		int[] parent = new int[P.size()];
		for (int i = 1; i < P.size(); i++) {
			dist[i] = Pos.distance(P.get(0), P.get(i));
			parent[i] = 0;
		}
		
		ArrayList<Segment> R = new ArrayList<Segment>();
		while (R.size() < P.size() - 1) {
			double minDist = Double.MAX_VALUE;
			int minID = -1;
			for (int i = 1; i < P.size(); i++) {
				if (in[i]) continue;
				if (dist[i] < minDist) {
					minDist = dist[i];
					minID = i;
				}
			}
			in[minID] = true;
			Pos newP = P.get(minID);
			R.add(new Segment(newP, P.get(parent[minID])));
			for (int i = 1; i < P.size(); i++) {
				if (in[i]) continue;
				double newDist = Pos.distance(newP, P.get(i));
				if (newDist < dist[i]) {
					dist[i] = newDist;
					parent[i] = minID;
				}
			}			
		}
		
		return R;
		
	}
	
}
