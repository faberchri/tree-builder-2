package ch.uzh.agglorecommender.clusterer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ch.uzh.agglorecommender.client.IDataset;
import ch.uzh.agglorecommender.client.IDatasetItem;
import ch.uzh.agglorecommender.client.INormalizer;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.TreeComponentFactory;

import com.google.common.collect.ImmutableMap;

/**
 * 
 * Parameter object that represents a passed data set in format processible
 * by the cluster and recommendation engines.
 *
 */
public class InitialNodesCreator {

	/**
	 * Map of user id as in data set to the corresponding node.
	 */
	private final ImmutableMap<String, INode> userLeavesMap;
	
	/**
	 * Map of content id as in data set to the corresponding node.
	 */
	private final ImmutableMap<String, INode> contentLeavesMap;
	
	/**
	 * Instantiates a new InitialNodesCreator object
	 * and creates the leaf nodes of the passed data set.
	 * This object is needed to start a new clustering process or
	 * as the input to the recommendation generation engine.
	 * 
	 * @param dataset the data set which shall be clustered (training) 
	 * or for which recommendations shall be generated (test).
	 * 
	 * @param treeComponentFactory the factory used to instantiate tree elements
	 */
	public InitialNodesCreator(IDataset dataset,
			TreeComponentFactory treeComponentFactory) {
		
		Map<String, List<IDatasetItem>> usersMap = new HashMap<String, List<IDatasetItem>>();
		Map<String, List<IDatasetItem>> contentsMap = new HashMap<String, List<IDatasetItem>>();
		
		// sort data items according to user id and content id
		Iterator<?> it = dataset.iterateOverDatasetItems();
		while(it.hasNext()) {
			IDatasetItem datasetItem = (IDatasetItem) it.next();
			if (usersMap.containsKey(datasetItem.getUserId())) {
				usersMap.get(datasetItem.getUserId()).add(datasetItem);
			} else {
				List<IDatasetItem> li = new ArrayList<IDatasetItem>();
				li.add(datasetItem);
				usersMap.put(datasetItem.getUserId(), li);
			}
			if (contentsMap.containsKey(datasetItem.getContentId())) {
				contentsMap.get(datasetItem.getContentId()).add(datasetItem);
			} else {
				List<IDatasetItem> li = new ArrayList<IDatasetItem>();
				li.add(datasetItem);
				contentsMap.put(datasetItem.getContentId(), li);
			}
		}
		
		// create for each user and content id one node
		Map<String, INode> usersNodeMap = new HashMap<String, INode>();
		for (String i : usersMap.keySet()) {
			usersNodeMap.put(i,
					treeComponentFactory.createLeafNode(
							ENodeType.User, i,
							usersMap.get(i).get(0).getNominalUserMetaMap(),
							usersMap.get(i).get(0).getNumericalUserMetaMap(),
							dataset));
		}		
		Map<String, INode> contentsNodeMap = new HashMap<String, INode>();
		for (String i : contentsMap.keySet()) {
			contentsNodeMap.put(i,
					treeComponentFactory.createLeafNode(
							ENodeType.Content, i,
							contentsMap.get(i).get(0).getNominalContentMetaMap(),
							contentsMap.get(i).get(0).getNumericalContentMetaMap(),
							dataset));
		}
		
		// attach to each node its attribute maps
		for (Map.Entry<String, List<IDatasetItem>> entry : usersMap.entrySet()) {
			Map<INode, IAttribute> numAttributes = new HashMap<INode, IAttribute>();
			for (IDatasetItem di : entry.getValue()) {
				double normalizedRating = ((INormalizer) dataset.getNormalizer()).normalizeRating(di.getRating());
				numAttributes.put(contentsNodeMap.get(di.getContentId()), treeComponentFactory.createNumericalLeafAttribute(normalizedRating));
			}
			usersNodeMap.get(entry.getKey()).setRatingAttributes(numAttributes);
		}
		for (Map.Entry<String, List<IDatasetItem>> entry : contentsMap.entrySet()) {
			Map<INode, IAttribute> attributes = new HashMap<INode, IAttribute>();
			for (IDatasetItem di : entry.getValue()) {			
				double normalizedRating = ((INormalizer) dataset.getNormalizer()).normalizeRating(di.getRating());
				attributes.put(usersNodeMap.get(di.getUserId()), treeComponentFactory.createNumericalLeafAttribute(normalizedRating));
			}
			contentsNodeMap.get(entry.getKey()).setRatingAttributes(attributes);
		}
		
		userLeavesMap = ImmutableMap.copyOf(usersNodeMap);
		contentLeavesMap = ImmutableMap.copyOf(contentsNodeMap);
	}
	
	/**
	 * Gets the data set user id to leaf node map.
	 * 
	 * @return the immutable map of the user id as in the data set to the
	 * node in the cluster tree.
	 */
	public ImmutableMap<String, INode> getUserLeaves() {
		return userLeavesMap;
	}

	/**
	 * Gets the data set content id to leaf node map.
	 * 
	 * @return the immutable map of the content id as in the data set to the
	 * node in the cluster tree.
	 */
	public ImmutableMap<String, INode> getContentLeaves() {
		return contentLeavesMap;
	}
}