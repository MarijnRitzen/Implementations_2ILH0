import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class CBLGroups {

	public static void main(String[] args) {
		Locale.setDefault(new Locale("en", "US")); // ensure that decimal separators are '.'
		
		String dataset = "small"; // choose the dataset
		CBLInstance inst = new CBLInstance("data/" + dataset + ".txt"); // load the problem instance
		CBLSolution sol = new CBLSolution(inst, true); // initialize a (random) solution
		System.out.println("random Cost = " + sol.getCost());
		sol = sol.IterativeImprovement();
		if (!sol.checkValid()) System.out.println("Solution not valid!"); // check validity of solution
		System.out.println("Cost = " + sol.getCost()); // output the cost
		sol.output("output/" + dataset + ".out"); // output the solution
		sol.saveStats("stats/" + dataset + ".txt"); // save the solution stats
	}

}
