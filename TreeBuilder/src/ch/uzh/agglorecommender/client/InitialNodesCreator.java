package ch.uzh.agglorecommender.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ch.uzh.agglorecommender.clusterer.treecomponent.ClassitTreeComponentFactory;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.Node;
import ch.uzh.agglorecommender.clusterer.treecomponent.TreeComponentFactory;

import com.google.common.collect.ImmutableMap;

/**
 * 
 * Parameter object that represents a passed data set in format processible
 * by the cluster and recommendation engines.
 *
 */
public class InitialNodesCreator {
	
	TreeComponentFactory metaTreeComponentFactory = ClassitTreeComponentFactory.getInstance();

	/**
	 * Map of user id as in data set to the corresponding node.
	 */
	private final ImmutableMap<Integer, INode> userLeavesMap;
	
	/**
	 * Map of content id as in data set to the corresponding node.
	 */
	private final ImmutableMap<Integer, INode> contentLeavesMap;
	
	/**
	 * Instantiates a new InitialNodesCreator object
	 * and creates the leaf nodes of the passed data set.
	 * This object is needed to start a new clustering process or
	 * as the input to the recommendation generation engine.
	 * 
	 * @param dataset the data set which shall be clustered (training) 
	 * or for which recommendations shall be generated (test).
	 * @param iDataset 
	 * @param iDataset 
	 * 
	 * @param contentTreeComponentFactory the initially specified factory
	 * @param userTreeComponentFactory the initially specified factory
	 */
	public InitialNodesCreator(IDataset<?> dataset,
			IDataset<?> contentMetaset, 
			IDataset<?> userMetaset, 
			TreeComponentFactory contentTreeComponentFactory,
			TreeComponentFactory userTreeComponentFactory) {

		// Create nominal nodes for users & content
		List<INode> userNominalNodes = createNominalNodes(userMetaset);
		List<INode> contentNominalNodes = createNominalNodes(contentMetaset);
		
		// Sort data items according to user id and content id
		Map<Integer, List<IDatasetItem<?>>> userDatasetItems = new HashMap<Integer, List<IDatasetItem<?>>>();
		Map<Integer, List<IDatasetItem<?>>> contentDatasetItems = new HashMap<Integer, List<IDatasetItem<?>>>();
		
		Iterator<?> it = dataset.iterateOverDatasetItems();
		while(it.hasNext()) {
			IDatasetItem<?> datasetItem = (IDatasetItem<?>) it.next();
			
			// Add every datasetItem to the user map
			if (userDatasetItems.containsKey(datasetItem.getUserId())) {
				userDatasetItems.get(datasetItem.getUserId()).add(datasetItem);
			} else {
				List<IDatasetItem<?>> li = new ArrayList<IDatasetItem<?>>();
				li.add(datasetItem);
				userDatasetItems.put(datasetItem.getUserId(), li);
			}
			
			// Add every datasetItem to the content map
			if (contentDatasetItems.containsKey(datasetItem.getContentId())) {
				contentDatasetItems.get(datasetItem.getContentId()).add(datasetItem);
			} else {
				List<IDatasetItem<?>> li = new ArrayList<IDatasetItem<?>>();
				li.add(datasetItem);
				contentDatasetItems.put(datasetItem.getContentId(), li);
			}
		}
		
		// Create Leaf Nodes for content and user
		Map<Integer, INode> userLeafNodes = createLeafNodes(userDatasetItems,userTreeComponentFactory,ENodeType.User,userMetaset);
		Map<Integer, INode> contentLeafNodes = createLeafNodes(contentDatasetItems,contentTreeComponentFactory,ENodeType.Content,contentMetaset);
		
		// Add attribute map to user nodes
		addAttributes(userDatasetItems,dataset,userLeafNodes,userNominalNodes,contentLeafNodes,contentTreeComponentFactory);
		addAttributes(contentDatasetItems,dataset,contentLeafNodes,contentNominalNodes,userLeafNodes,userTreeComponentFactory);
		
		userLeavesMap = ImmutableMap.copyOf(userLeafNodes);
		contentLeavesMap = ImmutableMap.copyOf(contentLeafNodes);
	}
	
