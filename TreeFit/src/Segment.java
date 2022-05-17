
public class Segment {
	Pos a, b;
	double lsq;
	
	// constructor
	public Segment(Pos a, Pos b) {
		this.a = a; this.b = b;
		lsq = Pos.distsqr(a, b);
	}
	
	// compute distance from p to segment
	public double getDistTo(Pos p) {
		return Math.sqrt(getSqDistTo(p));
	}
	
	// compute squared distance from p to segment
	public double getSqDistTo(Pos p) {
		double dotp = (p.x - a.x) * (b.x - a.x) + (p.y - a.y) * (b.y - a.y);
		double u = dotp / lsq;
		if (u >= 0.0 && u <= 1.0) {
			Pos p2 = new Pos(a.x + u * (b.x - a.x), a.y + u * (b.y - a.y));
			return Pos.distsqr(p, p2);
		}
		else return Math.min(Pos.distsqr(p, a), Pos.distsqr(p, b));
	}
}

