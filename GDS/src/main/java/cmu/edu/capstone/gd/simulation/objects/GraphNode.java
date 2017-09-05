package cmu.edu.capstone.gd.simulation.objects;

import java.util.HashMap;
import java.util.LinkedList;


/**
 * This class represents graph node. 
 * GraphNode object has: 
 * Integer id: This GraphNode's unique id
 * HashMap<String, Object> attributeList: A list of this GraphNode's attributes
 * LinkedList<GraphEdge> neighbors: This GraphNode's neighbors information
 */

public class GraphNode {

	Integer id;

	HashMap<String, Object> attributeList;

	LinkedList<GraphEdge> neighbors;

	
	/** 
	 * Graph Node constructor
	 * @param id unique identifier of this node  
	 */
	public GraphNode(Integer id) {
		this.id = id;
		this.attributeList = new HashMap<String, Object>();
		this.neighbors = new LinkedList<GraphEdge>();
	}

	
	/**
	 * Adds a pair attribute's name and its value in this GraphNode's attributeList 
	 * @param attributeName
	 * @param value
	 */
	public void addAttribute(String attributeName, Object value) {
		attributeList.put(attributeName, value);
	}

	/**
	 * Returns get a value of the specified attribute from this GraphNode's attributeList
	 * @param attributeName
	 * @return a value of the attribute
	 */
	public Object getAttribute(String attributeName) {
		Object value = attributeList.get(attributeName);
		return value;

	}
	
	/**
	 * Returns a list of GraphEdges are linked with this GraphNode
	 * @return LinkedList of neighbor GraphEdges of this GraphNode
	 */
	public LinkedList<GraphEdge> getNeighbors() {
		return neighbors;
	}
	
	/**
	 * Method to access attributes of the node
	 * @return a HashMap of attributes of the GraphNode
	 */
	public HashMap<String, Object> getAttributeList() {
		return attributeList;
	}
	/**
	 * Set a neighbor GraphEdge to this GraphNode
	 * @param neighbor
	 */
	public void setNeighbors(GraphEdge neighbor) {
		this.neighbors.add(neighbor);
	}
	
	
	/**
	 * Returns an ID of this GraphNode
	 * @return ID of this GraphNode
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * Set an ID of the GraphNode 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * Output description this GraphNode in string type.
	 */
	@Override
	public String toString() {
		return "GraphNode [id=" + id + ", attributeList=" + attributeList + ", neighbors=" + neighbors + "]";
	}
	
	

}
