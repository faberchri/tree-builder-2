package ch.uzh.agglorecommender.recommender;

import java.util.Comparator;
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

import ch.uzh.agglorecommender.client.ClusterResult;
import ch.uzh.agglorecommender.client.IDataset;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.recommender.utils.PositionFinder;
import ch.uzh.agglorecommender.recommender.utils.TreePosition;
import ch.uzh.agglorecommender.recommender.utils.UnpackerTool;

import com.google.common.collect.ImmutableMap;

/**
 * 
 * Calculates different recommendations based on a given tree structure
 * A test of the rmse value of the recommendation can be run by runTestRecommendation()
 * A recommendation of movies a user has not yet rated can be run by runRecommendation()
 *
 */
public final class RecommendationBuilder {
	
	// Parameters
	private ImmutableMap<String, INode> leavesMapU;
	private ImmutableMap<String, INode> leavesMapC;
	private INode rootU;
	private INode rootC;
	private IDataset<?> dataset;
	
	/**
	 * Instantiates a new recommendation builder which can give recommendations based on a given tree structure
	 * 
	 * @param clusterResult the trees on which the recommendation calculation is done
	 * @param testDataset 
	 * @param radiusU indicates the number of levels that should be incorporated from user tree
	 * @param radiusC indicates the number of levels that should be incorporated from content tree
	 */
	public RecommendationBuilder(ClusterResult clusterResult, IDataset<?> testDataset) {
		
		// Retrieve Root Nodes of the user tree
		this.rootU  		= clusterResult.getUserTreeRoot(); 
		this.rootC			= clusterResult.getContentTreeRoot();
		this.leavesMapU 	= clusterResult.getUserTreeLeavesMap();
		this.leavesMapC 	= clusterResult.getContentTreeLeavesMap();
		this.dataset		= testDataset;
		
	}
	
	/**
	 * Runs a test that compares the tree calculation with the real values of a given user
	 * This recommendation type allows to calculate an RMSE value that indicates the quality
	 * of the recommendations produced by the clusterer
	 */
	public Map<String, IAttribute> runQuantitativeTest(INode testNode, INode position){
		
		try {
			// Collect ratings of all content given by the input node
			Map<String, IAttribute> contentRatings = collectRatings(position,testNode,null);
			return contentRatings;
		}
		catch (Exception e){
			return null;
		}
	}
	
