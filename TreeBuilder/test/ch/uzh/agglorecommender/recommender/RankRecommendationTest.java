package ch.uzh.agglorecommender.recommender;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import ch.uzh.agglorecommender.client.ClusterResult;
import ch.uzh.agglorecommender.clusterer.treecomponent.ClassitAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.Node;

/*
 * TODO: 
 * 	1. Sorted list is empty right now
 *  2. getSumOfRatings() returns sum of squared ratings
 */

public class RankRecommendationTest {
 //public ArrayList<IAttribute> rankRecommendation(Map<INode, IAttribute> unsortedRecommendation,int direction, int limit)
	@Test
	public void rankRecommendationsTesI(){
		System.out.println("Start recommendations ranking test I..");
		// Create nodes
		INode node1 = new Node(ENodeType.Content, null, null);
		INode node2 = new Node(ENodeType.Content, null, null);
		INode node3 = new Node(ENodeType.Content, null, null);
		INode node4 = new Node(ENodeType.Content, null, null);
		
		// Create recommendations
		IAttribute att1 = new ClassitAttribute(5, 25, 1);
		IAttribute att2 = new ClassitAttribute(2, 4, 1);
		IAttribute att3 = new ClassitAttribute(1, 1, 1);
		IAttribute att4 = new ClassitAttribute(4, 16, 1);
		
		// Create map
		Map<INode,IAttribute> unsortedRecommendations = new HashMap<INode, IAttribute>();
		unsortedRecommendations.put(node1, att1);
		unsortedRecommendations.put(node2, att2);
		unsortedRecommendations.put(node3, att3);
		unsortedRecommendations.put(node4,att4);
		
		Iterator j = unsortedRecommendations.entrySet().iterator();
		while(j.hasNext()){
			Map.Entry pair = (Map.Entry) j.next();
			System.out.println(((IAttribute)pair.getValue()).getSumOfRatings());
			
		}
		
		System.out.println("Lenght unsorted: "+unsortedRecommendations.size());
		// Sort recommendations
		ClusterResult cr = new ClusterResult(null,null,null, null, null);
		RecommendationModel ranker = new RecommendationModel(cr, null);
		Map<INode,IAttribute> sortedRecommendations = new HashMap<INode, IAttribute>();
		ranker.rankRecommendation(unsortedRecommendations, 1, 4);
		
		System.out.println("Lenght sorted: "+sortedRecommendations.size());
		
		Iterator i = sortedRecommendations.entrySet().iterator();
		while(i.hasNext()){
			Map.Entry pair = (Map.Entry) i.next();
			System.out.println(((IAttribute)pair.getValue()).getSumOfRatings());
			
		}
		// Check results
		
	}
}
