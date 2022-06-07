import java.util.ArrayList;

public class ParticleSwarmOpt {
	
	double cCog; // cognitive coefficient
	double cSoc; // social coefficient
	double cIn; // inertia constant
	ArrayList<Particle> parts; // set of particles
	TFSolution globalBest; // the global best solution
	double globalBestCost; // the cost of the global best solution
	
	// constructor
	public ParticleSwarmOpt(double cCog, double cSoc, double cIn) {
		this.cCog = cCog;
		this.cSoc = cSoc; 
		this.cIn = cIn;
		parts = new ArrayList<Particle>();
		globalBest = null;
		globalBestCost = Double.MAX_VALUE;
	}
	
	
	// add particle
	public void addParticle(Particle p) {
		p.pso = this;
		parts.add(p);
		if (globalBest == null || p.sol.getCost() < globalBestCost) {
			globalBest = p.sol;
			globalBestCost = p.sol.cost;
		}
	}
	
	
	// get current best solution
	public TFSolution getBestSolution() {
		return globalBest;
	}
	
	
	// perform one iteration of the Particle Swarm Optimization 
	// You should also update globalBest if necessary (make sure to make a copy, not just copy the pointer!)
	public void updatePSO() {

		for (Particle particle : parts) {
			particle.update();
			if (particle.sol.getCost() < globalBestCost) {
				globalBest = particle.sol.copy();
				globalBestCost = particle.sol.cost;
			}
		}

	}
	
}
