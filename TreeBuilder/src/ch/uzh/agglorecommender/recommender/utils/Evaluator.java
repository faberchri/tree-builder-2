package ch.uzh.agglorecommender.recommender.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import ch.uzh.agglorecommender.clusterer.InitialNodesCreator;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.recommender.RecommendationModel;

import com.google.common.collect.ImmutableMap;

/**
 * Instantiates a new evaluation builder
 * Evaluation builder offers evaluation methods (single,multiple) to calculate rmse
 * It also offers helper methods to pick test users randomly from test set
 * or create random users as input nodes for recommendations
 * 
 */
public class Evaluator {
	
	private RecommendationModel rm;

	public Evaluator(RecommendationModel rm){
		this.rm	= rm;	
	}
	
	/**
	 * Runs multiple evaluations with different test nodes to eliminate variances
	 * 
	 * @param testNodes Set of nodes from test set
	 * @param rc this recommendation builder was initialized with the tree of the training set
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * 
	 */
	public Map<String,Double> kFoldEvaluation(InitialNodesCreator testSet) throws NullPointerException, InterruptedException, ExecutionException{
		
		System.out.println("kFoldEvaluation started..");
		
		// Reformat testSet
		Map<INode,String> testNodes = reformatTestSet(testSet);
		
		// Establish Maps
		Map<String,Double> eval = null;
		Map<String,Double> totalEvalValue = new HashMap<String,Double>();
		totalEvalValue.put("RMSE", 0.0);
		totalEvalValue.put("AME", 0.0);
			
		// Calculate evaluation for all test nodes and update mean
		for(INode testNode : testNodes.keySet()) {
			
			eval = evaluate(testNode);
				
			for(String evalMethod : totalEvalValue.keySet()){
				if(eval.get(evalMethod) != null){
					Double currentSum = totalEvalValue.get(evalMethod);
					currentSum += eval.get(evalMethod);
					totalEvalValue.put(evalMethod, currentSum);
				}
			}
		}
			
		// Take Mean of every value
		for(String evalMethod : totalEvalValue.keySet()){
			Double meanEvalValue = totalEvalValue.get(evalMethod) / testNodes.size();
			totalEvalValue.put(evalMethod, meanEvalValue);
		}
		
		// Print
		printEvaluationResult(totalEvalValue);
		
		return totalEvalValue;
	}

	/**
	 * Evaluates a given training-set based recommendation with a test node from a test set
	 * 
	 * @param testnode this node is from the test set
	 * @param rc this recommendation builder was initialized with the tree of the training set
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public Map<String,Double> evaluate(INode testNode) throws NullPointerException, InterruptedException, ExecutionException {
		
		if(testNode != null){
			
			// Find position of the similar node in the tree
			TreePosition position = rm.findMostSimilar(testNode);
//			System.out.println("found position" + position.toString());
			
			// Get Predicitions & Real Values
			Map<String, IAttribute> predictedRatings = rm.collectRatings(position.getNode(),testNode,null);
			
			if(predictedRatings != null) {
				
				Map<String,Double> eval = new HashMap<String,Double>();
				
				// Pick the real ratings for the predicted ratings from the recommendation
				eval.put("RMSE",calculateRMSE(testNode, predictedRatings));
				eval.put("AME",calculateAME(testNode, predictedRatings));
				
				return eval;
				
			}
			else {
				System.out.println("Recommendation Data is null");
				return null;
			}
		}
		else {
			System.out.println("InputParser Node is null");
			return null;
		}
	}
	
	public void printEvaluationResult(Map<String, Double> eval) {
		if(eval != null){
			System.out.println("=> Calculated Evaluation Values: " + eval.toString());
		}
	}

	/**
	 * Creates usable Map of test users for evaluation
	 * Helper method for recommendations of type 1 (rmse test)
	 * 
	 * @param testSet reference on the test set
	 */
	private Map<INode, String> reformatTestSet(InitialNodesCreator testSet){
		
		Map<INode,String> testUsers = new HashMap<INode,String>();
		
		ImmutableMap<String, INode> userLeaves = testSet.getUserLeaves();
		for(String userLeaf : userLeaves.keySet()){
			testUsers.put(userLeaves.get(userLeaf), userLeaf);
		}
		
		return testUsers;
	}

	/**
	 * Calculate Evalution Value with Root Mean Squared Error (RMSE) Method
	 * 
	 * @param datasetIDs set of the real ratings
	 * @param testNode the node that needs to be compared
	 * @param predictedRatings set of the predicted ratings
	 * 
	 */
	private double calculateRMSE (INode testNode, Map<String, IAttribute> predictedRatings){
		
		// Calculate Difference of predicted values to real values
		double sumOfSquaredDifferences = 0;
		
		for(INode ratingKey: testNode.getRatingAttributeKeys()) {
			
			// Calculate predicted rating - value could be null
			double pRating = 0;
			IAttribute pRatingAtt = predictedRatings.get(ratingKey.getDatasetId());
			if(pRatingAtt != null){
				pRating = pRatingAtt.getSumOfRatings() / pRatingAtt.getSupport();
			}
			
			// Calculate real rating
			IAttribute rRatingAtt = testNode.getNumericalAttributeValue(ratingKey);
			double rRating = rRatingAtt.getSumOfRatings() / rRatingAtt.getSupport();
			
			// Real Difference Squared
			sumOfSquaredDifferences += Math.pow(rRating - pRating,2);
		}
		
		// Division through number of Content Items
		double mse = sumOfSquaredDifferences / testNode.getRatingAttributeKeys().size();
		
		// Take root
		return Math.sqrt(mse);
	}
	
	/**
	 * Calculate Evalution Value as Absolute Min Error (AME)
	 * 
	 * @param datasetIDs set of the real ratings
	 * @param testNode the node that needs to be compared
	 * @param predictedRatings set of the predicted ratings
	 * 
	 */
	private double calculateAME(INode testNode,Map<String, IAttribute> predictedRatings) {
		
		// Calculate Difference of predicted values to real values
		double sumOfDifferences = 0;
		for(INode ratingKey: testNode.getRatingAttributeKeys()) {
			
			// Calculate predicted rating - value could be null
			double pRating = 0;
			IAttribute pRatingAtt = predictedRatings.get(ratingKey.getDatasetId());
			if(pRatingAtt != null){
				pRating = pRatingAtt.getSumOfRatings() / pRatingAtt.getSupport();
			}
			
			// Calculate real rating
			IAttribute rRatingAtt = testNode.getNumericalAttributeValue(ratingKey);
			double rRating = rRatingAtt.getSumOfRatings() / rRatingAtt.getSupport();
			
			// Absolute Difference Squared
			sumOfDifferences += Math.abs(rRating - pRating);
		}
		
		// Division through number of Content Items
		return sumOfDifferences / testNode.getRatingAttributeKeys().size();
	}
}