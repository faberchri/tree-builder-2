package ch.uzh.agglorecommender.recommender;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import ch.uzh.agglorecommender.clusterer.InitialNodesCreator;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.recommender.utils.NodeBuilder;
import ch.uzh.agglorecommender.recommender.utils.TreePosition;

import com.google.common.collect.ImmutableMap;

/**
 * This class acts as a proxy for multiple ways of interaction with the
 * tree resulting from the clustering
 *
 */
public class ClusterInteraction {
	
	private Searcher searcher;
	private Recommender recommender;
	private Evaluator evaluator;
	private NodeBuilder builder;
	private Inserter inserter;

	/**
	 * The class needs to be instantiated with a reference
	 * to a searcher Class instance which acts as a connector
	 * to the leaf and root nodes of the tree and is needed by
	 * the integrated tools
	 * 
	 * @param searcher
	 */
	public ClusterInteraction(Searcher searcher){
		
		this.searcher 		= searcher;
		this.recommender 	= new Recommender(searcher);
		this.evaluator 		= new Evaluator(searcher);
		this.inserter	 	= new Inserter();
		this.builder		= new NodeBuilder(searcher);
		
	}
	
	/**
	 * Proxy method to find most similar node
	 * 
	 * @param inputNode the node to find the most similar
	 * @return INode the most similar node
	 */
	public TreePosition getMostSimilarNode(INode inputNode) {
		
		// Input Check
		if(inputNode == null){
			System.out.println("Received Illegal Input");
			return null;
		}
		
		return searcher.getMostSimilarNode(inputNode);
	}

	/**
	 * Proxy method to find meta information from node
	 * 
	 * @param node node to find meta info
	 * @param attribute the information that should be returned
	 * @return meta information
	 */
	public String getMeta(INode node, String attribute){
		return searcher.getMeta(node, attribute);
	}

	/**
	 * Proxy method to retrieve a random list of available users or content items
	 * 
	 * @param type type of nodes
	 * @param limit number of nodes
	 * @param inputNode items already contained in this item are skipped
	 * @return List generated list of nodes
	 */
	public List<INode> getItemList(ENodeType type, int limit, INode inputNode){
		
		// Input Check
		if(type == null || inputNode == null){
			System.out.println("Received Illegal Input");
			return null;
		}
		
		return recommender.createItemList(type,limit, inputNode);
	}

	/**
	 * Proxy method to create a recommendation
	 * 
	 * @param inputNode nodes contained in this node are skipped in the recommendation
	 * @param position recommendation starts at this position
	 * @return SortedMap a sorted collection of recommended nodes
	 */
	public SortedMap<INode, IAttribute> recommend(INode inputNode, TreePosition position){
		
		// Input Check
		if(position == null || inputNode == null){
			System.out.println("Received Illegal Input");
			return null;
		}
		
		List<String> watched = recommender.createKnownList(inputNode);
		Map<INode,IAttribute> unsortedRecommendation = recommender.recommend(position.getNode(),watched);
		SortedMap<INode, IAttribute> sortedRecommendation = recommender.rankRecommendation(unsortedRecommendation); // Pick Top Movies for User
		
		return sortedRecommendation;
	}
	
	/**
	 * Proxy method to print recommendation collection
	 * 
	 * @param recommendation collection of recommendations
	 */
	public void printRecommendation (SortedMap<INode, IAttribute> recommendation){
		
		// Input Check
		if(recommendation == null){
			System.out.println("Received Illegal Input");
		}
		
		recommender.printRecommendation(recommendation);
	}
	
	/**
	 * Proxy method to evaluate a testset and receive mean values
	 * 
	 * @param testSet collection created with initialNodesCreator
	 * @return Map a map of different evaluation measurements of multiple nodes
	 */
	public Map<String, Double> kFoldEvaluation(InitialNodesCreator testSet){
		
		// Input Check
		if(testSet == null){
			System.out.println("Received Illegal Input");
			return null;
		}
		
		return evaluator.kFoldEvaluation(testSet);
	}

	/**
	 * Proxy method to evaluate multiple test nodes and receive mean values
	 * 
	 * @param testNodes collection of nodes to be evaluated
	 * @return Map a map of different evaluation measurements of multiple nodes
	 */
	public Map<String, Double> kFoldEvaluation(Set<INode> testNodes){
		
		// Input Check
		if(testNodes == null){
			System.out.println("Received Illegal Input");
			return null;
		}
		
		return evaluator.kFoldEvaluation(testNodes);
	}
	
	/**
	 * Proxy method to evaluate the quality of chosen 
	 * position as most similar node
	 * 
	 * @param testNode this node has to contain real ratings that can be tested on predicted values
	 * @param position this is the most similar position to the testNode in the tree
	 * @return Map a map of different evaluation measurements
	 */
	public Map<String, Double> evaluate(INode testNode, TreePosition position){
		
		// Input Check
		if(testNode == null || position == null){
			System.out.println("Received Illegal Input");
			return null;
		}
		
		return evaluator.evaluate(testNode, position);
	}

	/**
	 * Proxy method to insert a node into one of the trees
	 * depending on the nodes type
	 * 
	 * @param inputNode the node that should be inserted
	 * @param position the position where the node should be inserted
	 * @return boolean value about success of insertion
	 */
	public boolean insert(INode inputNode, TreePosition position){
		
		// Input Check
		if(inputNode == null || position == null){
			System.out.println("Received Illegal Input");
			return false;
		}
		
		return inserter.insert(inputNode, position);
	}

	/**
	 * Proxy method to build a node from string values
	 * 
	 * @param nomMetaInfo the nominal meta information
	 * @param numMetaInfo the numerical meta information
	 * @param ratings the rating information
	 * @param type the type of the node
	 * @return INode the created node
	 */
	public INode buildNode(List<String> nomMetaInfo,List<String> numMetaInfo, List<String> ratings, ENodeType type){
		
		// Input Check
		if(nomMetaInfo == null || numMetaInfo == null || ratings == null || type == null){
			System.out.println("Received Illegal Input");
			return null;
		}
		
		return builder.buildNode(nomMetaInfo, numMetaInfo, ratings, type);
	}
	
	/**
	 * Proxy method to build a node or multiple nodes from files 
	 * 
	 * @param ratingLocation the location of the rating file
	 * @param metaLocation the location of the respective meta file
	 * @param type the type of node that should be built
	 * @return ImmutableMap collection of built nodes
	 */
	public ImmutableMap<String, INode> buildNodesFromFile(String ratingLocation, String metaLocation, ENodeType type){
		return builder.buildNodesFromFile(ratingLocation, metaLocation, type);
	}

}
