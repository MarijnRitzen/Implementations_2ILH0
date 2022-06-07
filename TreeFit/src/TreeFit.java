import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TreeFit {

	public static void main(String[] args) {
		String dataset = "3Split"; // choose the dataset
		TFInstance inst = new TFInstance("data/" + dataset + ".txt"); // load the problem instance
		//TFSolution sol = new TFSolution(inst, true); // initialize a (random) solution
		//TFSolution sol = particleSwarm(inst, 20,5000, 0.8, 1.3, 0.75); // perform particle swarm optimization
		//TFSolution sol = evolStrategy(inst, 10, 20, 10000, 0.15, 1); // perform evolutionary strategy
		TFSolution sol = evolStrategyHybrid(inst,10, 20, 10000, 0.001, 3.5); // perform hybrid evolutionary strategy
		System.out.println("Cost = " + sol.cost); // output the cost
		sol.output("output/" + dataset + ".out"); // output the solution
		sol.visualize("figures/" + dataset + ".ipe"); // visualize the solution


	}
	
	
	
	// Particle Swarm optimization algorithm (does not require changes, but changing is allowed)
	// See the class ParticleSwarmOpt for the parameters
	public static TFSolution particleSwarm(TFInstance inst, int nParticles, int nIter, double cCog, double cSoc, double cIn) {
		
		ParticleSwarmOpt pso = new ParticleSwarmOpt(cCog, cSoc, cIn);
		for (int i = 0; i < nParticles; i++) {
			TFSolution sol = new TFSolution(inst, true);
			if (i > 0) sol.align(pso.getBestSolution());
			pso.addParticle(new Particle(sol));
		}
		
		for (int t = 0; t < nIter; t++) {
			pso.updatePSO();
		}
		
		return pso.getBestSolution();
	}
	
	
	
	// perform a (mu,lambda)-ES (Evolutionary Strategy) with [nIter] number of iterations/generations
	// [minStDev] is the minimum standard deviation of any solution and [initStDev] is the inital standard deviation
	// You should restrict yourself to only using mutation, no crossover
	public static TFSolution evolStrategy(TFInstance inst, int mu, int lambda, int nIter, double minStDev, double initStDev) {

		double tau = 1;

		TFSolution best = new TFSolution(inst, true);
		double bestCost = best.getCost();
		best.setStDev(initStDev); // this is how you initialize the standard deviation

		// Used for selection
		Random rand = new Random();

		// Creating starting population
		ArrayList<TFSolution> population = new ArrayList();

		for (int individual = 0; individual < lambda; individual++) {
			TFSolution indv = new TFSolution(inst, true);
			indv.getCost();
			indv.setStDev(initStDev);
			population.add(indv);

		}

		// Loop over nIter generations
		for (int generation = 0; generation < nIter; generation++) {

			// Sort population by fitness
			Collections.sort(population);

			// Get best mu from lambda parents
			TFSolution[] afterSelection = new TFSolution[mu];
			for (int i = 0; i < mu; i++) {
				afterSelection[i] = population.get(i);
			}
			population.clear();

			// Mutation and creation of offspring
			for (int nextGenInd = 0; nextGenInd < lambda; nextGenInd++) {
				// mutate variance
				TFSolution parent = afterSelection[nextGenInd % mu];

				// Set new variance
				parent.setStDev(
						Math.max(parent.getStDev() * Math.exp(rand.nextGaussian() * tau), minStDev)
				);

				// Mutate the parent using its own standard deviation
				parent.mutate(
						parent.getStDev()
				);

				TFSolution sol = parent.copy();

				// Keep track of best found so far
				if (sol.getCost() < bestCost) {
					bestCost = sol.cost;
					best = sol.copy();
				}

				// Make new population
				population.add(sol);
			}
		}
		
		return best;
	}
	
	
	
	// perform a hybrid (mu,lambda)-ES (Evolutionary Strategy) with [nIter] number of iterations/generations
	// [minStDev] is the final standard deviation and [initStDev] is the inital standard deviation
	// You may choose your own cooling scheme
	public static TFSolution evolStrategyHybrid(TFInstance inst, int mu, int lambda, int nIter, double minStDev, double initStDev) {

		// Global standard deviation
		double stDev = initStDev;

		TFSolution best = new TFSolution(inst, true);
		double bestCost = best.getCost();
		best.setStDev(stDev); // this is how you initialize the standard deviation

		// Creating starting population
		ArrayList<TFSolution> population = new ArrayList();

		for (int individual = 0; individual < lambda; individual++) {
			TFSolution indv = new TFSolution(inst, true);
			indv.getCost();
			indv.setStDev(stDev);
			population.add(indv);
		}

		// Loop over nIter generations
		for (int generation = 0; generation < nIter; generation++) {
			// Sort population by fitness
			Collections.sort(population);

			// Get best mu from lambda parents
			TFSolution[] afterSelection = new TFSolution[mu];
			for (int i = 0; i < mu; i++) {
				afterSelection[i] = population.get(i);
			}
			population.clear();

			// Mutation and creation of offspring
			for (int nextGenInd = 0; nextGenInd < lambda; nextGenInd++) {
				// mutate variance
				TFSolution parent = afterSelection[nextGenInd % mu];

				// Mutate the parent using its own standard deviation
				parent.mutate(
						Math.max(stDev, minStDev)
				);

				TFSolution sol = parent.copy();

				// Keep track of best found so far
				if (sol.getCost() < bestCost) {
					bestCost = sol.cost;
					best = sol.copy();
				}

				// Make new population
				population.add(sol);
			}

			stDev *= 0.9993;
		}

		return best;
	}
	
}