	/**
	 * Collect ratings of all content given by the input node
	 * 
	 * @param position this is the starting point for collecting
	 * @param inputNodeID this node defines the content that needs to be collected
	 * 
	 * @return Map<Integer,IAttribute> the key is the datasetItem ID and the value is an attribute
	 */
	private Map<String, IAttribute> collectRatings(INode position, INode testNode, Map<String, IAttribute> ratingList) {
		
//		UnpackerTool unpacker = new UnpackerTool();
		
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
		int i=0;
		for(INode posRatingKey : posRatingKeys){
			
			// Need all dataset item ids
			List<String> datasetIds = posRatingKey.getDataSetIds();
			for(String searchDatasetId : ratingList.keySet()){
				
				if(ratingList.get(searchDatasetId) == null){
					if(datasetIds.contains(searchDatasetId)){
						
						// Attribute is original
//						if(datasetIds.size() == 1){
							ratingList.put(searchDatasetId, position.getNumericalAttributeValue(posRatingKey));
//						}
						// Attribute is not original
//						else {
//							IAttribute originalAttribute = unpacker.findOriginalAttribute(posRatingKey,searchDatasetId);
//							ratingList.put(searchDatasetId, originalAttribute);
//						}
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
	 * Calculates a recommendation that includes content the user has not rated yet
	 * The scope of nodes that is incorporated into the recommendation depends on defined radius
	 * This recommendation type does not allow a statistical check on the quality of the recommendation
	 */
	public Map<INode, IAttribute> runRecommendation(INode inputNode) throws NullPointerException {
		
		// Find the most similar node in the tree
		TreePosition position = findMostSimilar(inputNode);
		
		if(position == null){
			System.out.println("Starting Position was not found");
			return null;
		}
		else {
			System.out.println("Found similar user: " + position.toString());
		}
		
		// Create List of movies that the user has already rated (user is leaf and has leaf attributes)
		List<String> watched = createWatchedList(inputNode);
		
		// Calculate recommendation based on this position
		Map<INode,IAttribute> recommended = recommend(position.getNode(),watched);
		
		return recommended;
	}
	
	/**
	 * Collect all the content that should be recommended based on given user and radius
	 * 
	 * @param position starting point to calculate recommendation 
	 * @param watched list of dataset items the user has already rated
	 */
	public Map<INode, IAttribute> recommend(INode position, List<String> watched) {
		
		UnpackerTool unpacker = new UnpackerTool(leavesMapC);
		INode unpacked = unpacker.unpack(position);
		
		Map<INode,IAttribute> recommendation = new HashMap<INode,IAttribute>();
		
		for(INode attKey : unpacked.getRatingAttributeKeys()){
			if(!(watched.contains(attKey.getDatasetId()))){
				recommendation.put(attKey, unpacked.getNumericalAttributeValue(attKey));
			}
		}
		
//		for(INode attKey : position.getRatingAttributeKeys()){
//			
//			List<INode> attributeLeafNodes = collectLeaves(attKey,null);
//			
//			// Find leaf nodes of attribute
//			for(INode attLeaf : attributeLeafNodes){
//				
//				// Watched check
//				if(!watched.contains(attLeaf.getDatasetId())){
//					
//					// Already in recommendation?
//					if(recommendation.containsKey(attLeaf)){
//						
//						// Identify ratings
//						IAttribute oldAtt = recommendation.get(attLeaf);
//						IAttribute newAtt = position.getNumericalAttributeValue(attKey);
//							
//						// Add Merged Node
//						int mergedSupport = oldAtt.getSupport() + newAtt.getSupport();
//						double mergedSum = (oldAtt.getSumOfRatings() * oldAtt.getSupport() + newAtt.getSumOfRatings() * newAtt.getSupport()) / (mergedSupport);
//						IAttribute merged = new ClassitAttribute(mergedSupport, mergedSum, Math.pow(mergedSum,2));
//						recommendation.put(attLeaf, merged);
//					}
//					else{
//						recommendation.put(attLeaf, position.getNumericalAttributeValue(attKey));
//					}
//				}
//			}		
//		}
		
	    return recommendation;
	}

//	/**
//	 * Gives back the relevant node based on the given radius
//	 * 
//	 * @param position starting point to calculate recommendation 
//	 * @param radius defines how many levels above start position should be incorporated 
//	 */
//	private INode collectNode(INode position,int radius){
//		
//		INode relevantNode = null;
//
//		// Going one level up if radius parameter stills allows it
//		if(radius > 0){
//			if(!position.isRoot()) {
//				INode parent = position.getParent();
//		    	relevantNode = collectNode(parent,radius - 1);
//			}
//			else {
//				relevantNode = position;
//			}
//		}
//		else {
//			relevantNode = position;
//		}
//		
//		return relevantNode;
//	}

	/**
	 * Gives back the leaf nodes related to a given node
	 * 
	 * @param position this node is the starting point to find leaves 
	 * @param leaves collection of all leaves
	 */
	private List<INode> collectLeaves(INode position, List<INode> leaves){
		
		// Create leaves set if not exists
		if(leaves == null){
			leaves = new LinkedList<INode>();
		}
		
		// If position is leaf
		if(position.isLeaf()){
			leaves.add(position);
			return leaves;
		}
	
		// If position is no leaf
		Iterator<INode> children = position.getChildren();
		
		while(children.hasNext()){
			
			INode child = children.next();
			if(child != null){
				List<INode> tempLeaves = (collectLeaves(child,leaves));
				for(INode tempLeaf : tempLeaves){
					if(!leaves.contains(tempLeaf)){
						leaves.add(tempLeaf);
					}
				}
			}
		}
		
		return leaves;
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

		ValueComparator bvc =  new ValueComparator(unsortedRecommendation);
		SortedMap<INode,IAttribute> sortedRecommendation = new TreeMap<INode,IAttribute>(bvc);
		sortedRecommendation.putAll(unsortedRecommendation);
		
	    return sortedRecommendation;
	}

    class ValueComparator implements Comparator<INode> {

        Map<INode, IAttribute> base;
        public ValueComparator(Map<INode, IAttribute> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with equals.    
        public int compare(INode n1, INode n2) {
        	
        	IAttribute a1 = base.get(n1);
        	IAttribute a2 = base.get(n2);
        	
        	double mean1 = a1.getSumOfRatings() / a1.getSupport();
        	double mean2 = a2.getSumOfRatings() / a2.getSupport();
        	
        	// FIXME Support muss auch noch miteinbezogen werden
        	
            if (mean1 >= mean2) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
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
	
	public IDataset<?> getDataset(){
		return this.dataset;
	}
	
	public TreePosition findMostSimilar(INode inputNode){
		
		// Find position of the similar node in the tree
		TreePosition position = new TreePosition(null,0,0);
		if(inputNode.isLeaf()){
			if(inputNode.getNodeType() == ENodeType.User)
				position.setNode(leavesMapU.get(inputNode.getDatasetId()));
			else if(inputNode.getNodeType() == ENodeType.Content)
				position.setNode(leavesMapC.get(inputNode.getDatasetId()));
		}
		else {
			if(inputNode.getNodeType() == ENodeType.User){
				PositionFinder finder = new PositionFinder(leavesMapC);
				position = finder.findPosition(inputNode,rootU,1);
			}
			else if(inputNode.getNodeType() == ENodeType.Content){
				PositionFinder finder = new PositionFinder(leavesMapU);
				position = finder.findPosition(inputNode,rootC,1);
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
