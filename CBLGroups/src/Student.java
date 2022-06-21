import java.util.ArrayList;

public class Student implements Comparable<Student> {
	
	int ID; // student ID
	double GPA; // student GPA
	Major major; // student major
	ArrayList<Integer> prefs; // list of preferences
	
	// constructor
	public Student(int ID, double GPA, Major major) {
		this.ID = ID;
		this.GPA = GPA;
		this.major = major;
		prefs = new ArrayList<Integer>();
	}

	// check if student is in preference list (better to use preference matrix of instance)
	public boolean containsPref(int ID) {
		return prefs.contains(ID);
	}
	
	// add student preference to list
	public void addPref(int ID) {
		prefs.add(ID);
	}
	
	// override of equals function using only ID (this way you can sort and use sets of students)
	public boolean equals(Object o) {
		if (!(o instanceof Student)) return false;
		return (ID == ((Student)o).ID);
	}

	// compare function using only ID (this way you can sort and use sets of students)
	public int compareTo(Student o) {
		return Integer.compare(ID, o.ID);
	}
	
}
