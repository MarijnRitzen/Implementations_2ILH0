import java.util.Random;

public class Pizza {

	public static void main(String[] args) {
		String dataset = "small"; // choose the dataset
		PizzaInstance inst = new PizzaInstance("data/" + dataset + ".txt"); // load the problem instance
		PizzaSolution sol = new PizzaSolution(inst, true); // initialize a (random) solution
		//sol.computeILPIngr();
		//sol.computeILPPref();
		System.out.println("Cost = " + sol.getCost()); // output the cost
		sol.output("output/" + dataset + ".out"); // output the solution
		sol.visualize("figures/" + dataset + ".png", true); // visualize the solution
	}
	
}
