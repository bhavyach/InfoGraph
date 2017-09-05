package cmu.edu.capstone.gd.simulation.execution;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import cmu.edu.capstone.gd.simulation.core.impl.BFSAlgorithmImpl;
import cmu.edu.capstone.gd.simulation.core.impl.DFSAlgorithmImpl;
import cmu.edu.capstone.gd.simulation.core.impl.DefaultRoundTraversalControllerImpl;
import cmu.edu.capstone.gd.simulation.core.impl.DefaultRunTerminationControllerImpl;
import cmu.edu.capstone.gd.simulation.core.impl.DefaultSimulationStarterControllerImpl;
import cmu.edu.capstone.gd.simulation.core.impl.DefaultUpdateGraphControllerImpl;
import cmu.edu.capstone.gd.simulation.exception.SimulationExecutionException;
import cmu.edu.capstone.gd.simulation.objects.Graph;
import cmu.edu.capstone.gd.simulation.parser.GraphMLToGraph;
import cmu.edu.capstone.gd.simulation.parser.GraphToGraphML;
import cmu.edu.capstone.gd.simulation.runner.SimulationRunner;
import cmu.edu.capstone.gd.simulation.utilities.NormalizationUtility;
import cmu.edu.capstone.gd.simulation.utilities.SimulationProperties;

/**
 * 
 * Class which is exposed to the end user for the execution of the simulation.
 * There are multiple ways to run a simulation.
 * 
 * First way being specifying input file as a command line argument.
 * 
 * Usage: -I [input file path]
 * 
 * If we want to provide user defined config.properties file it can be provided
 * like:
 * 
 * Usage: -C [configuration properties file location]
 * 
 * @author Saksham Gangwar
 * 
 * @version 1.0.0
 *
 */
public class CommandLineExecutor {

	final static Logger logger = Logger.getLogger(CommandLineExecutor.class);

	static String inputFilePath = null;
	static String configFilePath = null;
	static int simulationRunCount = 0;
	static TraversalAlgorithm traversalAlgorithm = null;
	static Graph inputGraph = null;

	/**
	 * Simulation controller: RoundTraversalController and
	 * SimulationStarterController are mandatory, other 3 controllers are
	 * optional
	 */
	/**
	 * [[[[[Mandatory]]]]] RoundTraversalController.
	 */
	static RoundTraversalController roundTraversalController = new DefaultRoundTraversalControllerImpl();
	/**
	 * [[[[[Mandatory]]]]] SimulationStarterController.
	 */
	static SimulationStarterController simulationStarterController = new DefaultSimulationStarterControllerImpl();
	/**
	 * Optional UpdateGraphController.
	 */
	static UpdateGraphController updateGraphController = new DefaultUpdateGraphControllerImpl();
	/**
	 * Optional RunTerminationController.
	 */
	static RunTerminationController runTerminationController = new DefaultRunTerminationControllerImpl();

	public static void main(String[] args) throws SimulationExecutionException {

		configFilePath = getConfigurationFilePath(args);

		inputFilePath = getInputFilePath(args);

		simulationRunCount = getTotalSimulationRunCount();

		traversalAlgorithm = getTraversalAlgorithm();

		traversalAlgorithm.setControllers(simulationStarterController, roundTraversalController,
				runTerminationController, updateGraphController);

		inputGraph = fetchInputGraph();

		NormalizationUtility.normalizeAttributeValues(inputGraph, null, new String[] { "weight" });

		SimulationRunner.runSimulationUsingConfigFile(inputGraph, traversalAlgorithm, simulationRunCount,
				updateGraphController);

		generateOutputGraphMLFile();

	}

	/**
	 * Checking for the existence of any file.
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean checkIfFileExist(String fileName) {
		File f = new File(fileName);
		if (f.exists() && !f.isDirectory()) {
			logger.info("File: " + fileName + " exists");
			return true;
		}
		logger.error("File: " + fileName + " does not exist");
		return false;
	}

	/**
	 * Method to get configuration File path
	 * 
	 * @return
	 */
	public static String getConfigFile() {
		return configFilePath;
	}

