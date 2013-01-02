package modules;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import clusterer.ENodeType;
import clusterer.IAttribute;
import clusterer.IMaxCategoryUtilitySearcher;
import clusterer.INode;
import clusterer.TreeBuilder;

public class ClassitMaxCategoryUtilitySearcherTest {

	/*
	 * Arguments: 1: Creates 2 nodes with 2 attributes each, calculates utility
	 * and merges the nodes. 2: Creates 6 nodes with 3 shared attributes. Merges
	 * nodes to create full tree. 3: Creates 3 nodes, each node having one
	 * common attribute with each other node
	 */
	public static void main(String[] args) {

		System.out.println("Starting Classit tests..");
		ClassitMaxCategoryUtilitySearcherTest tester = new ClassitMaxCategoryUtilitySearcherTest();

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			switch (arg) {
			case "1":
				tester.testGetMaxCategoryUtilityMergeSetOfINode();
				break;
			case "2":
				tester.testSimpleClassitTree();
				break;
			default:
				System.out.println("Invalid argument: " + arg);
				break;
			}
		}

	}

	@Test
	public void testGetMaxCategoryUtilityMergeSetOfINode() {

		System.out.println(" ");
		System.out
				.println("----------------------Starting test 1..----------------------");

		// Ratings are Integers

		// Based on example in Google Docs
		// https://docs.google.com/spreadsheet/ccc?key=0AnvRo1G6q1ffdEJLWjJiX2QtX2hza1l4WG5Sclp4WEE#gid=0

		// Creating the attributes

		IAttribute A1 = ClassitAttributeFactory.getInstance()
				.createAttribute(4);
		IAttribute A2 = ClassitAttributeFactory.getInstance()
				.createAttribute(3);
		IAttribute A3 = ClassitAttributeFactory.getInstance()
				.createAttribute(5);
		IAttribute A4 = ClassitAttributeFactory.getInstance()
				.createAttribute(5);

		// We want to calc the category utility of node 1 merged with node 2

		// Attribute map of node 1
		Map<INode, IAttribute> attMap1 = new HashMap<INode, IAttribute>();

		// this node is an attribute of node 1 and node 2
		INode sharedAttribute = new Node(ENodeType.Content, null, null);

		// add the corresponding attributes to the attribute map of node 1
		attMap1.put(new Node(ENodeType.Content, null, null), A1);
		attMap1.put(sharedAttribute, A2);

		// create node 1
		INode node1 = new Node(ENodeType.User, null, null);
		node1.setAttributes(attMap1);

		// attribute map of node 2
		Map<INode, IAttribute> attMap2 = new HashMap<INode, IAttribute>();

		// add the corresponding attributes to the attribute map of node 2
		attMap2.put(sharedAttribute, A3);
		attMap2.put(new Node(ENodeType.Content, null, null), A4);

		// create node 2
		INode node2 = new Node(ENodeType.User, null, null);
		node2.setAttributes(attMap2);

		// create the utility calcultaor
		IMaxCategoryUtilitySearcher utilityCalc = new ClassitMaxCategoryUtilitySearcher();

		// add the two created user nodes to a set (set of open nodes)
		Set<INode> openNodes = new IndexAwareSet<INode>();
		openNodes.add(node1);
		openNodes.add(node2);

		// get the utility of the node resulting of a merge of node 1 and node 2
		double calcCatUt = utilityCalc.getMaxCategoryUtilityMerge(openNodes)
				.getCategoryUtility();

		// Print standard deviations
		System.out.println("Node 1: " + node1.getAttributesString());
		System.out.println("Node 2: " + node2.getAttributesString());

		ArrayList<INode> nodesToUpdate = new ArrayList<INode>();
		nodesToUpdate.add(node1);
		nodesToUpdate.add(node2);

		TreeBuilder<Number> tr = new TreeBuilder(null,
				new ClassitMaxCategoryUtilitySearcher(),
				new ClassitMaxCategoryUtilitySearcher(), null);
		INode newNode = tr.createTestingMergedNode(nodesToUpdate, openNodes);

		System.out.println("New Node: " + newNode.getAttributesString());

		// evaluate the category utility result
		assertEquals("category utility", 1.0 / 3.0, calcCatUt, 0.000001);

	}

	@Test
	public void testSimpleClassitTree() {

		System.out.println(" ");
		System.out
				.println("----------------------Starting test 2..----------------------");
		// Ratings are doubles

		/*
		 * Based on example in J.H.Gennari et al.
		 * "Models of incremental concept formation", page 35, Figure 10
		 */

		// Creating the attributes

		IAttribute H1 = ClassitAttributeFactory.getInstance().createAttribute(
				13); // Ht of instance 1
		IAttribute H2 = ClassitAttributeFactory.getInstance().createAttribute(
				12); // Ht of instance 2
		IAttribute H4 = ClassitAttributeFactory.getInstance().createAttribute(
				28); // Ht of instance 4
		IAttribute H5 = ClassitAttributeFactory.getInstance().createAttribute(
				25); // Ht of instance 5
		IAttribute H6 = ClassitAttributeFactory.getInstance().createAttribute(
				41); // Ht of instance 6

		IAttribute W1 = ClassitAttributeFactory.getInstance().createAttribute(
				6.5); // Wid for instance 1
		IAttribute W2 = ClassitAttributeFactory.getInstance()
				.createAttribute(7); // Wid for instance 2
		IAttribute W4 = ClassitAttributeFactory.getInstance().createAttribute(
				13); // Wid for instance 4
		IAttribute W5 = ClassitAttributeFactory.getInstance().createAttribute(
				15); // Wid for instance 5
		IAttribute W6 = ClassitAttributeFactory.getInstance().createAttribute(
				36); // Wid for instance 6

		IAttribute T1 = ClassitAttributeFactory.getInstance().createAttribute(
				7.5); // Txt for instance 1
		IAttribute T2 = ClassitAttributeFactory.getInstance().createAttribute(
				20); // Txt for instance 2
		IAttribute T4 = ClassitAttributeFactory.getInstance().createAttribute(
				19); // Txt for instance 4
		IAttribute T5 = ClassitAttributeFactory.getInstance().createAttribute(
				24); // Txt for instance 5
		IAttribute T6 = ClassitAttributeFactory.getInstance().createAttribute(
				30); // Txt for instance 6

		// Attribute map of the nodes
		Map<INode, IAttribute> attMap1 = new HashMap<INode, IAttribute>();
		Map<INode, IAttribute> attMap2 = new HashMap<INode, IAttribute>();
		Map<INode, IAttribute> attMap4 = new HashMap<INode, IAttribute>();
		Map<INode, IAttribute> attMap5 = new HashMap<INode, IAttribute>();
		Map<INode, IAttribute> attMap6 = new HashMap<INode, IAttribute>();

		// Shared attribute nodes for each attribute
		INode attHt = new Node(ENodeType.Content, null, null);
		INode attWid = new Node(ENodeType.Content, null, null);
		INode attTxt = new Node(ENodeType.Content, null, null);

		// add the corresponding attributes to the attribute maps
		attMap1.put(attHt, H1);
		attMap1.put(attWid, W1);
		attMap1.put(attTxt, T1);

		attMap2.put(attHt, H2);
		attMap2.put(attWid, W2);
		attMap2.put(attTxt, T2);

		attMap4.put(attHt, H4);
		attMap4.put(attWid, W4);
		attMap4.put(attTxt, T4);

		attMap5.put(attHt, H5);
		attMap5.put(attWid, W5);
		attMap5.put(attTxt, T5);

		attMap6.put(attHt, H6);
		attMap6.put(attWid, W6);
		attMap6.put(attTxt, T6);

		// create nodes
		INode node1 = new Node(ENodeType.User, null, null);
		node1.setAttributes(attMap1);

		INode node2 = new Node(ENodeType.User, null, null);
		node2.setAttributes(attMap2);

		INode node4 = new Node(ENodeType.User, null, null);
		node4.setAttributes(attMap4);

		INode node5 = new Node(ENodeType.User, null, null);
		node5.setAttributes(attMap5);

		INode node6 = new Node(ENodeType.User, null, null);
		node6.setAttributes(attMap6);

		// create the utility calcultaor
		IMaxCategoryUtilitySearcher utilityCalc = new ClassitMaxCategoryUtilitySearcher();

		// add the created user nodes to a set (set of open nodes)
		Set<INode> openNodes = new IndexAwareSet<INode>();
		openNodes.add(node1);
		openNodes.add(node2);
		openNodes.add(node4);
		openNodes.add(node5);
		openNodes.add(node6);

		System.out.println("");
		System.out.println("Created nodes:");
		System.out.println(node1.getAttributesString());
		System.out.println(node2.getAttributesString());
		System.out.println(node4.getAttributesString());
		System.out.println(node5.getAttributesString());
		System.out.println(node6.getAttributesString());

		// Calculate which nodes to merge
		double maxUtility = utilityCalc.getMaxCategoryUtilityMerge(openNodes)
				.getCategoryUtility();
		utilityCalc.getMaxCategoryUtilityMerge(openNodes);

		System.out.println("	The following nodes will be merged: ");
		for (INode node : utilityCalc.getMaxCategoryUtilityMerge(openNodes)
				.getNodes()) {
			System.out.println(node.getAttributesString());
		}

		List<INode> nodesToUpdate = utilityCalc.getMaxCategoryUtilityMerge(
				openNodes).getNodes();

		TreeBuilder<Number> tr = new TreeBuilder(null,
				new ClassitMaxCategoryUtilitySearcher(),
				new ClassitMaxCategoryUtilitySearcher(), null);
		INode newNode = tr.createTestingMergedNode(nodesToUpdate, openNodes);

		System.out.println("++ First New Node: "
				+ newNode.getAttributesString());

		System.out.println("	Open nodes: ");
		for (INode node : openNodes) {
			System.out.println(node.getAttributesString());
		}

		// Evaluate the new node
		Double newTxt = newNode.getAttributeValue(attTxt).getAverage();
		Double newWid = newNode.getAttributeValue(attWid).getAverage();
		Double newHt = newNode.getAttributeValue(attHt).getAverage();

		// TODO: Verify the values we assume to be correct are correct!!!
		// and that the correct nodes were merged
		// and that std dev is correct :)
		assertEquals("Txt first merge", (7.5 + 20) / 2, newTxt, 0.000001);
		assertEquals("Wid first merge", (6.5 + 7) / 2, newWid, 0.000001);
		assertEquals("Ht first merge", (13.0 + 12.0) / 2, newHt, 0.000001);

		// Create the full tree

		while (openNodes.size() >= 2) {
			nodesToUpdate = utilityCalc.getMaxCategoryUtilityMerge(openNodes)
					.getNodes();
			newNode = tr.createTestingMergedNode(nodesToUpdate, openNodes);
			System.out.println(" ");
			System.out.println("Merging " + nodesToUpdate.size()
					+ " more nodes");
			for (INode node : nodesToUpdate) {
				System.out.println(node.getAttributesString());
			}
			System.out.println("++Created new node: "
					+ newNode.getAttributesString());
		}

		// Merging last two nodes to create root node
		System.out.println(" ");
		newNode = tr.createTestingMergedNode(nodesToUpdate, openNodes);

		// Check avg values of root node
		// TODO: Check std dev
		newTxt = newNode.getAttributeValue(attTxt).getAverage();
		newWid = newNode.getAttributeValue(attWid).getAverage();
		newHt = newNode.getAttributeValue(attHt).getAverage();

		assertEquals("Txt of root", ((35.25 * 4) + 30) / 5, newTxt, 0.000001);
		assertEquals("Wid of root", ((10.375 * 4) + 36) / 5, newWid, 0.000001);
		assertEquals("Ht of root", ((19.5 * 4) + 41) / 75, newHt, 0.000001);

		assertEquals("Size of openNodes", 1, openNodes.size(), 0.1);
	}

	@Test
	public void testMergeOfThreeNodes() {
		System.out.println(" ");
		System.out
				.println("----------------------Starting test 3..----------------------");
		// Ratings are doubles
		
		/*Nodes:
		 *  Node N1:
		 *  Attribute 1 (A1): 1
		 *  Attribute 2 (A2): 3
		 *  
		 *  Node N2:
		 *  Attribute 1 (A1): 1
		 *  Attribute 3 (A3): 5
		 *  
		 *  Node N3
		 *  Attribute 2 (A2): 3
		 *  Attribute 3 (A3): 5  
		 */

		// Creating the attributes
		IAttribute N1A1 = ClassitAttributeFactory.getInstance()
				.createAttribute(1); // Attribute 1 of node 1
		IAttribute N2A1 = ClassitAttributeFactory.getInstance()
				.createAttribute(1); // Attribute 1 of node 2
		IAttribute N1A2 = ClassitAttributeFactory.getInstance()
				.createAttribute(3); // Attribute 2 of node 1
		IAttribute N3A2 = ClassitAttributeFactory.getInstance()
				.createAttribute(3); // Attribute 2 of node 3
		IAttribute N2A3 = ClassitAttributeFactory.getInstance()
				.createAttribute(5); // Attribute 3 of node 2
		IAttribute N3A3 = ClassitAttributeFactory.getInstance()
				.createAttribute(5); // Attribute 3 of node 3

		// Attribute map of the nodes
		Map<INode, IAttribute> attMap1 = new HashMap<INode, IAttribute>();
		Map<INode, IAttribute> attMap2 = new HashMap<INode, IAttribute>();
		Map<INode, IAttribute> attMap3 = new HashMap<INode, IAttribute>();

		// Shared attribute nodes for each attribute
		INode A1 = new Node(ENodeType.Content, null, null);
		INode A2 = new Node(ENodeType.Content, null, null);
		INode A3 = new Node(ENodeType.Content, null, null);

		// add the corresponding attributes to the attribute maps
		attMap1.put(A1, N1A1);
		attMap1.put(A2, N1A2);
		attMap2.put(A1, N2A1);
		attMap2.put(A3, N2A3);
		attMap3.put(A2, N3A2);
		attMap3.put(A3, N3A3);

		// create nodes
		INode node1 = new Node(ENodeType.User, null, null);
		node1.setAttributes(attMap1);

		INode node2 = new Node(ENodeType.User, null, null);
		node2.setAttributes(attMap2);

		INode node3 = new Node(ENodeType.User, null, null);
		node3.setAttributes(attMap3);

		// create the utility calcultaor
		IMaxCategoryUtilitySearcher utilityCalc = new ClassitMaxCategoryUtilitySearcher();

		// add the created user nodes to a set (set of open nodes)
		Set<INode> openNodes = new IndexAwareSet<INode>();
		openNodes.add(node1);
		openNodes.add(node2);
		openNodes.add(node3);

		System.out.println("");
		System.out.println("Created nodes:");
		System.out.println(node1.getAttributesString());
		System.out.println(node2.getAttributesString());
		System.out.println(node3.getAttributesString());

		// Calculate which nodes to merge
		double maxUtility = utilityCalc.getMaxCategoryUtilityMerge(openNodes)
				.getCategoryUtility();
		utilityCalc.getMaxCategoryUtilityMerge(openNodes);

		List<INode> nodesToUpdate = utilityCalc.getMaxCategoryUtilityMerge(
				openNodes).getNodes();

		System.out.println("");
		System.out.println("Merging the following " + nodesToUpdate.size()
				+ " nodes:");
		for (INode node : nodesToUpdate) {
			System.out.println(node.getAttributesString());
		}
		System.out.println("Utility is = " + maxUtility);
		
		assertEquals("Nodes merged", 3, nodesToUpdate.size(), 0.000001);
		assertEquals("Utility",1,maxUtility,0.000001);
		
		/*
		 * TreeBuilder<Number> tr = new TreeBuilder(null,
		 * new ClassitMaxCategoryUtilitySearcher(),
				new ClassitMaxCategoryUtilitySearcher(), null);
				INode newNode = tr.createTestingMergedNode(nodesToUpdate, openNodes);
		 */
				
	}
}
