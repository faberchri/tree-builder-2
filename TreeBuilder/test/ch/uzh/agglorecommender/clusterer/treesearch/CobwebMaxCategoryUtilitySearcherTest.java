package ch.uzh.agglorecommender.clusterer.treesearch;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import ch.uzh.agglorecommender.clusterer.treecomponent.CobwebAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.Node;


public class CobwebMaxCategoryUtilitySearcherTest {

	INode node1 = new Node(ENodeType.User, null, null);
	INode node2 = new Node(ENodeType.User, null, null);
	INode node3 = new Node(ENodeType.User, null, null);
	INode node4 = new Node(ENodeType.User, null, null);
	
	@Test
	public void testGetMaxCategoryUtilityMergeSetOfINode() {
		// create attributes
		
		Map<String, Double> attMap = new HashMap<String, Double>();
		attMap.put("1A", 1.0);
		IAttribute attribute_1A = new CobwebAttribute(attMap);
		attMap = new HashMap<String, Double>();
		attMap.put("1B", 1.0);
		IAttribute attribute_1B = new CobwebAttribute(attMap);
		attMap = new HashMap<String, Double>();
		attMap.put("2B", 1.0);
		IAttribute attribute_2B = new CobwebAttribute(attMap);
		attMap = new HashMap<String, Double>();
		attMap.put("2C", 1.0);
		IAttribute attribute_2C = new CobwebAttribute(attMap);
		
		// establish shared attribute
		//INode sharedAttribute = new Node(ENodeType.Content, null, null);
		
		// add the corresponding attributes to the attribute map of node 1
		Map<String, IAttribute> attMap1 = new HashMap<String, IAttribute>();
		attMap1.put("1A", attribute_1A);
		attMap1.put("1B", attribute_1B);
		node1.setNominalMetaAttributes(attMap1);
		
		System.out.println(node1.getNominalAttributesString());

		// add the corresponding attributes to the attribute map of node 2
		Map<String, IAttribute> attMap2 = new HashMap<String, IAttribute>();
		attMap2.put("2B", attribute_1B);
		attMap2.put("2C", attribute_2C);
		node2.setNominalMetaAttributes(attMap2);

		// add the two created user nodes to a set (set of open nodes)
		Set<INode> openNodes = new IndexAwareSet<INode>();
		openNodes.add(node1);
		openNodes.add(node2);

		// get the utility of the node resulting of a merge of node 1 and node 2
		double calcCatUt = getUtility(openNodes);

		// evaluate the category utility result
		assertEquals("category utility", 1.0 / 3.0, calcCatUt, 0.000001);
	}

