package cmu.edu.capstone.gd.simulation.core.impl;

import cmu.edu.capstone.gd.simulation.core.RunTerminationController;
import cmu.edu.capstone.gd.simulation.objects.Graph;

public class DefaultRunTerminationControllerImpl implements RunTerminationController {

	public boolean doesRunTerminateOnSpecificCondition() {
		return false;
	}
	
	// for example: terminate the simulation run if more than 15% of the population is reached
	public boolean doesRunTerminateOnSpecificCondition(Graph graph) {
		// terminate run based on coverage
		if(graph.getCoverage()>0.15)
		return false;
		else
			return true;
	}
}
