package ch.uzh.agglorecommender.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
		
		Map<Integer, List<IDatasetItem<?>>> usersMap = new HashMap<Integer, List<IDatasetItem<?>>>();
		Map<Integer, List<IDatasetItem<?>>> contentsMap = new HashMap<Integer, List<IDatasetItem<?>>>();
		
		// Sort data items according to user id and content id
		Iterator<?> it = dataset.iterateOverDatasetItems();
		while(it.hasNext()) {
			IDatasetItem<?> datasetItem = (IDatasetItem<?>) it.next();
			
			// Add every datasetItem to the user node it belongs to
			if (usersMap.containsKey(datasetItem.getUserId())) {
				usersMap.get(datasetItem.getUserId()).add(datasetItem);
			} else {
				List<IDatasetItem<?>> li = new ArrayList<IDatasetItem<?>>();
				li.add(datasetItem);
				usersMap.put(datasetItem.getUserId(), li);
			}
			
			// Add every datasetItem to the content node it belongs to
			if (contentsMap.containsKey(datasetItem.getContentId())) {
				contentsMap.get(datasetItem.getContentId()).add(datasetItem);
			} else {
				List<IDatasetItem<?>> li = new ArrayList<IDatasetItem<?>>();
				li.add(datasetItem);
				contentsMap.put(datasetItem.getContentId(), li);
			}
			
		}
		
		// create for each user and content id one node
		Map<Integer, INode> usersNodeMap = new HashMap<Integer, INode>();
		for (Integer datasetID : usersMap.keySet()) {
			
			// ----- work -----------------
			// Add to every node its corresponding metadata
//			System.out.println("getting user info");
			List<String> metaData = findMetaData(datasetID,userMetaset); // FIXME MŸsste bei den Attributen sein 
//			if(metaData != null){
//				System.out.println(metaData.toString());
//			}
			// ----- work -----------------
			
			usersNodeMap.put(datasetID, userTreeComponentFactory.createLeafNode(ENodeType.User, datasetID, metaData));
		}		
		
		Map<Integer, INode> contentsNodeMap = new HashMap<Integer, INode>();
		for (Integer i : contentsMap.keySet()) {
			
			// ----- work -----------------
			// Add to every node its corresponding metadata
//			System.out.println("getting content info");
			List<String> metaData = findMetaData(i,contentMetaset); // FIXME MŸsste bei den Attributen sein 
//			if(metaData != null){
//				System.out.println(metaData.toString());
//			}
			// ----- work -----------------
			
			contentsNodeMap.put(i, contentTreeComponentFactory.createLeafNode(ENodeType.Content, i, metaData));
		}
		// ----- work -----------------
		// attach to each node its attributes map
		for (Map.Entry<Integer, List<IDatasetItem<?>>> entry : usersMap.entrySet()) {
			Map<INode, IAttribute> attributes = new HashMap<INode, IAttribute>();
			for (IDatasetItem<?> di : entry.getValue()) {
				
				List<String> metaData = findMetaData(di.getUserId(),userMetaset); // WORK
				
				double normalizedRating = ((INormalizer<Number>) dataset.getNormalizer()).normalizeRating( (Number) di.getValue());
				attributes.put(contentsNodeMap.get(di.getContentId()), contentTreeComponentFactory.createAttribute(normalizedRating,metaData)); // WORK
			}
			usersNodeMap.get(entry.getKey()).setAttributes(attributes);
//			userNodes.add(usersNodeMap.get(entry.getKey()));
		}
		for (Map.Entry<Integer, List<IDatasetItem<?>>> entry : contentsMap.entrySet()) {
			Map<INode, IAttribute> attributes = new HashMap<INode, IAttribute>();
			for (IDatasetItem<?> di : entry.getValue()) {			
				
				List<String> metaData = findMetaData(di.getContentId(),contentMetaset); // WORK
				
				double normalizedRating = ((INormalizer<Number>) dataset.getNormalizer()).normalizeRating( (Number) di.getValue());
				attributes.put(usersNodeMap.get(di.getUserId()), userTreeComponentFactory.createAttribute(normalizedRating,metaData)); // WORK
			}
			contentsNodeMap.get(entry.getKey()).setAttributes(attributes);
//			contentNodes.add(contentsNodeMap.get(entry.getKey()));
		}
		// ----- work -----------------
		userLeavesMap = ImmutableMap.copyOf(usersNodeMap);
		contentLeavesMap = ImmutableMap.copyOf(contentsNodeMap);
	}
	
	private List<String> findMetaData(Integer i, IDataset<?> metaset) {
		Iterator<?> it = metaset.iterateOverDatasetItems();
		while(it.hasNext()){
			MetaDatasetItem metadata = (MetaDatasetItem) it.next();

			if(metadata.getContentId() == i){ // FIXME Typ Unterscheidung fehlt
//				System.out.println("Found match");
//				System.out.println(metadata.getValue().toString());
				return (List<String>) metadata.getValue();
			}
		}
		return null;
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
