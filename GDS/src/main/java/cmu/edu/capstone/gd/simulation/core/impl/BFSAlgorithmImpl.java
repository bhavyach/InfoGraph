package cmu.edu.capstone.gd.simulation.core.impl;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
 * Class implementing BFS(Breadth-first search) Algorithm for traversal on the
 * graph. Inherits TraversalAlgorithm abstract class. Performs information
 * traversals over nodes of the graph according to simulation parameters
 * included in a SimulationController object "tControl"
 */
public class BFSAlgorithmImpl extends TraversalAlgorithm {



	private int counter = 0;

	
	

	/**
	 * A function used by BFS
	 * 
	 * @param graph
	 * @param v
	 *            source node
	 * @param visited
	 *            boolean array presenting if each node is visited or not
	 */
	void doBFSTraversal(Graph graph, GraphNode v , boolean visited[]) {
		// Mark the current node as visited and print it
		visited[v.getId()] = true;
		if (updateGraphController != null) {
			updateGraphController.updateGraphForRound(graph);
		}

		// roundTraversalController.updateNode(graph, v);
		GraphNode next;

		Queue<GraphNode> queue = new LinkedList<GraphNode>();
		queue.add((v));
		while (!queue.isEmpty()) {
			// removes from front of queue
			GraphNode r = queue.remove();
			Iterator<GraphEdge> i = v.getNeighbors().iterator();
					
			while (i.hasNext()) {
				GraphEdge edge = i.next();
				next = graph.getNodeList().get(edge.getToNode());
				if (!visited[r.getId()]
						&& roundTraversalController.doesTraversalHappen(v,
								next, edge)) {
					if (runTerminationController == null
							|| !runTerminationController
									.doesRunTerminateOnSpecificCondition(graph)) {
						queue.add(r);
						visited[r.getId()] = true;
					}
				}
			}
		}

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

	@Override
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
				doBFSTraversal(graph, startNode, visited);
			}

		System.out.println("Number of rounds in a run: " + counter);
	}

}