package cmu.edu.capstone.gd.simulation.utilities;

/**
 * 
 * This class for displaying result of simulation which is stored as a matrix type.
 */
public class DisplayUtility {

	public static void printMatrix(int[][] matrix) {
		int rows = matrix.length;
		int cols = matrix[0].length;

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				System.out.print(matrix[i][j] + " ");
			}

			System.out.println();
		}
	}
}
