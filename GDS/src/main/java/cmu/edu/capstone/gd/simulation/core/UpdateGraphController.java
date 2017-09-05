package cmu.edu.capstone.gd.simulation.core;

import cmu.edu.capstone.gd.simulation.objects.Graph;

/**
 * (Optional) If not implemented the simulation will not update the graph at the
 * end of each round or run (runs are collection of rounds)
 * 
 * User can choose to update the graph object at the end of each round or each
 * run. Default Implementation:
 * 
 * @see cmu.edu.capstone.gd.simulation.core.impl.
 *      DefaultUpdateGraphControllerImpl
 * 
 *      e.g. For epidemic diseases like flu, we might want to update the graph
 *      after each round, because as soon as the second person catches flu, the
 *      third person might be clever enough to get a vaccine, which will prevent
 *      him/her from receiving the virus from the second person.
 * 
 * @author Saksham Gangwar
 * @version 1.0.0
 *
 */
public interface UpdateGraphController {

	/**
	 * This method updates the graph object after each round It is called each
	 * time after the "doesTraversalHappen" method in RoundTraversalController
	 * is called It will update all the nodes and edges in the graph as
	 * specified by the user.
	 * 
	 * Default implementation:
	 * 
	 * @see cmu.edu.capstone.gd.simulation.core.impl.DefaultUpdateGraphControllerImpl#updateGraphForRound(Graph)
	 * 
	 * 
	 * 
	 * @param graph
	 *            A graph object
	 * @return boolean If the graph is updated, return true
	 */
	public boolean updateGraphForRound(Graph graph);

	/**
	 * This method updates the graph object after each run It is called each
	 * time at the end of a run of simulation It will update all the nodes and
	 * edges in the graph.
	 * 
	 * @see cmu.edu.capstone.gd.simulation.core.impl.DefaultUpdateGraphControllerImpl#updateGraphForRun(Graph)
	 * 
	 * @param graph
	 *            A graph object
	 * @return boolean If the graph is updated, return true
	 */
	public boolean updateGraphForRun(Graph graph);

}
