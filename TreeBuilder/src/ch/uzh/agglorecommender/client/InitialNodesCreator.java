package ch.uzh.agglorecommender.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
		
		Map<Integer, List<IDatasetItem<?>>> usersMap = new HashMap<Integer, List<IDatasetItem<?>>>();
		Map<Integer, List<IDatasetItem<?>>> contentsMap = new HashMap<Integer, List<IDatasetItem<?>>>();
		
		// Sort data items according to user id and content id
		Iterator<?> it = dataset.iterateOverDatasetItems();
		while(it.hasNext()) {
			IDatasetItem<?> datasetItem = (IDatasetItem<?>) it.next();
			
			// Add every datasetItem to the user map
			if (usersMap.containsKey(datasetItem.getUserId())) {
				usersMap.get(datasetItem.getUserId()).add(datasetItem);
			} else {
				List<IDatasetItem<?>> li = new ArrayList<IDatasetItem<?>>();
				li.add(datasetItem);
				usersMap.put(datasetItem.getUserId(), li);
			}
			
			// Add every datasetItem to the content map
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
		for (Integer datasetId : usersMap.keySet()) {
			
			// Find corresponding metadata to user node
			Map<String,String> metaData = findMetaData(datasetId,userMetaset);
			
			// Create User node
			usersNodeMap.put(datasetId, userTreeComponentFactory.createLeafNode(ENodeType.User, datasetId, metaData));
		}		
		
		Map<Integer, INode> contentsNodeMap = new HashMap<Integer, INode>();
		for (Integer i : contentsMap.keySet()) {
			
			// Add to every node its corresponding metadata
			Map<String,String> metaData = findMetaData(i,contentMetaset);
			
			contentsNodeMap.put(i, contentTreeComponentFactory.createLeafNode(ENodeType.Content, i, metaData));
		}
		
		// Add attribute map to user nodes
		for (Map.Entry<Integer, List<IDatasetItem<?>>> user : usersMap.entrySet()) {
			Map<INode, IAttribute> attributes = new HashMap<INode, IAttribute>();
			
			// Add corresponding content nodes to user node attributes
			for (IDatasetItem<?> contentDi : user.getValue()) {
				
				// Add to every content attribute its corresponding metadata
				Map<String,String> metaData = findMetaData(contentDi.getContentId(),contentMetaset);
				
				double normalizedRating = ((INormalizer<Number>) dataset.getNormalizer()).normalizeRating( (Number) contentDi.getValue());
				attributes.put(contentsNodeMap.get(contentDi.getContentId()), contentTreeComponentFactory.createNumericAttribute(normalizedRating,metaData));
			}
			
			// **************************************************************
			// Add corresponding nominal data to user node attributes
			Map<String,String> metaData = usersNodeMap.get(user.getKey()).getMeta();
			int i = 0;
			for(String metaKey : metaData.keySet()){
					Map<String,String> info = new HashMap<String,String>();
					info.put(metaKey,metaData.get(metaKey));
					Map<String,Integer> nominalValueMap = new HashMap<String,Integer>();
					nominalValueMap.put(metaKey,1);
					attributes.put(new Node(ENodeType.Nominal, i, info), contentTreeComponentFactory.createSymbolicAttribute(1, nominalValueMap, info));
					i++;
			}
			// **************************************************************
			
			// Add all attributes to user node
			usersNodeMap.get(user.getKey()).setAttributes(attributes);
		}
		
		// Add attribute map to content nodes
		for (Map.Entry<Integer, List<IDatasetItem<?>>> content : contentsMap.entrySet()) {
			Map<INode, IAttribute> attributes = new HashMap<INode, IAttribute>();
			
			// Add corresponding user nodes to content node attributes
			for (IDatasetItem<?> userDi : content.getValue()) {			
				
				// Add to every user attribute its corresponding metadata
				Map<String,String> metaData = findMetaData(userDi.getUserId(),userMetaset);
				
				double normalizedRating = ((INormalizer<Number>) dataset.getNormalizer()).normalizeRating( (Number) userDi.getValue());
				attributes.put(usersNodeMap.get(userDi.getUserId()), userTreeComponentFactory.createNumericAttribute(normalizedRating,metaData));
			}
			
			// **************************************************************
			// Add corresponding nominal data to content node attributes
			Map<String,String> metaData = contentsNodeMap.get(content.getKey()).getMeta();
			int i = 0;
			for(String metaKey : metaData.keySet()){
					Map<String,String> info = new HashMap<String,String>();
					info.put(metaKey,metaData.get(metaKey));
					Map<String,Integer> nominalValueMap = new HashMap<String,Integer>();
					nominalValueMap.put(metaKey,1);
					attributes.put(new Node(ENodeType.Nominal, i, info), contentTreeComponentFactory.createSymbolicAttribute(1, nominalValueMap, info));
					i++;
			}
			// **************************************************************
			
			// Add all attributes to content node
			contentsNodeMap.get(content.getKey()).setAttributes(attributes);
		}
		userLeavesMap = ImmutableMap.copyOf(usersNodeMap);
		contentLeavesMap = ImmutableMap.copyOf(contentsNodeMap);
	}
	
	/**
	 * Finds the corresponding metadata of a content or user node
	 * 
	 * @ id is the id of the node element
	 * @ metaset is the dataset where the metadata is searched for
	 * 
	 * @return map of meta information with value and description
	 */
	private Map<String,String> findMetaData(Integer id, IDataset<?> metaset) {
		Iterator<?> it = metaset.iterateOverDatasetItems();
		while(it.hasNext()){
			MetaDatasetItem metadata = (MetaDatasetItem) it.next();
			if(metadata.getContentId() == id){
				Map<String,String> metaMap = new HashMap<String,String>();
				List<String> metaItems = metadata.getValue();
				for(String metaItem : metaItems){
					metaMap.put(metaItem, "Gender"); // Need information over fields, gender is just test
				}
				return metaMap;
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
