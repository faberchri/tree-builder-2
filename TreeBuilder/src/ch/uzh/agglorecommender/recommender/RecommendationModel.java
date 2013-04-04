package ch.uzh.agglorecommender.recommender;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import ch.uzh.agglorecommender.client.ClusterResult;
import ch.uzh.agglorecommender.client.IDataset;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.recommender.utils.NodeUnpacker;
import ch.uzh.agglorecommender.recommender.utils.PositionFinder;
import ch.uzh.agglorecommender.recommender.utils.RatingComparator;
import ch.uzh.agglorecommender.recommender.utils.TreePosition;

import com.google.common.collect.ImmutableMap;

/**
 * 
 * Calculates different recommendations based on a given tree structure
 * A test of the rmse value of the recommendation can be run by runTestRecommendation()
 * A recommendation of movies a user has not yet rated can be run by runRecommendation()
 *
 */
public final class RecommendationModel {
	
	// Parameters
	private ImmutableMap<String, INode> leavesMapU;
	private ImmutableMap<String, INode> leavesMapC;
	private INode rootU;
	private INode rootC;
	private IDataset dataset;
	
	/**
	 * Instantiates a new recommendation builder which can give recommendations based on a given tree structure
	 * 
	 * @param clusterResult the trees on which the recommendation calculation is done
	 * @param testDataset 
	 * @param radiusU indicates the number of levels that should be incorporated from user tree
	 * @param radiusC indicates the number of levels that should be incorporated from content tree
	 */
	public RecommendationModel(ClusterResult clusterResult, IDataset testDataset) {
		
		// Retrieve Root Nodes of the user tree
		this.rootU  		= clusterResult.getUserTreeRoot(); 
		this.rootC			= clusterResult.getContentTreeRoot();
		this.leavesMapU 	= clusterResult.getUserTreeLeavesMap();
		this.leavesMapC 	= clusterResult.getContentTreeLeavesMap();
		this.dataset		= testDataset;
		
	}
	
	public ImmutableMap<String, INode> getLeavesMapU() {
		return leavesMapU;
	}

	public void setLeavesMapU(ImmutableMap<String, INode> leavesMapU) {
		this.leavesMapU = leavesMapU;
	}

	public ImmutableMap<String, INode> getLeavesMapC() {
		return leavesMapC;
	}

	public void setLeavesMapC(ImmutableMap<String, INode> leavesMapC) {
		this.leavesMapC = leavesMapC;
	}

	public INode getRootU() {
		return rootU;
	}

	public void setRootU(INode rootU) {
		this.rootU = rootU;
	}

	public INode getRootC() {
		return rootC;
	}

	public void setRootC(INode rootC) {
		this.rootC = rootC;
	}

	public void setDataset(IDataset dataset) {
		this.dataset = dataset;
	}

	/**
	 * Collect ratings of all content given by the input node
	 * 
	 * @param position this is the starting point for collecting
	 * @param inputNodeID this node defines the content that needs to be collected
	 * 
	 * @return Map<Integer,IAttribute> the key is the datasetItem ID and the value is an attribute
	 */
	public Map<String, IAttribute> collectRatings(INode position, INode testNode, Map<String, IAttribute> ratingList) {
		
		// Create Ratings Map with empty values if it does not already exist
		if(ratingList == null) {
			ratingList = new HashMap<String,IAttribute>(); // DatasetID, AttributeData
			
			Set<INode> numInputKeys = testNode.getRatingAttributeKeys();
			
			for(INode numInputKey : numInputKeys) {
				ratingList.put(numInputKey.getDatasetId(),null);
			}
		}
		
		// Look for content nodes on the list and add it to collected ratings map		
		Set<INode> posRatingKeys = position.getRatingAttributeKeys();
		for(INode posRatingKey : posRatingKeys){
			
			// Need all dataset item ids
			List<String> datasetIds = posRatingKey.getDataSetIds();
			for(String searchDatasetId : ratingList.keySet()){

				if(ratingList.get(searchDatasetId) == null){
					if(datasetIds.contains(searchDatasetId)){
						ratingList.put(searchDatasetId, position.getNumericalAttributeValue(posRatingKey));
					}
				}
			}
		}
			
		// Check if all movies were found
		if(ratingList.containsValue(null) != true){
			return ratingList;
		}
		else {
			
			// Go one level up if possible
			if(position.getParent() != null) {
				ratingList = collectRatings(position.getParent(),testNode,ratingList);
				if (ratingList != null){
					return ratingList;
				}
			}
			else {
				return ratingList;
			}
		}
		return null;
	}
	
