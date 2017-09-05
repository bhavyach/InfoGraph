package cmu.edu.capstone.gd.simulation.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cmu.edu.capstone.gd.simulation.objects.Graph;
import cmu.edu.capstone.gd.simulation.objects.GraphEdge;
import cmu.edu.capstone.gd.simulation.objects.GraphNode;
import cmu.edu.capstone.gd.simulation.utilities.SimulationProperties;

/**
 * This class handles the conversion between Graph and GraphML It will output a
 * GraphML file (.xml) with updated attributes after the simulation
 * 
 * A GraphToGraphML object contains these public class attributes: Document doc:
 * ArrayList<Node> keys: all the keys in the GraphML input file(.xml) (e.g.
 * "node", "edge") ArrayList<Node> nodes: all the nodes in the graph ArrayList
 * <Node> edges: all the edges in the graph
 * 
 * @author eLizZ
 *
 */
public class GraphToGraphML {
	static Document doc;
	static ArrayList<Node> keys;
	static ArrayList<Node> nodes;
	static ArrayList<Node> edges;
	static ArrayList<String> nodeAttributes;
	static ArrayList<String> edgeAttributes;
	static HashMap<String, String> newIdValue;

	/**
	 * adding keys to ArrayList<Node> keys To be called upon in convertor
	 * 
	 * @param forType
	 *            String forType should be either "node" or "edge"
	 * @param attrName
	 *            a String giving the name of the attributes (e.g. "weight",
	 *            "degree", "numtexts", "indegree", "outdegree", "frequency")
	 * @param attrType
	 *            a String defining the type of the attributes (e.g. degree
	 *            would be an 'int' type)
	 * @param desc
	 *            String for every key there is a describer (e.g. for weight
	 *            there is "default weight")
	 */
	public static void addKey(String forType, String attrName, String attrType, String desc) {

		int idNum = keys.size();
		String id = "d" + idNum;
		String for_type = null;

		if (forType == "node") {
			for_type = forType;
			nodeAttributes.add(attrName);
			newIdValue.put(attrName, id);
		} else {
			if (forType == "edge") {
				for_type = forType;
				edgeAttributes.add(attrName);
				newIdValue.put(attrName, id);
			} else
				System.out.println("please input one of 'node' and 'edge' for forType");
		}

		Element newKey = (Element) keys.get(0).cloneNode(true);
		newKey.setAttribute("id", id);
		newKey.setAttribute("for", for_type);
		newKey.setAttribute("attr.name", attrName);
		newKey.setAttribute("attr.type", attrType);
		newKey.getElementsByTagName("desc").item(0).setTextContent(desc);
		keys.add((Node) newKey);
	}

	/**
	 * Updates the contents in the ArrayList<node> nodes Called each time a node
	 * key is added to the key list
	 * 
	 * To be called upon in convertor
	 * 
	 * @param graph
	 *            a Graph object
	 * @param node_attribute
	 *            the string name of the attribute to be added to the graph
	 */
	public static void updateNodes(Graph graph, String node_attribute) {

		for (int i = 0; i < graph.getNoOfNodes(); i++) {
			Node node = nodes.get(i);
			Element nC = (Element) node.cloneNode(true);
			NodeList dataL = nC.getElementsByTagName("data");

			// get the right format of original children for node
			Element newData = (Element) dataL.item(0);

			newData.setAttribute("key", newIdValue.get(node_attribute));
			try {
				newData.setTextContent(
						String.valueOf((Integer) graph.getNodeFromGraph(i).getAttribute(node_attribute)));
			} catch (ClassCastException e) {
				newData.setTextContent(String.valueOf(graph.getNodeFromGraph(i).getAttribute(node_attribute)));
			}
			node.appendChild((Node) newData);

			nodes.set(i, node);

		}
	}

