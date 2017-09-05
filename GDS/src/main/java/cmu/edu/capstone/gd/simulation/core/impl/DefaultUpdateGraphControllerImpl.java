package cmu.edu.capstone.gd.simulation.core.impl;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import java.util.Iterator;

import cmu.edu.capstone.gd.simulation.core.UpdateGraphController;
import cmu.edu.capstone.gd.simulation.objects.Graph;
import cmu.edu.capstone.gd.simulation.objects.GraphEdge;
import cmu.edu.capstone.gd.simulation.objects.GraphNode;

public class DefaultUpdateGraphControllerImpl implements UpdateGraphController{

	public boolean updateGraphForRound(Graph graph) {
		graph.updateCoverage();
		return true;
	}

	
	/**
	 * This method randomly update a graph.
	 * There are 4 types of updates: adding node, adding edge, deleting node and deleting edge.
	 * Each update is executed independently each other according to its own random number. 
	 * If random number is higher than 0.5, this method will update graph.
	 * 
	 * add node: 2 parent nodes and 2 child nodes to be linked with the newly generated node are randomly picked in this method.
	 * add edge: randomly choose parent node and child node and take a "weight" as an attribute.
	 * 			A value of the weight is a random float between 0 and 1.  
	 * delete node: a node to remove is randomly selected
	 * delete edge: select fromNode randomly, then select edge to remove randomly among the fromNode's neighbor edges
	 */
	public boolean updateGraphForRun(Graph graph) {
		
		Random rand = new Random();
		int numOfNodes = graph.getNoOfNodes();
		boolean addEdge=false, addNode = false, deleteNode=false, deleteEdge = false;
		double threshold = .5;
		
		//add edge
		if (rand.nextDouble()>threshold) {
			
			int v = rand.nextInt(numOfNodes);
			int w = rand.nextInt(numOfNodes);
			while(v == w){
				w = rand.nextInt(numOfNodes);
			}
			
			HashMap<String, Object> attributeList =  new HashMap<String, Object>();
			double weight = rand.nextDouble();
			attributeList.put("weight", weight);
			graph.addEdge(v, w, attributeList);
			addEdge =true;
		}
			
		// add node		
			if (rand.nextDouble()>threshold) {				
				
				//default value
				int numOfParentNodes = 2;
				int numOfChildNodes = 2;
				
				int[] parentNodeID = new int[numOfParentNodes];
				int[] childNodeID = new int[numOfChildNodes];

				//generating parentNodeID array randomly
				Set<Integer> generated = new LinkedHashSet<Integer>();
				while (generated.size() < numOfParentNodes)
				{
				    Integer next = rand.nextInt(numOfNodes);
				    generated.add(next);
				}
				Iterator it = generated.iterator();
				
				for (int i=0; i<numOfParentNodes;i++){
					parentNodeID[i] = (Integer) it.next();					 
					}
				
				//generating childNodeID array randomly
				Set<Integer> generated_ch = new LinkedHashSet<Integer>();
				while (generated_ch.size() < numOfParentNodes)
				{
				    Integer next = rand.nextInt(numOfNodes);
				    generated_ch.add(next);
				}
				Iterator it_ch = generated_ch.iterator();
				
				for (int i=0; i<numOfChildNodes;i++){
					childNodeID[i] = (Integer) it_ch.next();					 
					}
				
				int nodeId = graph.getNoOfNodes();
				HashMap<String, Object> attributeList = null;
				
				HashMap<String, Object> edgeAttributeList = null;
				edgeAttributeList.put("weight", rand.nextDouble());
				
				graph.addNode(nodeId, attributeList, parentNodeID, childNodeID, edgeAttributeList);
				
				addNode =true;
			}
		
		
		// delete node
			if (rand.nextDouble()>threshold) {
				int nodeID = rand.nextInt(numOfNodes);
				graph.removeNodeFromGraph(nodeID);
				
				deleteNode =true;
			}
		
		
		// delete edge
			if (rand.nextDouble()>threshold) {
				int fromNodeID = rand.nextInt(numOfNodes);
				GraphNode fromNode = graph.getNodeFromGraph(fromNodeID);
				LinkedList<GraphEdge> edgeList = fromNode.getNeighbors();
				GraphEdge toEdge = (GraphEdge) edgeList.get(rand.nextInt(edgeList.size()));
				int toNodeID = toEdge.getToNode();
				graph.removeEdgeFromGraph(fromNodeID, toNodeID);
				
				deleteEdge =true;
			}
			
		return (addNode||addEdge||deleteNode||deleteEdge);
	}
}





