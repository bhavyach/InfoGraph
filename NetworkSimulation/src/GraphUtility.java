
import java.io.File;

import java.io.IOException;

import java.util.ArrayList;

import java.util.HashMap;

import java.util.HashSet;

import java.util.List;

import java.util.concurrent.locks.Lock;

import java.util.concurrent.locks.ReentrantLock;



import javax.xml.parsers.DocumentBuilder;

import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.parsers.ParserConfigurationException;



import org.w3c.dom.Document;

import org.w3c.dom.Element;

import org.w3c.dom.Node;

import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;



import com.google.gson.Gson;



public class GraphUtility {



static int count;

static int NUMBER_OF_SIMULATIONS = 1000;



static List<Integer> startingNodes = new ArrayList<>();



static HashSet<Integer> nodesVisited = new HashSet<Integer>();

static HashMap<Integer, HashSet<Integer>> edgesVisited = new HashMap<Integer, HashSet<Integer>>();



public static void main(String[] args) {

try {

double[][] result = convertGraphMLNetworkToMatrix("simplemodel.graphml");



// Initialize nodes to start from

startingNodes.add(60);

startingNodes.add(40);

startingNodes.add(30);



Lock lock = new ReentrantLock();



for (Integer node : startingNodes) {

Thread t = new Thread(

new Runnable() {

@Override

public void run() {

GraphUtility gu = new GraphUtility();

try {

int[][] probabilityResult = gu.traverseGraph(result, node);



lock.lock();

System.out.println(convertGraphMLToVisualJSON("simplemodel.graphml"));

System.out.println("Starting node: " + node + " for thread: " + Thread.currentThread().getName());

printMatrix(probabilityResult);



} catch (SAXException | IOException | ParserConfigurationException e) {

e.printStackTrace();

} finally {

lock.unlock();

}

}

},

"Thread-" + node);

t.start();

}

} catch (SAXException | IOException | ParserConfigurationException e1) {

e1.printStackTrace();

}

}



public int[][] traverseGraph(double[][] result, Integer startingNode) throws SAXException, IOException, ParserConfigurationException {

int[][] probabilityResult = new int[result.length][result[0].length];



for (int i = 0; i < probabilityResult.length; i++) {

for (int j = 0; j < probabilityResult[0].length; j++) {

probabilityResult[i][j] = 0;

}

}



System.out.println("\n");



boolean[] visited = new boolean[result[0].length];

for (int i=0 ; i<visited.length; i++) {

visited[i] = false;

}



for (int i = 0; i < NUMBER_OF_SIMULATIONS; i++) {

// System.out.println("\n\n\nStarting Node: " + startingNode +

// "\n");

count = 0;



visited = new boolean[result[0].length];



for (int k=0 ; k<visited.length; k++) {

visited[k] = false;

}



this.dfs(result, visited, startingNode, probabilityResult);

}

return probabilityResult;

}



public void dfs(double[][] graph, boolean[] visited, int startNode, int[][] probabilityResult) {



int num_nodes = graph[0].length;



if (startNode >= 0 && startNode < num_nodes) {

int nextNode;

visited[startNode] = true;



if (count == 0) {

count++;

} else {

// System.out.println("Visited node: " + startNode);

count++;

}



for (nextNode = 0; nextNode < num_nodes; nextNode++) {



double random = Math.random();

double weight = graph[startNode][nextNode] / 2;



if (graph[startNode][nextNode] > 0 && !visited[nextNode] && weight > random) {



nodesVisited.add(startNode);

nodesVisited.add(nextNode);



if (edgesVisited.containsKey(startNode)) {

edgesVisited.get(startNode).add(nextNode);

} else {

HashSet<Integer> set = new HashSet<Integer>();

set.add(nextNode);

edgesVisited.put(startNode, set);

}



// System.out.println("Start: " + startNode + " ----> Next:

// " + nextNode + " Random: " + random

// + " Weight: " + weight);



probabilityResult[startNode][nextNode]++;



dfs(graph, visited, nextNode, probabilityResult);

}

}

} else {

System.out.println("Invalid startNode " + startNode + " - will simply return");

}



}



public static void printMatrix(int[][] matrix) {

int rows = matrix.length;

int cols = matrix[0].length;



for (int i = 0; i < rows; i++) {

for (int j = 0; j < cols; j++) {

if (matrix[i][j] >= NUMBER_OF_SIMULATIONS / 3)

System.out.print(matrix[i][j] + " ");

else

System.out.print(0 + " ");

}



System.out.println();

}

}



public static void updateJSON(int[][] matrix) {

int rows = matrix.length;

int cols = matrix[0].length;



for (int i = 0; i < rows; i++) {

for (int j = 0; j < cols; j++) {

if (matrix[i][j] >= NUMBER_OF_SIMULATIONS / 3) {



}

}



System.out.println();

}

}



public static double[][] convertGraphMLNetworkToMatrix(String fileName)

throws SAXException, IOException, ParserConfigurationException {

File fXmlFile = new File(fileName);

DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

Document doc = dBuilder.parse(fXmlFile);



doc.getDocumentElement().normalize();



System.out.println("Root element :" + doc.getDocumentElement().getNodeName());



NodeList nodeList = doc.getElementsByTagName("node");



System.out.println("Number of Nodes: " + nodeList.getLength());



int size = nodeList.getLength();



double[][] networkMatrix = new double[size][size];



NodeList edgeList = doc.getElementsByTagName("edge");



System.out.println("Number of Edges: " + edgeList.getLength());



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



public static String convertGraphMLToVisualJSON(String fileName)

throws SAXException, IOException, ParserConfigurationException {



ArrayList<Nodes> nodesList = new ArrayList<Nodes>();

ArrayList<Links> linksList = new ArrayList<Links>();



File fXmlFile = new File(fileName);

DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

Document doc = dBuilder.parse(fXmlFile);



doc.getDocumentElement().normalize();



System.out.println("Root element :" + doc.getDocumentElement().getNodeName());



NodeList nodeList = doc.getElementsByTagName("node");



System.out.println("Number of Nodes: " + nodeList.getLength());



int size = nodeList.getLength();



for (int i = 0; i < size; i++) {

Nodes n1 = new Nodes();

n1.setName(i + "");



if (startingNodes.contains(i))

n1.setGroup(2);

else if (nodesVisited.contains(i))

n1.setGroup(3);

else

n1.setGroup(1);

nodesList.add(n1);

}



NodeList edgeList = doc.getElementsByTagName("edge");



System.out.println("Number of Edges: " + edgeList.getLength());



int noOfEdges = edgeList.getLength();



for (int i = 0; i < noOfEdges; i++) {

Node edge = edgeList.item(i);



if (edge.getNodeType() == Node.ELEMENT_NODE) {



Element eElement = (Element) edge;



int source = Integer.parseInt(eElement.getAttribute("source").replaceAll("[^\\d.]", ""));

int target = Integer.parseInt(eElement.getAttribute("target").replaceAll("[^\\d.]", ""));



// double weight =

// Double.parseDouble(eElement.getElementsByTagName("data").item(0).getTextContent());



Links l1 = new Links();

l1.setSource(source);

l1.setTarget(target);



if (edgesVisited.containsKey(source) && edgesVisited.get(source).contains(target))

l1.setValue(10);

else

l1.setValue(1);

linksList.add(l1);



}

}



displayjson dj = new displayjson();



dj.setLinks(linksList);

dj.setNodes(nodesList);



Gson gson = new Gson();

return gson.toJson(dj);

}



}



class displayjson {

ArrayList<Nodes> nodes;

ArrayList<Links> links;



public ArrayList<Nodes> getNodes() {

return nodes;

}



public void setNodes(ArrayList<Nodes> nodes) {

this.nodes = nodes;

}



public ArrayList<Links> getLinks() {

return links;

}



public void setLinks(ArrayList<Links> links) {

this.links = links;

}



}



class Nodes {

String name;

Integer group;



public String getName() {

return name;

}



public void setName(String name) {

this.name = name;

}



public Integer getGroup() {

return group;

}



public void setGroup(Integer group) {

this.group = group;

}

}



class Links {

Integer source;

Integer target;

Integer value;



public Integer getSource() {

return source;

}



public void setSource(Integer source) {

this.source = source;

}



public Integer getTarget() {

return target;

}



public void setTarget(Integer target) {

this.target = target;

}



public Integer getValue() {

return value;

}



public void setValue(Integer value) {

this.value = value;

}

}



/*import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gson.Gson;

public class GraphUtility {

	static int count;
	static int[][] probabilityResult;
	static int NUMBER_OF_SIMULATIONS = 1000;

	static Integer STARTING_NODE = 60;

	static HashSet<Integer> nodesVisited = new HashSet<Integer>();
	static HashMap<Integer, HashSet<Integer>> edgesVisited = new HashMap<Integer, HashSet<Integer>>();

	public static void main(String[] args) {
		try {
			double[][] result = convertGraphMLNetworkToMatrix("simplemodel.graphml");

			

			probabilityResult = new int[result.length][result[0].length];

			for (int i = 0; i < probabilityResult.length; i++) {
				for (int j = 0; j < probabilityResult[0].length; j++) {
					probabilityResult[i][j] = 0;
				}
			}

			System.out.println("\n");

			boolean[] visited = new boolean[result[0].length];
			for (boolean b : visited) {
				b = false;
			}

			for (int i = 0; i < NUMBER_OF_SIMULATIONS; i++) {
				// System.out.println("\n\n\nStarting Node: " + startingNode +
				// "\n");
				count = 0;

				visited = new boolean[result[0].length];

				int k = 0;
				for (boolean b : visited) {
					visited[k] = false;
					k++;
				}

				dfs(result, visited, STARTING_NODE);
				
				
				
			}
			
			System.out.println(convertGraphMLToVisualJSON("simplemodel.graphml"));

			printMatrix(probabilityResult);

		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static void dfs(double[][] graph, boolean[] visited, int startNode) {

		int num_nodes = graph[0].length;

		if (startNode >= 0 && startNode < num_nodes) {
			int nextNode;
			visited[startNode] = true;

			if (count == 0) {
				count++;
			} else {
				// System.out.println("Visited node: " + startNode);
				count++;
			}

			for (nextNode = 0; nextNode < num_nodes; nextNode++) {

				double random = Math.random();
				double weight = graph[startNode][nextNode] / 2;

				if (graph[startNode][nextNode] > 0 && !visited[nextNode] && weight > random) {

					nodesVisited.add(startNode);
					nodesVisited.add(nextNode);

					if (edgesVisited.containsKey(startNode)) {
						edgesVisited.get(startNode).add(nextNode);
					} else {
						HashSet<Integer> set = new HashSet<Integer>();
						set.add(nextNode);
						edgesVisited.put(startNode, set);
					}

					// System.out.println("Start: " + startNode + " ----> Next:
					// " + nextNode + " Random: " + random
					// + " Weight: " + weight);

					probabilityResult[startNode][nextNode]++;

					dfs(graph, visited, nextNode);
				}
			}
		} else {
			System.out.println("Invalid startNode " + startNode + " - will simply return");
		}

	}

	public static void printMatrix(int[][] matrix) {
		int rows = matrix.length;
		int cols = matrix[0].length;

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (matrix[i][j] >= NUMBER_OF_SIMULATIONS / 3)
					System.out.print(matrix[i][j] + " ");
				else
					System.out.print(0 + " ");
			}

			System.out.println();
		}
	}

	public static void updateJSON(int[][] matrix) {
		int rows = matrix.length;
		int cols = matrix[0].length;

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (matrix[i][j] >= NUMBER_OF_SIMULATIONS / 3) {

				}
			}

			System.out.println();
		}
	}

	public static double[][] convertGraphMLNetworkToMatrix(String fileName)
			throws SAXException, IOException, ParserConfigurationException {
		File fXmlFile = new File(fileName);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		doc.getDocumentElement().normalize();

		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

		NodeList nodeList = doc.getElementsByTagName("node");

		System.out.println("Number of Nodes: " + nodeList.getLength());

		int size = nodeList.getLength();

		double[][] networkMatrix = new double[size][size];

		NodeList edgeList = doc.getElementsByTagName("edge");

		System.out.println("Number of Edges: " + edgeList.getLength());

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

	public static String convertGraphMLToVisualJSON(String fileName)
			throws SAXException, IOException, ParserConfigurationException {

		ArrayList<Nodes> nodesList = new ArrayList<Nodes>();
		ArrayList<Links> linksList = new ArrayList<Links>();

		File fXmlFile = new File(fileName);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		doc.getDocumentElement().normalize();

		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

		NodeList nodeList = doc.getElementsByTagName("node");

		System.out.println("Number of Nodes: " + nodeList.getLength());

		int size = nodeList.getLength();

		for (int i = 0; i < size; i++) {
			Nodes n1 = new Nodes();
			n1.setName(i + "");

			if (i == STARTING_NODE)
				{
					n1.setGroup(2);
					n1.setColor("red");
				}
			else if (nodesVisited.contains(i))
				{
					n1.setGroup(3);
					n1.setColor("green");
				}
			else
				{
					n1.setGroup(1);
					n1.setColor("grey");
				}
			nodesList.add(n1);
		}

		NodeList edgeList = doc.getElementsByTagName("edge");

		System.out.println("Number of Edges: " + edgeList.getLength());

		int noOfEdges = edgeList.getLength();

		for (int i = 0; i < noOfEdges; i++) {
			Node edge = edgeList.item(i);

			if (edge.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) edge;

				int source = Integer.parseInt(eElement.getAttribute("source").replaceAll("[^\\d.]", ""));
				int target = Integer.parseInt(eElement.getAttribute("target").replaceAll("[^\\d.]", ""));

				// double weight =
				// Double.parseDouble(eElement.getElementsByTagName("data").item(0).getTextContent());

				Links l1 = new Links();
				l1.setSource(source);
				l1.setTarget(target);

				if (edgesVisited.containsKey(source) && edgesVisited.get(source).contains(target))
					l1.setValue(10);
				else
					l1.setValue(1);
				linksList.add(l1);

			}
		}

		displayjson dj = new displayjson();

		dj.setLinks(linksList);
		dj.setNodes(nodesList);

		Gson gson = new Gson();
		return gson.toJson(dj);
	}

}

class displayjson {
	ArrayList<Nodes> nodes;
	ArrayList<Links> links;

	public ArrayList<Nodes> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<Nodes> nodes) {
		this.nodes = nodes;
	}

	public ArrayList<Links> getLinks() {
		return links;
	}

	public void setLinks(ArrayList<Links> links) {
		this.links = links;
	}

}

class Nodes {
	String name;
	Integer group;
	String color;
	
	

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getGroup() {
		return group;
	}

	public void setGroup(Integer group) {
		this.group = group;
	}
}

class Links {
	Integer source;
	Integer target;
	Integer value;

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Integer getTarget() {
		return target;
	}

	public void setTarget(Integer target) {
		this.target = target;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
}
