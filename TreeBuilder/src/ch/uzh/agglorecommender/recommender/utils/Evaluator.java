package ch.uzh.agglorecommender.recommender.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import ch.uzh.agglorecommender.client.ClusterResult;
import ch.uzh.agglorecommender.clusterer.InitialNodesCreator;
import ch.uzh.agglorecommender.clusterer.treecomponent.CobwebAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.Node;
import ch.uzh.agglorecommender.recommender.RecommendationBuilder;

import com.google.common.collect.ImmutableMap;

/**
 * Instantiates a new evaluation builder
 * Evaluation builder offers evaluation methods (single,multiple) to calculate rmse
 * It also offers helper methods to pick test users randomly from test set
 * or create random users as input nodes for recommendations
 * 
 */
public class Evaluator {

	Random randomGenerator = new Random();
	
	/**
	 * Creates usable Map of test users for evaluation
	 * Helper method for recommendations of type 1 (rmse test)
	 * 
	 * @param testSet reference on the test set
	 */
	public Map<INode, Integer> getTestUsers(InitialNodesCreator testSet){
		
		Map<INode,Integer> testUsers = new HashMap<INode,Integer>();
		
		ImmutableMap<Integer, INode> userLeaves = testSet.getUserLeaves();
		for(Integer userLeaf : userLeaves.keySet()){
			testUsers.put(userLeaves.get(userLeaf), userLeaf);
		}
		
		return testUsers;
	}

	/**
	 * Runs multiple evaluations with different test nodes to eliminate variances
	 * 
	 * @param testNodes Set of nodes from test set
	 * @param rb this recommendation builder was initialized with the tree of the training set
	 * 
	 */
	public Map<String,Double> kFoldEvaluation(Map<INode,Integer> testNodes, RecommendationBuilder rb) throws NullPointerException{
		
		System.out.println("kFoldEvaluation started..");
			
		// Establish Maps
		Map<String,Double> eval = null;
		Map<String,Double> sumOfEval = new HashMap<String,Double>();
		sumOfEval.put("RMSE", 0.0);
		sumOfEval.put("AME", 0.0);
			
		// Calculate evaluation for all test nodes
		for(INode testNode : testNodes.keySet()) {
			
			eval = evaluate(testNode,rb);
				
			// Add up results
			for(String evMethod : sumOfEval.keySet()){
				if(eval.get(evMethod) != null){
					Double sumOfEValue = sumOfEval.get(evMethod);
					sumOfEValue += eval.get(evMethod);
					sumOfEval.put(evMethod, sumOfEValue);
				}
			}
		}
			
		// Take Mean of every value
		for(String eKey : sumOfEval.keySet()){
			Double meanEvalValue = sumOfEval.get(eKey) / testNodes.size();
			sumOfEval.put(eKey, meanEvalValue);
		}
		
		return eval;
	}

	/**
	 * Evaluates a given training-set based recommendation with a test node from a test set
	 * 
	 * @param testnode this node is from the test set
	 * @param rb this recommendation builder was initialized with the tree of the training set
	 */
	public Map<String,Double> evaluate(INode testNode, RecommendationBuilder rb) throws NullPointerException {
		
		if(testNode != null){
			
			// Get Predicitions & Real Values
			Map<Integer, IAttribute> predictedRatings = rb.runTestRecommendation(testNode);
			
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
			System.out.println("Input Node is null");
			return null;
		}
	}
	
