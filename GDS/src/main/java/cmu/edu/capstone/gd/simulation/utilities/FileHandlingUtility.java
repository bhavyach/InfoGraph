package cmu.edu.capstone.gd.simulation.utilities;

import java.io.File;

import org.apache.log4j.Logger;

public class FileHandlingUtility {
	final static Logger logger = Logger.getLogger(FileHandlingUtility.class);

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

}
