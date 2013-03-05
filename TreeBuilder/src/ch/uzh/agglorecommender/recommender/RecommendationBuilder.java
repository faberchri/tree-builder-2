package ch.uzh.agglorecommender.recommender;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import ch.uzh.agglorecommender.client.ClusterResult;
import ch.uzh.agglorecommender.clusterer.treecomponent.ClassitAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.recommender.utils.PositionFinder;

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
	private ImmutableMap<Integer,INode> leavesMapU;
	private ImmutableMap<Integer,INode> leavesMapC;
	private INode rootU;
	private int radiusU = 0;
	private int radiusC = 0;
	
	/**
	 * Instantiates a new recommendation builder which can give recommendations based on a given tree structure
	 * 
	 * @param clusterResult the trees on which the recommendation calculation is done
	 * @param radiusU indicates the number of levels that should be incorporated from user tree
	 * @param radiusC indicates the number of levels that should be incorporated from content tree
	 */
	public RecommendationBuilder(ClusterResult clusterResult) {
		
		// Retrieve Root Nodes of the user tree
		this.rootU  		= clusterResult.getUserTreeRoot(); 
		this.leavesMapU 	= clusterResult.getUserTreeLeavesMap();
		this.leavesMapC 	= clusterResult.getContentTreeLeavesMap();
		
		// Parameters for Recommendation
//		this.radiusU = radiusU;
//		this.radiusC = radiusC;
	}
	
	/**
	 * Runs a test that compares the tree calculation with the real values of a given user
	 * This recommendation type allows to calculate an RMSE value that indicates the quality
	 * of the recommendations produced by the clusterer
	 */
	public Map<Integer, IAttribute> runQuantitativeTest(INode testNode){
		
		// Find position of the similar node in the tree
		INode position = leavesMapU.get((int)testNode.getDatasetId());
		
		try {
			// Collect ratings of all content given by the input node
			Map<Integer, IAttribute> contentRatings = collectRatings(position,testNode,null);
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
	private Map<Integer, IAttribute> collectRatings(INode position, INode testNode, Map<Integer, IAttribute> ratingList) {
		
		// Create Ratings Map with empty values if it does not already exist
		if(ratingList == null) {
			ratingList = new HashMap<Integer,IAttribute>(); // DatasetID, AttributeData
			Set<INode> numInputKeys = testNode.getNumericalAttributeKeys();
			for(INode numInputKey : numInputKeys) {
				ratingList.put((int)numInputKey.getDatasetId(),null);
			}
		}
		
		// Look for content nodes on the list and add it to collected ratings map		
		Set<INode> posRatingKeys = position.getNumericalAttributeKeys();
		int i=0;
		for(INode posRatingKey : posRatingKeys){
			
			// Need all dataset item ids
			List<Integer> datasetIds = posRatingKey.getDataSetIds();
			for(int searchDatasetId : ratingList.keySet()){
				
				if(ratingList.get(searchDatasetId) == null){
					if(datasetIds.contains(searchDatasetId)){
						
						// Attribute is original
						if(datasetIds.size() == 1){
							ratingList.put(searchDatasetId, position.getNumericalAttributeValue(posRatingKey));
						}
						// Attribute is not original
						else {
							IAttribute originalAttribute = findOriginalAttribute(position,searchDatasetId);
							ratingList.put(searchDatasetId, originalAttribute);
						}
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
	
	private IAttribute findOriginalAttribute(INode position, int datasetID) {
		
		// Get all children of position
		Iterator<INode> children = position.getChildren();
		
		// Determine the one child that has the datasetID in one of its attributes
		while(children.hasNext()){
			INode child = children.next();
			for(INode attributeKey : child.getNumericalAttributeKeys()){
				
				List<Integer> datasetIds = attributeKey.getDataSetIds();
				
				// Catch condition
				if(datasetIds.size() == 1 && datasetIds.get(0) == datasetID){
					return child.getNumericalAttributeValue(attributeKey);
				}
				else {
					for(int tempDatasetId : datasetIds){
						if(tempDatasetId == datasetID){
							
							// Search on level deeper 
							IAttribute originalAttribute = findOriginalAttribute(child,datasetID);
							if(originalAttribute != null){
								return originalAttribute;
							}
						}
					}
				}
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
		PositionFinder finder = new PositionFinder();
		INode position = finder.findPosition(inputNode,rootU,0);
		
		if(position == null){
			System.out.println("Starting Position was not found");
			return null;
		}
		else {
			System.out.println("Found similar user: " + position.getNominalAttributesString());
		}
		
		// Create List of movies that the user has already rated (user is leaf and has leaf attributes)
		List<Integer> watched = new LinkedList<Integer>();
		Set<INode> ratingKeys = inputNode.getNumericalAttributeKeys();
		for(INode rating : ratingKeys) {
			watched.addAll(rating.getDataSetIds());
		}
		
		// Calculate recommendation based on this position
		Map<INode,IAttribute> recommended = new HashMap<INode,IAttribute>();
		recommended = recommend(position,watched);
		
		return recommended;
		
	}
	
	/**
	 * Collect all the content that should be recommended based on given user and radius
	 * 
	 * @param position starting point to calculate recommendation 
	 * @param watched list of dataset items the user has already rated
	 */
	private Map<INode, IAttribute> recommend(INode position, List<Integer> watched) {
		
		Map<INode,IAttribute> recommendation = new HashMap<INode,IAttribute>();
		
		// Find relevant User / Content Node
//		INode relevantNode = collectNode(position);
		
		// Collect leaf content / user nodes from relevant Node
		List<INode> leafNodes = collectLeaves(position,null);
		
		for(INode leafNode : leafNodes){
			for(INode attKey : leafNode.getNumericalAttributeKeys()){
				
				List<INode> attributeLeafNodes = collectLeaves(attKey,null);
				
				for(INode attLeaf : attributeLeafNodes){
					if(recommendation.containsKey(attLeaf)){
						
						IAttribute oldAtt = recommendation.get(attLeaf);
						IAttribute newAtt = leafNode.getNumericalAttributeValue(attKey); // FIXME wrong
						int mergedSupport = oldAtt.getSupport() + newAtt.getSupport();
						double mergedSum = (oldAtt.getSumOfRatings() * oldAtt.getSupport() + newAtt.getSumOfRatings() * newAtt.getSupport()) / (mergedSupport);
						IAttribute merged = new ClassitAttribute(mergedSupport, mergedSum, Math.pow(mergedSum,2));
								
						recommendation.put(attLeaf, merged);
					}
					else{
						recommendation.put(attLeaf, leafNode.getNumericalAttributeValue(attKey)); // FIXME wrong
					}
				}
			}
		}
		
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
			List<INode> tempLeaves = (collectLeaves(child,leaves));
			for(INode tempLeaf : tempLeaves){
				if(!leaves.contains(tempLeaf)){
					leaves.add(tempLeaf);
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
		SortedMap<INode,IAttribute> finalRecommendation = new TreeMap<INode,IAttribute>(bvc);
		finalRecommendation.putAll(unsortedRecommendation);
		
	    return finalRecommendation;
	    
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
        	
        	// FIXME ev braucht es noch eine Sortierung nach Support
        	
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
				
				//***************TERRIBLE -> FIXME***********************
				// Find Information about Recommendation
				Iterator<Entry<Object,Double>> title = null;
				for(Object attribute : entry.getKey().getNominalAttributeKeys()){
//					System.out.println(attribute.toString());
					if(attribute.equals("title")){
						title = entry.getKey().getNominalAttributeValue(attribute).getProbabilities();
					}
				}
				String titleText="";
				while(title.hasNext()){
					titleText = (String) title.next().getKey();
				}
				
				System.out.println(titleText + " -> " + entry.getValue().getSumOfRatings() / entry.getValue().getSupport());
				//**************************************
			}
		}
    }
}
