import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class TSPInstance {

	final List<Pos> points; // the positions of the points 
	final int N; // the number of points
	
	public TSPInstance(String filename) {
		// initialize local variable to get around try-catch/final issue
		int n = 0;
		ArrayList<Pos> pts = new ArrayList<Pos>();
		
		File file = new File(filename);
		try {
			Scanner scan = new Scanner(file);
			
			n = scan.nextInt(); // number of points
			for (int i = 0; i < n; i++) {
				int id;
				double x, y;
				id = scan.nextInt(); // ID will simply be based on order, so ignored
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
		points = Collections.unmodifiableList(pts);
	}
}
