package cmu.edu.capstone.gd.simulation.utilities;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;

import cmu.edu.capstone.gd.simulation.objects.Graph;
/**
 * This class contains methods to randomly generate a Graph object
 * generate data of arbitrary size, mainly for testing purpose
 * 
 * @author eLizZ
 *
 */
public class DataGenerator {
	
	/**
	 * 
	 * @param noOfNodes an int defining the number of nodes in the Graph
	 * @param noOfEdges an int defining the number of edges in the Graph
	 * @param attributes an array of Strings defining the edge attributes
	 * @return a Graph object
	 */
	public static Graph generateRandomGraph(int noOfNodes, int noOfEdges, String[] attributes) {
		Graph g = new Graph(noOfNodes);

		Random random = new Random();

		for (int i = 0; i < noOfEdges; i++) {
			int fromNode = random.nextInt(noOfNodes - 1);
			int toNode = random.nextInt(noOfNodes - 1);
			while (toNode == fromNode)
				toNode = random.nextInt(noOfNodes - 1);

			HashMap<String, Object> attributesMap = new HashMap<String, Object>();

			DecimalFormat decimalFormat = new DecimalFormat("#.##");

			for (String attribute : attributes) {
				attributesMap.put(attribute, Double.valueOf(decimalFormat.format(random.nextDouble())));
			}

			g.addEdge(fromNode, toNode, attributesMap);
		}

		return g;

	}
	
	/**
	 * the main method for DataGenerator
	 * Generates a Graph with 1000 nodes, 1600 edges and attributes as "degree", "numtexts", "indegree", "outdegree", "frequency", "type",
				"weight"
	 * @param args
	 */
	public static void main(String[] args) {

		String[] attributes = new String[] { "degree", "numtexts", "indegree", "outdegree", "frequency", "type",
				"weight" };
		Graph result = generateRandomGraph(1000, 1600, attributes);

		result.printGraph();

	}

}
