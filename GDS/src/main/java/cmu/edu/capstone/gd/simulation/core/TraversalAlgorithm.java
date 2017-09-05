package cmu.edu.capstone.gd.simulation.core;

import java.util.List;

import cmu.edu.capstone.gd.simulation.exception.SimulationExecutionException;
import cmu.edu.capstone.gd.simulation.objects.Graph;

/**
 * Traversal Algorithm abstract class is responsible for implementation of
 * traversal logic for traversing the graph. This is completely different from
 * RoundTravesalController method. TraversalAlgorithm's doGraphTraversal method
 * implements any desired graph traversal algorithm (default which is currently
 * present is DFS:
 * 
 * @see cmu.edu.capstone.gd.simulation.core.impl.DFSAlgorithmImpl#doGraphTraversal(Graph)
 *      )
 * 
 *      This class is responsible for handling one complete run.
 * 
 * @author Saksham Gangwar
 * @version 1.0.0
 * 
 */
public abstract class TraversalAlgorithm {

	public SimulationStarterController simulationStarterController;
	public RoundTraversalController roundTraversalController;
	public RunTerminationController runTerminationController;
	public UpdateGraphController updateGraphController;

	public SimulationStarterController getSimulationStarterController() {
		return simulationStarterController;
	}

	public void setSimulationStarterController(SimulationStarterController simulationStarterController) {
		this.simulationStarterController = simulationStarterController;
	}

	public RoundTraversalController getRoundTraversalController() {
		return roundTraversalController;
	}

	public void setRoundTraversalController(RoundTraversalController roundTraversalController) {
		this.roundTraversalController = roundTraversalController;
	}

	public RunTerminationController getRunTerminationController() {
		return runTerminationController;
	}

	public void setRunTerminationController(RunTerminationController runTerminationController) {
		this.runTerminationController = runTerminationController;
	}

	public UpdateGraphController getUpdateGraphController() {
		return updateGraphController;
	}

	public void setUpdateGraphController(UpdateGraphController updateGraphController) {
		this.updateGraphController = updateGraphController;
	}

	public abstract void setControllers(SimulationStarterController simulationStarterController,
			RoundTraversalController roundTraversalController, RunTerminationController runTerminationController,
			UpdateGraphController updateGraphController);

	/**
	 * This method handles graph traversal for the input graph which is being
	 * provided by the user. Default implementation of this method can be found
	 * at:
	 * 
	 * @see cmu.edu.capstone.gd.simulation.core.impl.DFSAlgorithmImpl#doGraphTraversal(Graph)
	 * 
	 *      User can make use of any graph traversal algorithm of his choice but
	 *      he has to make sure that he makes call to RoundTraversalController
	 *      and other required controllers in the traversal algorithm correctly.
	 * 
	 * @param graph
	 * @throws SimulationExecutionException
	 */
	public abstract void doGraphTraversal(Graph graph, List<Integer> startNodes) throws SimulationExecutionException;
}
