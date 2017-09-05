package cmu.edu.capstone.gd.simulation.core;

import cmu.edu.capstone.gd.simulation.objects.Graph;
import cmu.edu.capstone.gd.simulation.objects.GraphEdge;
import cmu.edu.capstone.gd.simulation.objects.GraphNode;

/**
 * (Mandatory) Has to be implemented for deciding how traversal should happen.
 * 
 * Default implementation has been provided at:
 * 
 * @see cmu.edu.capstone.gd.simulation.core.impl.
 *      DefaultRoundTraversalControllerImpl
 * 
 *      This is the core part of the whole simulation. It handles the traversal
 *      between two nodes, and update the graph (nodes and edges) as traversal
 *      happens Each time this is called, only three things are concerned(and
 *      affected): the node the traversal starts from, the node the traversal
 *      ends with, and the edge between these two nodes
 * 
 *      To change the rest of the graph (or say complete graph), please refer to
 *      UpdateGraphController:
 * @see cmu.edu.capstone.gd.simulation.core.UpdateGraphController
 * 
 * @author Saksham Gangwar
 * @version 1.0.0
 *
 */
public interface RoundTraversalController {

	/**
	 * This method handles the traversal between two nodes. This method is
	 * called each time traversal happens(or not) between two nodes. To be
	 * called inside the TraversalAlgorithm's doGraphTraversal() method.
	 * 
	 * Check the default implementation of doesTraversalHappen method in:
	 * 
	 * @see cmu.edu.capstone.gd.simulation.core.impl.DefaultRoundTraversalControllerImpl#doesTraversalHappen(GraphNode,
	 *      GraphNode, GraphEdge)
	 * 
	 *      And check how this method is being used in Traversal Algorithm:
	 * 
	 * @see cmu.edu.capstone.gd.simulation.core.impl.DFSAlgorithmImpl#doGraphTraversal(Graph)
	 * 
	 *      This method helps to define what are the required condition on which
	 *      ever attributes of nodes or edges to be considered for deciding
	 *      whether the flow should happen or not between two considered nodes
	 *      via given edge.
	 * 
	 * @param from
	 *            A GraphNode object specifying which node the traversal starts
	 *            from
	 * @param to
	 *            A GraphNode object specifying which node the traversal ends
	 *            with
	 * @param edge
	 *            A GraphEdge object the edge between the two nodes above
	 * @return boolean Returns true if traversal happens, false otherwise
	 */
	public boolean doesTraversalHappen(GraphNode from, GraphNode to, GraphEdge edge);

	/**
	 * This methods updates the node each time traversal happens between two
	 * nodes Can be called inside doesTraversalHappen to specify what attributes
	 * changed when traversal happens
	 * 
	 * Default implementation:
	 * 
	 * @see cmu.edu.capstone.gd.simulation.core.impl.DefaultRoundTraversalControllerImpl#updateNode(Graph,
	 *      int)
	 * 
	 *      e.g. Each time doesTraversalHappen should return true, the node
	 *      should be visited, thus its "visted_times" attribute(should there be
	 *      one) should add 1
	 * 
	 * @param graph
	 * @param nodeID
	 *            an int, specifying which node to update
	 */
	public void updateNode(Graph graph, GraphNode node, Object newContent);

	/**
	 * This methods updates the edge each time traversal happens between two
	 * nodes Can be called inside doesTraversalHappen to specify what attributes
	 * changed when traversal happens.
	 * 
	 * Have a look at default implementation:
	 * 
	 * @see cmu.edu.capstone.gd.simulation.core.impl.DefaultRoundTraversalControllerImpl#updateEdge(Graph,
	 *      GraphEdge)
	 * 
	 * @param graph
	 * @param startNodeID
	 *            an int, The node id the edge starts from
	 * @param endNodeID
	 *            an int, The node id the edge ends with
	 */
	public void updateEdge(Graph graph, GraphEdge edge, Object newContent);

}
