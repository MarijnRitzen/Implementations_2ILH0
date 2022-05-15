import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static java.util.Collections.emptyList;

public class TSPSolution {

	TSPInstance instance; // instance
	ArrayList<Integer> perm; // solution representation (perm[i] is i-th point in tour)
	int N; // a shorthand for the number of points

	// constructor
	public TSPSolution(TSPInstance inst, boolean random) {
		instance = inst;
		N = instance.N;

		// initialize with the standard order
		perm = new ArrayList<Integer>();
		for (int i = 0; i < N; i++) {
			perm.add(i);
		}

		if (random) {
			// randomize order
			Random rand = new Random();
			for (int i = 0; i < N - 1; i++) {
				int k = rand.nextInt(N - i) + i;
				Collections.swap(perm, i, k);
			}
		}

	}


	// copy the solution (if you want to keep track of the best solution found)
	public TSPSolution copy() {
		TSPSolution sol = new TSPSolution(instance, false);
		for (int i = 0; i < N; i++) {
			sol.perm.set(i, perm.get(i));
		}
		return sol;
	}

	/*
	 * Calculates total cost for trial solution for greedy algorithm format
	 */
	public double getCostGreedy(ArrayList<Integer> trialSol) {
		double cost = 0;
		for (int i = 0; i < trialSol.size(); i++) {
			if (i + 1 >= trialSol.size()) {
				cost += Pos.distance(instance.points.get(perm.get(i)), instance.points.get(perm.get(0)));
			} else {
				cost += Pos.distance(instance.points.get(perm.get(i)), instance.points.get(perm.get(i + 1)));
			}
		}
		return cost;
	}

	public ArrayList<Integer> copyList(ArrayList<Integer> list1, ArrayList<Integer> list2) {
		list1.clear();
		list1.addAll(list2);
		return list1;
	}

	// greedy algorithm that starts forming a tour with two random points and repeatedly inserts a random point in the best possible place in the tour
	public ArrayList<Integer> computeGreedy() {

		// TODO
		ArrayList<Integer> solution = new ArrayList<>();

		Random rand = new Random();
		int k, l;
		k = rand.nextInt(N);
		l = rand.nextInt(N);
		while (k == l) {
			l = rand.nextInt(N);
		}

		solution.add(k);
		solution.add(l);

		ArrayList<Integer> previousSol = new ArrayList<>();
		previousSol = copyList(previousSol, solution);
		ArrayList<Integer> trialSol = new ArrayList<>();
		ArrayList<Integer> bestSol = new ArrayList<>();
		double bestTotalCost;
		double newTotalCost;


		while (bestSol.size() < N) { //while solution contains less elements than there are points
			bestTotalCost = 100000000;


			for (int i = 0; i < N; i++) {//loop over elements
				if (!previousSol.contains(i)){ //solution does not already contain element
					for (int j = 0; j < previousSol.size(); j++) { //loop over possible insertions
						trialSol = copyList(trialSol, previousSol);

						//try new position
						trialSol.add(j, i);
						newTotalCost = getCostGreedy(trialSol);
						if (newTotalCost < bestTotalCost) {
							bestTotalCost = newTotalCost;
							bestSol = copyList(bestSol, trialSol);
						}
					}
				}
			}
			previousSol = copyList(previousSol, bestSol);
		}
		solution = bestSol;
		return solution;

}
	
	
	
	// performs 2-opt move on i-th and j-th edge in tour
	public void apply2OPT(int i, int j) {
		
		// TODO
		ArrayList<Integer> newSol = new ArrayList<>();
		//take all edges until i and add them to new solution
		for (int a = 0; a < i-1; a++) {
			newSol.add(a, perm.get(a));
		}
		newSol.add(j); // add first node of second edge
		//add edges from i to j in reverse order
		int dec = 0;
		for (int b = i; b <= j ; b++) {
			newSol.add(b, perm.get(j - dec));
			dec++;
		}

		//take remaining nodes between j and i
		for (int c = j+1; c < N; c++) {
			newSol.add(c, perm.get(c));
		}

		perm = newSol;
	}
	
	public void undo2OPT(int i, int j) {
		apply2OPT(i, j);
	}	
	
	
	
	// swaps the current i-th and j-th point in the tour
	public void applySwap(int i, int j) {
		Collections.swap(perm, i, j);
	}
	
	public void undoSwap(int i, int j) {
		applySwap(i, j);
	}
	
	
	
	// get the ID of the i^th point in the tour
	public int getPoint(int i) {
		return perm.get(i);
	}
	
	
	
	// compute the cost of the solution
	public double getCost() {
		// simply add distances of edges
		double cost = 0.0;
		for (int i = 0; i < N; i++) {
			int j = (i+1)%N;
			cost += Pos.distance(instance.points.get(perm.get(i)), instance.points.get(perm.get(j)));
		}
		return cost;
	}
	
	
	
	// -------------------------------- OUTPUT STUFF ----------------------------------------------------
	
	
	
	// visualize solution as ipe file
	public void visualize(String filename) {
		
		// first determine bounding box for coordinates
		double minx = Double.MAX_VALUE;
		double miny = Double.MAX_VALUE;
		double maxx = Double.MIN_VALUE;
		double maxy = Double.MIN_VALUE;
		
		for (int i = 0; i < N; i++) {
			minx = Math.min(minx, instance.points.get(i).x);
			miny = Math.min(miny, instance.points.get(i).y);
			maxx = Math.max(maxx, instance.points.get(i).x);
			maxy = Math.max(maxy, instance.points.get(i).y);
		}
		
		double scaleX = 512.0 / Math.max(maxx-minx, 1.0);
		double scaleY = 512.0 / Math.max(maxy-miny, 1.0);
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
			
			// first the points
			for (int i = 0; i < N; i++) {
				ps.println("<use name=\"mark/disk(sx)\" pos=\"" + (scale * (instance.points.get(i).x - minx)) + " " + (scale * (instance.points.get(i).y - miny)) + " \" size=\"normal\" stroke=\"black\"/>");
			}
			
			// then the path
			ps.println("<path stroke=\"black\">");
			for (int i = 0; i < N; i++) {
				ps.println((scale * (instance.points.get(perm.get(i)).x - minx)) + " " + (scale * (instance.points.get(perm.get(i)).y - miny)) + " " + (i == 0 ? "m" : "l"));
			}
			ps.println("h");
			ps.println("</path>");
			
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
			ps.println(N); // for checking
			// write the order of the points (using original IDs, so +1!)
			for (int i = 0; i < N; i++) {
				ps.println(perm.get(i)+1);
			}
			ps.close();
		} catch (IOException e) {
			System.out.println("Could not write to output file!");
			e.printStackTrace();
		}
		
	}

	
}
