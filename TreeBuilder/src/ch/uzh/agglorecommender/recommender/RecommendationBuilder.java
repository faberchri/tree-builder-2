package ch.uzh.agglorecommender.recommender;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import ch.uzh.agglorecommender.clusterer.TreeBuilder;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treesearch.ClassitMaxCategoryUtilitySearcher;

public final class RecommendationBuilder {
	
	private INode rootU;
	private INode rootC;
	
	private double userID;
	private INode inputNode;
	
	private int radiusU = 0;
	private int radiusC = 0;
	
	double highestUtility = 0;
	
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
	
	public void runRecommending1(){
		
		
		
	}
	
	//---------------------------------------------------
	
	// Finden der am besten passenden Position
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
	
	//Verschieben zum Evaluator
	// FŸr Variante 2 -> wenn man userId kennt kann darŸber die attribute abholen
	public INode getPosition(INode inputNode) {
		
		// Muss die Node finden im Leaf Set -> Fabian mach Leaf Set Anpassungen -> warten
		return null;
	}
	
	//---------------------------------------------------
	
	// Recommendation geben ausgehend von gefundener Position
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
	
//	// Evaluate
//	public double evaluate(int k) {
//	
////		ArrayList <Double> rmse = new Arraylist<Double>();
//		Set<Set<INode>> dataSets = null;
//	
//		while (k > 0) {
//			for (Set<INode> dataset : dataSets) {
//	            
//	            // Build Cluster without testset
////	            if(dataset != dataset){
////					TreeBuilder treebuilder = new TreeBuilder(null, null, null, null, null, null);
////	            	treebuilder.cluster(dataset.toString());
//////	            }
//	           
//	           // Test testset against recommendations
//	          user = pickLine(dataSet)
//	          testUser = pickAttributes(user)
//	          position = find(node,root,0)
//	          recommendations = recommend(position,x,y)
//	          difference = calculateDifference(user,recommendations)
//	          
//	          rmse.add(root(difference/testUser.attributes.size))
//			}
//	    k--;
//		}
//	
//	
//		double rmse = sumRMSE / rmse.size;
//		return rmse;
//	}

	
//	// Incorporate
//	public void incorporate() {
//		
//	}
//	
//	// Add
//	public void add() {
//		
//	}

}
