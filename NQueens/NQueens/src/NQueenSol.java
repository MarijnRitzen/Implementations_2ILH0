import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import javax.imageio.ImageIO;

public abstract class NQueenSol {
	
	NQueenInstance instance; // the instance
	int N; // shorthand for size of chessboard
	
	// counts for computing cost function
	int[] countRow; // number of pieces per row
	int[] countColumn; // number of pieces per column
	int[] countLTRBDiag; // number of pieces per left-top to right-bottom diagonals
	int[] countLBRTDiag; // number of pieces per left-bottom to right-top diagonals
	
	// constructor
	public NQueenSol(NQueenInstance inst) {
		instance = inst;
		N = instance.N;
		
		// initialize auxiliary structures for computing cost
		countRow = new int[N];
		countColumn = new int[N];
		countLTRBDiag = new int[2 * N - 1]; // left-top to right-bottom diagonals
		countLBRTDiag = new int[2 * N - 1]; // left-bottom to right-top diagonals
	}
	
	
	
	// get position of a queen (must be implemented in subclass!)
	public abstract Pos getPosition(int i);
	
	
	
	// cost function
	public final int getCost() {
		
		// compute number of queens per row, column, and diagonals
		for (int i = 0; i < N; i++) {
			countRow[i] = 0; countColumn[i] = 0;
		}
		
		for (int i = 0; i < 2 * N - 1; i++) {
			countLTRBDiag[i] = 0; countLBRTDiag[i] = 0;
		}
		
		for (int i = 0; i < N; i++) {
			Pos p = getPosition(i);
			countRow[p.y]++;
			countColumn[p.x]++;
			countLTRBDiag[p.x + p.y]++;
			countLBRTDiag[p.x - p.y + N - 1]++;
		}
		
		// compute number of conflicting pairs (if z on the same row/column/diag, then cost is z (z-1)/2)
		int cost = 0;
		
		// rows/columns
		for (int i = 0; i < N; i++) {
			cost += (countRow[i] * (countRow[i] - 1))/2;
			cost += (countColumn[i] * (countColumn[i] - 1))/2;
		}	
		
		// diagonals
		for (int i = 0; i < 2 * N - 1; i++) {
			cost += (countLTRBDiag[i] * (countLTRBDiag[i] - 1))/2;
			cost += (countLBRTDiag[i] * (countLBRTDiag[i] - 1))/2;
		}
		
		return cost;
	}
	
	
	
	// ---------------------------- OUTPUT STUFF ----------------------------------------------------
	
	// visualize solution as png image
	public final void visualize(String filename) {
		
		// Does not work if N is very large
		if (instance.N > 1000) {
			System.out.println("N is too large!");
			return;
		}
		
		// compute grid
		boolean[][] grid = new boolean[instance.N][instance.N];
		for (int i = 0; i < instance.N; i++) {
			Pos p = getPosition(i);
			grid[p.x][p.y] = true;
		}
		
		if (instance.N <= 100) {
			// Visualize with little queens
			BufferedImage image = new BufferedImage(32 * instance.N, 32 * instance.N, BufferedImage.TYPE_INT_RGB);
			
			// load queen images
			BufferedImage queenDark, queenLight;
			try {
				queenDark = ImageIO.read(new File("figures/queendark.png"));
				queenLight = ImageIO.read(new File("figures/queenlight.png"));
			} catch (IOException e1) {
				System.out.println("Could not load little queen images!");
				e1.printStackTrace();
				return;
			}
			
			// make image
			for (int x = 0; x < instance.N; x++) {
				for (int y = 0; y < instance.N; y++) {
					
					boolean dark = ((x+y)%2 == 0);
					Color darkCol = new Color(125, 148, 93);
					Color lightCol = new Color(238, 238, 210);
					
					for (int i = 0; i < 32; i++) {
						for (int j = 0; j < 32; j++) {
							if (grid[x][y]) {
								image.setRGB(32 * x + i, 32 * y + j, (dark ? queenDark.getRGB(i, j) : queenLight.getRGB(i, j)));
							}
							else {
								image.setRGB(32 * x + i, 32 * y + j, (dark ? darkCol.getRGB() : lightCol.getRGB()));
							}
						}
					}
				}
			}			
			
			File file = new File(filename);
			try {
				ImageIO.write(image, "png", file);
			} catch (IOException e) {
				System.out.println("Could not output image!");
				e.printStackTrace();
			}
		}
		else {
			// visualize as red squares
			BufferedImage image = new BufferedImage(3 * instance.N, 3 * instance.N, BufferedImage.TYPE_INT_RGB);
			for (int x = 0; x < instance.N; x++) {
				for (int y = 0; y < instance.N; y++) {
					
					// set color based on grid position
					Color color;
					if (grid[x][y]) {
						color = new Color(128, 0, 0);
					}
					else {
						if ((x+y)%2 == 0) color = new Color(125, 148, 93);
						else color = new Color(238, 238, 210);
					}
					
					for (int i = 0; i < 3; i++) {
						for (int j = 0; j < 3; j++) {
							image.setRGB(3 * x + i, 3 * y + j, color.getRGB());
						}
					}
				}
			}
			
			File file = new File(filename);
			try {
				ImageIO.write(image, "png", file);
			} catch (IOException e) {
				System.out.println("Could not output image!");
				e.printStackTrace();
			}
		}
		
		
	}
	
	
	// write solution to file
	public final void output(String filename) {
		
		File file = new File(filename);
		try {
			PrintStream ps = new PrintStream(file);
			
			ps.println(N); // output the number of chess pieces
			// output the pieces
			for (int i = 0; i < instance.N; i++) {
				Pos p = getPosition(i);
				ps.println(p.x + " " + p.y);
			}			
			
			ps.close();
		} catch (IOException e) {
			System.out.println("Could not write to output file!");
			e.printStackTrace();
		}

		
	}
	
}
