package cmu.edu.capstone.gd.simulation.objects;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.*;

import cmu.edu.capstone.gd.simulation.utilities.DataGenerator;

/**
 * Graph Test class to test methods for the Graph related methods.
 * 
 * @author Saksham Gangwar
 * @version 1.0.0
 * 
 */
public class GraphTest {

	Graph inputGraph;

	@BeforeClass
	public void setUp() {
		String[] attributes = new String[] { "weight" };
		inputGraph = DataGenerator.generateRandomGraph(10, 20, attributes);
		inputGraph.printGraph();
	}

	@Test
	public void testAddEdge() {
		/**
		 * Edge to be added
		 */
		int fromNode = 1;
		int toNode = 5;
		HashMap<String, Object> attributeList = new HashMap<String, Object>();
		attributeList.put("weight", (Double) 0.8);
		/**
		 * Call the method to add edge
		 */
		inputGraph.addEdge(fromNode, toNode, attributeList);

		boolean doesPathExist = inputGraph.checkIfPathExist(1, 5);

		Assert.assertEquals(doesPathExist, true);
	}

	@Test
	public void testRemoveNode() {
		inputGraph.printGraph();

		inputGraph.removeNodeFromGraph(5);

		inputGraph.printGraph();
	}

	@Test
	public void testRemoveEdge() {
		System.out.println("Original Graph before removing edges:");
		inputGraph.printGraph();
		System.out.println("Remove Edges between connected nodes:");
		for (int i = 1; i<inputGraph.getNoOfNodes(); i++){
		if(inputGraph.checkIfEdgeExistBetweenNodes(i, i+1)){
			inputGraph.removeEdgeFromGraph(i, i+1);
		}
		}
		inputGraph.printGraph();
	}
	
	@Test
	public void testAddNode() {
		System.out.println("Original Graph before adding node:");
		inputGraph.printGraph();
		System.out.println("Adding Node 13 with parent node 8 and 6, child node 3:");
		int[] parents= new int[2];
		parents[0] =8;
		parents[1] = 6;
		int[] children = new int[1];
		children[0]=3;
		HashMap<String, Object> edgeAttributeList = new HashMap<String, Object>();
		edgeAttributeList.put("weight", 0.5);
		inputGraph.addNode(13, null, parents, children, edgeAttributeList);
		inputGraph.printGraph();
	}

}
