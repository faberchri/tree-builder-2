package ch.uzh.agglorecommender.clusterer.treesearch;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import ch.uzh.agglorecommender.clusterer.TreeBuilder;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.Node;
import ch.uzh.agglorecommender.clusterer.treecomponent.TreeComponentFactory;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;


public class ClassitMaxCategoryUtilitySearcherTest {


	//Node1, A1 = 4, A2 = 3
	//Node2, A2 = 5, A3 = 5
	@Test
	public void classitTestI() throws InstantiationException, IllegalAccessException {

		System.out.println(" ");
		System.out
				.println("----------------------Starting test 1..----------------------");

		// Based on example in Google Docs
		// https://docs.google.com/spreadsheet/ccc?key=0AnvRo1G6q1ffdEJLWjJiX2QtX2hza1l4WG5Sclp4WEE#gid=0

		// Creating the attributes

		IAttribute N1A1 = TreeComponentFactory.getInstance()
				.createNumericalLeafAttribute(4.0);
		IAttribute N1A2 = TreeComponentFactory.getInstance()
				.createNumericalLeafAttribute(3.0);
		IAttribute N2A2 = TreeComponentFactory.getInstance()
				.createNumericalLeafAttribute(5.0);
		IAttribute N2A3 = TreeComponentFactory.getInstance()
				.createNumericalLeafAttribute(5.0);

		// ClassitAttribute map of node 1
		Map<INode, IAttribute> attMap1 = new HashMap<INode, IAttribute>();

		// this node is an attribute of node 1 and node 2
		INode sharedAttribute = new Node(ENodeType.Content, null, null);
		
		//public Node(ENodeType nodeType, String dataSetId, ImmutableMap<String, Boolean> useForClustering)

		// add the corresponding attributes to the attribute map of node 1
		attMap1.put(new Node(ENodeType.Content, null, null), N1A1);
		attMap1.put(sharedAttribute, N1A2);

		// create node 1
		INode node1 = new Node(ENodeType.User, null, null);
		node1.setRatingAttributes(attMap1);

		// attribute map of node 2
		Map<INode, IAttribute> attMap2 = new HashMap<INode, IAttribute>();

		// add the corresponding attributes to the attribute map of node 2
		attMap2.put(sharedAttribute, N2A2);
		attMap2.put(new Node(ENodeType.Content, null, null), N2A3);

		// create node 2
		INode node2 = new Node(ENodeType.User, null, null);
		node2.setRatingAttributes(attMap2);

		

		// add the two created user nodes to a set (set of open nodes)
		Set<INode> openNodes = new IndexAwareSet<INode>();
		openNodes.add(node1);
		openNodes.add(node2);

		// Print standard deviations
		System.out.println("Node 1: " + node1.getNodeType()+ ", num atts: " + node1.getNumericalAttributesString());
		System.out.println("Node 2: " + node2.getNodeType()+ ", num atts: " + node2.getNumericalAttributesString());

		ArrayList<INode> nodesToUpdate = new ArrayList<INode>();
		nodesToUpdate.add(node1);
		nodesToUpdate.add(node2);

		// Merge
		IMaxCategoryUtilitySearcher utilityCalc = new ClassitMaxCategoryUtilitySearcher();
		
		IMergeResult merge = null;
		TreeBuilder tr = null;
		ImmutableCollection<INode> nodeSet = ImmutableSet.copyOf(nodesToUpdate);
		IClusterSetIndexed<INode> leafNodes = new ClusterSetIndexed<INode>(nodeSet);
		

		//Instantiate TreeBuilder
		try {
            //Class[] parameterTypes = {};
            Constructor cons[] = TreeBuilder.class.getDeclaredConstructors();
            cons[0].setAccessible(true);
            tr = (TreeBuilder)cons[0].newInstance(null);
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
		
		
		//Search best merge
		try {
            Class[] parameterTypes = {IClusterSetIndexed.class, IMaxCategoryUtilitySearcher.class};
            
          //This causes an Exception.. Why?
            //private IMergeResult searchBestMergeResultIndexed(IClusterSetIndexed<INode> nodes, IMaxCategoryUtilitySearcher mcus)
            Method method = TreeBuilder.class.getDeclaredMethod("searchBestMergeResultIndexed", parameterTypes);
            
            method.setAccessible(true);
            merge = (IMergeResult) method.invoke(tr,leafNodes,utilityCalc);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        
				
		Double utility = merge.getCategoryUtility();
		
		System.out.println("Merge result: "+merge.toString());

		// evaluate the category utility result
		assertEquals("category utility", 1/(Math.sqrt(2))/3, utility, 0.000001);

	}
	
	// Node 1: A1 = 1, A2 = 8
	// Node 2: A1 = 4, A2 = 5
	//@Test
//	public void classitTestII() {
//
//		System.out.println(" ");
//		System.out
//				.println("----------------------Starting test ..----------------------");
//
//		// Creating the attributes
//
//		IAttribute N1A1 = TreeComponentFactory.getInstance()
//				.createNumericalLeafAttribute(1.0);
//		IAttribute N1A2 = TreeComponentFactory.getInstance()
//				.createNumericalLeafAttribute(8.0);
//		IAttribute N2A1 = TreeComponentFactory.getInstance()
//				.createNumericalLeafAttribute(4.0);
//		IAttribute N2A2 = TreeComponentFactory.getInstance()
//				.createNumericalLeafAttribute(5.0);
//
//		// ClassitAttribute map of node 1
//		Map<INode, IAttribute> attMap1 = new HashMap<INode, IAttribute>();
//
//		// this node is an attribute of node 1 and node 2
//		INode sharedAttribute1 = new Node(ENodeType.Content, 0);
//		INode sharedAttribute2 = new Node(ENodeType.Content, 0);
//		
//		// add the corresponding attributes to the attribute map of node 1
//		attMap1.put(sharedAttribute1, N1A1);
//		attMap1.put(sharedAttribute2, N1A2);
//
//		// create node 1
//		INode node1 = new Node(ENodeType.User, 0);
//		node1.setRatingAttributes(attMap1);
//
//		// attribute map of node 2
//		Map<INode, IAttribute> attMap2 = new HashMap<INode, IAttribute>();
//
//		// add the corresponding attributes to the attribute map of node 2
//		attMap2.put(sharedAttribute1, N2A1);
//		attMap2.put(sharedAttribute2, N2A2);
//
//		// create node 2
//		INode node2 = new Node(ENodeType.User, 0);
//		node2.setRatingAttributes(attMap2);
//
//		// add the two created user nodes to a set (set of open nodes)
//		Set<INode> openNodes = new IndexAwareSet<INode>();
//		openNodes.add(node1);
//		openNodes.add(node2);
//
//		// Print standard deviations
//		System.out.println("Node 1: " + node1.getNumericalAttributesString());
//		System.out.println("Node 2: " + node2.getNumericalAttributesString());
//
//		ArrayList<INode> nodesToUpdate = new ArrayList<INode>();
//		nodesToUpdate.add(node1);
//		nodesToUpdate.add(node2);
//
//		// Merge
//		IMaxCategoryUtilitySearcher utilityCalc = new ClassitMaxCategoryUtilitySearcher();
//		
//		IMergeResult merge = null;
//		ImmutableCollection<INode> nodeSet = ImmutableSet.copyOf(nodesToUpdate);
//		IClusterSet<INode> leafNodes = new ClusterSet<INode>(nodeSet);
//		
//
//		TreeBuilder tr = new TreeBuilder(
//				new ClassitMaxCategoryUtilitySearcher(),
//				new ClassitMaxCategoryUtilitySearcher(),
//				TreeComponentFactory.getInstance(),
//				TreeComponentFactory.getInstance(),
//				null);
//		
//		try {
//            Class[] parameterTypes = {IClusterSet.class, IMaxCategoryUtilitySearcher.class};
//            Method method = TreeBuilder.class.getDeclaredMethod("searchBestMergeResult", parameterTypes);
//            method.setAccessible(true);
//          
//            //This causes an IllegalArgumentException (object is not an instance of declaring class).. Why?
//            merge = (IMergeResult) method.invoke(tr,leafNodes,utilityCalc);
//        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        
//				
//		Double utility = merge.getCategoryUtility();
//		
//		System.out.println("Merge result: "+merge.toString());
//
//		// evaluate the category utility result
//		
//		assertEquals("category utility", 3.0 / 2.0, utility, 0.000001);
//
//	}
//	
//	// Node 1: A1 = 14, A2 = 7, A3 = 8
//	// Node 2: A1 = 12, A2 = 7, A3 = 20
//	// (Example from J.H.Gennari et al."Models of incremental concept formation", p. 32, figure 6)
//	@Test
//	public void classitTestIII() {
//
//		System.out.println(" ");
//		System.out
//				.println("----------------------Starting test ..----------------------");
//
//		// Creating the attributes
//
//		IAttribute N1A1 = TreeComponentFactory.getInstance()
//				.createNumericalLeafAttribute(14.0);
//		IAttribute N1A2 = TreeComponentFactory.getInstance()
//				.createNumericalLeafAttribute(7.0);
//		IAttribute N1A3 = TreeComponentFactory.getInstance()
//				.createNumericalLeafAttribute(8.0);
//		IAttribute N2A1 = TreeComponentFactory.getInstance()
//				.createNumericalLeafAttribute(12.0);
//		IAttribute N2A2 = TreeComponentFactory.getInstance()
//				.createNumericalLeafAttribute(7.0);
//		IAttribute N2A3 = TreeComponentFactory.getInstance()
//				.createNumericalLeafAttribute(20.0);
//
//		// ClassitAttribute map of node 1
//		Map<INode, IAttribute> attMap1 = new HashMap<INode, IAttribute>();
//
//		// this node is an attribute of node 1 and node 2
//		INode sharedAttribute1 = new Node(ENodeType.Content, 0);
//		INode sharedAttribute2 = new Node(ENodeType.Content, 0);
//		INode sharedAttribute3 = new Node(ENodeType.Content, 0);
//		
//		// add the corresponding attributes to the attribute map of node 1
//		attMap1.put(sharedAttribute1, N1A1);
//		attMap1.put(sharedAttribute2, N1A2);
//		attMap1.put(sharedAttribute3, N1A3);
//
//		// create node 1
//		INode node1 = new Node(ENodeType.User, 0);
//		node1.setRatingAttributes(attMap1);
//
//		// attribute map of node 2
//		Map<INode, IAttribute> attMap2 = new HashMap<INode, IAttribute>();
//
//		// add the corresponding attributes to the attribute map of node 2
//		attMap2.put(sharedAttribute1, N2A1);
//		attMap2.put(sharedAttribute2, N2A2);
//		attMap2.put(sharedAttribute2, N2A3);
//
//		// create node 2
//		INode node2 = new Node(ENodeType.User, 0);
//		node2.setRatingAttributes(attMap2);
//
//		// add the two created user nodes to a set (set of open nodes)
//		Set<INode> openNodes = new IndexAwareSet<INode>();
//		openNodes.add(node1);
//		openNodes.add(node2);
//
//		// Print standard deviations
//		System.out.println("Node 1: " + node1.getNumericalAttributesString());
//		System.out.println("Node 2: " + node2.getNumericalAttributesString());
//
//		ArrayList<INode> nodesToUpdate = new ArrayList<INode>();
//		nodesToUpdate.add(node1);
//		nodesToUpdate.add(node2);
//
//		// Merge
//		IMaxCategoryUtilitySearcher utilityCalc = new ClassitMaxCategoryUtilitySearcher();
//		
//		IMergeResult merge = null;
//		ImmutableCollection<INode> nodeSet = ImmutableSet.copyOf(nodesToUpdate);
//		IClusterSet<INode> leafNodes = new ClusterSet<INode>(nodeSet);
//		
//
//		TreeBuilder tr = new TreeBuilder(
//				new ClassitMaxCategoryUtilitySearcher(),
//				new ClassitMaxCategoryUtilitySearcher(),
//				TreeComponentFactory.getInstance(),
//				TreeComponentFactory.getInstance(),
//				null);
//		
//		try {
//            Class[] parameterTypes = {IClusterSet.class, IMaxCategoryUtilitySearcher.class};
//            Method method = TreeBuilder.class.getDeclaredMethod("searchBestMergeResult", parameterTypes);
//            method.setAccessible(true);
//          
//            merge = (IMergeResult) method.invoke(tr,leafNodes,utilityCalc);
//        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        
//				
//		Double utility = merge.getCategoryUtility();
//		
//		System.out.println("Merge result: "+merge.toString());
//
//		// evaluate the category utility result
//		
//		assertEquals("category utility", 0.72222222, utility, 0.000001);
//
//	}
///*
//	@Test
//	public void testSimpleClassitTree() {
//
//		System.out.println(" ");
//		System.out
//				.println("----------------------Starting test 2..----------------------");
//		// Ratings are doubles

		/*
		 * Based on example in J.H.Gennari et al.
		 * "Models of incremental concept formation", page 35, Figure 10
		 

		// Creating the attributes

		IAttribute H1 = TreeComponentFactory.getInstance().createAttribute(
				13); // Ht of instance 1
		IAttribute H2 = TreeComponentFactory.getInstance().createAttribute(
				12); // Ht of instance 2
		IAttribute H4 = TreeComponentFactory.getInstance().createAttribute(
				28); // Ht of instance 4
		IAttribute H5 = TreeComponentFactory.getInstance().createAttribute(
				25); // Ht of instance 5
		IAttribute H6 = TreeComponentFactory.getInstance().createAttribute(
				41); // Ht of instance 6

		IAttribute W1 = TreeComponentFactory.getInstance().createAttribute(
				6.5); // Wid for instance 1
		IAttribute W2 = TreeComponentFactory.getInstance()
				.createAttribute(7); // Wid for instance 2
		IAttribute W4 = TreeComponentFactory.getInstance().createAttribute(
				13); // Wid for instance 4
		IAttribute W5 = TreeComponentFactory.getInstance().createAttribute(
				15); // Wid for instance 5
		IAttribute W6 = TreeComponentFactory.getInstance().createAttribute(
				36); // Wid for instance 6

		IAttribute T1 = TreeComponentFactory.getInstance().createAttribute(
				7.5); // Txt for instance 1
		IAttribute T2 = TreeComponentFactory.getInstance().createAttribute(
				20); // Txt for instance 2
		IAttribute T4 = TreeComponentFactory.getInstance().createAttribute(
				19); // Txt for instance 4
		IAttribute T5 = TreeComponentFactory.getInstance().createAttribute(
				24); // Txt for instance 5
		IAttribute T6 = TreeComponentFactory.getInstance().createAttribute(
				30); // Txt for instance 6

		// ClassitAttribute map of the nodes
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

		TreeBuilder tr = new TreeBuilder(
				new ClassitMaxCategoryUtilitySearcher(),
				new ClassitMaxCategoryUtilitySearcher(),
				TreeComponentFactory.getInstance(),
				TreeComponentFactory.getInstance(),
				null);
		//INode newNode = tr.createTestingMergedNode(nodesToUpdate, openNodes);
		INode newNode = new Node(ENodeType.Content,null,null);
		
		try {
            Class[] parameterTypes = {List.class, Set.class, Counter.class};
            Method method = TreeBuilder.class.getDeclaredMethod("mergeNodes", parameterTypes);
            method.setAccessible(true);
            newNode = (INode) method.invoke(tr, nodesToUpdate, openNodes, Counter.getInstance());
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }

		System.out.println("++ First New Node: "
				+ newNode.getAttributesString());

		System.out.println("	Open nodes: ");
		for (INode node : openNodes) {
			System.out.println(node.getAttributesString());
		}

		// Evaluate the new node
		Double newTxt = newNode.getAttributeValue(attTxt).getSumOfRatings() / newNode.getAttributeValue(attTxt).getSupport();
		Double newWid = newNode.getAttributeValue(attWid).getSumOfRatings() / newNode.getAttributeValue(attWid).getSupport();
		Double newHt = newNode.getAttributeValue(attHt).getSumOfRatings() / newNode.getAttributeValue(attHt).getSupport();

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
			//newNode = tr.createTestingMergedNode(nodesToUpdate, openNodes);
			
			try {
	            Class[] parameterTypes = {List.class, Set.class, Counter.class};
	            Method method = TreeBuilder.class.getDeclaredMethod("mergeNodes", parameterTypes);
	            method.setAccessible(true);
	            newNode = (INode) method.invoke(tr, nodesToUpdate, openNodes, Counter.getInstance());
	        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
	            e.printStackTrace();
	        }
			
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
		//newNode = tr.createTestingMergedNode(nodesToUpdate, openNodes);
		
		try {
            Class[] parameterTypes = {List.class, Set.class, Counter.class};
            Method method = TreeBuilder.class.getDeclaredMethod("mergeNodes", parameterTypes);
            method.setAccessible(true);
            newNode = (INode) method.invoke(tr, nodesToUpdate, openNodes, Counter.getInstance());
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }

		// Check avg values of root node
		// TODO: Check std dev
		newTxt = newNode.getAttributeValue(attTxt).getSumOfRatings() / newNode.getAttributeValue(attTxt).getSupport();
		newWid = newNode.getAttributeValue(attWid).getSumOfRatings() / newNode.getAttributeValue(attWid).getSupport();
		newHt = newNode.getAttributeValue(attHt).getSumOfRatings() / newNode.getAttributeValue(attHt).getSupport();

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
		 *  ClassitAttribute 1 (A1): 1
		 *  ClassitAttribute 2 (A2): 3
		 *  
		 *  Node N2:
		 *  ClassitAttribute 1 (A1): 1
		 *  ClassitAttribute 3 (A3): 5
		 *  
		 *  Node N3
		 *  ClassitAttribute 2 (A2): 3
		 *  ClassitAttribute 3 (A3): 5  
		 
		// Creating the attributes
		IAttribute N1A1 = TreeComponentFactory.getInstance()
				.createAttribute(1); // ClassitAttribute 1 of node 1
		IAttribute N2A1 = TreeComponentFactory.getInstance()
				.createAttribute(1); // ClassitAttribute 1 of node 2
		IAttribute N1A2 = TreeComponentFactory.getInstance()
				.createAttribute(3); // ClassitAttribute 2 of node 1
		IAttribute N3A2 = TreeComponentFactory.getInstance()
				.createAttribute(3); // ClassitAttribute 2 of node 3
		IAttribute N2A3 = TreeComponentFactory.getInstance()
				.createAttribute(5); // ClassitAttribute 3 of node 2
		IAttribute N3A3 = TreeComponentFactory.getInstance()
				.createAttribute(5); // ClassitAttribute 3 of node 3

		// ClassitAttribute map of the nodes
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
		 
				
	}

	@Test
	public void testSimpleStDevCalculation(){
		
		System.out.println(" ");
		System.out
				.println("----------------------Starting test 3..----------------------");
		// Ratings are integers
		
		//Create the attributes
		IAttribute A1 = TreeComponentFactory.getInstance().createAttribute(3);
		IAttribute A2 = TreeComponentFactory.getInstance().createAttribute(7);
		
		//Create the attribute maps
		Map<INode,IAttribute> attMap1 = new HashMap<INode,IAttribute>();
		Map<INode,IAttribute> attMap2 = new HashMap<INode,IAttribute>();
		
		//Create shared ClassitAttribute node
		INode sharedAttribute = new Node(ENodeType.Content, null, null);
		
		//Add the shared attribute to the attribute nodes
		attMap1.put(sharedAttribute,A1);
		attMap2.put(sharedAttribute,A2);
		
		//Create the nodes
		INode node1 = new Node(ENodeType.User,null,null);
		node1.setAttributes(attMap1);
		INode node2 = new Node(ENodeType.User,null,null);		
		node2.setAttributes(attMap2);
		
		//Create the utility calculator
		IMaxCategoryUtilitySearcher utilityCalc = new ClassitMaxCategoryUtilitySearcher();
		
		//Add the two nodes to a set
		Set<INode> openNodes = new IndexAwareSet<INode>();
		openNodes.add(node1);
		openNodes.add(node2);
		
		System.out.println("Open nodes:");
		for(INode node:openNodes){
			System.out.println(node.getAttributesString());
		}
		
		List<INode> nodesToUpdate = utilityCalc.getMaxCategoryUtilityMerge(
				openNodes).getNodes();
		
		//Merge the two nodes
		TreeBuilder tr = new TreeBuilder(
				new ClassitMaxCategoryUtilitySearcher(),
				new ClassitMaxCategoryUtilitySearcher(),
				TreeComponentFactory.getInstance(),
				TreeComponentFactory.getInstance(),
				null);
		//INode newNode = tr.createTestingMergedNode(nodesToUpdate, openNodes);
		INode newNode = new Node(ENodeType.Content,null,null);
		
		try {
            Class[] parameterTypes = {List.class, Set.class, Counter.class};
            Method method = TreeBuilder.class.getDeclaredMethod("mergeNodes", parameterTypes);
            method.setAccessible(true);
            newNode = (INode) method.invoke(tr, nodesToUpdate, openNodes, Counter.getInstance());
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }

		
		System.out.println("New node: "+newNode.getAttributesString());
		
		//Retreive the attribute
//		double newStDev = newNode.getAttributeValue(sharedAttribute).getStdDev();
		INode[] merge = {sharedAttribute};
		double newStDev = ClassitMaxCategoryUtilitySearcher.calcStdDevOfAttribute(sharedAttribute, merge);
		
		assertEquals("Standard dev of new node",2,newStDev,0.000001);
	}
	*/
}
