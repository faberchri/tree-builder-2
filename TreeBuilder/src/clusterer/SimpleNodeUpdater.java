package clusterer;

public class SimpleNodeUpdater implements NodeUpdater {

	@Override
	public void updateNodes(Node newNode) {
		for (Node attNode : newNode.getAttributeKeys()) {
			attNode.addAttribute(newNode, newNode.getAttributeValue(attNode));
		}
	}

}
