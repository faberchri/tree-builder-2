package ch.uzh.agglorecommender.recommender.evaluator;

import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.jfree.data.general.Dataset;

import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.recommender.RecommendationBuilder;

/**
 * Instantiates a new evaluation builder
 * Evaluation builder offers evaluation methods (single,multiple) to calculate rmse
 * It also offers helper methods to pick test users randomly from test set
 * or create random users as input nodes for recommendations
 * 
 */
public class EvaluationBuilder {

//	private INode rootU;
//	private INode rootC;
//	private TreeComponentFactory userTreeComponentFactory;
	Random randomGenerator = new Random();
	
//	/**
//	 * Instantiates a new evaluation builder
//	 * 
//	 */
//	public EvaluationBuilder() {
//		// No parameters necessary
//	}
	
	/**
	 * Evaluates a given training-set based recommendation with a test node from a test set
	 * 
	 * @param testnode this node is from the test set
	 * @param rb this recommendation builder was initialized with the tree of the training set
	 */
	public double evaluate(INode testNode, RecommendationBuilder rb) throws NullPointerException {
		
		// Test on input data
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
				return 0;
			}
		}
		catch (NullPointerException e) {
			System.out.println("Input Node fehlerhaft/null");
			return 0;
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
	 * @param testset reference on the test set
	 * @param percentage how many of the test users should be picked
	 */
	public Set<INode> pickTestUser(Dataset testset, double percentage){
		return null; // Implement
	}
	
	/**
	 * Creates a random user 
	 * Helper method for recommendations of type 2 (recommend unknown content)
	 * 
	 * @param testset reference on the test set
	 */
	public INode createRandomUser() {
		INode randomUser = null; // Implement
	return randomUser;
}
	
	// ---------- For deletion ------------------------------------
	
//	// Quick Solution for Recommendations of Type 1 Testing -> Not a very useful solution, adds movies afterwards
//	public INode pickRandomLeaf(TreeBuilder tb, ENodeType type) {
//		
//		// Retrieve Root Nodes
//		ArrayList<INode> rootNodes = tb.getRootNodes();
//		this.rootU = rootNodes.get(0);
//		this.rootC = rootNodes.get(1);
//		
//		INode tempNode = null;
//		
//		if(type == ENodeType.User){
//			tempNode = rootU;
//		}
//		
//		if(type == ENodeType.Content){
//			tempNode = rootC;
//		}
//		
//		// Pick random leaf from user tree
//		while(tempNode.getChildrenCount() > 0) {
//			Iterator<INode> children = tempNode.getChildren();
//			
//			// Pick random child -> ERROR: Not random yet, the last element is picked
//			while(children.hasNext()){
//				tempNode = children.next();
//			}
//		}
//		
//		// Add more movies to it if Type is user
//		if(type == ENodeType.User){
////			INode movie1key = pickRandomLeaf(tb,ENodeType.Content);
////			IAttribute movie1att = userTreeComponentFactory.createAttribute(randomGenerator.nextInt(10));
////			tempNode.addAttribute(movie1key,movie1att);
////			INode movie2key = pickRandomLeaf(tb,ENodeType.Content);
////			IAttribute movie2att = userTreeComponentFactory.createAttribute(randomGenerator.nextInt(10));
////			tempNode.addAttribute(movie2key,movie2att);
////			INode movie3key = pickRandomLeaf(tb,ENodeType.Content);
////			IAttribute movie3att = userTreeComponentFactory.createAttribute(randomGenerator.nextInt(10));
////			tempNode.addAttribute(movie3key,movie3att);
//		}
//		
//		return tempNode;
//	}
}
