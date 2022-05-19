import java.util.ArrayList;
import java.util.Random;

public class Particle {

	ParticleSwarmOpt pso; // pointer to optimization parameters
	TFSolution sol; // the corresponding solution of the particle
	ArrayList<Pos> velocity; // velocities of each of the points
	ArrayList<Pos> best; // best solution found by particle
	Random rand;
	double bestCost; // cost of the best solution found by this particle
	
	// constructor
	public Particle(TFSolution sol) {
		this.sol = sol;
		velocity = new ArrayList<Pos>();
		best = new ArrayList<Pos>();
		for (int i = 0; i < sol.K; i++) {
			best.add(sol.getPoint(i));
			velocity.add(new Pos(0.0, 0.0));
		}
		bestCost = sol.getCost();
		this.rand = new Random();
	}
	
	
	// return the current solution
	public TFSolution getSolution() {
		return sol;
	}
	
	
	// return the cost of the best solution found
	public double getBestCost() {
		return bestCost;
	}
	
	
	// clip points to bounding box
	// The velocity becomes 0 if a point hits the boundary of the box, but you may choose a different effect
	public void clipPoint(int i) {
		Pos v = velocity.get(i);
		Pos p = sol.getPoint(i);
		if (p.x < sol.instance.minx) {
			p.x = sol.instance.minx; v.x = 0;
		}
		else if (p.x > sol.instance.maxx) {
			p.x = sol.instance.maxx; v.x = 0;
		}
		if (p.y < sol.instance.miny) {
			p.y = sol.instance.miny; v.y = 0;
		}
		else if (p.y > sol.instance.maxy) {
			p.y = sol.instance.maxy; v.y = 0;
		}	
	}
	
	
	// perform one iteration for this particle and update its personal best solution
	public void update() {
		
		for (int i = 0; i < sol.K; i++) { // For every point in solution
			// For every dimension of one of the points, update the velocity
			velocity.get(i).x = pso.cIn * velocity.get(i).x + pso.cCog * rand.nextDouble() * (best.get(i).x - sol.getPoint(i).x)
					+ pso.cSoc * rand.nextDouble() * (pso.getBestSolution().getPoint(i).x - sol.getPoint(i).x);
			velocity.get(i).y = pso.cIn * velocity.get(i).y + pso.cCog * rand.nextDouble() * (best.get(i).y - sol.getPoint(i).y)
					+ pso.cSoc * rand.nextDouble() * (pso.getBestSolution().getPoint(i).y - sol.getPoint(i).y);

			// Next update the positions
			double x = sol.getPoint(i).x + velocity.get(i).x;
			double y = sol.getPoint(i).y + velocity.get(i).y;
			sol.setPoint(i, new Pos(x, y));
		}

		if (sol.getCost() < bestCost) {
			bestCost = sol.cost;
			for (int i = 0; i < sol.K; i++) {
				best.set(i, sol.getPoint(i));
			}
		}
		
	}
	
	
}
