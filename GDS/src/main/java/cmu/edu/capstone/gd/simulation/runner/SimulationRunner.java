package cmu.edu.capstone.gd.simulation.runner;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import cmu.edu.capstone.gd.simulation.core.RoundTraversalController;
import cmu.edu.capstone.gd.simulation.core.RunTerminationController;
import cmu.edu.capstone.gd.simulation.core.SimulationStarterController;
import cmu.edu.capstone.gd.simulation.core.TraversalAlgorithm;
import cmu.edu.capstone.gd.simulation.core.UpdateGraphController;
import cmu.edu.capstone.gd.simulation.exception.SimulationExecutionException;
import cmu.edu.capstone.gd.simulation.objects.Graph;
import cmu.edu.capstone.gd.simulation.parser.GraphMLToGraph;
import cmu.edu.capstone.gd.simulation.parser.GraphToGraphML;
import cmu.edu.capstone.gd.simulation.utilities.FileHandlingUtility;

/**
 * The runner for simulation Contains two running options with different input
 * formats: 1. Input as a Graph object 2. Input directly as input file path.
 * 
 * @author Saksham Gangwar
 * @version 1.0.0
 *
 */
public class SimulationRunner {
	final static Logger logger = Logger.getLogger(SimulationRunner.class);

	/**
	 * No instantiation allowed
	 */
	private SimulationRunner() {
	}

	/**
	 * 
	 * @param inputGraph
	 * @param algo
	 * @param noOfSimulations
	 * @param updateGraphController
	 * @throws SimulationExecutionException
	 */
	public static void runSimulationUsingConfigFile(Graph inputGraph, TraversalAlgorithm algo, int noOfSimulations,
			UpdateGraphController updateGraphController) throws SimulationExecutionException {
		if (inputGraph == null) {
			logger.error(
					"Input Graph is found null while trying to run simulation. Please provide Input file correctly.");
			logger.error("Exiting Execution !");
			throw new SimulationExecutionException("Input file not found");
		}

		if (algo == null) {
			logger.error(
					"Traversal Algorithm is found null while trying to run simulation. Please provide Algorithm Object Reference correctly.");
			logger.error("Exiting Execution !");
			throw new SimulationExecutionException("Traversal Algorithm Instance Not found");
		}

		if (noOfSimulations <= 0) {
			logger.error("Please provide Valid Simulation Runs Count");
			logger.error("Exiting Execution !");
			throw new SimulationExecutionException("Incorrect Simulation Run Count");
		}
		if (updateGraphController == null) {
			logger.info("UpdateGraphController is passed as null to Simulation Runner");
		}

		for (int i = 0; i < noOfSimulations; i++) {
			algo.doGraphTraversal(inputGraph, null);
			if (updateGraphController != null) {
				updateGraphController.updateGraphForRun(inputGraph);
			}
		}

		inputGraph.printGraph();

	}

	public static void runSimulationWithoutConfigFile(Graph graph, List<Integer> startNodes, int numberOfSimulationRuns,
			RoundTraversalController roundTraversalController, RunTerminationController runTerminationController,
			SimulationStarterController simulationStarterController, TraversalAlgorithm traversalAlgorithm,
			UpdateGraphController updateGraphController) throws SimulationExecutionException {

		/**
		 * STEP 1: Checking if mandatory controllers exists or not.
		 */
		if (roundTraversalController == null) {
			logger.error("Round Traversal Controller Instance Not found");
			throw new SimulationExecutionException("Round Traversal Controller Instance Not found");
		}

		/**
		 * STEP 2: Checking if start nodes provided or Simulation Starter
		 * Controller to be used.
		 */
		if ((startNodes == null || startNodes.size() == 0)
				&& (simulationStarterController == null || simulationStarterController.getStartNodes(graph) == null
						|| simulationStarterController.getStartNodes(graph).size() == 0)) {
			logger.error("Start Nodes not found");
			throw new SimulationExecutionException("Start Nodes not found");

		}

		/**
		 * STEP 3: Getting the instance of traversal algorithm.
		 */
		if (traversalAlgorithm == null) {
			logger.error("Traversal Algorithm Instance Not found");
			throw new SimulationExecutionException("Traversal Algorithm Instance Not found");
		}

		/**
		 * SETP 4: Setting all the required controllers in the Algorithm Object.
		 */
		traversalAlgorithm.setControllers(simulationStarterController, roundTraversalController,
				runTerminationController, updateGraphController);

		/**
		 * STEP 5: Checking for valid number of simulation runs
		 */
		if (numberOfSimulationRuns <= 0) {
			logger.error("Please provide Valid Simulation Runs Count");
			logger.error("Exiting Execution !");
			throw new SimulationExecutionException("Incorrect Simulation Run Count");
		}

		/**
		 * STEP 6: Simulation Execution Main Step
		 */
		for (int i = 0; i < numberOfSimulationRuns; i++) {
			traversalAlgorithm.doGraphTraversal(graph, startNodes);
			if (updateGraphController != null) {
				updateGraphController.updateGraphForRun(graph);
			}
		}

		/**
		 * STEP 7: Print the final graph
		 */
		graph.printGraph();
		
		logger.info("Simulation Ended");

	}

	public static Graph fetchInputGraph(String inputFilePath, String[] nodeAtributesToBeUsed,
			String[] edgeAtributesToBeUsed) throws SimulationExecutionException {

		FileHandlingUtility.checkIfFileExist(inputFilePath);

		Graph inputGraph = GraphMLToGraph.convertGraphMLToGraph(inputFilePath, nodeAtributesToBeUsed,
				edgeAtributesToBeUsed);

		if (inputGraph == null) {
			logger.error("Input Graph is found Null");
			throw new SimulationExecutionException("Input Graph is found Null");
		}
		return inputGraph;
	}

}
