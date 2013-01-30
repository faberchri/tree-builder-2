package ch.uzh.agglorecommender.recommender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ch.uzh.agglorecommender.clusterer.TreeBuilder;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.recommender.treeutils.PositionFinder;

/**
 * 
 * Calculates different recommendations based on a given tree structure
 * A test of the rmse value of the recommendation can be run by runTestRecommendation()
 * A recommendation of movies a user has not yet rated can be run by runRecommendation()
 *
 */
public final class RecommendationBuilder {
	
	// Parameters
	private INode rootU;
	private int radiusU = 0;
	private int radiusC = 0;
	
	// Calculation Process
	private Map<INode,Double> moviesToCollect = new HashMap<INode,Double>();
	private Map<INode,IAttribute> collectedRatings = new HashMap<INode,IAttribute>();
	
	/**
	 * Instantiates a new recommendation builder which can give recommendations based on a given tree structure
	 * 
	 * @param tree the trees on which the recommendation calculation is done
	 * @param radiusU indicates the number of levels that should be incorporated from user tree
	 * @param radiusC indicates the number of levels that should be incorporated from content tree
	 */
	public RecommendationBuilder(TreeBuilder tree, int radiusU, int radiusC) {
		
		// Retrieve Root Nodes of the user tree
		ArrayList<INode> rootNodes = tree.getRootNodes();
		this.rootU = rootNodes.get(0);
		
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
		PositionFinder finder = new PositionFinder();
		INode position = finder.getPosition(rootU,testNode.getId());
		
		if(position == null) {
			System.out.println("getPosition: No Node with this ID was found in the user tree");
			return null;
		}	
		else {
			
			System.out.println("getPosition: Found position of the node in the user Tree");
			
			// Collect ratings of all content given by the input node
			collectRatings(position,testNode);
			
			return collectedRatings;
		}
	}
	
	/**
	 * Collect ratings of all content given by the input node
	 * 
	 * @param position this is the starting point for collecting
	 * @param inputNodeID this node gives the content that needs to be collected
	 */
	public void collectRatings(INode position, INode inputNode) {
		
		//Create Map for all movies
		if(moviesToCollect.size() == 0) {
			Set<INode> inputAttKeys = inputNode.getAttributeKeys();
			for(INode attributeKey : inputAttKeys) {
				moviesToCollect.put(attributeKey,0.0);
			}
		}
		
		System.out.println("These movies need to be found: " + moviesToCollect.keySet().toString());
		
		// Retrieve necessary ratings and add to collectedRatings
		INode parent = position.getParent();
		Set<INode> attributeKeys = position.getAttributeKeys();
		for(INode attributeKey : attributeKeys){
			
			// look if attribute is in inputNode
			if(moviesToCollect.containsKey(attributeKey)){
				
				//System.out.println("Found movie that is on the list: " + attributeKey);
				
				// Look if attribute is not already added to Set
				if(collectedRatings.containsKey(attributeKey) == false){
					collectedRatings.put(attributeKey,position.getAttributeValue(attributeKey));
				}
			}
		}
		
		// Check if all movies were found
		if(position.getAttributeKeys() == inputNode.getAttributeKeys()){ // Suche besseren Vergleich
			System.out.println("Found all movie ratings");
		}
		else {
			if(parent.getParent() != null) {
				collectRatings(parent,inputNode);
			}
			else {
				System.out.println("Did not find all movie ratings");
			}
		}
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
