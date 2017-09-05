package cmu.edu.capstone.gd.simulation.utilities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cmu.edu.capstone.gd.simulation.objects.Graph;
import cmu.edu.capstone.gd.simulation.objects.GraphEdge;
import cmu.edu.capstone.gd.simulation.objects.GraphNode;

/**
 * Class sets values of node attributes and edge attributes. Values for every
 * attribute would be normalized so that it can be ranged only between 0 and 1
 * for generalized traversal implementation.
 */
public class NormalizationUtility {

	/**
	 * Method to set node attributes and edge attributes with normalized value.
	 * 
	 * @param graph
	 * @param nodeAttributes
	 * @param edgeAttributes
	 */
	public static void normalizeAttributeValues(Graph graph, String[] nodeAttributes, String[] edgeAttributes) {

		if (nodeAttributes != null && nodeAttributes.length > 0) {

			HashMap<String, Double> nodeNormalizationFactor = getNodeAttributesNormalizationFactor(graph,
					nodeAttributes);

			for (String nAttribute : nodeAttributes) {
				for (int i = 0; i < graph.getNoOfNodes(); i++) {
					GraphNode node = graph.getNodeFromGraph(i);
					Double value = Double.parseDouble((String) node.getAttribute(nAttribute));
					value = value / nodeNormalizationFactor.get(nAttribute);
					node.addAttribute(nAttribute, value);
				}
			}

		}

		if (edgeAttributes != null && edgeAttributes.length > 0) {

			HashMap<String, Double> edgeNormalizationFactor = getEdgeAttributesNormalizationFactor(graph,
					edgeAttributes);

			for (String eAttribute : edgeAttributes) {
				for (int i = 0; i < graph.getNoOfNodes(); i++) {
					GraphNode node = graph.getNodeFromGraph(i);

					for (int e = 0; e < node.getNeighbors().size(); e++) {
						GraphEdge edge = node.getNeighbors().get(e);

						Double value = Double.parseDouble((String) edge.getAttribute(eAttribute));
						value = value / edgeNormalizationFactor.get(eAttribute);
						edge.addAttribute(eAttribute, value);

					}

				}

			}

		}

	}

	/**
	 * Method to normalize value of each attribute of Node in a range between 0
	 * and 1.
	 * 
	 * @param graph
	 * @param nodeAttributes
	 * @return HashMap of attributes and its normalized value.
	 */
	public static HashMap<String, Double> getNodeAttributesNormalizationFactor(Graph graph, String[] nodeAttributes) {

		HashMap<String, Double> maxValues = new HashMap<String, Double>();
		HashMap<String, Double> minValues = new HashMap<String, Double>();

		HashMap<String, Double> normalizationFactor = new HashMap<String, Double>();

		for (String nAttribute : nodeAttributes) {
			maxValues.put(nAttribute, Double.MIN_VALUE);
			minValues.put(nAttribute, Double.MAX_VALUE);
		}

		for (int i = 0; i < graph.getNoOfNodes(); i++) {
			GraphNode node = graph.getNodeFromGraph(i);

			for (String nAttribute : nodeAttributes) {
				Double value = Double.parseDouble((String) node.getAttribute(nAttribute));
				if (maxValues.get(nAttribute) < value) {
					maxValues.put(nAttribute, value);
				}

				if (minValues.get(nAttribute) > value) {
					minValues.put(nAttribute, value);
				}
			}
		}

		for (String nAttribute : nodeAttributes) {
			Double range = maxValues.get(nAttribute) - minValues.get(nAttribute) + 1;
			if (range == 0) {
				normalizationFactor.put(nAttribute, 1.0);
			} else {
				normalizationFactor.put(nAttribute, range);
			}
		}

		return normalizationFactor;
	}

	/**
	 * Method to normalize value of each attribute of Node in a range between 0
	 * and 1.
	 * 
	 * @param graph
	 * @param nodeAttributes
	 * @return
	 */
	public static HashMap<String, Double> getEdgeAttributesNormalizationFactor(Graph graph, String[] edgeAttributes) {

		HashMap<String, Double> maxValues = new HashMap<String, Double>();
		HashMap<String, Double> minValues = new HashMap<String, Double>();

		HashMap<String, Double> normalizationFactor = new HashMap<String, Double>();

		for (String eAttribute : edgeAttributes) {
			maxValues.put(eAttribute, Double.MIN_VALUE);
			minValues.put(eAttribute, Double.MAX_VALUE);
		}

		for (int i = 0; i < graph.getNoOfNodes(); i++) {
			GraphNode node = graph.getNodeFromGraph(i);

			for (int e = 0; e < node.getNeighbors().size(); e++) {
				GraphEdge edge = node.getNeighbors().get(e);
				for (String eAttribute : edgeAttributes) {
					Double value = Double.parseDouble((String) edge.getAttribute(eAttribute));
					if (maxValues.get(eAttribute) < value) {
						maxValues.put(eAttribute, value);
					}

					if (minValues.get(eAttribute) > value) {
						minValues.put(eAttribute, value);
					}
				}
			}

		}

		for (String eAttribute : edgeAttributes) {
			Double range = maxValues.get(eAttribute) - minValues.get(eAttribute) + 1;
			if (range == 0) {
				normalizationFactor.put(eAttribute, 1.0);
			} else {
				normalizationFactor.put(eAttribute, range);
			}
		}

		return normalizationFactor;
	}

}
