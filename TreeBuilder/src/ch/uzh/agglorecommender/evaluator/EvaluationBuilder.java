package ch.uzh.agglorecommender.evaluator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import ch.uzh.agglorecommender.clusterer.TreeBuilder;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.TreeComponentFactory;
import ch.uzh.agglorecommender.recommender.RecommendationBuilder;

public class EvaluationBuilder {

	
	private INode rootU;
	private INode rootC;
	
	private TreeComponentFactory userTreeComponentFactory;
	Random randomGenerator = new Random();

	public double kFoldEvaluation(INode inputNode, RecommendationBuilder rb, int k){
		
		double sumOfRMSE = 0;
		
		// Get all RMSE Values
		while(k > 0) {
			sumOfRMSE += evaluate(inputNode,rb);
		}
		
		// Take Mean
		double rmse = sumOfRMSE / k;
		
		return rmse;
	}
	
	public double evaluate(INode inputNode, RecommendationBuilder rb) {
		
		// Get Predicitions & Real Values
		Map<INode, IAttribute> predictedRatings = rb.runTestRecommendation();
//		System.out.println("These are the predictions: " + predictedRatings.toString());
		Set<INode> realRatingsKeys = inputNode.getAttributeKeys();
		
		// Calculate Difference of predicted values to real values
		double sumOfSquaredDifferences = 0;
		for(INode ratingKey: realRatingsKeys) {
			
			IAttribute pRatingAtt = predictedRatings.get(ratingKey);
			double pRating = pRatingAtt.getSumOfRatings() / pRatingAtt.getSupport();
			//System.out.println("pRating: " + pRating);
			
			IAttribute rRatingAtt = inputNode.getAttributeValue(ratingKey);
			double rRating = rRatingAtt.getSumOfRatings() / rRatingAtt.getSupport();
			//System.out.println("rRating: " + rRating);
			
			sumOfSquaredDifferences += Math.pow(rRating - pRating,2);
		}
		//System.out.println("ssd: " + sumOfSquaredDifferences);
		
		// Division through number of Content Items
		double mse = sumOfSquaredDifferences / realRatingsKeys.size();
		//System.out.println("mse: " + mse);
		
		// Take root
		double rmse = Math.sqrt(mse);
		//System.out.println("rmse: " + rmse);
	
		return rmse;
	}
	
	// Quick Solution for Recommendations of Type 1 Testing -> Not a very useful solution, adds movies afterwards
	public INode pickRandomLeaf(TreeBuilder tb, ENodeType type) {
		
		// Retrieve Root Nodes
		ArrayList<INode> rootNodes = tb.getRootNodes();
		this.rootU = rootNodes.get(0);
		this.rootC = rootNodes.get(1);
		
		INode tempNode = null;
		
		if(type == ENodeType.User){
			tempNode = rootU;
		}
		
		if(type == ENodeType.Content){
			tempNode = rootC;
		}
		
		// Pick random leaf from user tree
		while(tempNode.getChildrenCount() > 0) {
			Iterator<INode> children = tempNode.getChildren();
			
			// Pick random child -> ERROR: Not random yet, the last element is picked
			while(children.hasNext()){
				tempNode = children.next();
			}
		}
		
		// Add more movies to it if Type is user
		if(type == ENodeType.User){
//			INode movie1key = pickRandomLeaf(tb,ENodeType.Content);
//			IAttribute movie1att = userTreeComponentFactory.createAttribute(randomGenerator.nextInt(10));
//			tempNode.addAttribute(movie1key,movie1att);
//			INode movie2key = pickRandomLeaf(tb,ENodeType.Content);
//			IAttribute movie2att = userTreeComponentFactory.createAttribute(randomGenerator.nextInt(10));
//			tempNode.addAttribute(movie2key,movie2att);
//			INode movie3key = pickRandomLeaf(tb,ENodeType.Content);
//			IAttribute movie3att = userTreeComponentFactory.createAttribute(randomGenerator.nextInt(10));
//			tempNode.addAttribute(movie3key,movie3att);
		}
		
		return tempNode;
	}
	
	// Needed for Recommendations of Type 2 -> Just using pickRandomLeaf at the moment
	public INode createRandomUser(TreeBuilder tb) {
		INode randomUser = pickRandomLeaf(tb,ENodeType.User);
		return randomUser;
	}
}
