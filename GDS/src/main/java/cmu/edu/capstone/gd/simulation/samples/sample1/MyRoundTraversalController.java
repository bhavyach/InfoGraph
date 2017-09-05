package cmu.edu.capstone.gd.simulation.samples.sample1;

import java.util.ArrayList;
import java.util.List;

import cmu.edu.capstone.gd.simulation.core.RoundTraversalController;
import cmu.edu.capstone.gd.simulation.objects.Graph;
import cmu.edu.capstone.gd.simulation.objects.GraphEdge;
import cmu.edu.capstone.gd.simulation.objects.GraphNode;

public class MyRoundTraversalController implements RoundTraversalController {

	public String[] edgeAttributeList = { "weight" };
	public String[] nodeAttributeList = { "messages_received" };

	public boolean doesTraversalHappen(GraphNode from, GraphNode to, GraphEdge edge) {
		double random = Math.random();
		if ((Double) edge.getAttribute(edgeAttributeList[0]) > random) {
			if (from.getAttribute(nodeAttributeList[0]) != null) {
				@SuppressWarnings("unchecked")
				List<String> messagesReceivedOnFromNode = (List<String>) from.getAttribute(nodeAttributeList[0]);
				String getLatestMessageOnFromNode = messagesReceivedOnFromNode
						.get(messagesReceivedOnFromNode.size() - 1);
				updateNode(null, to, corruptMessage(getLatestMessageOnFromNode));
			}
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public void updateNode(Graph graph, GraphNode node, Object newContent) {
		List<String> messagesReceived = null;
		if (node.getAttribute(nodeAttributeList[0]) != null) {
			messagesReceived = (List<String>) node.getAttribute(nodeAttributeList[0]);
		} else {
			messagesReceived = new ArrayList<String>();
		}

		if (newContent != null) {
			messagesReceived.add((String) newContent);
		}
		node.addAttribute(nodeAttributeList[0], messagesReceived);

	}

	public void updateEdge(Graph graph, GraphEdge edge, Object newContent) {
		// TODO Auto-generated method stub

	}

	private String corruptMessage(String messageToBeCorrupted) {
		double random = Math.random() * 10;
		for (int i = 0; i < random; i++) {
			messageToBeCorrupted = messageToBeCorrupted + 'x';
		}

		return messageToBeCorrupted;

	}

}
