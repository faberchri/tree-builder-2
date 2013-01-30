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
 * Implementation of COBWEB inspired hierarchical
 * agglomerative two-dimensional clustering algorithm 
 * for media recommendation generation.
 *
 */
public final class RecommendationBuilder {
	
	private INode rootU;
	
	private INode inputNode;
	
	private int radiusU = 0;
	private int radiusC = 0;
	
	private INode position = null;
	
	private Map<INode,Double> moviesToCollect = new HashMap<INode,Double>();
	private Map<INode,IAttribute> collectedRatings = new HashMap<INode,IAttribute>();
	
	private double highestUtility = 0;
	
	Random randomGenerator = new Random();
	
	// Recommendation Type 1 -> Return predicted ratings of movies a user hat already seen -> Evalution
	// Recommendation Type 2 -> Recommend Movies that the user has not seen yet -> Automatic evaluation is not possible
	public RecommendationBuilder(TreeBuilder tree, INode inputNode, int radiusU, int radiusC) {
		
		// Retrieve Root Nodes
		ArrayList<INode> rootNodes = tree.getRootNodes();
		this.rootU = rootNodes.get(0); // wacklig
		
		// Input for Recommendation
		this.inputNode = inputNode;
		
		// Parameters for Recommendation
		this.radiusU = radiusU;
		this.radiusC = radiusC;
	}
	
	//---------------- Methods for Type 1 Recommendation -----------------------------------
	
	public Map<INode, IAttribute> runTestRecommendation(){
		
		// Searching for the node with same ID as the inputNode in the tree
		getPosition(rootU,inputNode);
		
		if(this.position == null) {
			System.out.println("No Node with this ID was found in the user tree");
			return null;
		}
			
		System.out.println("Found position of the node in the user Tree");
		
		// Collect predicted ratings of all movies featured in the input node by going upwards in the tree
		collectRatings(this.position,inputNode);
		
		return collectedRatings;
	}
	
	public void getPosition(INode parent, INode inputNode) {
		
		// Look for position recursively
		Iterator<INode> children = parent.getChildren();
		while(children.hasNext()) {
			INode child = children.next();
			
			if(child.getId() == inputNode.getId()) {
				System.out.println("found the node: " + child.toString());
				this.position = child;
			}
			else {
				getPosition(child, inputNode);
			}
		}
	}
	
	// Collect all movies of inputNode starting from defined position
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
	
	//---------------- Methods for Type 2 Recommendation -----------------------------------

	public Map<INode, IAttribute> runRecommendation(){
		
		// Ausgehend von der Input Node sucht man die Node die am Šhnlichsten ist im Baum (findPosition)
		findPosition(inputNode,rootU,0);
		
		// Dann wird ausgehend von dieser Position eine Empfehlung ausgesprochen (recommend)
		Map<INode,IAttribute> recommendedContent = new HashMap();
		
		if(!(position == null)){
			recommendedContent = recommend(position,radiusU,radiusC);
		}
		
		return recommendedContent;
		
	}
	
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
	
	public Map<INode, IAttribute> recommend(INode position, int radiusU, int radiusC) {
		
		System.out.println("running recommendation from position " + position.toString());
		
		Map<INode,IAttribute> recommendation = new HashMap();
		
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
