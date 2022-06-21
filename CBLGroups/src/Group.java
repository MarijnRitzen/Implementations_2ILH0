import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

public class Group {
	
	int groupID; // group ID
	ArrayList<Student> members; // list of members
	
	// constructor
	public Group(int groupID) {
		this.groupID = groupID;
		members = new ArrayList<Student>();
	}
	
	// add student to group
	public void addToGroup(Student student) {
		members.add(student);
	}
	
	// remove student from group
	public void removeFromGroup(Student student) {
		members.remove(student);
	}
	
	// clear the list of group members
	public void clear() {
		members.clear();
	}
	
	// get a random student from the group (convenient function)
	public Student getRandomMember(Random rand) {
		int k = rand.nextInt(members.size());
		return members.get(k);
	}
	
	// get the size of the group
	public int getGroupSize() {
		return members.size();
	}
	
	// compute the average GPA of the group
	public double getGradeAverage() {
		if (getGroupSize() == 0) return 0.0;
		double sum = 0.0;
		for (Student s: members) sum += s.GPA;
		return (sum / getGroupSize());
	}
	
	// compute the variance of the GPAs in the group (0 if group is empty)
	public double getGradeVar() {
		if (getGroupSize() == 0) return 0.0;
		double avg = getGradeAverage();
		double sum = 0.0;
		for (Student s: members) sum += (s.GPA - avg) * (s.GPA - avg);
		return (sum / getGroupSize());
	}
	
	// compute the number of distinct majors in the group
	public int getNrMajors() {
		TreeSet<Major> majors = new TreeSet<Major>();
		for (Student s: members) majors.add(s.major);
		return majors.size();
	}
	
	// check if a particular student is in this group (not very efficient)
	public boolean containsStudentID(int ID) {
		for (Student s: members) if (s.ID == ID) return true;
		return false;
	}
	
}