	/**
	 * Calculate Evalution Value with Root Mean Squared Error (RMSE) Method
	 * 
	 * @param datasetIDs set of the real ratings
	 * @param testNode the node that needs to be compared
	 * @param predictedRatings set of the predicted ratings
	 * 
	 */
	public double calculateRMSE (INode testNode, Map<Integer, IAttribute> predictedRatings){
		
		// Calculate Difference of predicted values to real values
		double sumOfSquaredDifferences = 0;
		
		for(INode ratingKey: testNode.getNumericalAttributeKeys()) {
			
			// Calculate predicted rating - value could be null
			double pRating = 0;
			IAttribute pRatingAtt = predictedRatings.get((int)ratingKey.getDatasetId());
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
		double mse = sumOfSquaredDifferences / testNode.getNumericalAttributeKeys().size();
		
		// Take root
		double rmse = Math.sqrt(mse);
	
		return rmse;
	}
	
	/**
	 * Calculate Evalution Value as Absolute Min Error (AME)
	 * 
	 * @param datasetIDs set of the real ratings
	 * @param testNode the node that needs to be compared
	 * @param predictedRatings set of the predicted ratings
	 * 
	 */
	private double calculateAME(INode testNode,Map<Integer, IAttribute> predictedRatings) {
		
		// Calculate Difference of predicted values to real values
		double sumOfSquaredDifferences = 0;
		for(INode ratingKey: testNode.getNumericalAttributeKeys()) {
			
			// Calculate predicted rating - value could be null
			double pRating = 0;
			IAttribute pRatingAtt = predictedRatings.get((int)ratingKey.getDatasetId());
			if(pRatingAtt != null){
				pRating = pRatingAtt.getSumOfRatings() / pRatingAtt.getSupport();
			}
			
			// Calculate real rating
			IAttribute rRatingAtt = testNode.getNumericalAttributeValue(ratingKey);
			double rRating = rRatingAtt.getSumOfRatings() / rRatingAtt.getSupport();
			
			// Absolute Difference Squared
			sumOfSquaredDifferences += Math.pow(Math.abs(rRating - pRating),2);
		}
		
		// Division through number of Content Items
		double mse = sumOfSquaredDifferences / testNode.getNumericalAttributeKeys().size();
		
		// Take root
		double ame = Math.sqrt(mse);
	
		return ame;
	}
	
	/**
	 * Creates a random user with random ratings & demographics
	 * Helper method for recommendations of type 2 (recommend unknown content)
	 * 
	 * @param testRatings 
	 * @param testDemographics 
	 * @param testset reference on the test set
	 * 
	 * @return INode testUser
	 */
	public INode createTestUser(Map<INode, IAttribute> testRatings, Map<Object, IAttribute> testDemographics) {
		
		INode testUser = new Node(ENodeType.User, 0);
		testUser.setNumericalAttributes(testRatings);
		testUser.setNominalAttributes(testDemographics);
		
		return testUser;
	}
	
	/**
	 * Picks some random content from the tree that the user has to rate
	 * 
	 * @param limit number of movies that should be rated
	 * @param trainingOutput the tree that should be used to pick random content
	 * 
	 * @ return Map<INode,IAttribute> attribute map
	 */
	public Map<INode,IAttribute> rateRandomContent(ClusterResult trainingOutput, int limit) {
		
		Map<INode, IAttribute> contentRatings = new HashMap<INode, IAttribute>();
		int randomUserID = randomGenerator.nextInt(trainingOutput.getUserTreeLeavesMap().size());
		INode randomUser = trainingOutput.getUserTreeLeavesMap().get(randomUserID);
		
		int i = 0;
		for(INode contentKey : randomUser.getNumericalAttributeKeys()){
			
			if(i < limit){
				contentRatings.put(contentKey, randomUser.getNumericalAttributeValue(contentKey));
			}
			i++;
		}
		
		return contentRatings;
	}
	
	/**
	 * Creates the demographic data of the user
	 * 
	 * @ return Map<INode,IAttribute> attribute map
	 */
	public Map<Object,IAttribute> defineDemographics() {
		
		// Define demographic values
		Map<Object,IAttribute> demographics = new HashMap<Object,IAttribute>();
		
		Map<Object,Double> a1Map = new HashMap<Object,Double>();
		a1Map.put(24, 1.0);
		IAttribute a1  = new CobwebAttribute(a1Map);
		Map<Object,Double> a2Map = new HashMap<Object,Double>();
		a1Map.put("M", 1.0);
		IAttribute a2  = new CobwebAttribute(a2Map);
		Map<Object,Double> a3Map = new HashMap<Object,Double>();
		a1Map.put("Student", 1.0);
		IAttribute a3  = new CobwebAttribute(a3Map);
		
		demographics.put("Age",a1);
		demographics.put("Gender",a2);
		demographics.put("Occupation",a3);
		
		return demographics;
	}
}