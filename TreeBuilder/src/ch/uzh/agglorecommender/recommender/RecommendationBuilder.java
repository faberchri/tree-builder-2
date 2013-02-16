package ch.uzh.agglorecommender.recommender;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ch.uzh.agglorecommender.client.ClusterResult;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.recommender.treeutils.PositionFinder;

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
	public RecommendationBuilder(ClusterResult clusterResult, int radiusU, int radiusC) {
		
		// Retrieve Root Nodes of the user tree
		this.rootU  		= clusterResult.getUserTreeRoot(); 
		this.leavesMapU 	= clusterResult.getUserTreeLeavesMap();
		this.leavesMapC 	= clusterResult.getContentTreeLeavesMap();
		
		// Parameters for Recommendation
		this.radiusU = radiusU;
		this.radiusC = radiusC;
	}
	
	/**
	 * Runs a test that compares the tree calculation with the real values of a given user
	 * This recommendation type allows to calculate an RMSE value that indicates the quality
	 * of the recommendations produced by the clusterer
	 */
	public Map<Integer, IAttribute> runTestRecommendation(INode testNode){
		
		// Find position of the similar node in the tree
		INode position = leavesMapU.get((int)testNode.getDatasetId());
		
		if(position == null) {
//			System.out.println("No Node with the Dataset ID " + testNode.getDatasetId() + " was found in the user tree -> wrong training set?");
			return null;
		}	
		else {
//			System.out.println("Found position of the node with Dataset ID " + testNode.getDatasetId() + " in the user Tree: " + position.toString());
			
			// Collect ratings of all content given by the input node
			Map<Integer, IAttribute> contentRatings = collectRatings(position,testNode,null);
			return contentRatings;
		}
	}
	
	/**
	 * Collect ratings of all content given by the input node
	 * 
	 * @param position this is the starting point for collecting
	 * @param inputNodeID this node defines the content that needs to be collected
	 */
	public Map<Integer, IAttribute> collectRatings(INode position, INode testNode, Map<Integer, IAttribute> contentRatings) {
		
		//Create Map for content of test user with empty values if not existing
		if(contentRatings == null) {
			
			contentRatings = new HashMap<Integer,IAttribute>(); // DatasetID, AttributeData
			Set<INode> testContentKeys = testNode.getAttributeKeys();
			for(INode testContentKey : testContentKeys) {
//				System.out.println("Adding DataSetID to Set:" + testContentKey.getDatasetId());
				contentRatings.put((int)testContentKey.getDatasetId(),null);
			}
			
//			System.out.println("Ratings for these movies need to be found: " + contentRatings.keySet().toString());
		}
		
		// Look for content nodes on the list and add it to collected ratings map		
		Set<INode> posContentKeys = position.getAttributeKeys();
		Map<Integer,IAttribute> posContentMap = new HashMap<Integer,IAttribute>();
		for(INode posContentKey : posContentKeys){
			
			// Find Dataset ID of Content Node -> FIXME: very inefficient
			int contentDatasetID = 0;
			for (Iterator iter = leavesMapC.entrySet().iterator(); iter.hasNext();) {
				  Map.Entry<Integer,INode> e = (Map.Entry<Integer,INode>) iter.next();
				  if (posContentKey.getId() == e.getValue().getId()){
					  contentDatasetID = e.getKey();
//					  System.out.print(contentDatasetID + ",");
				  }
			}
			
			//System.out.println("Content Node Dataset ID: " + contentDatasetID);
			posContentMap.put(contentDatasetID, position.getAttributeValue(posContentKey));
		}
//		System.out.println("posContentMap size: " + posContentMap.toString());
		
		for (Entry<Integer, IAttribute> contentRating : contentRatings.entrySet()) {
			
			int datasetID = contentRating.getKey();
		    IAttribute rating = contentRating.getValue();
		    
//		    System.out.println("Searching for " + datasetID);
		    
		    // Check if value still needs to be found
		    if(rating == null){
		    	
		    	try {
		    		IAttribute contentAttributes = posContentMap.get(datasetID);
//		    		System.out.print(contentAttributes.toString());
		    		contentRatings.put(datasetID, posContentMap.get(datasetID));
		    	}
		    	catch (Exception e){
//		    		System.out.println(e);
		    	}
		    	
		    }
		    
		}
		
		// Check if all movies were found
		if(contentRatings.containsValue(null) != true){
//			System.out.println("Found all movie ratings: " + contentRatings);
			return contentRatings;
		}
		else {
			
			// Go one level up if possible
			if(position.getParent() != null) {
				contentRatings = collectRatings(position.getParent(),testNode,contentRatings);
				if (contentRatings != null){
					return contentRatings;
				}
			}
			else {
//				System.out.println("Error: Did not find all movie ratings " + contentRatings);
				return contentRatings;
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
		Map<INode,IAttribute> recommendedContent = new HashMap<INode,IAttribute>();
		
		if(!(position == null)){
			recommendedContent = recommend(position,radiusU,radiusC);
		}
		
		return recommendedContent;
		
	}
	
	/**
	 * Collect all the content that should be recommended based on given user and radius
	 * 
	 * @param position starting point to calculate recommendation 
	 * @param radiusU defines how many levels of users should be incorporated 
	 * @param radiusC defines how many levels of content should be incorporated 
	 */
	public Map<INode, IAttribute> recommend(INode position, int radiusU, int radiusC) {
		
		//System.out.println("running recommendation from position " + position.toString());
		
		Map<INode,IAttribute> recommendation = new HashMap<INode,IAttribute>();
		
		// Find relevant Users
		INode relUserNode = collectNode(position,radiusU);
		System.out.println("starting recommendation from user node: " + relUserNode.toString());
		
		// Find relevant Content for every relevant User
		Set<INode> userAttributes = relUserNode.getAttributeKeys();
		for(INode content : userAttributes){
			
			// Find appropriate level for given radius
			INode relContentNode = collectNode(content,radiusC);
			
			//System.out.println("relevant Content Node: " + relContentNode.toString());
			
			// Find leave nodes related to the relevantContent
			if(relContentNode != null) {
				
				Set<INode> contentLeaves = collectLeaves(relContentNode,null);
				
				if(contentLeaves != null){
				
					// Add recommendation into Map
					for(INode contentLeaf : contentLeaves){
						recommendation.put(contentLeaf, contentLeaf.getAttributeValue(contentLeaf));
					}
					
				}
				else {
					System.out.println("contentLeaves == null");
				}
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
	public INode collectNode(INode position,int radius){
		
//		System.out.println("User tree: " + position.toString());

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
	public Set<INode> collectLeaves(INode position, Set<INode> leaves){
		
		// Create leaves set if not exists
		if(leaves == null){
			leaves = new HashSet<INode>();
			//System.out.println("leave set erstellt");
		}
		
		//System.out.println("position: " + position);
		
		// If position is leaf
		if(position.isLeaf()){
			leaves.add(position);
			return leaves;
		}
	
		// If position is no leaf
		Iterator<INode> children = position.getChildren();
		
		while(children.hasNext()){
			
			INode child = children.next();
			Set<INode> tempLeaves = (collectLeaves(child,leaves));
			for(INode tempLeaf : tempLeaves){
				if(!leaves.contains(tempLeaf)){
					leaves.add(tempLeaf);
				}
			}
		}
		
		return leaves;
	}

	public Map<INode, IAttribute> rankRecommendation(Map<INode, IAttribute> unsortedRecommendation){
		// Implement
		return unsortedRecommendation;
	}
}