	/**
	 * Collect all the content that should be recommended based on given user and radius
	 * 
	 * @param position starting point to calculate recommendation 
	 * @param watched list of dataset items the user has already rated
	 */
	public Map<INode, IAttribute> recommend(INode position, List<String> watched) {
		
		NodeUnpacker unpacker = new NodeUnpacker(this);
		INode unpacked = unpacker.unpack(position);
		
		Map<INode,IAttribute> recommendation = new HashMap<INode,IAttribute>();
		
		for(INode attKey : unpacked.getRatingAttributeKeys()){
			if(!(watched.contains(attKey.getDatasetId()))){
				recommendation.put(attKey, unpacked.getNumericalAttributeValue(attKey));
			}
		}
		
	    return recommendation;
	}

	/**
	 * Sorts the recommendation with an insertion sort and returns the movies which the
	 * user would most likely rate the best respectively the worst depending on the sorting
	 * parameter. the number of returned recommendations depends on the limit parameter.
	 * 
	 * @param unsortedRecommendation the unsorted recommendation
	 * @param direction defines if best or worst recommendations are returned (1=best,0=worst)
	 * @param limit defines the number of returned recommendations
	 */
	public SortedMap<INode, IAttribute> rankRecommendation(Map<INode, IAttribute> unsortedRecommendation,int direction, int limit){

		RatingComparator bvc =  new RatingComparator(unsortedRecommendation);
		SortedMap<INode,IAttribute> sortedRecommendation = new TreeMap<INode,IAttribute>(bvc);
		sortedRecommendation.putAll(unsortedRecommendation);
		
	    return sortedRecommendation;
	}

    public void printRecommendation(SortedMap<INode, IAttribute> recommendationCollection){
		
		if(recommendationCollection != null){
			System.out.println("=> Recommended: ");
			for(Entry<INode,IAttribute> entry : recommendationCollection.entrySet()){
				
				// Find Information about Recommendation -> FIXME unflexibel
				String title = getMeta(entry.getKey(),"title");
				double rating = entry.getValue().getSumOfRatings() / entry.getValue().getSupport();
				
				System.out.printf("%.2f", rating);
				System.out.println(" -> " + title);
			}
		}
	}

	public List<INode> createItemList(ENodeType type, int limit, INode inputNode){
		
		List<String> watched = createWatchedList(inputNode);
		
		List<INode> itemList = new LinkedList<INode>();
		Random randomGenerator = new Random();
		
		for(int i = 0;i < limit;i++){
			INode randomNode = null;
			if(type == ENodeType.Content){
				int randomID = randomGenerator.nextInt(leavesMapU.size());
				randomNode = leavesMapU.get("" + randomID);
			}
			else if (type == ENodeType.User){
				int randomID = randomGenerator.nextInt(leavesMapC.size());
				randomNode = leavesMapC.get("" + randomID);	
			}
			
			// Null check
			if(randomNode != null){
			
				// Already Added Check
				if(!itemList.contains(randomNode)){
				
					// Already Rated Check
					if(watched != null){
						if(randomNode.getDatasetId() != null){
							if(!watched.contains(randomNode.getDatasetId())){
								itemList.add(randomNode);
							}
						}
					}
				}
			}
		}
		
		return itemList;
	}
	
	public INode findNode(int datasetID, ENodeType type){
		INode node = null;
		if(type == ENodeType.User){
			node = leavesMapC.get("" + datasetID);
		}
		else if (type == ENodeType.Content){
			node = leavesMapU.get("" + datasetID);
		}
		
		return node;
	}
	
	private String getMeta(INode node, String attribute){
		Iterator<Entry<Object, Double>> titleIt = node.getNominalAttributeValue(attribute).getProbabilities();
		String title="";
		while(titleIt.hasNext()){
			title = (String) titleIt.next().getKey();
		}
		return title;
	}
	
	public IDataset getDataset(){
		return this.dataset;
	}
	
	public TreePosition findMostSimilar(INode inputNode) throws InterruptedException, ExecutionException{
		
		// Find position of the similar node in the tree
		TreePosition position = new TreePosition(null,0,0);
		if(inputNode.isLeaf()){
			if(inputNode.getNodeType() == ENodeType.User)
				position.setNode(leavesMapU.get(inputNode.getDatasetId()));
			else if(inputNode.getNodeType() == ENodeType.Content)
				position.setNode(leavesMapC.get(inputNode.getDatasetId()));
		}
		
		if(position.getNode() == null){
			if(inputNode.getNodeType() == ENodeType.User){
				PositionFinder finder = new PositionFinder(this);
				position = finder.findPosition(inputNode,rootU);
			}
			else if(inputNode.getNodeType() == ENodeType.Content){
				PositionFinder finder = new PositionFinder(this);
				position = finder.findPosition(inputNode,rootC);
			}
		}
		
		return position;
	}
	
	public List<String> createWatchedList(INode inputNode){
		
		// Create List of movies that the user has already rated (user is leaf and has leaf attributes)
		List<String> watched = new LinkedList<String>();
		Set<INode> ratingKeys = inputNode.getRatingAttributeKeys();
		for(INode rating : ratingKeys) {
			watched.addAll(rating.getDataSetIds());
		}
		
		return watched;
	}
}
