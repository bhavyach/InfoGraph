package cmu.edu.capstone.gd.simulation.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cmu.edu.capstone.gd.simulation.utilities.ConversionUtility;
import cmu.edu.capstone.gd.simulation.utilities.NormalizationUtility;
import cmu.edu.capstone.gd.simulation.utilities.SimulationProperties;

/**
 * 
 * This class represents a directed graph using adjacency list representation
 *
 */
public class Graph {
	private int noOfNodes; // No. of vertices
	private double coverage;// how much percent of the total nodes are visited
							// in a run
	// private int noOfEdges;

	/**
	 * Array of lists for Adjacency List Representation
	 */
	private LinkedList<GraphNode> nodeList;

	/**
	 * Update coverage : each time a round is over, coverage gets updated if
	 * traversal happens between two nodes
	 * 
	 * @param noOfVistedNodes
	 */
	public void updateCoverage() {
		coverage = ((noOfNodes * coverage) + 1) / noOfNodes;
	}

	/**
	 * gets the coverage in the graph
	 * 
	 * @return double coverage
	 */
	public double getCoverage() {
		return coverage;
	}

	/**
	 * reset the coverage in the graph to be zero at the start of every run
	 * 
	 * @return double coverage
	 */
	public void resetCoverage() {
		coverage = 0;
	}

	/**
	 * Constructor
	 * 
	 * @param noOfNodes
	 */
	public Graph(int noOfNodes) {
		this.noOfNodes = noOfNodes;
		this.nodeList = new LinkedList<GraphNode>();
		for (int i = 0; i < noOfNodes; ++i)
			nodeList.add(new GraphNode(i));
	}

	/**
	 * Function to add an edge into the graph
	 * 
	 * @param v
	 *            : The number of a source node
	 * @param w
	 *            : The number of a target node
	 * @param attributeList
	 *            : Containing the edge's attribution information <key: Edge's
	 *            attribute name, value: Edge's attribute value>
	 */

	public void addEdge(int v, int w, HashMap<String, Object> attributeList) {
		GraphEdge e = new GraphEdge(v, w);
		e.addAttributeList(attributeList);
		getNodeFromGraph(v).setNeighbors(e);
	}

	/**
	 * 
	 * @param nodeId
	 *            Integer The new node ID
	 * @param attributeList
	 *            HashMap<String, Object> The attributeList of the new GraphNode
	 * @param parentNodeID
	 *            int[] The parent node IDs of this new node
	 * @param childNodeID
	 *            int[] The child node IDS of this new node
	 * @param edgeAttributeList
	 *            HashMap<String, Object> The attributeList of the edges between
	 *            this new node and its parent and children
	 */
	public void addNode(Integer nodeId, HashMap<String, Object> attributeList, int[] parentNodeID, int[] childNodeID,
			HashMap<String, Object> edgeAttributeList) {
		if (!checkIfNodeExist(nodeId)) {
			GraphNode n = new GraphNode(nodeId);
			if (attributeList != null) {
				for (String key : attributeList.keySet()) {
					n.addAttribute(key, attributeList.get(key));
				}
			}

			nodeList.add(n);
			if (edgeAttributeList != null) {
				for (int i = 0; i < parentNodeID.length; i++) {
					addEdge(parentNodeID[i], nodeId, edgeAttributeList);
				}
				for (int i = 0; i < childNodeID.length; i++) {
					addEdge(nodeId, childNodeID[i], edgeAttributeList);
				}
			}
		}
	}

	/**
	 * Method to get how many nodes this graph has
	 * 
	 * @return the number of Nodes
	 */
	public int getNoOfNodes() {
		return noOfNodes;
	}

	/**
	 * Set the number of Nodes that the graph has
	 */
	public void setNoOfNodes(int noOfNodes) {
		this.noOfNodes = noOfNodes;
	}

