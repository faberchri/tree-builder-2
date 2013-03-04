package ch.uzh.agglorecommender.recommender;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import ch.uzh.agglorecommender.client.ClusterResult;
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
		
		// Calculate recommendation based on this position
		Map<INode,IAttribute> recommended = new HashMap<INode,IAttribute>();
		
		if(!(position == null)){
			recommended = recommend(position,radiusU,radiusC);
		}
		
		// Remove content nodes that the user has already rated
//		for(INode recommended : recommendedContent.keySet()){
//			for(INode seen : inputNode.getAttributeKeys()){
//				if(seen.getMeta().get(2) == recommended.getMeta().get(2)){
//					recommendedContent.remove(recommended);
//				}
//			}
//		}
		
		return recommended;
		
	}
	
	/**
	 * Collect all the content that should be recommended based on given user and radius
	 * 
	 * @param position starting point to calculate recommendation 
	 * @param radiusU defines how many levels of users should be incorporated 
	 * @param radiusC defines how many levels of content should be incorporated 
	 */
	private Map<INode, IAttribute> recommend(INode position, int radiusU, int radiusC) {
		
		Map<INode,IAttribute> recommendation = new HashMap<INode,IAttribute>();
		
		// Find relevant User / Content Node
		INode relevantNode = collectNode(position,radiusU);
		
		// Collect leaf content / user nodes from relevant Node
		Set<INode> userAttributes = relevantNode.getNumericalAttributeKeys();
		for(INode content : userAttributes){
			
			Map<INode, IAttribute> contentLeafNodes = collectLeaves(content,null);
			for(INode leafNode : contentLeafNodes.keySet()){
				recommendation.put(leafNode, contentLeafNodes.get(leafNode));
			}

		}
		
	    return recommendation;
	}

	/**
	 * Gives back the relevant node based on the given radius
	 * 
	 * @param position starting point to calculate recommendation 
	 * @param radius defines how many levels above start position should be incorporated 
	 */
	private INode collectNode(INode position,int radius){
		
		INode relevantNode = null;

		// Going one level up if radius parameter stills allows it
		if(radius > 0){
			if(!position.isRoot()) {
				INode parent = position.getParent();
		    	relevantNode = collectNode(parent,radius - 1);
			}
			else {
				relevantNode = position;
			}
		}
		else {
			relevantNode = position;
		}
		
		return relevantNode;
	}

	/**
	 * Gives back the leaf nodes related to a given node
	 * 
	 * @param position this node is the starting point to find leaves 
	 * @param leaves collection of all leaves
	 */
	private Map<INode, IAttribute> collectLeaves(INode position, Map<INode,IAttribute> leaves){
		
		// Create leaves set if not exists
		if(leaves == null){
			leaves = new HashMap<INode,IAttribute>();
		}
		
		// If position is leaf
		if(position.isLeaf()){
			leaves.put(position,position.getNumericalAttributeValue(position));
			return leaves;
		}
	
		// If position is no leaf
		Iterator<INode> children = position.getChildren();
		
		while(children.hasNext()){
			
			INode child = children.next();
			Map<INode, IAttribute> tempLeaves = (collectLeaves(child,leaves));
			for(INode tempLeaf : tempLeaves.keySet()){
				if(!leaves.keySet().contains(tempLeaf)){
					leaves.put(tempLeaf,tempLeaf.getNumericalAttributeValue(tempLeaf));
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

	    //********** NEW WAY - HAS STILL BUGS ********************
//		 FIXME sollte hier Comparator verwenden http://www.roseindia.net/java/example/java/util/sortedmap.shtml
		ValueComparator bvc =  new ValueComparator(unsortedRecommendation);
		SortedMap<INode,IAttribute> finalRecommendation = new TreeMap<INode,IAttribute>(bvc);
		finalRecommendation.putAll(unsortedRecommendation);
		
//		for(INode node : unsortedRecommendation.keySet()){
//			finalRecommendation.put(node, unsortedRecommendation.get(node));
//		}

	    System.out.println(finalRecommendation); 

	    return finalRecommendation;
	    
	    //********** NEW WAY - HAS STILL BUGS ********************

//		// Create Array from Map
//		IAttribute[] sortedRecommendation = new IAttribute[unsortedRecommendation.size()];
//		int x = 0;
//		for(INode recommendation : unsortedRecommendation.keySet()) {
//			IAttribute attribute = unsortedRecommendation.get(recommendation);
//			sortedRecommendation[x] = attribute;
//			x++;
//		}
//		
//		// Insertion Sort
//		int i;
//		IAttribute temp;
//
//		for (int f = 1; f < sortedRecommendation.length; f++) {
//			
//			IAttribute newRating = sortedRecommendation[f];
//			IAttribute insertedRating = sortedRecommendation[f-1];
//			
//			if ((newRating.getSumOfRatings() / newRating.getSupport()) > (insertedRating.getSumOfRatings() / insertedRating.getSupport())) continue;
//			temp = sortedRecommendation[f];
//			i    = f-1;
//			
//			while ((i >= 0)&&(sortedRecommendation[i].getMeanOfRatings() > temp.getMeanOfRatings())) {
//				sortedRecommendation[i+1] = sortedRecommendation[i];
//				i--;
//			}
//			sortedRecommendation[i+1]=temp;
//		}
//		
//		// Limit
//		SortedMap<INode,IAttribute> finalRecommendation = new ArrayList<IAttribute>();
//		for(int j = 0; j < limit; j++){
//			if(j < sortedRecommendation.length){
//				
//				// Returns the best
//				if(direction == 1){
//					finalRecommendation.add(sortedRecommendation[sortedRecommendation.length-1-j]);
//				}
//				// Returns the worst
//				else if(direction == 0){
//					finalRecommendation.add(sortedRecommendation[j]);
//				}
//			}
//		}
//		
//		return finalRecommendation;
	}

    class ValueComparator implements Comparator<INode> {

        Map<INode, IAttribute> base;
        public ValueComparator(Map<INode, IAttribute> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with equals.    
        public int compare(INode n1, INode n2) {
            if (base.get(n1).getSumOfRatings()/base.get(n1).getSupport() >= base.get(n2).getSumOfRatings()/base.get(n2).getSupport()) {
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }
    
    public void printRecommendation(SortedMap<INode, IAttribute> sortedRecommendation){
		if(sortedRecommendation != null){
			System.out.println("=> Recommended Movies: ");
			for(INode recommendation: sortedRecommendation.keySet()){
//				if(recommendation.getMeta() != null){
//					System.out.println(recommendation.getMeta() + " -> Rating: " + recommendation.getMeanOfRatings());
//				}
				System.out.println(recommendation.getNominalAttributesString() + " -> Rating: " + sortedRecommendation.get(recommendation).getSumOfRatings()/sortedRecommendation.get(recommendation).getSupport());
			}
		}
    }
}
