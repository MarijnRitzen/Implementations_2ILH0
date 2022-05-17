import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;

public class TFSolution implements Comparable<TFSolution> {

	TFInstance instance; // instance
	ArrayList<Pos> tree; // points in the fitting tree
	int K; // a shorthand for the number of points in tree
	int N; // a shorthand for the number of points in dataset
	double cost; // cost of last call of getCost
	double stDev; // standard deviation for evolution strategy
	
	// constructor
	public TFSolution(TFInstance inst, boolean random) {
		instance = inst;
		N = instance.N;
		K = instance.K;
		stDev = 1.0;
		
		// initialize with points from the input
		tree = new ArrayList<Pos>();
		ArrayList<Integer> A = new ArrayList<Integer>();
		for (int i = 0; i < N; i++) A.add(i);
		if (random) Collections.shuffle(A); // choose random points from the input
		
		for (int i = 0; i < K; i++) {
			Pos p = inst.points.get(A.get(i));
			tree.add(new Pos(p.x, p.y));
		}
		
	}
	
	
	
	// special copy constructor
	public TFSolution(TFInstance inst, TFSolution sol) {
		instance = inst;
		N = instance.N;
		K = instance.K;
		stDev = sol.stDev;
		tree = new ArrayList<Pos>();
		for (int i = 0; i < K; i++) {
			tree.add(new Pos(sol.getPoint(i)));
		}
	}
	
	
	
	// copy the solution
	public TFSolution copy() {
		TFSolution sol = new TFSolution(instance, false);
		sol.tree.clear();
		for (int i = 0; i < K; i++) {
			Pos p = tree.get(i);
			sol.tree.add(new Pos(p.x, p.y));
		}
		sol.stDev = stDev;
		return sol;
	}
	
	
	
	// comparison function for ordering solutions (must call getCost on solutions first!)
	public int compareTo(TFSolution o) {
		return Double.compare(cost, o.cost);
	}
	
	
	
	// align with other solution, that is, order the points in the tree similarly
	// More precisely: order the points of this solution (in [tree]) such that the sum of squared distances
	// between the i^th point of this solution and the i^th point of [sol] is minimized
	public void align(TFSolution sol) {
		Hungarian H = new Hungarian(K);
		tree = H.align(sol.tree, tree);
	}
	
	
	
	// clip the points to the bounding box 
	public void clipPoint(int i) {
		Pos p = tree.get(i);
		p.x = Math.max(p.x, instance.minx);
		p.y = Math.max(p.y, instance.miny);
		p.x = Math.min(p.x, instance.maxx);
		p.y = Math.min(p.y, instance.maxy);	
	}
	
	
	
	// mutate point coordinates with radius [r] (you may choose your own distribution)
	public void mutate(double r) {
		
		// TODO
		
	}
	
	
	
	// update position of a point
	public void setPoint(int i, Pos p) {
		p.x = Math.max(p.x, instance.minx);
		p.x = Math.min(p.x, instance.maxx);
		p.y = Math.max(p.y, instance.miny);
		p.y = Math.min(p.y, instance.maxy);		
		tree.set(i, p);
	}
	
	
	
	// get position of a point
	public Pos getPoint(int i) {
		return tree.get(i);
	}
	
	
	
	// get standard deviation
	public double getStDev() {
		return stDev;
	}
	
	
	
	// set standard deviation
	public void setStDev(double val) {
		stDev = val;
	}
	
	
	
	// get the cost of the solution
	public double getCost() {
		
		// compute MST
		ArrayList<Segment> segs = MST.computeMST(tree);
		
		cost = 0.0;
		double fac = 1.0 / (double)N;
		
		// for each point in the input, compute the distance to the closest segment
		for (int i = 0; i < N; i++) {
			double minDist = Double.MAX_VALUE;
			for (Segment s: segs) {
				minDist = Math.min(minDist, s.getSqDistTo(instance.points.get(i)));
			}
			cost += fac * minDist;
		}
		
		return cost;
	}
	
	
	
	// -------------------------------- OUTPUT STUFF ----------------------------------------------------
	
	
	
	// visualize solution as ipe file
	public void visualize(String filename) {
		
		double scaleX = 512.0 / Math.max(instance.maxx - instance.minx, 1.0);
		double scaleY = 512.0 / Math.max(instance.maxy - instance.miny, 1.0);
		double scale = Math.min(scaleX, scaleY);
		
		File file = new File(filename);
		try {
			PrintStream ps = new PrintStream(file);
			
			// ipe preamble stuff
			ps.println("<?xml version=\"1.0\"?>");
			ps.println("<!DOCTYPE ipe SYSTEM \"ipe.dtd\">");
			ps.println("<ipe version=\"70218\" creator=\"Ipe 7.2.21\">");
			ps.println("<info created=\"D:20210227165909\" modified=\"D:20210227165909\"/>"); // nobody cares about this data
			
			// ipe style
			ps.println("<ipestyle name=\"custom\">");
			ps.println("<symbol name=\"mark/disk(sx)\" transformations=\"translations\">");
			ps.println("<path fill=\"sym-stroke\">");
			ps.println("0.6 0 0 0.6 0 0 e");
			ps.println("</path>");
			ps.println("</symbol>");
			ps.println("</ipestyle>");
			
			// the page (actual stuff!)
			ps.println("<page>");
			
			// first the input points
			for (int i = 0; i < N; i++) {
				ps.println("<use name=\"mark/disk(sx)\" pos=\"" + (scale * (instance.points.get(i).x - instance.minx)) + " " + (scale * (instance.points.get(i).y - instance.miny)) + " \" size=\"normal\" stroke=\"black\"/>");
			}
			
			// then the tree points
			for (int i = 0; i < K; i++) {
				ps.println("<use name=\"mark/disk(sx)\" pos=\"" + (scale * (tree.get(i).x - instance.minx)) + " " + (scale * (tree.get(i).y - instance.miny)) + " \" size=\"normal\" stroke=\"1 0 0\"/>");
			}
			
			// then the tree
			ArrayList<Segment> segs = MST.computeMST(tree);
			for (Segment s: segs) {
				ps.println("<path stroke=\"1 0 0\">");
				ps.println((scale * (s.a.x - instance.minx)) + " " + (scale * (s.a.y - instance.miny)) + " m");
				ps.println((scale * (s.b.x - instance.minx)) + " " + (scale * (s.b.y - instance.miny)) + " l");
				ps.println("</path>");
			}
			
			ps.println("</page>");
			ps.println("</ipe>");
			
			ps.close();
		} catch (IOException e) {
			System.out.println("Could not write to output file!");
			e.printStackTrace();
		}		
		
	}
	
	
	
	// write solution to file
	public void output(String filename) {
		
		File file = new File(filename);
		try {
			PrintStream ps = new PrintStream(file);
			ps.println(K); // for checking
			// write the coordinates of the points
			for (int i = 0; i < K; i++) {
				ps.println(tree.get(i).x + " " + tree.get(i).y);
			}
			ps.close();
		} catch (IOException e) {
			System.out.println("Could not write to output file!");
			e.printStackTrace();
		}
		
	}

	
}