	/**
	 * Creates three nodes and looks for best merge 
	 * 
	 * Nodes: 
	 * N1 
	 * A1 -> 5 
	 * A2 -> 3
	 * 
	 * N2 
	 * A1 -> 5 
	 * A3 -> 1
	 * 
	 * N3 
	 * A1 -> 4 
	 * A3 -> 1
	 */
	//@Test
	//This test does not make sense yet
	public void testGetMaxCategoryUtilityMergeSetOfThreeNodes() {

		System.out.println("----------------------Starting test Cobweb 1..----------------------");
		
		// create attributes
		// ratings are integers
		Map<Integer, Double> attMap = new HashMap<Integer, Double>();
		attMap.put(5, 1.0);
		IAttribute attribute_N1A1 = new CobwebAttribute(attMap);
		attMap = new HashMap<Integer, Double>();
		attMap.put(5, 1.0);
		IAttribute attribute_N2A1 = new CobwebAttribute(attMap);
		attMap = new HashMap<Integer, Double>();
		attMap.put(4, 1.0);
		IAttribute attribute_N3A1 = new CobwebAttribute(attMap);
		attMap = new HashMap<Integer, Double>();
		attMap.put(3, 1.0);
		IAttribute attribute_N1A2 = new CobwebAttribute(attMap);
		attMap = new HashMap<Integer, Double>();
		attMap.put(1, 1.0);
		IAttribute attribute_N2A3 = new CobwebAttribute(attMap);
		attMap = new HashMap<Integer, Double>();
		attMap.put(3, 1.0);
		IAttribute attribute_N3A3 = new CobwebAttribute(attMap);

		// Create the common attributes
		INode sharedAttributeA1 = new Node(ENodeType.Content, null, null);
		INode sharedAttributeA3 = new Node(ENodeType.Content, null, null);

		// attribute maps
		Map<INode, IAttribute> attMap1 = new HashMap<INode, IAttribute>();
		Map<INode, IAttribute> attMap2 = new HashMap<INode, IAttribute>();
		Map<INode, IAttribute> attMap3 = new HashMap<INode, IAttribute>();

		// add the corresponding attributes to the attribute maps
		attMap1.put(sharedAttributeA1, attribute_N1A1);
		attMap1.put(new Node(ENodeType.Content, null, null), attribute_N1A2);

		attMap2.put(sharedAttributeA1, attribute_N2A1);
		attMap2.put(sharedAttributeA3, attribute_N2A3);

		attMap3.put(sharedAttributeA1, attribute_N3A1);
		attMap3.put(sharedAttributeA3, attribute_N3A3);

		// create nodes
		node1.setRatingAttributes(attMap1);
		node2.setRatingAttributes(attMap2);
		node3.setRatingAttributes(attMap3);

		// create the utility calcultaor

		// add the  created user nodes to a set (set of open nodes)
		Set<INode> openNodes = new IndexAwareSet<INode>();
		openNodes.add(node1);
		openNodes.add(node2);
		openNodes.add(node3);

		// get the utility of the node resulting of a merge of node 1 and node 2
		double calcCatUt = getUtility(openNodes);

		// evaluate the category utility result
		assertEquals("category utility", 0.75, calcCatUt, 0.000001);
	}
	/**
	 * Creates three nodes not having any common attribute
	 * 
	 *  * Nodes: 
	 * N1 
	 * A1 -> 4
	 * 
	 * N2 
	 * A2 -> 4
	 * 
	 * N3 
	 * A3 -> 3
	 */
	@Test
	public void testGetMaxCategoryUtilityNoCommonAttributes() {

		System.out.println("----------------------Starting test Cobweb 2..----------------------");
		
		// create attributes
		// ratings are integers
		Map<Integer, Double> attMap = new HashMap<Integer, Double>();
		attMap.put(4, 1.0);
		IAttribute attribute_1 = new CobwebAttribute(attMap);
		attMap = new HashMap<Integer, Double>();
		attMap.put(4, 1.0);
		IAttribute attribute_2 = new CobwebAttribute(attMap);
		attMap = new HashMap<Integer, Double>();
		attMap.put(3, 1.0);
		IAttribute attribute_3 = new CobwebAttribute(attMap);

		// attribute maps
		Map<INode, IAttribute> attMap1 = new HashMap<INode, IAttribute>();
		Map<INode, IAttribute> attMap2 = new HashMap<INode, IAttribute>();
		Map<INode, IAttribute> attMap3 = new HashMap<INode, IAttribute>();

		// add the corresponding attributes to the attribute maps
		attMap1.put(new Node(ENodeType.Content, null, null), attribute_1);
		attMap2.put(new Node(ENodeType.Content, null, null), attribute_2);
		attMap3.put(new Node(ENodeType.Content, null, null), attribute_3);

		// create nodes
		node1.setRatingAttributes(attMap1);
		node2.setRatingAttributes(attMap2);
		node3.setRatingAttributes(attMap3);

		// add the  created user nodes to a set (set of open nodes)
		Set<INode> openNodes = new IndexAwareSet<INode>();
		openNodes.add(node1);
		openNodes.add(node2);
		openNodes.add(node3);

		// get the utility of the node resulting of a merge of node 1 and node 2
		double calcCatUt = getUtility(openNodes);

		// evaluate the category utility result
		assertEquals("category utility", 0.25, calcCatUt, 0.000001);
	}

