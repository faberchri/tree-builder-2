package ch.uzh.agglorecommender.evaluator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import ch.uzh.agglorecommender.clusterer.TreeBuilder;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.recommender.RecommendationBuilder;

public class EvaluationBuilder {

	
	private INode rootU;
	private INode rootC;

	public double kFoldEvaluation(int k){
		// just run evaluate k times and calculate mean
		return 0;
	}
	
	public double evaluate(RecommendationBuilder rb) {
		
		// Get Predicitions
		Set<IAttribute> predictedMovieRatings = rb.runTestRecommendation();
		
		// Evaluate Predicitions	

		return 0;
		
////	ArrayList <Double> rmse = new Arraylist<Double>();
//	Set<Set<INode>> dataSets = null;
//
//	while (k > 0) {
//		for (Set<INode> dataset : dataSets) {
//            
//            // Build Cluster without testset
////            if(dataset != dataset){
////				TreeBuilder treebuilder = new TreeBuilder(null, null, null, null, null, null);
////            	treebuilder.cluster(dataset.toString());
//////            }
//           
//           // Test testset against recommendations
//          user = pickLine(dataSet)
//          testUser = pickAttributes(user)
//          position = find(node,root,0)
//          recommendations = recommend(position,x,y)
//          difference = calculateDifference(user,recommendations)
//          
//          rmse.add(root(difference/testUser.attributes.size))
//		}
//    k--;
//	}
//
//
//	double rmse = sumRMSE / rmse.size;
//	return rmse;
	}
	
	public INode pickRandomLeaf(TreeBuilder tree) {
		
		// Retrieve Root Nodes
		ArrayList<INode> rootNodes = tree.getRootNodes();
		this.rootU = rootNodes.get(0);
		this.rootC = rootNodes.get(1);
		
		// Pick random leaf from user tree
		INode tempNode = rootU; 
		while(tempNode.getChildrenCount() > 0) {
			Iterator<INode> children = tempNode.getChildren();
			
			// Pick random child -> ERROR: Not random yet, the last element is picked
			while(children.hasNext()){
				tempNode = children.next();
			}
		}
		
		return tempNode;
	}
	
	public INode createRandomUser() {
		return null;
	}
}
