package cmu.edu.capstone.gd.simulation.objects;

import java.util.HashMap;
/**
 * This class represents edge using adjacency list representation 
 * It has 3 attributes which are:
 * int fromNode: Node id of the source Node
 * int toNode: Node id of the target Node
 * HashMap<String, Object> attributeList : Containing all attributes' name and its value in this Edge  
 */


public class GraphEdge {
	int fromNode;
	int toNode;

	HashMap<String, Object> attributeList;

	/**
	 * GraphEdge Constructor
	 * @param fromNode
	 * @param toNode
	 */
	public GraphEdge(Integer fromNode, Integer toNode) {
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.attributeList = new HashMap<String, Object>();
	}

	/**
	 * Adds a pair of attribute name and attribute value to the attributeList 
	 * @param attributeName attribute name
	 * @param value attribute value
	 */
	public void addAttribute(String attributeName, Object value) {
		attributeList.put(attributeName, value);
	}

	/**
	 * Adds an hashmap of attribute list to the attributeList of this GraphEdge
	 * @param attributeList
	 */
	public void addAttributeList(HashMap<String, Object> attributeList) {
		this.attributeList = attributeList;
	}
	
	/**
	 * Returns a value of attribute specified as the "attributeName" argument.
	 * @param attributeName
	 * @return returns a value of the attribute.
	 */
	public Object getAttribute(String attributeName) {
		Object value = attributeList.get(attributeName);
		return value;

	}

	/**
	 * Returns the id of a source node linked to this GraphEdge. 
	 * @return the number of a source node this edge is linked with 
	 */
	public int getFromNode() {
		return fromNode;
	}

	/**
	 * Set a source node of this GraphEdge
	 * @param fromNode
	 */
	public void setFromNode(int fromNode) {
		this.fromNode = fromNode;
	}

	/**
	 * Returns a target node of this GraphEdge
	 * @return The number of a target node of this GraphEdge  
	 */
	public int getToNode() {
		return toNode;
	}

	/**
	 * Set a target node of this GraphEdge
	 * @param toNode
	 */
	public void setToNode(int toNode) {
		this.toNode = toNode;
	}
	
	/**
	 * Returns how many attributes this GraphEdge has 
	 * @return  a number of attributes that this GraphEdge has
	 */
	public int getNoOfAttributesEdge(){
		return attributeList.size();
	}
	
	/**
	 * Returns an attribute list of this GraphEdge
	 * @return an attribute list of this GraphEdge as a hashmap type
	 */
	public HashMap<String, Object> getAttributeListEdge(){
		return attributeList;
	}

	/**
	 * Output description this GraphEdge in string type.
	 */
	@Override
	public String toString() {
		return "GraphEdge [fromNode= "+fromNode+" ,toNode=" + toNode + ", attributeList=" + attributeList + "]";
	}
	
	

}
