package modules;

import java.util.Set;

import clusterer.INode;
import clusterer.INodeUpdater;

public class SimpleNodeUpdater implements INodeUpdater {

	@Override
	public void updateNodes(INode newNode, Set<INode> nodesToUpdate) {
		for (INode attNode : newNode.getAttributeKeys()) {
			attNode.addAttribute(newNode, newNode.getAttributeValue(attNode));
		}
	}

}
