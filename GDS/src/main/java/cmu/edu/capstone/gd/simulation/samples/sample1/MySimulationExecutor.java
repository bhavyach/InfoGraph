package cmu.edu.capstone.gd.simulation.samples.sample1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import cmu.edu.capstone.gd.simulation.core.TraversalAlgorithm;
import cmu.edu.capstone.gd.simulation.core.impl.DFSAlgorithmImpl;
import cmu.edu.capstone.gd.simulation.exception.SimulationExecutionException;
import cmu.edu.capstone.gd.simulation.objects.Graph;
import cmu.edu.capstone.gd.simulation.parser.GraphToGraphML;
import cmu.edu.capstone.gd.simulation.runner.SimulationRunner;
import cmu.edu.capstone.gd.simulation.utilities.NormalizationUtility;

public class MySimulationExecutor {

	public static void main(String[] args) throws SimulationExecutionException {

		/**
		 * STEP 1: Provide input file name.
		 */
		String inputFileName = "simplemodel.graphml";

		/**
		 * STEP 2: Create Graph object. Specify attributes to be used and
		 * attributes to be normalized.
		 */

		String[] nodeAttributesToBeUsed = { "id", "messages_received" };
		String[] edgeAttributesToBeUsed = { "weight" };

		Graph inputGraph = SimulationRunner.fetchInputGraph(inputFileName, nodeAttributesToBeUsed,
				edgeAttributesToBeUsed);

		/**
		 * Normalize required.
		 */
		String[] nodeAttributesToBeNormalized = null;
		String[] edgeAttributesToBeNormalized = { "weight" };
		NormalizationUtility.normalizeAttributeValues(inputGraph, nodeAttributesToBeNormalized,
				edgeAttributesToBeNormalized);

		/**
		 * STEP 3: Defining start nodes
		 */

		List<Integer> startNodes = new ArrayList<Integer>();
		List<String> messages = new ArrayList<String>();
		messages.add("This is a message.");
		inputGraph.getNodeFromGraph(12).addAttribute("messages_received", messages);
		inputGraph.getNodeFromGraph(60).addAttribute("messages_received", messages);
		startNodes.add(12);
		startNodes.add(60);

		/**
		 * STEP 4: Number of Simulation Runs
		 */
		int numberOfSimulationRuns = 1000;

		/**
		 * STEP 5: Getting desired Controller Instances.
		 */

		MyRoundTraversalController roundTraversalController = new MyRoundTraversalController();

		/**
		 * STEP 6: Get Algorithm Instance.
		 */
		TraversalAlgorithm traversalAlgorithm = new DFSAlgorithmImpl();

		/**
		 * STEP 7: Do Simulation Execution.
		 */
		try {
			SimulationRunner.runSimulationWithoutConfigFile(inputGraph, startNodes, numberOfSimulationRuns,
					roundTraversalController, null, null, traversalAlgorithm, null);
		} catch (SimulationExecutionException e) {

			e.printStackTrace();
		}

		/**
		 * STEP 8: Save Result in OUTPUT file.
		 */
		String[] nodeAttributesToBeInOutput = { "id", "messages_received" };
		String[] edgeAttributesToBeInOutput = { "weight" };
		try {
			GraphToGraphML.convertor(inputGraph, inputFileName, nodeAttributesToBeInOutput, edgeAttributesToBeInOutput);
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
