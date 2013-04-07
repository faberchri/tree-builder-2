package ch.uzh.agglorecommender.recommender;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.ExecutionException;

import ch.uzh.agglorecommender.clusterer.InitialNodesCreator;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.recommender.utils.Evaluator;
import ch.uzh.agglorecommender.recommender.utils.NodeBuilder;
import ch.uzh.agglorecommender.recommender.utils.NodeInserter;
import ch.uzh.agglorecommender.recommender.utils.TreePosition;

public class RecommenderProxy {
	
	// Connection to Model
	private Recommender rm;
	
	// Connection to Tools
	private Evaluator evaluator;
	private NodeBuilder builder;
	private NodeInserter inserter;

	public RecommenderProxy(Recommender rm){
		
		this.rm 			= rm;
		this.evaluator 		= new Evaluator(rm);
		this.builder		= new NodeBuilder(rm);
		this.inserter	 	= new NodeInserter();
		
	}
	
	public List<INode> getItemList(ENodeType type, int limit, INode inputNode){
		return rm.createItemList(type,limit, inputNode);
	}

	public String getMeta(INode node, String attribute){
		return rm.getMeta(node, attribute);
	}

	public INode buildNode(List<String> nomMetaInfo,List<String> numMetaInfo, List<String> ratings, ENodeType type){
		return builder.buildNode(nomMetaInfo, numMetaInfo, ratings, type);
	}

	public TreePosition getSimilarPosition(INode inputNode) throws InterruptedException, ExecutionException{
		return rm.findMostSimilar(inputNode);
	}

	/**
	 * Calculates a recommendation that includes content the user has not rated yet
	 * The scope of nodes that is incorporated into the recommendation depends on defined radius
	 * This recommendation type does not allow a statistical check on the quality of the recommendation
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public SortedMap<INode, IAttribute> recommend(INode inputNode, TreePosition position) throws NullPointerException, InterruptedException, ExecutionException {
		
		if(position == null){
			System.out.println("Starting Position was not found");
			return null;
		}
		
		List<String> watched = rm.createWatchedList(inputNode);
		Map<INode,IAttribute> unsortedRecommendation = rm.recommend(position.getNode(),watched);
		SortedMap<INode, IAttribute> sortedRecommendation = rm.rankRecommendation(unsortedRecommendation,1,15); // Pick Top Movies for User
		
		return sortedRecommendation;
	}
	
	public boolean insert(INode inputNode, TreePosition position) throws InterruptedException, ExecutionException{
		return inserter.insert(inputNode, position);
	}
	
	public Map<String, Double> kFoldEvaluation(InitialNodesCreator testSet) throws NullPointerException, InterruptedException, ExecutionException {
		return evaluator.kFoldEvaluation(testSet);
	}

	public Map<String, Double> evaluate(INode inputNode) throws NullPointerException, InterruptedException, ExecutionException{
		return evaluator.evaluate(inputNode);
	}

}