	private void addAttributes(Map<Integer, List<IDatasetItem<?>>> datasetItems, 
			IDataset<?> dataset, 
			Map<Integer, INode> leafNodes, 
			List<INode> nominalNodes, 
			Map<Integer, INode> oppositeLeafNodes, 
			TreeComponentFactory oppositeTreeComponentFactory) {
		
		for (Map.Entry<Integer, List<IDatasetItem<?>>> user : datasetItems.entrySet()) {
			
			// Create numerical attributes
			Map<INode,IAttribute> numAttributes = buildNumericalAttributes(user, dataset, oppositeLeafNodes, oppositeTreeComponentFactory);
			leafNodes.get(user.getKey()).setNumericalAttributes(numAttributes);
			
			// Create nominal attributes
			Map<Object,IAttribute> nomAttributes = buildNominalAttributes(user, leafNodes, nominalNodes, oppositeTreeComponentFactory);
			leafNodes.get(user.getKey()).setNominalAttributes(nomAttributes);
		}
	}

	private Map<Object, IAttribute> buildNominalAttributes(
			Entry<Integer, List<IDatasetItem<?>>> datasetItems, 
			Map<Integer, INode> leafNodes, 
			List<INode> nominalNodes, 
			TreeComponentFactory oppositeTreeComponentFactory) {
		
		Map<Object, IAttribute> attributes = new HashMap<Object, IAttribute>();
		Map<Object, Object> metaData = leafNodes.get(datasetItems.getKey()).getMeta(); // Meta Info from User Node
		int i = 0;
		for(Object metaKey : metaData.keySet()){
			attributes.put(nominalNodes.get(i), 
					oppositeTreeComponentFactory.createNominalAttribute(1, metaKey, metaData.get(metaKey)));
			i++;
		}
		return attributes;
	}

	private Map<INode, IAttribute> buildNumericalAttributes(
			Entry<Integer, List<IDatasetItem<?>>> datasetItems, 
			IDataset<?> dataset, 
			Map<Integer, INode> oppositeLeafNodes, 
			TreeComponentFactory oppositeTreeComponentFactory) {
		
		Map<INode, IAttribute> attributes = new HashMap<INode, IAttribute>();
		for (IDatasetItem<?> contentItem : datasetItems.getValue()) {
			
			double normalizedRating = ((INormalizer<Number>) dataset.getNormalizer()).normalizeRating( (Number) contentItem.getValue());
			attributes.put(oppositeLeafNodes.get(contentItem.getContentId()), oppositeTreeComponentFactory.createNumericAttribute(normalizedRating));
		}
		return attributes;
	}

	private Map<Integer, INode> createLeafNodes(
			Map<Integer, List<IDatasetItem<?>>> datasetItems,
			TreeComponentFactory treeComponentFactory, ENodeType type,
			IDataset<?> metaset) {
	
		Map<Integer, INode> usersNodeMap = new HashMap<Integer, INode>();
		for (Integer datasetId : datasetItems.keySet()) {	
			usersNodeMap.put(datasetId, treeComponentFactory.createLeafNode(type, datasetId, metaset));
		}		
		
		return usersNodeMap;
	}

	private List<INode>  createNominalNodes(IDataset<?> metaset) {
		List<INode> nominalNodes = new LinkedList<INode>();
		Iterator<?> userIt = metaset.iterateOverDatasetItems();
		MetaDatasetItem userItem = (MetaDatasetItem) userIt.next();
		Map<String,String> userInfos = userItem.getValue();
		
		for(String infoKey : userInfos.keySet()){
			Map<String,String> attributeInfo = new HashMap<String,String>();
			attributeInfo.put(userInfos.get(infoKey),null);
			INode info = new Node(ENodeType.Nominal, 0, null);
			nominalNodes.add(info);	
		}
		return nominalNodes;
	}

	/**
	 * Gets the data set user id to leaf node map.
	 * 
	 * @return the immutable map of the user id as in the data set to the
	 * node in the cluster tree.
	 */
	public ImmutableMap<Integer, INode> getUserLeaves() {
		return userLeavesMap;
	}

	/**
	 * Gets the data set content id to leaf node map.
	 * 
	 * @return the immutable map of the content id as in the data set to the
	 * node in the cluster tree.
	 */
	public ImmutableMap<Integer, INode> getContentLeaves() {
		return contentLeavesMap;
	}
}
