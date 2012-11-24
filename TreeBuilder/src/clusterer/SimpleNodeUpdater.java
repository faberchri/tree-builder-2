package clusterer;

public class SimpleNodeUpdater implements INodeUpdater {

	@Override
	public void updateNodes(INode newNode) {
		for (INode attNode : newNode.getAttributeKeys()) {
			attNode.addAttribute(newNode, newNode.getAttributeValue(attNode));
		}
	}

}