	/**
	 * Creates four nodes, two of them having a commont attribute
	 * 
	 *  * Nodes: 
	 * N1 
	 * A1 -> 10
	 * 
	 * N2 
	 * A1 -> 1
	 * 
	 * N3 
	 * A2 -> 5
	 * 
	 * N4
	 * A3 -> 3
	 */
	@Test
	public void testGetMaxCategoryUtilityFourNodes() {

		System.out.println("----------------------Starting test Cobweb 3..----------------------");
		
		// create attributes
		// ratings are integers
		Map<Integer, Double> attMap = new HashMap<Integer, Double>();
		attMap.put(10, 1.0);
		IAttribute attribute_1 = new CobwebAttribute(attMap);
		attMap = new HashMap<Integer, Double>();
		attMap.put(1, 1.0);
		IAttribute attribute_2 = new CobwebAttribute(attMap);
		attMap = new HashMap<Integer, Double>();
		attMap.put(5, 1.0);
		IAttribute attribute_3 = new CobwebAttribute(attMap);
		attMap = new HashMap<Integer, Double>();
		attMap.put(3, 1.0);
		IAttribute attribute_4 = new CobwebAttribute(attMap);

		// attribute maps
		Map<INode, IAttribute> attMap1 = new HashMap<INode, IAttribute>();
		Map<INode, IAttribute> attMap2 = new HashMap<INode, IAttribute>();
		Map<INode, IAttribute> attMap3 = new HashMap<INode, IAttribute>();
		Map<INode, IAttribute> attMap4 = new HashMap<INode, IAttribute>();

		// one common attribute
		INode sharedAttribute = new Node(ENodeType.Content, null, null);

		// add the corresponding attributes to the attribute maps
		attMap1.put(sharedAttribute, attribute_1);
		attMap2.put(sharedAttribute, attribute_2);
		attMap3.put(new Node(ENodeType.Content, null, null), attribute_3);
		attMap4.put(new Node(ENodeType.Content, null, null), attribute_4);

		// create nodes
		node1.setRatingAttributes(attMap1);
		node2.setRatingAttributes(attMap2);
		node3.setRatingAttributes(attMap3);
		node4.setRatingAttributes(attMap4);

		// add the  created user nodes to a set (set of open nodes)
		Set<INode> openNodes = new IndexAwareSet<INode>();
		openNodes.add(node1);
		openNodes.add(node2);
		openNodes.add(node3);
		openNodes.add(node4);

		// get the utility of the node resulting of a merge of node 1 and node 2
		double calcCatUt = getUtility(openNodes);;

		// evaluate the category utility result
		assertEquals("category utility", 0.5, calcCatUt, 0.000001);
	}

	/**
	 * Creates two nodes with one common attribute and different probabilities
	 * 
	 *  * Nodes: 
	 * N1 
	 * A1 -> 10, p = 2
	 * 
	 * N2 
	 * A1 -> 1, p = 10

	 */
	@Test
	public void testGetMaxCategoryUtilityWithHighProbabilities() {
		
		System.out.println("----------------------Starting test Cobweb 4..----------------------");
		
		// create attributes
		// ratings are integers
		Map<Integer, Double> attMap = new HashMap<Integer, Double>();
		attMap.put(10, 0.02);
		IAttribute attribute_1 = new CobwebAttribute(attMap);
		attMap = new HashMap<Integer, Double>();
		attMap.put(1, 0.1);
		IAttribute attribute_2 = new CobwebAttribute(attMap);

		// attribute maps
		Map<INode, IAttribute> attMap1 = new HashMap<INode, IAttribute>();
		Map<INode, IAttribute> attMap2 = new HashMap<INode, IAttribute>();

		// one common attribute
		INode sharedAttribute = new Node(ENodeType.Content, null, null);

		// add the corresponding attributes to the attribute maps
		attMap1.put(sharedAttribute, attribute_1);
		attMap2.put(sharedAttribute, attribute_2);

		// create nodes
		node1.setRatingAttributes(attMap1);
		node2.setRatingAttributes(attMap2);

		// add the  created user nodes to a set (set of open nodes)
		Set<INode> openNodes = new IndexAwareSet<INode>();
		openNodes.add(node1);
		openNodes.add(node2);

		// get the utility of the node resulting of a merge of node 1 and node 2
		double calcCatUt = getUtility(openNodes);

		// evaluate the category utility result
		assertEquals("category utility",0.0026 , calcCatUt, 0.000000001);
	}

