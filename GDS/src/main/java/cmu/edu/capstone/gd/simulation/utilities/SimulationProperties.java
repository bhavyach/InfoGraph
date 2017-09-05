package cmu.edu.capstone.gd.simulation.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import cmu.edu.capstone.gd.simulation.exception.SimulationExecutionException;
import cmu.edu.capstone.gd.simulation.execution.CommandLineExecutor;

public class SimulationProperties {
	final static Logger logger = Logger.getLogger(SimulationProperties.class);
	static Properties properties;

	static {
		try {

			if (CommandLineExecutor.getConfigFile() == null) {
				logger.error("Unable to fetch Configuration File. Please provide Correct Configuration File");
				properties = null;
			} else {
				logger.info("Configuration File :" + CommandLineExecutor.getConfigFile() + " fetched.");

				File file = new File(CommandLineExecutor.getConfigFile());
				FileInputStream fileInput = new FileInputStream(file);
				properties = new Properties();
				properties.load(fileInput);
				fileInput.close();

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String[] NODE_ATTRIBUTES = properties==null?null:properties.getProperty("node_attributes").split("\\s*,\\s*");
	public static String[] EDGE_ATTRIBUTES = properties==null?null:properties.getProperty("edge_attributes").split("\\s*,\\s*");
	public static String SIMULATION_RUN_COUNT = properties==null?null:properties.getProperty("simulation_run_count");
	public static String SOURCE_NODES = properties==null?null:properties.getProperty("source_nodes");
	public static String TRAVERSAL_ALGORITHM = properties==null?null:properties.getProperty("traversal_algorithm");
	public static String INPUT_FILE_NAME = properties==null?null:properties.getProperty("input_file_name");

}
