package cmu.edu.capstone.gd.simulation.utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
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

import cmu.edu.capstone.gd.simulation.objects.Graph;
import cmu.edu.capstone.gd.simulation.objects.GraphNode;

/**
 * This class handles the conversion between GraphML and Graph object
 * 
 * @author eLizZ
 *
 */
public class ConversionUtility {

	final static Logger logger = Logger.getLogger(ConversionUtility.class);

	public static double[][] convertGraphMLNetworkToMatrix(String fileName)
			throws SAXException, IOException, ParserConfigurationException {
		File fXmlFile = new File(fileName);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		doc.getDocumentElement().normalize();

		logger.info("Root element :" + doc.getDocumentElement().getNodeName());

		NodeList nodeList = doc.getElementsByTagName("node");

		logger.info("Number of Nodes: " + nodeList.getLength());

		int size = nodeList.getLength();

		double[][] networkMatrix = new double[size][size];

		NodeList edgeList = doc.getElementsByTagName("edge");

		logger.info("Number of Edges: " + edgeList.getLength());

		int noOfEdges = edgeList.getLength();

		for (int i = 0; i < noOfEdges; i++) {
			Node edge = edgeList.item(i);

			if (edge.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) edge;

				int source = Integer.parseInt(eElement.getAttribute("source").replaceAll("[^\\d.]", ""));
				int target = Integer.parseInt(eElement.getAttribute("target").replaceAll("[^\\d.]", ""));

				double weight = Double.parseDouble(eElement.getElementsByTagName("data").item(0).getTextContent());

				networkMatrix[source][target] = weight;
			}
		}

		return networkMatrix;
	}

}
