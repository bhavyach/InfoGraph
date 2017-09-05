package cmu.edu.capstone.gd.simulation.objects;

/**
 * This class takes the result of a simulation as a matrix type.
 * Size of this matrix would be : [numbers of nodes in the graph]*[numbers of nodes in the graph]
 */
public class ResultMatrix {

	int[][] result;
	
	/**
	 * Returns a matrix of simulation result.
	 * @return integer matrix of a simulation result
	 */
	public int[][] getResult() {
		return result;
	}
	
	/**
	 * Set the result of simulation in a matrix type.
	 * @param result
	 */
	public void setResult(int[][] result) {
		this.result = result;
	}

}
