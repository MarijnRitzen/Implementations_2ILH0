import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class CBLInstance {

	final List<Student> studs; // the students
	final int N; // the number of students
	final int G; // the number of groups
	final List<Group> prevGroups; // the groups in the previous project (can be empty)
	final double cGroup, cPref, penRepeat; // coefficients and penalties for cost function
	final int minGroupSize, maxGroupSize; // minimum and maximum group size
	boolean[][] prefMatrix; // prefMatrix[i][j] indicates whether student i prefers student j
	boolean[][] repeatMatrix; // repeatMatrix[i][j] indicates whether student i and student j were already together in a group
	
	public CBLInstance(String filename) {
		// initialize local variables to get around try-catch/final issue
		int n = 0, g = 0;
		double cG = 0.0, cP = 0.0, pR = 0.0;
		ArrayList<Student> students = new ArrayList<Student>();
		ArrayList<Group> grps = new ArrayList<Group>();
		
		File file = new File(filename);
		try {
			Scanner scan = new Scanner(file);
			
			n = scan.nextInt(); // number of students
			g = scan.nextInt(); // number of groups
			cG = scan.nextDouble(); // group coefficient
			cP = scan.nextDouble(); // preference coefficient
			pR = scan.nextDouble(); // repeat penalty
			
			for (int i = 0; i < n; i++) { // for all students
				double grade = scan.nextDouble(); // Student GPA
				Major major = Major.valueOf(scan.next()); // Student major
				Student s = new Student(i, grade, major);
				int nPref = scan.nextInt(); // number of preferences
				for (int j = 0; j < nPref; j++) s.addPref(scan.nextInt()); // list of preferences
				students.add(s); // add to student list
			}
			
			int nPrevGroups = scan.nextInt(); // number of groups in previous project
			for (int i = 0; i < nPrevGroups; i++) { // for all previous groups
				Group grp = new Group(i);
				int nStudents = scan.nextInt(); // number of students in group
				for (int j = 0; j < nStudents; j++) {
					grp.addToGroup(students.get(scan.nextInt())); // list of group members
				}
				grps.add(grp); // add to group list
			}
			
			scan.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not read file!");
			e.printStackTrace();
		}
		
		// set final variables
		N = n;
		G = g;
		minGroupSize = N/G; // minimum group size (for convenience)
		maxGroupSize = (N+G-1)/G; // maximum group size (for convenience)
		cGroup = cG;
		cPref = cP;
		penRepeat = pR;
		studs = Collections.unmodifiableList(students);
		prevGroups = Collections.unmodifiableList(grps);
		
		// make preference matrix and repeat matrix
		prefMatrix = new boolean[n][n];
		for (Student s: studs) {
			for (int k: s.prefs) prefMatrix[s.ID][k] = true;
		}
		repeatMatrix = new boolean[n][n];
		for (Group grp: prevGroups) {
			for (Student s: grp.members) {
				for (Student s2: grp.members) {
					if (s == s2) continue;
					repeatMatrix[s.ID][s2.ID] = true;
				}
			}
		}
		
	}
	
}