	/**
	 * Returns a list of GraphNodes belongs to this Graph
	 * 
	 * @return GraphNode list of this Graph in a LinkedList structure
	 */
	public LinkedList<GraphNode> getNodeList() {
		return nodeList;
	}

	public GraphNode getNodeFromGraph(int nodeID) {
		for (GraphNode node : getNodeList()) {
			if (node.getId() == nodeID)
				return node;
		}
		return null;
	}

	/**
	 * Set the specified node list as a nodeList of this Graph.
	 */
	public void setNodeList(LinkedList<GraphNode> nodeList) {
		this.nodeList = nodeList;
	}

	/**
	 * Print out this graph. Shows all nodes and edges. Also, displays number of
	 * visited times for each node and weight on the edges.
	 */
	public void printGraph() {

		System.out.println("Graph [noOfNodes=" + noOfNodes + "]");

		Iterator<GraphNode> n = nodeList.iterator();

		while (n.hasNext()) {
			GraphNode gn = n.next();

			System.out.println(gn);

			LinkedList<GraphEdge> geList = gn.getNeighbors();
			Iterator<GraphEdge> geListIterator = geList.iterator();

			while (geListIterator.hasNext()) {
				GraphEdge ge = geListIterator.next();
				System.out.print(ge);
			}

			System.out.println();
		}

	}

	public boolean checkIfPathExist(int startNode, int endNode) {

		boolean visited[] = new boolean[getNoOfNodes()];

		return doTraversalForCheckingPath(startNode, endNode, visited);
	}

	private boolean doTraversalForCheckingPath(int startNode, int endNode, boolean[] visited) {

		visited[startNode] = true;

		GraphNode next;

		// Recur for all the vertices adjacent to this vertex
		Iterator<GraphEdge> i = getNodeList().get(startNode).getNeighbors().iterator();

		while (i.hasNext()) {
			GraphEdge edge = i.next();
			next = getNodeList().get(edge.getToNode());

			if (endNode == next.getId())
				return true;

			if (!visited[next.getId()])
				doTraversalForCheckingPath(next.getId(), endNode, visited);

		}
		return false;
	}

	public boolean removeNodeFromGraph(int nodeID) {
		getNodeList().remove(nodeID);
		for (GraphNode node : getNodeList()) {
			if (node.getId() != nodeID) {
				List<GraphEdge> edgesToBeRemoved = new ArrayList<GraphEdge>();
				for (GraphEdge edge : node.getNeighbors()) {
					if (edge.toNode == nodeID) {
						edgesToBeRemoved.add(edge);
					}
				}

				for (GraphEdge e : edgesToBeRemoved) {
					node.getNeighbors().remove(e);
				}
			}
		}

		return true;
	}

	public boolean checkIfEdgeExistBetweenNodes(int fromNodeID, int toNodeID) {
		GraphNode fromNode = getNodeFromGraph(fromNodeID);
		for (GraphEdge edge : fromNode.neighbors) {
			if (edge.toNode == toNodeID)
				return true;
		}
		return false;
	}

	public boolean checkIfNodeExist(int nodeID) {
		if (getNodeFromGraph(nodeID) != null)
			return true;
		return false;
	}

	public void removeEdgeFromGraph(int fromNodeID, int toNodeID) {
		if (checkIfEdgeExistBetweenNodes(fromNodeID, toNodeID)) {
			GraphNode fromNode = getNodeFromGraph(fromNodeID);

			for (GraphEdge edge : fromNode.getNeighbors()) {
				if (edge.toNode == toNodeID) {
					fromNode.getNeighbors().remove(edge);
					break;
				}
			}
		}
	}

	public void normalizeGraphEdgesAttributes(String edgeAttributeName) {
		NormalizationUtility.normalizeAttributeValues(this, null, new String[] { edgeAttributeName });
	}

	public void normalizeGraphNodesAttributes(String nodeAttributeName) {
		NormalizationUtility.normalizeAttributeValues(this, new String[] { nodeAttributeName }, null);
	}

}