	public static String getConfigurationFilePath(String[] args) throws SimulationExecutionException {
		String configFilePath = null;
		/**
		 * Checking for the existence of Configuration File path in the command
		 * line arguments otherwise setting to default config.properties.
		 */
		if ((args[0].equals("-C") && args.length >= 2) || (args[2].equals("-C") && args.length >= 4)) {
			if (args[0].equals("-C") && args.length >= 2)
				configFilePath = args[1];
			if (args[2].equals("-C") && args.length >= 4)
				configFilePath = args[3];
		} else {
			configFilePath = "config.properties";
		}
		/**
		 * Checking if the configuration file exists or not.
		 */
		if (!checkIfFileExist(configFilePath)) {
			logger.error("Configuration File " + configFilePath + "does not exist");
			throw new SimulationExecutionException("Configuration file Missing");
		} else {
			logger.info("Configuration file found: " + configFilePath);
		}

		return configFilePath;
	}

	public static String getInputFilePath(String[] args) throws SimulationExecutionException {
		String inputFilePath = null;
		/**
		 * Checking for the Input file Existence. So there are two ways to
		 * provide input file. Once way is to provide it as a command line
		 * argument:
		 * 
		 * -I [input file path]
		 * 
		 * or we can provide it in config.properties file like:
		 * 
		 * input_file_name=simplemodel.graphml
		 * 
		 */
		if ((args[0].equals("-I") && args.length >= 2) || (args[2].equals("-I") && args.length >= 4)) {
			if (args[0].equals("-I") && args.length >= 2)
				inputFilePath = args[1];
			else if (args[2].equals("-I") && args.length >= 4)
				inputFilePath = args[3];
		} else if (SimulationProperties.INPUT_FILE_NAME != null) {
			inputFilePath = SimulationProperties.INPUT_FILE_NAME;
		} else {
			logger.error("Please Provide Argument with -I for Input file (In GraphML format)");
			throw new SimulationExecutionException("Unable to fetch input file");
		}

		/**
		 * Checking if the input file exists or not.
		 */
		if (!checkIfFileExist(inputFilePath)) {
			logger.error("Input File " + inputFilePath + "does not exist");
			throw new SimulationExecutionException("Input file does not exist");
		} else {
			logger.info("Input file found: " + inputFilePath);
		}

		return inputFilePath;
	}

	public static int getTotalSimulationRunCount() throws SimulationExecutionException {
		if (SimulationProperties.SIMULATION_RUN_COUNT != null
				&& SimulationProperties.SIMULATION_RUN_COUNT.length() != 0) {
			logger.info("Simulation run count fetched: " + SimulationProperties.SIMULATION_RUN_COUNT);
			return Integer.parseInt(SimulationProperties.SIMULATION_RUN_COUNT);
		} else {
			logger.error("Unable to get Total Simulation Run count from the Configuration file");
			logger.error("Make sure the configuration file is having: simulation_run_count = N");
			throw new SimulationExecutionException("Simulation Run Count Missing");
		}
	}

	public static TraversalAlgorithm getTraversalAlgorithm() {

		String algo = null;

		if (SimulationProperties.TRAVERSAL_ALGORITHM != null
				&& SimulationProperties.TRAVERSAL_ALGORITHM.length() != 0) {
			algo = SimulationProperties.TRAVERSAL_ALGORITHM;

		} else {
			logger.error("Unable to fetch desired algorithm to be used for execution");
			logger.error("Please provide in configuration file like: traversal_algorithm=dfs");
			logger.error(
					"Also make sure for any new implementation of Algorithm class, the call is made in CommandLineExecutor");
		}

		if (algo.equals("dfs")) {
			// if any of the above are not implemented, just pass null
			// null not allowed for roundTraversalController
			TraversalAlgorithm traversalAlgorithm = new DFSAlgorithmImpl();

			return traversalAlgorithm;
		}

		return null;
	}

	public static Graph fetchInputGraph() throws SimulationExecutionException {

		Graph inputGraph = GraphMLToGraph.convertGraphMLToGraph(inputFilePath, null, null);

		if (inputGraph == null) {
			logger.error("Input Graph is found Null");
			throw new SimulationExecutionException("Input Graph found null");
		}
		return inputGraph;
	}

	@SuppressWarnings("static-access")
	public static void generateOutputGraphMLFile() {
		GraphToGraphML graphToGraphML = new GraphToGraphML();

		try {
			graphToGraphML.convertor(inputGraph, inputFilePath, SimulationProperties.NODE_ATTRIBUTES,
					SimulationProperties.EDGE_ATTRIBUTES);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

}
