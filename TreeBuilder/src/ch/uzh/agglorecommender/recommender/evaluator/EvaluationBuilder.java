package ch.uzh.agglorecommender.recommender.evaluator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import ch.uzh.agglorecommender.client.InitialNodesCreator;
import ch.uzh.agglorecommender.clusterer.treecomponent.ClassitTreeComponentFactory;
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
public class EvaluationBuilder {

	Random randomGenerator = new Random();
	
	/**
	 * Evaluates a given training-set based recommendation with a test node from a test set
	 * 
	 * @param testnode this node is from the test set
	 * @param rb this recommendation builder was initialized with the tree of the training set
	 */
	public double evaluate(INode testNode, int testNodeID, RecommendationBuilder rb) throws NullPointerException {
		
		if(testNode != null){
			// Get Predicitions & Real Values
			Map<INode, IAttribute> predictedRatings = rb.runTestRecommendation(testNode,testNodeID);
			
			if(predictedRatings != null) {
				Set<INode> realRatingsKeys = testNode.getAttributeKeys();
				
				// Calculate Difference of predicted values to real values
				double sumOfSquaredDifferences = 0;
				for(INode ratingKey: realRatingsKeys) {
					
					// Calculate predicted rating - value could be null
					double pRating = 0;
					IAttribute pRatingAtt = predictedRatings.get(ratingKey);
					if(pRatingAtt != null){
						pRating = pRatingAtt.getSumOfRatings() / pRatingAtt.getSupport();
					}
					//System.out.println("pRating: " + pRating);
					
					// Calculate real rating
					IAttribute rRatingAtt = testNode.getAttributeValue(ratingKey);
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
			else {
				System.out.println("Recommendation Data is null");
				return -1;
			}
		}
		else {
			System.out.println("Input Node is null");
			return -1;
		}
	}
	
	/**
	 * Runs multiple evaluations with different test nodes to eliminate variances
	 * 
	 * @param testNodes Set of nodes from test set
	 * @param rb this recommendation builder was initialized with the tree of the training set
	 * 
	 */
	public double kFoldEvaluation(Map<INode,Integer> testNodes, RecommendationBuilder rb) throws NullPointerException{
		
		try {
			double sumOfRMSE = 0;
			
			// Get all RMSE Values
			for(INode testNode : testNodes.keySet()) {
				sumOfRMSE += evaluate(testNode,testNodes.get(testNode),rb);
			}
			
			// Take Mean
			double rmse = sumOfRMSE / testNodes.size();
			
			return rmse;
		}
		catch (NullPointerException e) {
			return -1;
		}
		
	}
	
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
	 * Creates a random user with random ratings 
	 * Helper method for recommendations of type 2 (recommend unknown content)
	 * 
	 * @param testset reference on the test set
	 */
	public INode createRandomUser() {
		
		// Creating the content nodes
		INode A1n = new Node(ENodeType.Content, null, null);
		INode A2n = new Node(ENodeType.Content, null, null);
		INode A3n = new Node(ENodeType.Content, null, null);
		INode A4n = new Node(ENodeType.Content, null, null);
		
		A1n.setId(3);
		A2n.setId(47);
		A3n.setId(5);
		A4n.setId(6);
		
		// Creating the attributes
		IAttribute A1a = ClassitTreeComponentFactory.getInstance().createAttribute(randomGenerator.nextInt(10));
		IAttribute A2a = ClassitTreeComponentFactory.getInstance().createAttribute(randomGenerator.nextInt(10));
		IAttribute A3a = ClassitTreeComponentFactory.getInstance().createAttribute(randomGenerator.nextInt(10));
		IAttribute A4a = ClassitTreeComponentFactory.getInstance().createAttribute(randomGenerator.nextInt(10));

		// ClassitAttribute map
		Map<INode, IAttribute> attMap = new HashMap<INode, IAttribute>();
		attMap.put(A1n, A1a);
		attMap.put(A2n, A2a);
		attMap.put(A3n, A3a);
		attMap.put(A4n, A4a);

		// Create the user node
		INode randomUser = new Node(ENodeType.User, null, null);
		randomUser.setAttributes(attMap);
		randomUser.setId(1);
		
		return randomUser;
	}
}