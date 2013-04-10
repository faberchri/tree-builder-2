package ch.uzh.agglorecommender.recommender;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.uzh.agglorecommender.clusterer.InitialNodesCreator;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.recommender.utils.TreePosition;

import com.google.common.collect.ImmutableMap;

/**
 * Instantiates a new evaluator, which offers evaluation 
 * methods (single, multiple) to calculate quality measurements
 * 
 */
public class Evaluator {
	
	private Searcher searcher;

	/**
	 * Needs to be instantiated with a reference to a searcher instance,
	 * which provides connections to the trees
	 * 
	 * @param searcher
	 */
	public Evaluator(Searcher searcher){
		this.searcher = searcher;	
	}
	
	/**
	 * Evaluates a testset and delivers mean values of measurements
	 * 
	 * @param testSet testset collection created with initialNodesCreator
	 * @return Map a map of different evaluation measurements of multiple nodes
	 */
	public Map<String,Double> kFoldEvaluation(InitialNodesCreator testSet){
	
		// Reformat testSet
		Set<INode> testNodes = reformatTestSet(testSet);
		
		return kFoldEvaluation(testNodes);
	}
	
	/**
	 * Evaluates multiple test nodes and delivers mean values of measurements
	 * 
	 * @param testNodes collection of nodes to be evaluated
	 * @return Map a map of different evaluation measurements of multiple nodes
	 */
	public Map<String, Double> kFoldEvaluation(Set<INode> testNodes){	
		
		System.out.println("kFoldEvaluation started..");

		// Establish Maps
		Map<String,Double> eval = null;
		Map<String,Double> totalEvalValue = new HashMap<String,Double>();
		totalEvalValue.put("RMSE", 0.0);
		totalEvalValue.put("AME", 0.0);
			
		// Calculate evaluation for all test nodes and update mean
		for(INode testNode : testNodes) {
			
			TreePosition position = searcher.getMostSimilarNode(testNode);
			eval = evaluate(testNode, position);
				
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
	 * Evaluates the quality of chosen position as most similar node
	 * 
	 * @param testNode this node has to contain real ratings that can be tested on predicted values
	 * @param position this is the most similar position to the testNode in the tree
	 * @return Map a map of different evaluation measurements
	 */
	public Map<String,Double> evaluate(INode testNode, TreePosition position){
		
		if(testNode != null && position != null){
			
			// Get Predicitions & Real Values
			Map<String, IAttribute> predictedRatings = collectRatings(position.getNode(),testNode,null);
			
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
	
	/**
	 * Prints collection of evaluation measurements
	 * 
	 * @param eval collection of evaluation
	 */
	public void printEvaluationResult(Map<String, Double> eval){
		if(eval != null){
			System.out.println("=> Calculated Evaluation Values: " + eval.toString());
		}
	}

	/**
	 * Collects ratings of all contained nodes of the testNode starting
	 * from the given position
	 * 
	 * @param position this is the starting point for collecting
	 * @param testNode this node defines the content that needs to be collected
	 * @return Map the key is the datasetItem ID and the value is an attribute
	 */
	private Map<String, IAttribute> collectRatings(INode position, INode testNode, Map<String, IAttribute> ratingList){
		
		// Create Ratings Map with empty values if it does not already exist
		if(ratingList == null) {
			ratingList = new HashMap<String,IAttribute>(); // DatasetID, AttributeData
			
			Set<INode> numInputKeys = testNode.getRatingAttributeKeys();
			
			for(INode numInputKey : numInputKeys) {
				ratingList.put(numInputKey.getDatasetId(),null);
			}
		}
		
		// Look for content nodes on the list and add it to collected ratings map		
		Set<INode> posRatingKeys = position.getRatingAttributeKeys();
		for(INode posRatingKey : posRatingKeys){
			
			// Need all dataset item ids
			List<String> datasetIds = posRatingKey.getDataSetIds();
			for(String searchDatasetId : ratingList.keySet()){
	
				if(ratingList.get(searchDatasetId) == null){
					if(datasetIds.contains(searchDatasetId)){
						ratingList.put(searchDatasetId, position.getNumericalAttributeValue(posRatingKey));
					}
				}
			}
		}
			
		// Check if all movies were found
		if(ratingList.containsValue(null) != true){
			return ratingList;
		}
		else {
			
			// Go one level up if possible
			if(position.getParent() != null) {
				ratingList = collectRatings(position.getParent(),testNode,ratingList);
				if (ratingList != null){
					return ratingList;
				}
			}
			else {
				return ratingList;
			}
		}
		return null;
	}

	/**
	 * Rebuilds testset collection to usable format
	 * 
	 * @param testset the testset
	 */
	private Set<INode> reformatTestSet(InitialNodesCreator testset){
		
		Set<INode> testUsers = new HashSet<>();
		
		ImmutableMap<String, INode> userLeaves = testset.getUserLeaves();
		for(String userLeaf : userLeaves.keySet()){
			testUsers.add(userLeaves.get(userLeaf));
		}
		
		return testUsers;
	}

	/**
	 * Calculate evaluation value with Root Mean Squared Error (RMSE) Method
	 * 
	 * @param testNode the node that contains the real ratings
	 * @param predictedRatings set of the predicted ratings
	 */
	private double calculateRMSE (INode testNode, Map<String, IAttribute> predictedRatings){
		
		// Calculate Difference of predicted values to real values
		double sumOfSquaredDifferences = 0;
		
		for(INode ratingKey: testNode.getRatingAttributeKeys()) {
			
			// Calculate real rating
			IAttribute rRatingAtt = testNode.getNumericalAttributeValue(ratingKey);
			double rRating = rRatingAtt.getSumOfRatings() / rRatingAtt.getSupport();
			
			// Calculate predicted rating
			double pRating = 0;
			IAttribute pRatingAtt = predictedRatings.get(ratingKey.getDatasetId());
			if(pRatingAtt != null){
				pRating = pRatingAtt.getSumOfRatings() / pRatingAtt.getSupport();
			}
			
			// Calculate squared difference
			sumOfSquaredDifferences += Math.pow(rRating - pRating,2);
		}
		
		// Divide sum of squared differences through number of ratings, take root
		return Math.sqrt(sumOfSquaredDifferences / testNode.getRatingAttributeKeys().size());
	}
	
	/**
	 * Calculate evaluation value with Absolute Mean Error (AME) Method
	 * 
	 * @param testNode the node that contains the real ratings
	 * @param predictedRatings set of the predicted ratings
	 */
	private double calculateAME(INode testNode,Map<String, IAttribute> predictedRatings){
		
		// Calculate Difference of predicted values to real values
		double sumOfDifferences = 0;
		for(INode ratingKey: testNode.getRatingAttributeKeys()) {
			
			// Calculate real rating
			IAttribute rRatingAtt = testNode.getNumericalAttributeValue(ratingKey);
			double rRating = rRatingAtt.getSumOfRatings() / rRatingAtt.getSupport();
			
			// Calculate predicted rating - value could be null
			double pRating = 0;
			IAttribute pRatingAtt = predictedRatings.get(ratingKey.getDatasetId());
			if(pRatingAtt != null){
				pRating = pRatingAtt.getSumOfRatings() / pRatingAtt.getSupport();
			}
			
			// Calculate absolute difference
			sumOfDifferences += Math.abs(rRating - pRating);
		}
		
		// Divide sum of absolute differences through number of ratings
		return sumOfDifferences / testNode.getRatingAttributeKeys().size();
	}
}