package ch.uzh.agglorecommender.recommender;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.ExecutionException;

import ch.uzh.agglorecommender.clusterer.InitialNodesCreator;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.recommender.utils.NodeBuilder;
import ch.uzh.agglorecommender.recommender.utils.TreePosition;

import com.google.common.collect.ImmutableMap;

public class ClusterInteraction {
	
	private Searcher searcher;
	private Recommender recommender;
	private Evaluator evaluator;
	private NodeBuilder builder;
	private Inserter inserter;

	public ClusterInteraction(Searcher searcher){
		
		this.searcher 		= searcher;
		this.recommender 	= new Recommender(searcher);
		this.evaluator 		= new Evaluator(searcher);
		this.inserter	 	= new Inserter();
		this.builder		= new NodeBuilder(searcher);
		
	}
	
	public TreePosition getMostSimilarNode(INode inputNode) throws InterruptedException, ExecutionException{
		
		// Input Check
		if(inputNode == null){
			System.out.println("Received Illegal Input");
			return null;
		}
		
		return searcher.getMostSimilarNode(inputNode);
	}

	public String getMeta(INode node, String attribute){
		return searcher.getMeta(node, attribute);
	}

	public List<INode> getItemList(ENodeType type, int limit, INode inputNode){
		
		// Input Check
		if(type == null || inputNode == null){
			System.out.println("Received Illegal Input");
			return null;
		}
		
		return recommender.createItemList(type,limit, inputNode);
	}

	/**
	 * Calculates a recommendation that includes content the user has not rated yet
	 * The scope of nodes that is incorporated into the recommendation depends on defined radius
	 * This recommendation type does not allow a statistical check on the quality of the recommendation
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public SortedMap<INode, IAttribute> recommend(INode inputNode, TreePosition position) throws NullPointerException, InterruptedException, ExecutionException {
		
		// Input Check
		if(position == null || inputNode == null){
			System.out.println("Received Illegal Input");
			return null;
		}
		
		List<String> watched = recommender.createWatchedList(inputNode);
		Map<INode,IAttribute> unsortedRecommendation = recommender.recommend(position.getNode(),watched);
		SortedMap<INode, IAttribute> sortedRecommendation = recommender.rankRecommendation(unsortedRecommendation,1,15); // Pick Top Movies for User
		
		return sortedRecommendation;
	}
	
	public void printRecommendation (SortedMap<INode, IAttribute> recommendation){
		
		// Input Check
		if(recommendation == null){
			System.out.println("Received Illegal Input");
		}
		
		recommender.printRecommendation(recommendation);
	}
	
	public Map<String, Double> kFoldEvaluation(Set<INode> testNodes) throws NullPointerException, InterruptedException, ExecutionException {
		
		// Input Check
		if(testNodes == null){
			System.out.println("Received Illegal Input");
			return null;
		}
		
		return evaluator.kFoldEvaluation(testNodes);
	}
	
	public Map<String, Double> kFoldEvaluation(InitialNodesCreator testSet) throws NullPointerException, InterruptedException, ExecutionException {
		
		// Input Check
		if(testSet == null){
			System.out.println("Received Illegal Input");
			return null;
		}
		
		return evaluator.kFoldEvaluation(testSet);
	}

	public Map<String, Double> evaluate(INode inputNode, TreePosition position) throws NullPointerException, InterruptedException, ExecutionException{
		
		// Input Check
		if(inputNode == null || position == null){
			System.out.println("Received Illegal Input");
			return null;
		}
		
		return evaluator.evaluate(inputNode, position);
	}

	public boolean insert(INode inputNode, TreePosition position) throws InterruptedException, ExecutionException{
		
		// Input Check
		if(inputNode == null || position == null){
			System.out.println("Received Illegal Input");
			return false;
		}
		
		return inserter.insert(inputNode, position);
	}

	public INode buildNode(List<String> nomMetaInfo,List<String> numMetaInfo, List<String> ratings, ENodeType type){
		
		// Input Check
		if(nomMetaInfo == null || numMetaInfo == null || ratings == null || type == null){
			System.out.println("Received Illegal Input");
			return null;
		}
		
		return builder.buildNode(nomMetaInfo, numMetaInfo, ratings, type);
	}
	
	public ImmutableMap<String, INode> buildNodesFromFile(String ratingLoc, String metaLoc, ENodeType type){
		return builder.buildNodesFromFile(ratingLoc, metaLoc, type);
	}

}
