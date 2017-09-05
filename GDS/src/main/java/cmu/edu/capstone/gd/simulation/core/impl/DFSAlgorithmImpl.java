package cmu.edu.capstone.gd.simulation.core.impl;

import java.util.Iterator;
import java.util.List;

import cmu.edu.capstone.gd.simulation.core.RoundTraversalController;
import cmu.edu.capstone.gd.simulation.core.RunTerminationController;
import cmu.edu.capstone.gd.simulation.core.SimulationStarterController;
import cmu.edu.capstone.gd.simulation.core.TraversalAlgorithm;
import cmu.edu.capstone.gd.simulation.core.UpdateGraphController;
import cmu.edu.capstone.gd.simulation.exception.SimulationExecutionException;
import cmu.edu.capstone.gd.simulation.objects.Graph;
import cmu.edu.capstone.gd.simulation.objects.GraphEdge;
import cmu.edu.capstone.gd.simulation.objects.GraphNode;

/**
 * Class implementing DFS(Depth-first search) Algorithm for traversal on the
 * graph. Inherits TraversalAlgorithm abstract class. Performs information
 * traversals over nodes of the graph according to simulation parameters
 * included in a SimulationController object "tControl"
 */
public class DFSAlgorithmImpl extends TraversalAlgorithm {

	private int counter = 0;

	/**
	 * A function used by DFS
	 * 
	 * @param graph
	 * @param v
	 *            ID of the source node
	 * @param visited
	 *            boolean array presenting if each node is visited or not
	 */
	void doDFSTraversal(Graph graph, GraphNode v, boolean visited[]) {
		// Mark the current node as visited and print it
		visited[v.getId()] = true;

		if (updateGraphController != null) {
			updateGraphController.updateGraphForRound(graph);
		}

		/**
		 * If we want we can update the node here
		 */
		// roundTraversalController.updateNode(graph, v, null);

		GraphNode next;

		// Recur for all the vertices adjacent to this vertex
		Iterator<GraphEdge> i = v.getNeighbors().iterator();

		while (i.hasNext()) {
			GraphEdge edge = i.next();
			next = graph.getNodeFromGraph(edge.getToNode());

			if (!visited[next.getId()] && roundTraversalController.doesTraversalHappen(v, next, edge))
				if (runTerminationController == null
						|| !runTerminationController.doesRunTerminateOnSpecificCondition(graph)) {
					/**
					 * If we want we can update node and edge here
					 */
					// roundTraversalController.updateEdge(graph, edge, null);
					counter++;
					doDFSTraversal(graph, next, visited);
				}

		}
	}

	public void doGraphTraversal(Graph graph, List<Integer> startNodes) throws SimulationExecutionException {
		// Mark all the vertices as not visited(set as
		// false by default in java)

		counter = 0;

		boolean visited[] = new boolean[graph.getNoOfNodes()];
		if (startNodes == null || startNodes.size() <= 0)
			startNodes = simulationStarterController.getStartNodes(graph);

		// Call the recursive helper function to print DFS traversal
		// starting from all vertices one by one
		for (int i = 0; i < startNodes.size(); i++)
			if (visited[startNodes.get(i)] == false) {
				GraphNode startNode = graph.getNodeFromGraph(startNodes.get(i));
				doDFSTraversal(graph, startNode, visited);
			}

		System.out.println("Number of rounds in a run: " + counter);
	}

	@Override
	public void setControllers(SimulationStarterController simulationStarterController,
			RoundTraversalController roundTraversalController, RunTerminationController runTerminationController,
			UpdateGraphController updateGraphController) {
		this.setSimulationStarterController(simulationStarterController);
		this.setRoundTraversalController(roundTraversalController);
		this.setRunTerminationController(runTerminationController);
		this.setUpdateGraphController(updateGraphController);

	}

}
