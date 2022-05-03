import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class NQueenInstance {

	final int N; // the size of the chessboard
	
	public NQueenInstance(String filename) {
		// initialize local variable to get around try-catch/final issue
		int n = 0;
		
		File file = new File(filename);
		try {
			Scanner scan = new Scanner(file);
			n = scan.nextInt();
			scan.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not read file!");
			e.printStackTrace();
		}
		
		// set final variables
		N = n;
	}
}
