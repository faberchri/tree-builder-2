package ch.uzh.agglorecommender.recommender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import ch.uzh.agglorecommender.clusterer.TreeBuilder;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treesearch.ClassitMaxCategoryUtilitySearcher;

/**
 * 
 * Calculates different recommendations based on a given tree structure
 * A test of the rmse value of the recommendation can be run by runTestRecommendation()
 * A recommendation of movies a user has not yet rated can be run by runRecommendation()
 *
 */
public final class RecommendationBuilder {
	
	// Basic Input Data
	private INode rootU;
	private INode inputNode;
	
	// Futher Parameters
	private int radiusU = 0;
	private int radiusC = 0;
	
	// Calculation Process
	private INode position = null;
	private double highestUtility = 0;
	private Map<INode,Double> moviesToCollect = new HashMap<INode,Double>();
	private Map<INode,IAttribute> collectedRatings = new HashMap<INode,IAttribute>();
	private Random randomGenerator = new Random();
	
	/**
	 * Instantiates a new recommendation builder which can give recommendations based on a given tree structure
	 * 
	 * @param tree the trees on which the recommendation calculation is done
	 * @param inputNode this node is the base for the recommendation calculation
	 * @param radiusU indicates the number of levels that should be incorporated from user tree
	 * @param radiusC indicates the number of levels that should be incorporated from content tree
	 */
	public RecommendationBuilder(TreeBuilder tree, INode inputNode, int radiusU, int radiusC) {
		
		// Retrieve Root Nodes of the user tree
		ArrayList<INode> rootNodes = tree.getRootNodes();
		this.rootU = rootNodes.get(0);
		
		// Input for Recommendation
		this.inputNode = inputNode;
		
		// Parameters for Recommendation
		this.radiusU = radiusU;
		this.radiusC = radiusC;
	}
	
	/**
	 * Runs a test that compares the tree calculation with the real values of a given user
	 * This recommendation type allows to calculate an RMSE value that indicates the quality
	 * of the recommendations produced by the clusterer
	 */
	public Map<INode, IAttribute> runTestRecommendation(){
		
		// Find position of the similar node in the tree
		getPosition(rootU,inputNode.getId());
		
		if(this.position == null) {
			System.out.println("No Node with this ID was found in the user tree");
			return null;
		}
			
		System.out.println("Found position of the node in the user Tree");
		
		// Collect ratings of all content given by the input node
		collectRatings(this.position,inputNode);
		
		return collectedRatings;
	}
	
	/**
	 * Find position of a node with a given id in the tree
	 * 
	 * @param parent the current node from which the search is starting
	 * @param inputNodeID the node that should be found has this id
	 */
	public void getPosition(INode parent, long inputNodeID) {
		
		// Look for position recursively
		Iterator<INode> children = parent.getChildren();
		while(children.hasNext()) {
			INode child = children.next();
			
			if(child.getId() == inputNodeID) {
				System.out.println("found the node: " + child.toString());
				this.position = child;
			}
			else {
				getPosition(child, inputNodeID);
			}
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
	public Map<INode, IAttribute> runRecommendation(){
		
		// Find the most similar node in the tree
		findPosition(inputNode,rootU,0);
		
		// Calculate recommendation based on this position
		Map<INode,IAttribute> recommendedContent = new HashMap<INode,IAttribute>();
		
		if(!(position == null)){
			recommendedContent = recommend(position,radiusU,radiusC);
		}
		
		return recommendedContent;
		
	}
	
	/**
	 * Finds the best position (most similar node) in the tree for a given node
	 * Calculations are based on category utility. If the previously calculated
	 * utility value is higher than the best utility value on the current level
	 * then the previous position is the best. If the search does not stop in
	 * the tree, it ends on a leaf node.
	 * 
	 * @param inputNodeID this node is the base of the search
	 * @param parent this is the current starting point of the search
	 * @param cutoff this is the previously calculated utility value
	 */
	public void findPosition(INode inputNode,INode parent,double cutoff) {
		
		INode[] nodesToCalculate = new INode[2];
		nodesToCalculate[0] = inputNode;
		nodesToCalculate[1] = parent;
		ClassitMaxCategoryUtilitySearcher helper = new ClassitMaxCategoryUtilitySearcher(); // <--------- unschšn, sollte je nach Typ anderst sein
		
		// Establish cut off value when 0
		if(cutoff == 0) {
		    cutoff = helper.calculateCategoryUtility(nodesToCalculate); //unschšn
			System.out.println("Established cut off: " + cutoff);
		}
		
		if(parent.getChildrenCount() > 0){
	
			Iterator<INode> compareSet = parent.getChildren();
			INode nextPosition = null;
			while(compareSet.hasNext()) {
				  
				INode tempPosition = compareSet.next();
				nodesToCalculate[1] = tempPosition;
				double utility = helper.calculateCategoryUtility(nodesToCalculate);
				
				if(utility > highestUtility){
					System.out.println("Found higher utility: " + utility + ">" + highestUtility);
					highestUtility = utility;
					nextPosition = tempPosition;
				}
				
			}
	
			if(highestUtility > cutoff) {
				System.out.println("Continue one level down");
				findPosition(inputNode,nextPosition,cutoff);
			}
			else {
				System.out.println("Best position was found " + parent.toString() + "; better utility than children");
				position = parent;
			}
		}
		else {
			System.out.println("Best position was found " + parent.toString() + "; no children left");
			position = parent;
		}
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
