package ch.uzh.agglorecommender.recommender.evaluator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import ch.uzh.agglorecommender.client.InitalNodesCreator;
import ch.uzh.agglorecommender.clusterer.treecomponent.ClassitTreeComponentFactory;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.Node;
import ch.uzh.agglorecommender.clusterer.treesearch.IndexAwareSet;
import ch.uzh.agglorecommender.recommender.RecommendationBuilder;

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
	public double evaluate(INode testNode, RecommendationBuilder rb) throws NullPointerException {
		
		try{
			// Get Predicitions & Real Values
			Map<INode, IAttribute> predictedRatings = rb.runTestRecommendation(testNode);
			
			try {
				Set<INode> realRatingsKeys = testNode.getAttributeKeys();
				
				// Calculate Difference of predicted values to real values
				double sumOfSquaredDifferences = 0;
				for(INode ratingKey: realRatingsKeys) {
					
					IAttribute pRatingAtt = predictedRatings.get(ratingKey);
					double pRating = pRatingAtt.getSumOfRatings() / pRatingAtt.getSupport();
					//System.out.println("pRating: " + pRating);
					
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
			catch (NullPointerException e) {
				System.out.println("Recommendation Data fehlerhaft/null");
				return -1;
			}
		}
		catch (NullPointerException e) {
			System.out.println("Input Node fehlerhaft/null");
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
	public double kFoldEvaluation(Set<INode> testNodes, RecommendationBuilder rb) throws NullPointerException{
		
		try {
			double sumOfRMSE = 0;
			
			// Get all RMSE Values
			for(INode testNode : testNodes) {
				sumOfRMSE += evaluate(testNode,rb);
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
	 * Randomly picks certain percentage of test users from the test set
	 * Helper method for recommendations of type 1 (rmse test)
	 * 
	 * @param testSet reference on the test set
	 * @param percentage how many of the test users should be picked
	 */
	public Set<INode> pickTestUsers(InitalNodesCreator testSet, double percentage){
		
		Set<INode> testUsers = new IndexAwareSet<INode>();
		
		// Dirty trick for now
		for(int i = 0; i < 2; i++){
			testUsers.add(createRandomUser());
		}
		
		return testUsers;
	}
	
	/**
	 * Creates a random user 
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