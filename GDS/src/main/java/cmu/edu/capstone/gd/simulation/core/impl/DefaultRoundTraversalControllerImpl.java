package cmu.edu.capstone.gd.simulation.core.impl;

import cmu.edu.capstone.gd.simulation.core.RoundTraversalController;
import cmu.edu.capstone.gd.simulation.objects.Graph;
import cmu.edu.capstone.gd.simulation.objects.GraphEdge;
import cmu.edu.capstone.gd.simulation.objects.GraphNode;
import cmu.edu.capstone.gd.simulation.utilities.SimulationProperties;

public class DefaultRoundTraversalControllerImpl implements RoundTraversalController {
	/**
	 * This is an example Assumptions : the author of this class would know the
	 * attribute list and order in the config file Options are \ 1. parse using
	 * utils 2. use constant variable
	 */

	// private static final String[] nodeAttributeList =
	// SimulationProperties.NODE_ATTRIBUTES;
	// private static final String[] edgeAttributeList =
	// SimulationProperties.EDGE_ATTRIBUTES;

	private static final String[] nodeAttributeList = { "visited_times" };
	private static final String[] edgeAttributeList = { "weight" };

	public boolean doesTraversalHappen(GraphNode from, GraphNode to, GraphEdge edge) {
		double random = Math.random();
		if ((Double) edge.getAttribute(edgeAttributeList[0]) > random) {
			/**
			 * A node or an edge can be updated here too.
			 */
			updateNode(null, to, null);
			return true;
		}

		return false;
	}

	public void updateNode(Graph graph, GraphNode node, Object newContent) {
		Integer currentCount = 0;
		if (node.getAttribute(nodeAttributeList[0]) != null) {
			currentCount = (Integer) node.getAttribute(nodeAttributeList[0]);
		}
		node.addAttribute(nodeAttributeList[0], currentCount + 1);

	}

	public void updateEdge(Graph graph, GraphEdge edge, Object newContent) {

	}

}
