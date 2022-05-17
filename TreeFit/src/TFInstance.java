import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class TFInstance {

	final List<Pos> points; // points in the dataset
	final int N; // the number of points in the dataset
	final int K; // the number of points that can be used in the tree
	final double minx; // minimum x-coordinate
	final double miny; // minimum y-coordinate
	final double maxx; // maximum x-coordinate
	final double maxy; // maximum y-coordinate
	
	public TFInstance(String filename) {
		// initialize local variables to get around try-catch/final issue
		int n = 0;
		int k = 0;
		ArrayList<Pos> pts = new ArrayList<Pos>();
		
		File file = new File(filename);
		try {
			Scanner scan = new Scanner(file);
			pts = new ArrayList<Pos>();
			
			k = scan.nextInt(); // number of points in tree
			n = scan.nextInt(); // number of points in dataset
			for (int i = 0; i < n; i++) {
				double x, y;
				x = scan.nextDouble();
				y = scan.nextDouble();
				pts.add(new Pos(x, y));
			}
			
			scan.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not read file!");
			e.printStackTrace();
		}
		
		// set final variables
		N = n;
		K = k;
		points = Collections.unmodifiableList(pts);
		
		// compute bounding box for coordinates
		double minx_temp = Double.MAX_VALUE;
		double miny_temp = Double.MAX_VALUE;
		double maxx_temp = Double.MIN_VALUE;
		double maxy_temp = Double.MIN_VALUE;
		
		for (int i = 0; i < N; i++) {
			minx_temp = Math.min(minx_temp, points.get(i).x);
			miny_temp = Math.min(miny_temp, points.get(i).y);
			maxx_temp = Math.max(maxx_temp, points.get(i).x);
			maxy_temp = Math.max(maxy_temp, points.get(i).y);
		}
		
		minx = minx_temp;
		miny = miny_temp;
		maxx = maxx_temp;
		maxy = maxy_temp;
		
	}
	
}
