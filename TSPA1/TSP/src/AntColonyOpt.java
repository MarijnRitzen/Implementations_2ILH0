
public class AntColonyOpt {
	
	double[][] pher; // current pheromones
	double[][] deltaPher; // change in pheromones for current round
	double alpha, beta, C, rho; // parameters for ant colony optimization (in the book C = Q)
	int nCities; // number of cities
	// you can add other parameters for Ant Colony System yourself
	
	// In this constructor, [initPher] is the initial pheromone level for each pair of cities
	public AntColonyOpt(int nCities, double alpha, double beta, double C, double rho, double initPher) {
		this.nCities = nCities;
		this.alpha = alpha;
		this.beta = beta;
		this.C = C;
		this.rho = rho;
		pher = new double[nCities][nCities];
		deltaPher = new double[nCities][nCities];
		for (int i = 0; i < nCities; i++) for (int j = 0; j < nCities; j++) pher[i][j] = initPher; // initialize pheromone levels
	}
	
	
	// initalize a round (iteration) of the ant colony optimization algorithm
	public void initRound() {
		
		// TODO
		
	}
	
	
	// add pheromone to a specific pair of cities
	public void addPheromone(int i, int j, double amount) {

		// TODO
		
	}
	
	
	// compute the preference for a specific pair of cities
	public double getPreference(int i, int j, double attractiveness) {
		
		// TODO
		
		return 0.0;
	}
	
	
	// conclude a round (iteration) of the ant colony optimization algorithm
	public void concludeRound() {
		
		// TODO
		
	}
	
}
