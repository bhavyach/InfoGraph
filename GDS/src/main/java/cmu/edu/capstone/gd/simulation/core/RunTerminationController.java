package cmu.edu.capstone.gd.simulation.core;

import cmu.edu.capstone.gd.simulation.objects.Graph;

/**
 * (Optional Interface)
 * 
 * If not implemented, one simulation run happens till the complete graph
 * traversal happens.
 * 
 * This interface handles the termination check for each run.
 * 
 * Default Implementation:
 * 
 * @see cmu.edu.capstone.gd.simulation.core.impl.
 *      DefaultRunTerminationControllerImpl
 * 
 * @author Saksham Gangwar
 * @version 1.0.0
 *
 */
public interface RunTerminationController {
	/**
	 * This method handles when a run of simulation should terminate e.g. When
	 * the coverage reaches 50% of the graph nodes, run terminates
	 * 
	 * TODO: add a double coverage in Graph object and update it each time a
	 * round of traversal happens
	 * 
	 * Have a look at default implementation:
	 * 
	 * @see cmu.edu.capstone.gd.simulation.core.impl.DefaultRunTerminationControllerImpl#doesRunTerminateOnSpecificCondition(Graph)
	 * 
	 * @return boolean: true if the termination should happen and false
	 *         otherwise.
	 */
	public boolean doesRunTerminateOnSpecificCondition(Graph graph);
}