	/**
	 * Creates two nodes with one common attribute and different probabilities
	 * 
	 *  * Nodes: 
	 * N1 
	 * A1 -> 10, p = 2
	 * 
	 * N2 
	 * A1 -> 1, p = 10

	 */
	@Test
	public void testGetMaxCategoryUtilityWithVerySmallProbabilitiesII() {
		
		System.out.println("----------------------Starting test Cobweb 5..----------------------");
		
		// create attributes
		// ratings are integers
		Map<Integer, Double> attMap = new HashMap<Integer, Double>();
		attMap.put(3, 0.00000000000005);
		IAttribute attribute_1 = new CobwebAttribute(attMap);
		attMap = new HashMap<Integer, Double>();
		attMap.put(1, 0.000000000022);
		IAttribute attribute_2 = new CobwebAttribute(attMap);

		// attribute maps
		Map<INode, IAttribute> attMap1 = new HashMap<INode, IAttribute>();
		Map<INode, IAttribute> attMap2 = new HashMap<INode, IAttribute>();

		// one common attribute
		INode sharedAttribute = new Node(ENodeType.Content, null, null);

		// add the corresponding attributes to the attribute maps
		attMap1.put(sharedAttribute, attribute_1);
		attMap2.put(sharedAttribute, attribute_2);

		// create nodes
		node1.setRatingAttributes(attMap1);
		node2.setRatingAttributes(attMap2);

		// add the  created user nodes to a set (set of open nodes)
		Set<INode> openNodes = new IndexAwareSet<INode>();
		openNodes.add(node1);
		openNodes.add(node2);

		// get the utility of the node resulting of a merge of node 1 and node 2
		double calcCatUt = getUtility(openNodes);

		// evaluate the category utility result
		assertEquals("category utility",0.000000000000000000000121000625 , calcCatUt, 0.000000000000000000000000000000000001);
	}

	private static double getUtility(Set<INode> nodes){
		
		IMergeResult merge = null;
//		ImmutableCollection<INode> nodeSet = ImmutableSet.copyOf(nodes);
//		IClusterSetIndexed<INode> leafNodes = new ClusterSetIndexed<INode>(nodeSet);
		CobwebMaxCategoryUtilitySearcher utilityCalc = new CobwebMaxCategoryUtilitySearcher();
		return utilityCalc.calculateCategoryUtility(nodes);
		
//		//Instantiate TreeBuilder
//		TreeBuilder tr = null;
//		try {
//			//Class[] parameterTypes = {};
//			Constructor cons[] = TreeBuilder.class.getDeclaredConstructors();
//			cons[0].setAccessible(true);
//			tr = (TreeBuilder)cons[0].newInstance(null);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
//
//		try {
//			Class[] parameterTypes = {IClusterSetIndexed.class, IMaxCategoryUtilitySearcher.class};
//			Method method = TreeBuilder.class.getDeclaredMethod("searchBestMergeResultIndexed", parameterTypes);
//			method.setAccessible(true);
//			merge = (IMergeResult) method.invoke(tr,leafNodes,utilityCalc);
//		} 
//		catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//			e.printStackTrace();
//		}
//		
//		Double utility = merge.getCategoryUtility();
//		return utility; 
	}
}