	/**
	 * This initialize the edges as DOM node list this method create the edges
	 * as empty nodes in the following format:
	 * <edge id="e0" source="n9" target="n49"></edge>
	 * 
	 * @param graph
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	public static void initializeEdges(Graph graph, LinkedList<GraphEdge> edgeList)
			throws TransformerFactoryConfigurationError, TransformerException {
		// LinkedList<GraphEdge> edgeList = graph.getEdgeList();
		Iterator<GraphEdge> edgeList_i = edgeList.iterator();
		int i = 0;
		while (edgeList_i.hasNext()) {
			GraphEdge graphEdge = edgeList_i.next();
			int fromNode = graphEdge.getFromNode();
			int toNode = graphEdge.getToNode();
			Element edgeNodeDOM = doc.createElement("edge");
			String id = "e" + i;
			String from = "n" + fromNode;
			String to = "n" + toNode;
			edgeNodeDOM.setAttribute("id", id);
			edgeNodeDOM.setAttribute("source", from);
			edgeNodeDOM.setAttribute("target", to);
			edges.add((Node) edgeNodeDOM);
			i++;
		}

	}

	/**
	 * Updates the contents in the ArrayList<node> edges Called each time a edge
	 * attribute is added (regardless of old or new)
	 * 
	 * To be called upon in convertor
	 * 
	 * @param graph
	 *            a Graph object
	 * @param edge_attribute
	 *            the string name of the attribute to be added to the graph
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	public static void updateEdges(Graph graph, String edge_attribute, LinkedList<GraphEdge> edgeList)
			throws TransformerFactoryConfigurationError, TransformerException {
		// iterator for the edges (DOM node) list
		Iterator<Node> edge_i = edges.iterator();
		// iterator for the edgeList in Graph object // these two list should
		// have same sequence, to be ensured in initiator
		Iterator<GraphEdge> edgeList_i = edgeList.iterator();

		// int i = 0;
		while (edge_i.hasNext()) {
			// getting both the edge in ArrayList<Node> and the corresponding
			// GraphEdge
			Node edge = edge_i.next();
			GraphEdge graphEdge = edgeList_i.next();
			// getting the id
			String id = newIdValue.get(edge_attribute);
			// Setting the attribute(DOM) for the element
			Element edgeAttribute = doc.createElement("data");
			edgeAttribute.setAttribute("key", id);
			String edge_attribute_value = String.valueOf(graphEdge.getAttribute(edge_attribute));

			// Setting the content
			edgeAttribute.setTextContent(edge_attribute_value);
			edge.appendChild((Node) edgeAttribute);
			// i++;
		}

	}

	/**
	 * Make GraphML file (.xml) out of the keys, nodes, edges and doc To be
	 * called upon in convertor
	 * 
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	public static void convertToGraphML() throws ParserConfigurationException, TransformerException {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document resultGraphML = docBuilder.newDocument();

		Element rootElement = resultGraphML.createElement("graphML");
		resultGraphML.appendChild(rootElement);
		System.out.println("rootElement appended");
		// createElement(String tagName): Creates an element of the type
		// specified.

		Iterator<Node> it_key = keys.iterator();
		while (it_key.hasNext()) {
			Node key = it_key.next();
			rootElement.appendChild(resultGraphML.importNode(key, true));
		}

		Node graphE = doc.getElementsByTagName("graph").item(0);

		while (graphE.hasChildNodes()) {
			graphE.removeChild(graphE.getFirstChild());
		}

		Iterator<Node> it_node = nodes.iterator();
		while (it_node.hasNext()) {
			Node node = it_node.next();
			graphE.appendChild(node);
		}

		Iterator<Node> it_edge = edges.iterator();
		while (it_edge.hasNext()) {
			Node edge = it_edge.next();
			graphE.appendChild(edge);
		}

		rootElement.appendChild(resultGraphML.importNode(graphE, true));

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource src = new DOMSource(resultGraphML);
		String path = "result.graphml";
		StreamResult rst = new StreamResult(new File(path));

		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(src, rst);

		System.out.println("file saved.");

	}

	/**
	 * a method which takes the inputFile as a String and a Graph object, uses
	 * the addKeys, updateNodes and convertToGraphML methods to complete the
	 * conversion process
	 * 
	 * @param graph
	 *            a Graph object
	 * @param inputFile
	 *            String
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerException
	 */
	public static void convertor(Graph graph, String inputFile, String[] node_attributes, String[] edge_attributes)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {

		// get the GraphEdge list
		LinkedList<GraphEdge> edgeList = new LinkedList<GraphEdge>();
		Iterator<GraphNode> nodeList_i = graph.getNodeList().iterator();
		int edgeListSize = 0;
		while (nodeList_i.hasNext()) {
			GraphNode node = nodeList_i.next();
			LinkedList<GraphEdge> edges = node.getNeighbors();
			try {
				Iterator<GraphEdge> edges_i = edges.iterator();
				while (edges_i.hasNext()) {
					edgeList.add(edges_i.next());
					edgeListSize++;
				}
			} catch (NullPointerException e) {
			}

		}

		System.out.println("size of the edgelist: " + edgeListSize);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		File fXmlFile = new File(inputFile);
		doc = dBuilder.parse(fXmlFile);

		doc.getDocumentElement().normalize();
		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

		NodeList nodeList = doc.getElementsByTagName("node");

		int nodeSize = nodeList.getLength();

		NodeList edgeListOld = doc.getElementsByTagName("edge");
		System.out.println("Number of Original Edges: " + edgeListOld.getLength());

		NodeList keyList = doc.getElementsByTagName("key");
		System.out.println("Length of the Key List:");
		System.out.println(keyList.getLength());

		keys = new ArrayList<Node>();
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Node>();
		nodeAttributes = new ArrayList<String>();
		edgeAttributes = new ArrayList<String>();
		newIdValue = new HashMap<String, String>();

		for (int i = 0; i < nodeSize; i++) {
			nodes.add(i, (Node) nodeList.item(i));
		}

		/*
		 * for (int i = 0; i < edgeListOld.getLength(); i++) { edges.add(i,
		 * (Node) edgeListOld.item(i)); }
		 */

		for (int i = 0; i < keyList.getLength(); i++) {
			keys.add(i, (Node) keyList.item(i));
			Element key = (Element) keyList.item(i);
			if (key.getAttribute("for").equals("node")) {
				nodeAttributes.add(key.getAttribute("attr.name"));
				newIdValue.put(key.getAttribute("attr.name"), key.getAttributeNode("id").getValue());
			} else {
				if (key.getAttribute("for").equals("edge")) {
					edgeAttributes.add(key.getAttribute("attr.name"));
					newIdValue.put(key.getAttribute("attr.name"), key.getAttributeNode("id").getValue());
				} else
					System.out.println(key.getAttribute("for"));
			}
		}

		// initializing empty edges
		initializeEdges(graph, edgeList);

		System.out.println("GraphEdge list is of size " + edgeList.size());
		System.out.println("edges list is of size " + edges.size());

		// adding keys for edges & edge attributes for the edges
		for (int i = 0; i < edge_attributes.length; i++) {
			if (!edgeAttributes.contains(edge_attributes[i])) {
				addKey("edge", edge_attributes[i], "", "");
			}
			updateEdges(graph, edge_attributes[i], edgeList);

			System.out.println("updated attribute for edge: " + edge_attributes[i]);
		}

		// adding keys for nodes & node attributes for the nodes
		for (int i = 0; i < node_attributes.length; i++) {
			if (!nodeAttributes.contains(node_attributes[i])) {
				addKey("node", node_attributes[i], "", "");
				updateNodes(graph, node_attributes[i]);
			}
		}

		convertToGraphML();
	}

}
