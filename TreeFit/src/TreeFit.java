

public class TreeFit {

	public static void main(String[] args) {
		String dataset = "3split"; // choose the dataset
		TFInstance inst = new TFInstance("data/" + dataset + ".txt"); // load the problem instance
		TFSolution sol = new TFSolution(inst, true); // initialize a (random) solution
		//TFSolution sol = particleSwarm(inst, ..., ..., ..., ..., ...); // perform particle swarm optimization
		//TFSolution sol = evolStrategy(inst, ..., ..., ..., ..., ...); // perform evolutionary strategy
		//TFSolution sol = evolStrategyHybrid(inst, ..., ..., ..., ..., ...); // perform hybrid evolutionary strategy
		System.out.println("Cost = " + sol.getCost()); // output the cost
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
		
		TFSolution best = new TFSolution(inst, true);
		best.setStDev(initStDev); // this is how you initialize the standard deviation
		
		// TODO
		
		return best;
	}
	
	
	
	// perform a hybrid (mu,lambda)-ES (Evolutionary Strategy) with [nIter] number of iterations/generations
	// [minStDev] is the final standard deviation and [initStDev] is the inital standard deviation
	// You may choose your own cooling scheme
	public static TFSolution evolStrategyHybrid(TFInstance inst, int mu, int lambda, int nIter, double minStDev, double initStDev) {
		
		TFSolution best = new TFSolution(inst, true);
		best.setStDev(initStDev); // this is how you initialize the standard deviation
		
		// TODO
		
		return best;
	}
	
}
