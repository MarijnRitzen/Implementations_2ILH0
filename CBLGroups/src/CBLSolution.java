import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.TreeSet;

public class CBLSolution {

	CBLInstance instance; // the instance
	ArrayList<Group> groups; // the groups in the solution (you may pick a different solution representation)
	int N; // shorthand for number of students
	double cost; // cost of most recent call to getCost
	boolean valid; // validity of most recent check

	// constructor
	public CBLSolution(CBLInstance inst, boolean random) {
		instance = inst;
		this.N = instance.N;
		
		// make groups
		groups = new ArrayList<Group>();
		for (int i = 0; i < instance.G; i++) {
			groups.add(new Group(i));
		}
		
		// assign students to groups
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < N; i++) ids.add(i);
		if (random) Collections.shuffle(ids);
		for (int i = 0; i < N; i++) { // add students to groups by cycling through the groups
			groups.get(i%instance.G).addToGroup(instance.studs.get(ids.get(i)));
		}
		
	}
	
	
	// deep copy a solution
	public CBLSolution copy() {
		CBLSolution sol = new CBLSolution(instance, false);
		for (int i = 0; i < instance.G; i++) {
			sol.groups.get(i).clear();
			for (Student s: groups.get(i).members) sol.groups.get(i).addToGroup(s);
		}
		return sol;
	}

	// random iterative improvement method for CBL groups
	// do iterative improvement for m iterations or no improvement found
	// Local move: swap two students in different groups and save if better.
	public CBLSolution IterativeImprovement() {
		int m = 10000;
		Random rand = new Random();
		int k, l, stuI, stuJ;
		CBLSolution sol_old = copy();
		CBLSolution sol_new = copy();
		for(int i = 0; i < m; i++) {
			while (isEqual(sol_new, sol_old)) {
				//take two random groups
				k = rand.nextInt(groups.size());
				l = rand.nextInt(groups.size());
				//select groups and get random students
				stuI = rand.nextInt(groups.get(k).getGroupSize());
				stuJ = rand.nextInt(groups.get(l).getGroupSize());

				sol_new = Swap(k, l, stuI, stuJ);
				if(sol_new.getCost() < sol_old.getCost()) {
					break;
				} else {
					sol_new = sol_old;
				}
			}

		}
		return sol_new;
	}

	public CBLSolution Swap(int Group1, int Group2, int Student1, int Student2){
		//copy solution from instance
		CBLSolution trial_sol = copy();

		//get student objects to be swapped
		Student studentswap1 = groups.get(Group1).members.get(Student1);
		Student studentswap2 = groups.get(Group2).members.get(Student2);

		//perform swap
		trial_sol.groups.get(Group1).members.remove(Student1);
		trial_sol.groups.get(Group2).members.remove(Student2);
		trial_sol.groups.get(Group2).members.add(studentswap1);
		trial_sol.groups.get(Group1).members.add(studentswap2);

		return trial_sol;
	}

	public boolean isEqual(CBLSolution sol1, CBLSolution sol2) {
		if(sol1.N != sol2.N){
			return false;
		} else {
			for (Group g : sol1.groups) {
				int i = 0;
				for (Group h : sol2.groups) {
					if (g.members.equals(h.members)) {
						break;//breaks the outer for loop
					} else if (i++ == sol2.groups.size()-1) {
						return false;
					}

				}
			}
			return true;
		}
	}
	// check if the group sizes are correct and that every student is in exactly one group (not very efficient)
	// does not check if student IDs are valid!
	public boolean checkValid() {
		valid = true;
		
		// check group sizes
		for (Group g: groups) if (g.getGroupSize() < instance.minGroupSize || g.getGroupSize() > instance.maxGroupSize) valid = false;
		
		// check if all students are in exactly one group
		int total = 0;
		TreeSet<Student> studs = new TreeSet<Student>();
		for (Group g: groups) {
			total += g.getGroupSize();
			studs.addAll(g.members);
		}
		if (total != N || studs.size() != N) valid = false;
		
		return valid;
	}
	
	
	// compute the cost of the solution (change at your own risk!)
	public double getCost() {
		double groupCost = 0.0; // group cost
		double prefCost = 0.0; // preference cost
		int penalties = 0; // number of penalties
		
		for (Group g: groups) {
			groupCost += g.getGradeVar() / g.getNrMajors(); // add the group cost
			for (Student s: g.members) {
				int prefCount = 0;
				for (Student s2: g.members) {
					if (s == s2) continue;
					if (s.ID < s2.ID && instance.repeatMatrix[s.ID][s2.ID]) penalties++; // check for penalties (make sure not to double count)
					if (instance.prefMatrix[s.ID][s2.ID]) prefCount++; // count preferences of s in group
				}
				if (s.prefs.size() > 0) prefCost += 1.0 - (double)prefCount / Math.min(g.getGroupSize() - 1.0, s.prefs.size()); // add preference cost
			}
		}
		
		cost = instance.cPref * (prefCost / N) + instance.cGroup * (groupCost / instance.G) + penalties * instance.penRepeat; // compute complete cost
		
		return cost;
	}
	
	
	
	// -------------------------------- OUTPUT STUFF ----------------------------------------------------
	
	// visualize solution as ipe file
	public void visualize(String filename) {	
		// no implementation :(
	}
	
	
	// simple class for storing pairs
	class StudPair {
		int A, B;
		public StudPair(int a, int b) {
			A = a; B = b;
		}
	}
	
	
	// write statistics to file (no comments here)
	public void saveStats(String filename) {
	
		double groupCost = 0.0;
		double prefCost = 0.0;
		int penalties = 0;
		for (Group g: groups) {
			groupCost += g.getGradeVar() / g.getNrMajors();
			for (Student s: g.members) {
				int prefCount = 0;
				for (Student s2: g.members) {
					if (s == s2) continue;
					if (s.ID < s2.ID && instance.repeatMatrix[s.ID][s2.ID]) penalties++;
					if (instance.prefMatrix[s.ID][s2.ID]) prefCount++;
				}
				if (s.prefs.size() > 0) prefCost += 1.0 - (double)prefCount / Math.min(g.getGroupSize() - 1.0, s.prefs.size());
			}
		}
		
		cost = instance.cPref * (prefCost / N) + instance.cGroup * (groupCost / instance.G) + penalties * instance.penRepeat;
		
		File file = new File(filename);
		try {
			PrintStream ps = new PrintStream(file);
			
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			
			ps.println("Total Group cost: " + (instance.cGroup * (groupCost / instance.G)));
			ps.println("Total Preference cost: " + (instance.cPref * (prefCost / N)));
			ps.println("Total Penalty cost: " + (penalties * instance.penRepeat) + " (" + penalties + " penalties)");
			ps.println("Total cost: " + cost);
			ps.println();
			
			for (Group g: groups) {
				ps.println("------------------------------------------------------------------------");
				ps.println("GROUP " + (g.groupID+1) + ":");
				ps.println("Grades: " + df.format(g.getGradeAverage()) + " (VAR = " + df.format(g.getGradeVar()) + ")");
				ps.println("Majors: " + g.getNrMajors());
				ArrayList<StudPair> prefs = new ArrayList<StudPair>();
				ArrayList<StudPair> pens = new ArrayList<StudPair>();
				
				for (Student s: g.members) {
					for (Student s2: g.members) {
						if (s == s2) continue;
						if (s.ID < s2.ID && instance.repeatMatrix[s.ID][s2.ID]) pens.add(new StudPair(s.ID, s2.ID));
						if (instance.prefMatrix[s.ID][s2.ID]) prefs.add(new StudPair(s.ID, s2.ID));
					}
				}
				
				ps.print("Preferences: " + prefs.size() + " (");
				for (StudPair sp: prefs) ps.print(sp.A + "+" + sp.B + " ");
				ps.println(")");
				ps.print("Penalties: " + pens.size() + " (");
				for (StudPair sp: pens) ps.print(sp.A + "+" + sp.B + " ");
				ps.println(")");				
				
				ps.println();
				ps.println("Members:");
				for (Student s: g.members) {
					String SID = "" + s.ID;
					while (SID.length() < 5) SID = SID.concat(" ");
					ps.println(SID + '\t' + df.format(s.GPA) + '\t' + s.major.toString());
				}
				ps.println();
				ps.println("------------------------------------------------------------------------");
			}
			
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
			for (Group g: groups) {
				ps.print(g.getGroupSize()); // write group size
				for (Student s: g.members) ps.print(" " + s.ID); // write student IDs
				ps.println();
			}
			ps.close();
		} catch (IOException e) {
			System.out.println("Could not write to output file!");
			e.printStackTrace();
		}
		
	}
	
	
}
