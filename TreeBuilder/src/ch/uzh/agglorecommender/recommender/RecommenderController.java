package ch.uzh.agglorecommender.recommender;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.recommender.utils.TreePosition;

public class RecommenderController {
	
	private RecommendationModel rm;

	public RecommenderController(RecommendationModel rm){
		this.rm = rm;
	}
	
	/**
	 * Calculates a recommendation that includes content the user has not rated yet
	 * The scope of nodes that is incorporated into the recommendation depends on defined radius
	 * This recommendation type does not allow a statistical check on the quality of the recommendation
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public Map<INode, IAttribute> runRecommendation(INode inputNode) throws NullPointerException, InterruptedException, ExecutionException {
		
		// Find the most similar node in the tree
		TreePosition position = rm.findMostSimilar(inputNode);
		
//		if(position == null){
//			System.out.println("Starting Position was not found");
//			return null;
//		}
//		else {
//			System.out.println("Found similar user: " + position.toString());
//		}
		
		// Create List of movies that the user has already rated (user is leaf and has leaf attributes)
		List<String> watched = rm.createWatchedList(inputNode);
		
		// Calculate recommendation based on this position
		Map<INode,IAttribute> recommended = rm.recommend(position.getNode(),watched);
		
		return recommended;
	}
	
	/**
	 * Runs a test that compares the tree calculation with the real values of a given user
	 * This recommendation type allows to calculate an RMSE value that indicates the quality
	 * of the recommendations produced by the clusterer
	 */
	public Map<String, IAttribute> runQuantitativeTest(INode testNode, INode position){
		
		try {
			// Collect ratings of all content given by the input node
			Map<String, IAttribute> contentRatings = rm.collectRatings(position,testNode,null);
			return contentRatings;
		}
		catch (Exception e){
			return null;
		}
	}

}
