package ch.uzh.agglorecommender.recommender;

import java.util.HashMap;
import java.util.Map;
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
		
		// Parameters for Recommendation
		this.radiusU = radiusU;
		this.radiusC = radiusC;
	}
	
	/**
	 * Runs a test that compares the tree calculation with the real values of a given user
	 * This recommendation type allows to calculate an RMSE value that indicates the quality
	 * of the recommendations produced by the clusterer
	 */
	public Map<INode, IAttribute> runTestRecommendation(INode testNode){
		
		// Find position of the similar node in the tree
		int testNodeID = 1; // Woher kriege ich diese ID?
		INode position = leavesMapU.get(testNodeID);
		System.out.println("Looking for " + testNodeID + "," + testNode.toString() + "/ Found " + position.toString());
//		PositionFinder finder = new PositionFinder(leavesMapU);
//		INode position = finder.getPosition(rootU,testNodeID);
		
		if(position == null) {
			System.out.println("getPosition: No Node with this ID was found in the user tree");
			return null;
		}	
		else {
			
			System.out.println("getPosition: Found position of the node in the user Tree");
			
			// Collect ratings of all content given by the input node
			Map<INode,IAttribute> contentRatings = collectRatings(position,testNode,null);
			if(contentRatings != null){
				System.out.println("Received collected content ratings");
				return contentRatings;
			}
			else {
				return null;
			}
		}
	}
	
	/**
	 * Collect ratings of all content given by the input node
	 * 
	 * @param position this is the starting point for collecting
	 * @param inputNodeID this node gives the content that needs to be collected
	 */
	public Map<INode,IAttribute> collectRatings(INode position, INode inputNode, Map<INode,IAttribute> contentRatings) {
		
		System.out.println("current position: " + position);
		
		//Create Map for content of test user with empty values if not existing
		if(contentRatings == null) {
			contentRatings = new HashMap<INode,IAttribute>();
			Set<INode> inputAttKeys = inputNode.getAttributeKeys();
			for(INode attributeKey : inputAttKeys) {
				contentRatings.put(attributeKey,null);
			}
			
			System.out.println("These movies need to be found: " + contentRatings.keySet().toString());
		}
		
		// Look for content nodes on the list and add it to collected ratings map		
		Set<INode> attributeKeys = position.getAttributeKeys();
		for(INode attributeKey : attributeKeys){
			
			// Check if content node is in the map
			if(contentRatings.containsKey(attributeKey)){
				
				System.out.println("Found movie that is on the list: " + attributeKey);
				
				// Check if specific content node already has a rating in the map
				if(contentRatings.get(attributeKey) == null){
					contentRatings.put(attributeKey, position.getAttributeValue(attributeKey));
					System.out.println("Added movie to list");
				}
			}
		}
		
		// Check if all movies were found -> If no null values are left in the map
		if(contentRatings.containsValue(null) != true){
			System.out.println("Found all movie ratings");
			return contentRatings;
		}
		else {
			
			// Go one level up if possible
			if(position.getParent() != null) {
				collectRatings(position.getParent(),inputNode,contentRatings);	
			}
			else {
				System.out.println("Did not find all movie ratings");
				return contentRatings; // Return sparse map
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
	 * Finds the best position (most similar node) in the tree for a given node
	 * 
	 * @param position starting point to calculate recommendation 
	 * @param radiusU defines how many levels of users should be incorporated 
	 * @param radiusC defines how many levels of content should be incorporated 
	 */
	public Map<INode, IAttribute> recommend(INode position, int radiusU, int radiusC) {
		
		System.out.println("running recommendation from position " + position.toString());
		
		Map<INode,IAttribute> recommendation = new HashMap<INode,IAttribute>();
		
		// Find relevant Users
		INode relevantUser = relevantUser(position,radiusU);
		System.out.println("found relevant user node: " + relevantUser.toString());
		
		// Find relevant Content for every relevant User
		Set<INode> userAttributes = relevantUser.getAttributeKeys();
		for(INode attributeKey : userAttributes){
			INode relevantContent = relevantContent(attributeKey,radiusC,null);
			
			if(relevantContent != null) {
				//System.out.println("found relevantContent: " + relevantContent);

				Set<INode> attKeysContent = relevantContent.getAttributeKeys();
				for(INode attKeyContent : attKeysContent){
					if(!(recommendation.containsKey(attKeyContent))){
						recommendation.put(attKeyContent,relevantContent.getAttributeValue(attKeyContent));
					}
				}
			}
		}

	    return recommendation;
	}

	/**
	 * Gives back the relevant user node based on the given radius
	 * 
	 * @param position starting point to calculate recommendation 
	 * @param radiusU defines how many levels of users should be incorporated 
	 */
	public INode relevantUser(INode position,int radiusU){
		
//		System.out.println("User tree: " + position.toString());

		INode relevantUser = null;

		// Going one level up if radius parameter stills allows it
		if(radiusU > 0){
			if(position.getParent() != null) {
				INode parent = position.getParent();
		    	relevantUser = relevantUser(parent,radiusU - 1);
			}
			else {
				relevantUser = position;
			}
		}
		else {
			relevantUser = position;
		}
		
		return relevantUser;
	}

	/**
	 * Gives back the relevant content node based on the given radius
	 * 
	 * @param content this node is the starting point to calculate recommendation 
	 * @param radiusC defines how many levels of content should be incorporated 
	 * @param relevantContent collection of previously collected content
	 */
	public INode relevantContent(INode content, int radiusC, INode relevantContent){
	
		// Get all content nodes for recommendation, rating does not matter
		if(radiusC > 0){
			if(content.getParent() != null){
				INode parent = content.getParent();
				relevantContent(parent,radiusC -1,relevantContent);
			}
			else{
					relevantContent = content;
			}
	    }
	    else {
	    		relevantContent = content;
	    }
	
		return relevantContent;
	}
}
