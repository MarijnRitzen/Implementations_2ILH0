
public class Pos {
	double x, y;
	
	public Pos(double x, double y) {
		this.x = x; this.y = y;
	}
	
	public Pos(Pos p) {
		x = p.x; y = p.y;
	}
	
	public static double distsqr(Pos p, Pos q) {
		return (p.x - q.x) * (p.x - q.x) + (p.y - q.y) * (p.y - q.y);
	}	
	
	public static double distance(Pos p, Pos q) {
		return Math.sqrt((p.x - q.x) * (p.x - q.x) + (p.y - q.y) * (p.y - q.y));
	}
}
