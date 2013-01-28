package ch.uzh.agglorecommender.recommender;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import ch.uzh.agglorecommender.clusterer.TreeBuilder;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treesearch.ClassitMaxCategoryUtilitySearcher;

public final class RecommendationBuilder {
	
	private INode rootU;
	private INode rootC;
	
	private double userID;
	private INode inputNode;
	
	private int radiusU = 0;
	private int radiusC = 0;
	
	private INode position = null;
	
	private Map<INode,Double> moviesToCollect;
	private Set<IAttribute> collectedRatings;
	
	private double highestUtility = 0;
	
	// Recommendation Type 1 -> Return predicted ratings of movies a user hat already seen -> Evalution
	// Recommendation Type 2 -> Recommend Movies that the user has not seen yet -> Automatic evaluation is not possible
	public RecommendationBuilder(TreeBuilder tree, INode inputNode, int radiusU, int radiusC) {
		
		// Retrieve Root Nodes
		ArrayList<INode> rootNodes = tree.getRootNodes();
		this.rootU = rootNodes.get(0);
		this.rootC = rootNodes.get(1);
		
		// Input for Recommendation
		this.inputNode = inputNode;
		
		// Parameters for Recommendation
		this.radiusU = radiusU;
		this.radiusC = radiusC;
	}
	
	//---------------- Methods for Type 1 Recommendation -----------------------------------
	
	public Set<IAttribute> runTestRecommendation(){
		
		// Searching for the node with same ID as the inputNode in the tree
		getPosition(rootU,inputNode);
		
		if(this.position == null) {
			System.out.println("No Node with this ID was found in the user tree");
			return null;
		}
			
		System.out.println("Found position of the node in the user Tree");
		
		// Collect predicted ratings of all movies featured in the input node by going upwards in the tree
		collectRatings(this.position,inputNode);
		
		return this.collectedRatings;
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
		if(moviesToCollect == null) {
			Set<INode> inputAttKeys = inputNode.getAttributeKeys();
			for(INode attributeKey : inputAttKeys) {
				System.out.println("WŸrde " + attributeKey.toString() + "Ÿbergeben");
				//moviesToCollect.put(attributeKey,0.0);
			}
		}
		
		INode parent = position.getParent();
		
		// Retrieve necessary ratings and add to collectedRatings
		Set<INode> attributeKeys = position.getAttributeKeys();
		for(INode attributeKey : attributeKeys){
			
			// look if attribute is in inputNode
			if(moviesToCollect.containsKey(attributeKey)){
				
				System.out.println("Found movie in list");
				
				// Look if attribute is not already added to Set
				if(!collectedRatings.contains(attributeKey)){
					
					System.out.println("Movie Rating was not added yet");
					collectedRatings.add(position.getAttributeValue(attributeKey));
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

	public Set<INode> runRecommendation(){
		return null;
		
		// Ausgehend von der Input Node sucht man die Node die am Šhnlichsten ist im Baum (findPosition)
		// Dann wird ausgehend von dieser Position eine Empfehlung ausgesprochen (recommend)
		
	}
	
	public INode findPosition(INode node,INode parent,double cutoff) {
		
		INode[] nodesToCalculate = new INode[2];
		ClassitMaxCategoryUtilitySearcher helper = new ClassitMaxCategoryUtilitySearcher(); // Sollte je nach Typ anderst sein
		
		// Stop find process when cutoff is 0
		if(cutoff == 0) {
			nodesToCalculate[0] = node;
			nodesToCalculate[1] = parent;
		    cutoff = helper.calculateCategoryUtility(nodesToCalculate);
		}

		Iterator<INode> compareSet = parent.getChildren();
		INode position = null;
		while(compareSet.hasNext()) {
			  INode tempPosition = compareSet.next();
			  
			  nodesToCalculate[1] = tempPosition;
		      double utility = helper.calculateCategoryUtility(nodesToCalculate);
		      if(utility> highestUtility)
		             highestUtility = utility;
		             position = tempPosition;
		}

		if(highestUtility > cutoff) {
		   findPosition(node,position,cutoff);
		}
		else {
		   return parent; // utility mit parent ist hšher als mit dem besten child
		}
		return null;
	}
	
	public Set<INode> recommend(INode position) {
		
		int radiusU = 0;
		int radiusC = 0;
		
		Set<INode> recommendation = null;
		
		Set<INode> relevantUsers = relevantUsers(position,radiusU);

		for(INode user : relevantUsers){
			Set<INode> userAttributes = user.getAttributeKeys();
			for(INode attributeKey : userAttributes){
				Set<INode> movies = relevantContent(attributeKey,radiusC);
				recommendation.addAll(movies);
			}
		}

	    return recommendation;
	}

	public Set<INode> relevantUsers(INode position,int radiusU){

		Set<INode> relevantUsers = null;

		// Alle Nutzer abholen die in die Empfehlung einfliessen
		if(radiusU > 0){
			INode parent = position.getParent();
	    	Iterator<INode> children = parent.getChildren();
	    	while(children.hasNext()) {
	    		INode child = children.next();
	    		relevantUsers.addAll(relevantUsers(child,radiusU - 1));
	    	}
		}
		else {
			relevantUsers.add(position);
		}
		
		return relevantUsers;
	}

	public Set<INode> relevantContent(INode content, int radiusC){
	
		Set<INode> relevantContent = null;
	
		// Alle Filme abholen die Empfohlen werden sollen (Rating spielt keine Rolle)
		if(radiusC > 0){
	      INode parent = content.getParent();
	      Iterator<INode> children = parent.getChildren();
	      while(children.hasNext()) {
	    	  INode child = children.next();
	          relevantContent.addAll(relevantContent(child,radiusC -1));
	      }
	    }
	    else {
	      relevantContent.add(content);
	    }
	
		return relevantContent;
	}
}
