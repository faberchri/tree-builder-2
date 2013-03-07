package ch.uzh.agglorecommender.clusterer.treesearch;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import ch.uzh.agglorecommender.clusterer.treecomponent.CobwebAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.Node;


public class CobwebMaxCategoryUtilitySearcherTest {

	@Test
	public void testGetMaxCategoryUtilityMergeSetOfINode() {

		// create attributes
		// interval of ratings: [1, 5]
		// ratings are integers
		Map<Integer, Double> attMap = new HashMap<Integer, Double>();
		attMap.put(4, 1.0);
		IAttribute attribute_1A = new CobwebAttribute(attMap);
		attMap = new HashMap<Integer, Double>();
		attMap.put(3, 1.0);
		IAttribute attribute_1B = new CobwebAttribute(attMap);
		attMap = new HashMap<Integer, Double>();
		attMap.put(5, 1.0);
		IAttribute attribute_2B = new CobwebAttribute(attMap);
		attMap = new HashMap<Integer, Double>();
		attMap.put(5, 1.0);
		IAttribute attribute_2C = new CobwebAttribute(attMap);

		// we want to calc the category utility of node 1 merged with node 2
		// attribute map of node 1
		Map<INode, IAttribute> attMap1 = new HashMap<INode, IAttribute>();

		// this node is an attribute of node 1 and node 2
		INode sharedAttribute = new Node(ENodeType.Content, null, null, null, 0);

		// add the corresponding attributes to the attribute map of node 1
		attMap1.put(new Node(ENodeType.Content, null, null, null, 0), attribute_1A);
		attMap1.put(sharedAttribute, attribute_1B);

		// create node 1
		INode node1 = new Node(ENodeType.User, null, null, null, 0);
		node1.setAttributes(attMap1);

		// attribute map of node 2
		Map<INode, IAttribute> attMap2 = new HashMap<INode, IAttribute>();

		// add the corresponding attributes to the attribute map of node 2
		attMap2.put(sharedAttribute, attribute_2B);
		attMap2.put(new Node(ENodeType.Content, null, null, null, 0), attribute_2C);

		// create node 2
		INode node2 = new Node(ENodeType.User, null, null, null, 0);
		node2.setAttributes(attMap2);

		// create the utility calcultaor
		IMaxCategoryUtilitySearcher utilityCalc = new CobwebMaxCategoryUtilitySearcher();

		// add the two created user nodes to a set (set of open nodes)
		Set<INode> openNodes = new IndexAwareSet<INode>();
		openNodes.add(node1);
		openNodes.add(node2);

		// get the utility of the node resulting of a merge of node 1 and node 2
		double calcCatUt = utilityCalc.getMaxCategoryUtilityMerge(openNodes)
				.getCategoryUtility();

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

		System.out.println(" ");
		System.out
				.println("----------------------Starting test Cobweb 1..----------------------");
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
		INode sharedAttributeA1 = new Node(ENodeType.Content, null, null, null, 0);
		INode sharedAttributeA3 = new Node(ENodeType.Content, null, null, null, 0);
		
		// attribute maps
		Map<INode, IAttribute> attMap1 = new HashMap<INode, IAttribute>();
		Map<INode, IAttribute> attMap2 = new HashMap<INode, IAttribute>();
		Map<INode, IAttribute> attMap3 = new HashMap<INode, IAttribute>();
		
		// add the corresponding attributes to the attribute maps
		attMap1.put(sharedAttributeA1, attribute_N1A1);
		attMap1.put(new Node(ENodeType.Content, null, null, null, 0), attribute_N1A2);
		
		attMap2.put(sharedAttributeA1, attribute_N2A1);
		attMap2.put(sharedAttributeA3, attribute_N2A3);
		
		attMap3.put(sharedAttributeA1, attribute_N3A1);
		attMap3.put(sharedAttributeA3, attribute_N3A3);
		
		// create nodes
		INode node1 = new Node(ENodeType.User, null, null, null, 0);
		node1.setAttributes(attMap1);
		INode node2 = new Node(ENodeType.User, null, null, null, 0);
		node2.setAttributes(attMap2);
		INode node3 = new Node(ENodeType.User, null, null, null, 0);
		node3.setAttributes(attMap3);

		// create the utility calcultaor
		IMaxCategoryUtilitySearcher utilityCalc = new CobwebMaxCategoryUtilitySearcher();

		// add the  created user nodes to a set (set of open nodes)
		Set<INode> openNodes = new IndexAwareSet<INode>();
		openNodes.add(node1);
		openNodes.add(node2);
		openNodes.add(node3);

		// get the utility of the node resulting of a merge of node 1 and node 2
		double calcCatUt = utilityCalc.getMaxCategoryUtilityMerge(openNodes)
				.getCategoryUtility();
		
		List<INode> nodesToUpdate = utilityCalc.getMaxCategoryUtilityMerge(
				openNodes).getNodes();
		
		System.out.println("	Merging the following nodes: ");
		for (INode node : nodesToUpdate) {
			System.out.println(node.getNumericalAttributesString() + node.getNominalAttributesString());
		}

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
		System.out.println(" ");
		System.out
				.println("----------------------Starting test Cobweb 2..----------------------");
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
		attMap1.put(new Node(ENodeType.Content, null, null, null, 0), attribute_1);
		attMap2.put(new Node(ENodeType.Content, null, null, null, 0), attribute_2);
		attMap3.put(new Node(ENodeType.Content, null, null, null, 0), attribute_3);
		
		// create nodes
		INode node1 = new Node(ENodeType.User, null, null, null, 0);
		node1.setAttributes(attMap1);
		System.out.println("Node 1:"+node1.getNumericalAttributesString() + node1.getNominalAttributesString());
		INode node2 = new Node(ENodeType.User, null, null, null, 0);
		node2.setAttributes(attMap2);
		INode node3 = new Node(ENodeType.User, null, null, null, 0);
		node3.setAttributes(attMap3);

		// create the utility calcultaor
		IMaxCategoryUtilitySearcher utilityCalc = new CobwebMaxCategoryUtilitySearcher();

		// add the  created user nodes to a set (set of open nodes)
		Set<INode> openNodes = new IndexAwareSet<INode>();
		openNodes.add(node1);
		openNodes.add(node2);
		openNodes.add(node3);

		// get the utility of the node resulting of a merge of node 1 and node 2
		double calcCatUt = utilityCalc.getMaxCategoryUtilityMerge(openNodes)
				.getCategoryUtility();
		
		List<INode> nodesToUpdate = utilityCalc.getMaxCategoryUtilityMerge(
				openNodes).getNodes();
		
		System.out.println("	Merging the following nodes: ");
		for (INode node : nodesToUpdate) {
			System.out.println(node.getNumericalAttributesString() + node.getNominalAttributesString());
		}

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
		System.out.println(" ");
		System.out
				.println("----------------------Starting test Cobweb 3..----------------------");
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
		INode sharedAttribute = new Node(ENodeType.Content, null, null, null, 0);
		
		// add the corresponding attributes to the attribute maps
		attMap1.put(sharedAttribute, attribute_1);
		attMap2.put(sharedAttribute, attribute_2);
		attMap3.put(new Node(ENodeType.Content, null, null, null, 0), attribute_3);
		attMap4.put(new Node(ENodeType.Content, null, null, null, 0), attribute_4);
		
		// create nodes
		INode node1 = new Node(ENodeType.User, null, null, null, 0);
		node1.setAttributes(attMap1);
		INode node2 = new Node(ENodeType.User, null, null, null, 0);
		node2.setAttributes(attMap2);
		INode node3 = new Node(ENodeType.User, null, null, null, 0);
		node3.setAttributes(attMap3);
		INode node4 = new Node(ENodeType.User, null, null, null, 0);
		node3.setAttributes(attMap4);

		// create the utility calcultaor
		IMaxCategoryUtilitySearcher utilityCalc = new CobwebMaxCategoryUtilitySearcher();

		// add the  created user nodes to a set (set of open nodes)
		Set<INode> openNodes = new IndexAwareSet<INode>();
		openNodes.add(node1);
		openNodes.add(node2);
		openNodes.add(node3);
		openNodes.add(node4);

		// get the utility of the node resulting of a merge of node 1 and node 2
		double calcCatUt = utilityCalc.getMaxCategoryUtilityMerge(openNodes).getCategoryUtility();
		
		List<INode> nodesToUpdate = utilityCalc.getMaxCategoryUtilityMerge(
				openNodes).getNodes();
		
		System.out.println("	Merging the following nodes: ");
		for (INode node : nodesToUpdate) {
			System.out.println(node.getNumericalAttributesString() + node.getNominalAttributesString());
		}

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
		System.out.println(" ");
		System.out
				.println("----------------------Starting test Cobweb 4..----------------------");
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
		INode sharedAttribute = new Node(ENodeType.Content, null, null, null, 0);
		
		// add the corresponding attributes to the attribute maps
		attMap1.put(sharedAttribute, attribute_1);
		attMap2.put(sharedAttribute, attribute_2);
		
		// create nodes
		INode node1 = new Node(ENodeType.User, null, null, null, 0);
		node1.setAttributes(attMap1);
		INode node2 = new Node(ENodeType.User, null, null, null, 0);
		node2.setAttributes(attMap2);

		// create the utility calcultaor
		IMaxCategoryUtilitySearcher utilityCalc = new CobwebMaxCategoryUtilitySearcher();

		// add the  created user nodes to a set (set of open nodes)
		Set<INode> openNodes = new IndexAwareSet<INode>();
		openNodes.add(node1);
		openNodes.add(node2);

		// get the utility of the node resulting of a merge of node 1 and node 2
		double calcCatUt = utilityCalc.getMaxCategoryUtilityMerge(openNodes)
				.getCategoryUtility();
		
		List<INode> nodesToUpdate = utilityCalc.getMaxCategoryUtilityMerge(
				openNodes).getNodes();
		
		System.out.println("	Merging the following nodes: ");
		for (INode node : nodesToUpdate) {
			System.out.println(node.getNumericalAttributesString() + node.getNominalAttributesString());
		}

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
		System.out.println(" ");
		System.out
				.println("----------------------Starting test Cobweb 5..----------------------");
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
		INode sharedAttribute = new Node(ENodeType.Content, null, null, null, 0);
		
		// add the corresponding attributes to the attribute maps
		attMap1.put(sharedAttribute, attribute_1);
		attMap2.put(sharedAttribute, attribute_2);
		
		// create nodes
		INode node1 = new Node(ENodeType.User, null, null, null, 0);
		node1.setAttributes(attMap1);
		INode node2 = new Node(ENodeType.User, null, null, null, 0);
		node2.setAttributes(attMap2);

		// create the utility calcultaor
		IMaxCategoryUtilitySearcher utilityCalc = new CobwebMaxCategoryUtilitySearcher();

		// add the  created user nodes to a set (set of open nodes)
		Set<INode> openNodes = new IndexAwareSet<INode>();
		openNodes.add(node1);
		openNodes.add(node2);

		// get the utility of the node resulting of a merge of node 1 and node 2
		double calcCatUt = utilityCalc.getMaxCategoryUtilityMerge(openNodes)
				.getCategoryUtility();
		
		List<INode> nodesToUpdate = utilityCalc.getMaxCategoryUtilityMerge(
				openNodes).getNodes();
		
		System.out.println("	Merging the following nodes: ");
		for (INode node : nodesToUpdate) {
			System.out.println(node.getNumericalAttributesString() + node.getNominalAttributesString());
		}

		// evaluate the category utility result
		assertEquals("category utility",0.000000000000000000000121000625 , calcCatUt, 0.000000000000000000000000000000000001);

	}
}
