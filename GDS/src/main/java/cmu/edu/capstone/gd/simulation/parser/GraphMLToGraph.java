package cmu.edu.capstone.gd.simulation.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cmu.edu.capstone.gd.simulation.exception.SimulationExecutionException;
import cmu.edu.capstone.gd.simulation.objects.Graph;
import cmu.edu.capstone.gd.simulation.utilities.SimulationProperties;

public class GraphMLToGraph {

	final static Logger logger = Logger.getLogger(GraphMLToGraph.class);

	/**
	 * This method converts a GraphML (.xml) file to a Graph object
	 * 
	 * @param fileName
	 *            a String as the input file name
	 * @return a Graph object
	 * @throws SimulationExecutionException
	 * 
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static Graph convertGraphMLToGraph(String fileName, String[] nodeAttributesToBeUsed,
			String[] edgeAttributesToBeUsed) throws SimulationExecutionException {

		Document doc = getXMLDocument(fileName);

		logger.info("Root element :" + doc.getDocumentElement().getNodeName());

		NodeList nodeList = doc.getElementsByTagName("node");

		logger.info("Number of Nodes: " + nodeList.getLength());

		List<String> nodeAttributeList = getNodeAttributeList(fileName);
		List<String> edgeAttributeList = getEdgeAttributeList(fileName);

		int size = nodeList.getLength();

		Graph graph = new Graph(size);

		for (int i = 0; i < size; i++) {
			Node node = nodeList.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) node;

				NodeList l = eElement.getElementsByTagName("data");

				String[] nodeAttributes = null;

				if (SimulationProperties.NODE_ATTRIBUTES != null) {
					nodeAttributes = SimulationProperties.NODE_ATTRIBUTES;
				} else if (nodeAttributesToBeUsed != null) {
					nodeAttributes = nodeAttributesToBeUsed;
				} else {
					logger.error("Unable to fetch desired node attribute list to be used for Simulation");
					throw new SimulationExecutionException(
							"Unable to fetch desired node attribute list to be used for Simulation");
				}

				HashSet<String> nodeAttributesSet = new HashSet<String>();

				for (String att : nodeAttributes) {
					nodeAttributesSet.add(att.toLowerCase());
				}

				for (int attID = 0; attID < nodeAttributeList.size(); attID++) {
					if (nodeAttributesSet.contains(nodeAttributeList.get(attID).toLowerCase())) {
						graph.getNodeFromGraph(i).addAttribute(nodeAttributeList.get(attID),
								l.item(attID).getTextContent());

						nodeAttributesSet.remove(nodeAttributeList.get(attID).toLowerCase());
					}
				}

				for (String remainingAttribute : nodeAttributesSet) {
					graph.getNodeFromGraph(i).addAttribute(remainingAttribute, null);
				}
			}
		}

		NodeList edgeList = doc.getElementsByTagName("edge");

		logger.info("Number of Edges: " + edgeList.getLength());

		int noOfEdges = edgeList.getLength();

		for (int i = 0; i < noOfEdges; i++) {
			Node edge = edgeList.item(i);

			if (edge.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) edge;

				int source = Integer.parseInt(eElement.getAttribute("source").replaceAll("[^\\d.]", ""));
				int target = Integer.parseInt(eElement.getAttribute("target").replaceAll("[^\\d.]", ""));

				HashMap<String, Object> attributeList = new HashMap<String, Object>();

				String[] edgeAttributes = null;

				if (SimulationProperties.EDGE_ATTRIBUTES != null) {
					edgeAttributes = SimulationProperties.EDGE_ATTRIBUTES;
				} else if (edgeAttributesToBeUsed != null) {
					edgeAttributes = edgeAttributesToBeUsed;
				} else {
					logger.error("Unable to fetch desired edge attribute list to be used for Simulation");
					throw new SimulationExecutionException(
							"Unable to fetch desired edge attribute list to be used for Simulation");
				}

				HashSet<String> edgeAttributesSet = new HashSet<String>();

				for (String att : edgeAttributes) {
					edgeAttributesSet.add(att.toLowerCase());
				}

				for (int attID = 0; attID < edgeAttributeList.size(); attID++) {
					if (edgeAttributesSet.contains(edgeAttributeList.get(attID).toLowerCase())) {
						attributeList.put(edgeAttributeList.get(attID),
								eElement.getElementsByTagName("data").item(attID).getTextContent());
					}
				}

				graph.addEdge(source, target, attributeList);

			}

		}

		return graph;
	}

	public static Document getXMLDocument(String fileName) {
		File fXmlFile = new File(fileName);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
			logger.error(e1.getMessage());
		}
		Document doc = null;
		try {
			doc = dBuilder.parse(fXmlFile);
		} catch (SAXException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		if (doc != null)
			doc.getDocumentElement().normalize();

		return doc;
	}

	public static List<String> getNodeAttributeList(String fileName) {
		Document doc = getXMLDocument(fileName);

		List<String> attributeList = new ArrayList<String>();
		NodeList nodeList = doc.getElementsByTagName("key");

		int size = nodeList.getLength();

		for (int i = 0; i < size; i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getAttributes().getNamedItem("for").getNodeValue().equals("node"))
					attributeList.add(node.getAttributes().getNamedItem("attr.name").getNodeValue());
			}
		}

		return attributeList;

	}

	public static List<String> getNodeAttributeIDList(String fileName) {
		Document doc = getXMLDocument(fileName);

		List<String> attributeList = new ArrayList<String>();
		NodeList nodeList = doc.getElementsByTagName("key");

		int size = nodeList.getLength();

		for (int i = 0; i < size; i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getAttributes().getNamedItem("for").getNodeValue().equals("node"))
					attributeList.add(node.getAttributes().getNamedItem("id").getNodeValue());
			}
		}

		return attributeList;

	}

	public static List<String> getEdgeAttributeList(String fileName) {
		Document doc = getXMLDocument(fileName);

		List<String> attributeList = new ArrayList<String>();
		NodeList nodeList = doc.getElementsByTagName("key");

		int size = nodeList.getLength();

		for (int i = 0; i < size; i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getAttributes().getNamedItem("for").getNodeValue().equals("edge"))
					attributeList.add(node.getAttributes().getNamedItem("attr.name").getNodeValue());
			}
		}

		return attributeList;

	}

	public static List<String> getEdgeAttributeIDList(String fileName) {
		Document doc = getXMLDocument(fileName);

		List<String> attributeList = new ArrayList<String>();
		NodeList nodeList = doc.getElementsByTagName("key");

		int size = nodeList.getLength();

		for (int i = 0; i < size; i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getAttributes().getNamedItem("for").getNodeValue().equals("edge"))
					attributeList.add(node.getAttributes().getNamedItem("id").getNodeValue());
			}
		}

		return attributeList;

	}

}
