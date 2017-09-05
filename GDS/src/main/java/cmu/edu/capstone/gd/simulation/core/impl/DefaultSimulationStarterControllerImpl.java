package cmu.edu.capstone.gd.simulation.core.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cmu.edu.capstone.gd.simulation.core.SimulationStarterController;
import cmu.edu.capstone.gd.simulation.exception.SimulationExecutionException;
import cmu.edu.capstone.gd.simulation.objects.Graph;
import cmu.edu.capstone.gd.simulation.utilities.SimulationProperties;

public class DefaultSimulationStarterControllerImpl implements SimulationStarterController {

	final static Logger logger = Logger.getLogger(DefaultSimulationStarterControllerImpl.class);

	public List<Integer> getStartNodes(Graph graph) throws SimulationExecutionException {
		List<Integer> startNodes = new ArrayList<Integer>();
		String[] startNodesInString = SimulationProperties.SOURCE_NODES.split(",");
		for (int i = 0; i < startNodesInString.length; i++) {
			Integer id = Integer.parseInt(startNodesInString[i]);
			startNodes.add(id);
			if (!graph.checkIfNodeExist(id)) {
				logger.error("Node with ID: " + id + " Does not exist in the Graph");
				logger.error("Please provide valid Start Nodes");
				throw new SimulationExecutionException("Unable to fetch Start Nodes");
			}
		}
		return startNodes;
	}

}
