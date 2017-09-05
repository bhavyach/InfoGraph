package cmu.edu.capstone.gd.simulation.core;

import java.util.List;

import cmu.edu.capstone.gd.simulation.exception.SimulationExecutionException;
import cmu.edu.capstone.gd.simulation.objects.Graph;
import cmu.edu.capstone.gd.simulation.objects.GraphNode;

/**
 * (Optional)
 * 
 * This Interface is having method which can been used to get a list of start
 * nodes for the simulation run. The way to get these start nodes can be
 * dependent on what end user desires for.
 * 
 * Default way of providing start nodes is: Using config.properties file. If
 * start nodes neither provided in config.properties nor specified in the
 * following method, simulation execution will throw error to provide start
 * nodes via any possible way.
 *
 * Default implementation:
 * 
 * @see cmu.edu.capstone.gd.simulation.core.impl.
 *      DefaultSimulationStarterControllerImpl
 * 
 * @author Saksham Gangwar
 * @version 1.0.0
 *
 */
public interface SimulationStarterController {
	/**
	 * Method to get start nodes for the simulation based on any user defined
	 * condition. e.g. randomly generate 10% of the generation as starting
	 * nodes, or all the nodes aged between 22-24 as starting nodes
	 * 
	 * Default implementation:
	 * 
	 * @see cmu.edu.capstone.gd.simulation.core.impl.DefaultSimulationStarterControllerImpl#getStartNodes(Graph)
	 * 
	 * @param graph
	 *            a Graph object
	 * @return List<GraphNode> list of start nodes.
	 * @throws SimulationExecutionException
	 */
	public List<Integer> getStartNodes(Graph graph) throws SimulationExecutionException;

}
