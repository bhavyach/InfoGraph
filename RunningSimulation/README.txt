This Jar contains Code to perform simulation.

In Order to run any simulation we need to use following command:
    > java -jar GDSimulation.jar -I [inputFilePath which should be a graphml file] -C [path for config.properties file]

Inside config.properties file we need to specify desired parameters required for Simulation run:

    source_Nodes = [starting nodes for the simulation run to be specified with comma separated values]
      If not specified we take 0th node as DEFAULT start node.


    simulation_Rounds = [Number of runs to happen in a simulation]
      If not specified we take 1000 as number of DEFAULT runs to be done.


    traversal_Algorithm = [currently we are having bfs support only so just type 'bsf']
      If not specified we take BFS as our DEFAULT algorithm.
