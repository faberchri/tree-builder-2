package ch.uzh.agglorecommender.clusterer.treesearch;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import ch.uzh.agglorecommender.clusterer.TreeBuilder;
import ch.uzh.agglorecommender.clusterer.treecomponent.CobwebAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.CobwebTreeComponentFactory;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.Node;

public class CobwebMergeTest {
	/*
	 * simpleTest: Merge two nodes and check value for rating and probability of resulting node.
	 * twoLevelTest: Merge two nodes. Merge resulting node with a third node and check value for rating and probability of resulting node.
	*/

	
	private double getProbability(Double value, INode node, INode attributeNode){
		IAttribute attribute = node.getNominalAttributeValue(attributeNode);
		Iterator<Entry<Object,Double>> iterator = attribute.getProbabilities();
		Double probability = 2.0;
		while (iterator.hasNext()) {
			Entry<Object,Double> e = iterator.next();
			Integer key = (Integer) e.getKey();
			Double value2 = e.getValue();
			
			if(value.doubleValue() == ((Integer)e.getKey()).doubleValue() ){
				probability = e.getValue();
			}
		}
		return probability;
	}
	
	private int getNumberOfValues(INode node, INode attributeNode){
		IAttribute attribute = node.getNominalAttributeValue(attributeNode);
		Iterator<Entry<Object,Double>> iterator = attribute.getProbabilities();
		int counter = 0; 
		while(iterator.hasNext()){
			counter++;
			iterator.next();
		}
		return counter;
	}
		
	/*
	 * Node 1: rating = 3, p = 0.2
	 * Node 2: rating = 1, p = 0.8
	 * 
	 * This test merges the two nodes and checks rating and p for the merged node
	 */
	@Test
	public void simpleTestI() {
		
		System.out.println(" ");
		System.out
				.println("----------------------Starting Cobweb simple merge test 1..----------------------");
		// create attributes
		// ratings are integers
		Map<Integer, Double> attMap = new HashMap<Integer, Double>();
		attMap.put(3, 0.2);
		IAttribute attribute_1 = new CobwebAttribute(attMap);
		
		attMap = new HashMap<Integer, Double>();
		attMap.put(1, 0.8);
		IAttribute attribute_2 = new CobwebAttribute(attMap);
		
		// attribute maps
		Map<INode, IAttribute> attMap1 = new HashMap<INode, IAttribute>();
		Map<INode, IAttribute> attMap2 = new HashMap<INode, IAttribute>();
		
		// one common attribute
		INode sharedAttribute = new Node(ENodeType.Content, 0);
		
		// add the corresponding attributes to the attribute maps
		attMap1.put(sharedAttribute, attribute_1);
		attMap2.put(sharedAttribute, attribute_2);
		
		// create nodes
		INode node1 = new Node(ENodeType.User, 0);
		node1.setAttributes(attMap1);
		INode node2 = new Node(ENodeType.User, 0);
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

		INode newNode = new Node(ENodeType.User,0);

		TreeBuilder tr = new TreeBuilder(
				new CobwebMaxCategoryUtilitySearcher(),
				new CobwebMaxCategoryUtilitySearcher(), 
				CobwebTreeComponentFactory.getInstance(),
				CobwebTreeComponentFactory.getInstance(),
				null);
		//Merge the nodes
		try {
        Class[] parameterTypes = {List.class, Set.class};
        Method method = TreeBuilder.class.getDeclaredMethod("mergeNodes", parameterTypes);
        method.setAccessible(true);
        newNode = (INode) method.invoke(tr, nodesToUpdate, openNodes);
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
    }
		System.out.println("New node: "+newNode.getNumericalAttributesString() + newNode.getNominalAttributesString());
		
		CobwebMergeTest tester = new CobwebMergeTest();
		
		double numberOfRatings = tester.getNumberOfValues(newNode,sharedAttribute);
		double p1 = tester.getProbability(1.0,newNode,sharedAttribute);
		double p3 = tester.getProbability(3.0,newNode,sharedAttribute);
		
		assertEquals("Number of Ratings", 2.0, numberOfRatings, 0.000001);
		assertEquals("P for value 1",0.4,p1,0.000001);
		assertEquals("P for value 3",0.1,p3,0.000001);
		
		System.out.println("Done");
	
	}
	
	/*
	 * Node 1: rating = 8, p = 1.0
	 * Node 2: rating = 8, p = 1.0
	 * 
	 * This test merges the two nodes and checks rating and p for the merged node
	 */
	@Test
	public void simpleTestII() {
		
		System.out.println(" ");
		System.out
				.println("----------------------Starting Cobweb simple merge test 2..----------------------");
		// create attributes
		// ratings are integers
		Map<Integer, Double> attMap = new HashMap<Integer, Double>();
		attMap.put(8, 1.0);
		IAttribute attribute_1 = new CobwebAttribute(attMap);
		
		attMap = new HashMap<Integer, Double>();
		attMap.put(8, 1.0);
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

		INode newNode = new Node(ENodeType.User,0);

		TreeBuilder tr = new TreeBuilder(
				new CobwebMaxCategoryUtilitySearcher(),
				new CobwebMaxCategoryUtilitySearcher(), 
				CobwebTreeComponentFactory.getInstance(),
				CobwebTreeComponentFactory.getInstance(),
				null);
		//Merge the nodes
		try {
        Class[] parameterTypes = {List.class, Set.class};
        Method method = TreeBuilder.class.getDeclaredMethod("mergeNodes", parameterTypes);
        method.setAccessible(true);
        newNode = (INode) method.invoke(tr, nodesToUpdate, openNodes);
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
    }
		
		System.out.println("New node: "+newNode.getNumericalAttributesString() + newNode.getNominalAttributesString());
		
		CobwebMergeTest tester = new CobwebMergeTest();
		
		double numberOfRatings = tester.getNumberOfValues(newNode,sharedAttribute);
		double p8 = tester.getProbability(8.0,newNode,sharedAttribute);
		
		assertEquals("Number of Ratings", 1.0, numberOfRatings, 0.000001);
		assertEquals("P for value 1",1.0,p8,0.000001);
		
		System.out.println("Done");
	}
	
	/*
	 * Node 1: Attribute 1: rating = 1, p = 0.5 Attribute 2: rating = 2, p = 1
	 * Node 2: Attribute 1: rating = 1, p = 0.5 Attribute 2: rating = 8, p = 1
	 * 
	 * This test merges the two nodes and checks rating and p for the merged node
	 */
	@Test
	public void simpleTestIII() {
		
		System.out.println(" ");
		System.out
				.println("----------------------Starting Cobweb simple merge test 3..----------------------");
		// create attributes
		// ratings are integers
		Map<Integer, Double> attMap = new HashMap<Integer, Double>();
		attMap.put(1, 0.5);
		IAttribute attribute1N1 = new CobwebAttribute(attMap);
		
		attMap = new HashMap<Integer, Double>();
		attMap.put(1, 0.5);
		IAttribute attribute1N2 = new CobwebAttribute(attMap);
		
		attMap = new HashMap<Integer, Double>();
		attMap.put(2, 1.0);
		IAttribute attribute2N1 = new CobwebAttribute(attMap);
		
		attMap = new HashMap<Integer, Double>();
		attMap.put(8, 1.0);
		IAttribute attribute2N2 = new CobwebAttribute(attMap);
		
		
		
		// attribute maps
		Map<INode, IAttribute> attMap1 = new HashMap<INode, IAttribute>();
		Map<INode, IAttribute> attMap2 = new HashMap<INode, IAttribute>();
		
		// common attributes
		INode sharedAttribute1 = new Node(ENodeType.Content, null, null, null, 0);
		INode sharedAttribute2 = new Node(ENodeType.Content, null, null, null, 0);
		
		// add the corresponding attributes to the attribute maps
		attMap1.put(sharedAttribute1, attribute1N1);
		attMap1.put(sharedAttribute2, attribute2N1);
		attMap2.put(sharedAttribute1, attribute1N2);
		attMap2.put(sharedAttribute2, attribute2N2);
		
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

		INode newNode = new Node(ENodeType.User,0);

		TreeBuilder tr = new TreeBuilder(
				new CobwebMaxCategoryUtilitySearcher(),
				new CobwebMaxCategoryUtilitySearcher(), 
				CobwebTreeComponentFactory.getInstance(),
				CobwebTreeComponentFactory.getInstance(),
				null);
		//Merge the nodes
		try {
        Class[] parameterTypes = {List.class, Set.class};
        Method method = TreeBuilder.class.getDeclaredMethod("mergeNodes", parameterTypes);
        method.setAccessible(true);
        newNode = (INode) method.invoke(tr, nodesToUpdate, openNodes);
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
    }
		/*
		 * Node 1: Attribute 1: rating = 1, p = 0.5 Attribute 2: rating = 2, p = 1
		 * Node 2: Attribute 1: rating = 1, p = 0.5 Attribute 2: rating = 8, p = 1
		 * 
		 * Merged node: Attribute 1: rating = 1, p = 1 Attribute 2: rating = 2, p = 0.5. rating = 8, p = 0.5
		 */
		
		System.out.println("New node: "+newNode.getNumericalAttributesString() + newNode.getNominalAttributesString());
		
		CobwebMergeTest tester = new CobwebMergeTest();
		
		double numberOfRatingsAtt1 = tester.getNumberOfValues(newNode,sharedAttribute1);
		double numberOfRatingsAtt2 = tester.getNumberOfValues(newNode,sharedAttribute2);
		double pAtt1 = tester.getProbability(1.0,newNode,sharedAttribute1);
		double pAtt2Value2 = tester.getProbability(2.0,newNode,sharedAttribute2);
		double pAtt2Value8 = tester.getProbability(8.0,newNode,sharedAttribute2);
		
		assertEquals("Number of Ratings Attribute 1", 1.0, numberOfRatingsAtt1, 0.000001);
		assertEquals("Number of Ratings Attribute 2", 2.0, numberOfRatingsAtt2, 0.000001);
		assertEquals("P value 1 in attribute 1", 0.5, pAtt1, 0.00001);
		assertEquals("P value 2 in attribute 2", 0.5, pAtt2Value2, 0.00001);
		assertEquals("P value 8 in attribute 2", 0.5, pAtt2Value8, 0.00001);
		
		System.out.println("Done");
	}

	/*
	 * Node 1: rating = 1, p = 0.5
	 * Node 2: rating = 1, p = 1
	 * 
	 * This test merges the two nodes and checks rating and p for the merged node
	 */
	
	@Test
	public void simpleTestIV() {
		
		System.out.println(" ");
		System.out
				.println("----------------------Starting Cobweb simple merge test 4..----------------------");
		// create attributes
		// ratings are integers
		Map<Integer, Double> attMap = new HashMap<Integer, Double>();
		attMap.put(1, 0.5);
		IAttribute attribute_1 = new CobwebAttribute(attMap);
		
		attMap = new HashMap<Integer, Double>();
		attMap.put(1, 1.0);
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

		INode newNode = new Node(ENodeType.User,0);

		TreeBuilder tr = new TreeBuilder(
				new CobwebMaxCategoryUtilitySearcher(),
				new CobwebMaxCategoryUtilitySearcher(), 
				CobwebTreeComponentFactory.getInstance(),
				CobwebTreeComponentFactory.getInstance(),
				null);
		//Merge the nodes
		try {
        Class[] parameterTypes = {List.class, Set.class};
        Method method = TreeBuilder.class.getDeclaredMethod("mergeNodes", parameterTypes);
        method.setAccessible(true);
        newNode = (INode) method.invoke(tr, nodesToUpdate, openNodes);
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
    }
		
		System.out.println("New node: "+newNode.getNumericalAttributesString() + newNode.getNominalAttributesString());
	
	/*
	 * Node 1: rating = 1, p = 0.5
	 * Node 2: rating = 1, p = 1
	 * 
	 * This test merges the two nodes and checks rating and p for the merged node
	 */
		
		CobwebMergeTest tester = new CobwebMergeTest();
		
		double numberOfRatingsAtt = tester.getNumberOfValues(newNode,sharedAttribute);
		double p = tester.getProbability(1.0,newNode,sharedAttribute);
		
		assertEquals("Number of values attribute 1", 1.0, numberOfRatingsAtt, 0.000001);
		assertEquals("Probability of attribute 1", 0.75, p, 0.00001);
		
		System.out.println("Done");
		

	}
 
	/*
	 * Node 1: rating = 3, p = 0.6
	 * Node 2: rating = 3, p = 0.3
	 * 
	 * This test merges the two nodes and checks rating and p for the merged node
	 */
	
	@Test
	public void simpleTestV() {
		
		System.out.println(" ");
		System.out
				.println("----------------------Starting Cobweb simple merge test 5..----------------------");
		// create attributes
		// ratings are integers
		Map<Integer, Double> attMap = new HashMap<Integer, Double>();
		attMap.put(3, 0.6);
		IAttribute attribute_1 = new CobwebAttribute(attMap);
		
		attMap = new HashMap<Integer, Double>();
		attMap.put(3, 0.3);
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

		INode newNode = new Node(ENodeType.User,0);

		TreeBuilder tr = new TreeBuilder(
				new CobwebMaxCategoryUtilitySearcher(),
				new CobwebMaxCategoryUtilitySearcher(), 
				CobwebTreeComponentFactory.getInstance(),
				CobwebTreeComponentFactory.getInstance(),
				null);
		//Merge the nodes
		try {
        Class[] parameterTypes = {List.class, Set.class};
        Method method = TreeBuilder.class.getDeclaredMethod("mergeNodes", parameterTypes);
        method.setAccessible(true);
        newNode = (INode) method.invoke(tr, nodesToUpdate, openNodes);
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
    }
		
		System.out.println("New node: "+newNode.getNumericalAttributesString() + newNode.getNominalAttributesString());
		/*
		 * Node 1: rating = 3, p = 0.6
		 * Node 2: rating = 3, p = 0.3
		 * 
		 * This test merges the two nodes and checks rating and p for the merged node
		 */
		
		CobwebMergeTest tester = new CobwebMergeTest();
		
		double numberOfRatingsAtt = tester.getNumberOfValues(newNode,sharedAttribute);
		double p = tester.getProbability(3.0,newNode,sharedAttribute);
		
		assertEquals("Number of values attribute 1", 1.0, numberOfRatingsAtt, 0.000001);
		assertEquals("Probability of attribute 1", (0.6+0.3)/2, p, 0.00001);
		
		System.out.println("Done");
		
	}
 
	/*
	 * Node 1: rating = 1, p = 1
	 * Node 2: rating = 2, p = 1
	 * Node 3: rating = 2, p = 1
	 * 
	 * This test merges node 1 and 2. Then merges resulting node with node 3.
	 */
	
	@Test
	public void twoLevelTestI() {
		
		System.out.println(" ");
		System.out
				.println("----------------------Starting Cobweb two level merge test 1..----------------------");
		// create attributes
		// ratings are integers
		Map<Integer, Double> attMap = new HashMap<Integer, Double>();
		attMap.put(1, 1.0);
		IAttribute attribute_1 = new CobwebAttribute(attMap);
		
		attMap = new HashMap<Integer, Double>();
		attMap.put(2, 1.0);
		IAttribute attribute_2 = new CobwebAttribute(attMap);
		
		attMap = new HashMap<Integer, Double>();
		attMap.put(2, 1.0);
		IAttribute attribute_3 = new CobwebAttribute(attMap);
		
		// attribute maps
		Map<INode, IAttribute> attMap1 = new HashMap<INode, IAttribute>();
		Map<INode, IAttribute> attMap2 = new HashMap<INode, IAttribute>();
		Map<INode, IAttribute> attMap3 = new HashMap<INode, IAttribute>();
		
		// one common attribute
		INode sharedAttribute = new Node(ENodeType.Content, null, null, null, 0);
		
		// add the corresponding attributes to the attribute maps
		attMap1.put(sharedAttribute, attribute_1);
		attMap2.put(sharedAttribute, attribute_2);
		attMap3.put(sharedAttribute, attribute_3);
		
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

		// get the utility of the node resulting of a merge of node 1 and node 2
		double calcCatUt = utilityCalc.getMaxCategoryUtilityMerge(openNodes)
				.getCategoryUtility();
		
		// merging node 1 and 1
		List<INode> nodesToUpdate = utilityCalc.getMaxCategoryUtilityMerge(
				openNodes).getNodes();
		
		System.out.println("	Merging first two nodes: ");
		for (INode node : nodesToUpdate) {
			System.out.println(node.getNumericalAttributesString() + node.getNominalAttributesString());
		}

		INode newNode12 = new Node(ENodeType.User,0);

		TreeBuilder tr = new TreeBuilder(
				new CobwebMaxCategoryUtilitySearcher(),
				new CobwebMaxCategoryUtilitySearcher(), 
				CobwebTreeComponentFactory.getInstance(),
				CobwebTreeComponentFactory.getInstance(),
				null);
		//Merge the nodes
		try {
        Class[] parameterTypes = {List.class, Set.class};
        Method method = TreeBuilder.class.getDeclaredMethod("mergeNodes", parameterTypes);
        method.setAccessible(true);
        newNode12 = (INode) method.invoke(tr, nodesToUpdate, openNodes);
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
    }
				
		System.out.println("New node: "+newNode12.getNumericalAttributesString() + newNode12.getNominalAttributesString());
		
		// merging resulting node with node 3
		openNodes.add(node3);
		
		INode newNode123 = new Node(ENodeType.User,0);
		
		nodesToUpdate = utilityCalc.getMaxCategoryUtilityMerge(
				openNodes).getNodes();
		
		System.out.println("	Merging remaining two nodes: ");
		for (INode node : nodesToUpdate) {
			System.out.println(node.getNumericalAttributesString() + node.getNominalAttributesString());
		}

		try {
        Class[] parameterTypes = {List.class, Set.class};
        Method method = TreeBuilder.class.getDeclaredMethod("mergeNodes", parameterTypes);
        method.setAccessible(true);
        newNode123 = (INode) method.invoke(tr, nodesToUpdate, openNodes);
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
    }
		
		System.out.println("New node: "+newNode123.getNumericalAttributesString() + newNode123.getNominalAttributesString());
	
		/*
		 * Node 1: rating = 1, p = 1
		 * Node 2: rating = 2, p = 1
		 * Node 3: rating = 2, p = 1
		 * 
		 * This test merges node 1 and 2. Then merges resulting node with node 3.
		 */
		
		CobwebMergeTest tester = new CobwebMergeTest();
		
		//Values for successful first merge
		double numberOfRatingsAtt12 = tester.getNumberOfValues(newNode12,sharedAttribute);
		double pNode12Value1 = tester.getProbability(1.0,newNode12,sharedAttribute);
		double pNode12Value2 = tester.getProbability(2.0,newNode12,sharedAttribute);
		
		//Values for successful second merge
		double numberOfRatingsAtt123 = tester.getNumberOfValues(newNode123, sharedAttribute);
		double pNode123Value1 = tester.getProbability(1.0, newNode123, sharedAttribute);
		double pNode123Value2 = tester.getProbability(2.0, newNode123, sharedAttribute);
		
		//Assertions for first merge
		assertEquals("Number of distinct attribute values first merge", 2.0, numberOfRatingsAtt12, 0.000001);
		assertEquals("Probability value 1 after first merge", 0.5, pNode12Value1 , 0.00001);
		assertEquals("Probability value 2 after first merge", 0.5, pNode12Value2, 0.00001);
		
		//Assertions for second merge
		assertEquals("Number of distinct attribute values second merge",2.0,numberOfRatingsAtt123,0.00001);
		assertEquals("Probability value 1 after second merge",1.0/3,pNode123Value1,0.00001);
		assertEquals("Probability value 2 after second merge",1.0/3*2,pNode123Value2,0.00001);
		
		System.out.println("Done");
	}
 
	/*
	 * Node 1: rating = 1, p = 0.5
	 * Node 2: rating = 1, p = 0.5
	 * Node 3: rating = 1, p = 0.8
	 * 
	 * This test merges node 1 and 2. Then merges resulting node with node 3.
	 */
	
	@Test
	public void twoLevelTestII() {
		
		System.out.println(" ");
		System.out
				.println("----------------------Starting Cobweb two level merge test 2..----------------------");
		// create attributes
		// ratings are integers
		Map<Integer, Double> attMap = new HashMap<Integer, Double>();
		attMap.put(1, 0.5);
		IAttribute attribute_1 = new CobwebAttribute(attMap);
		
		attMap = new HashMap<Integer, Double>();
		attMap.put(1, 0.5);
		IAttribute attribute_2 = new CobwebAttribute(attMap);
		
		attMap = new HashMap<Integer, Double>();
		attMap.put(1, 0.8);
		IAttribute attribute_3 = new CobwebAttribute(attMap);
		
		// attribute maps
		Map<INode, IAttribute> attMap1 = new HashMap<INode, IAttribute>();
		Map<INode, IAttribute> attMap2 = new HashMap<INode, IAttribute>();
		Map<INode, IAttribute> attMap3 = new HashMap<INode, IAttribute>();
		
		// one common attribute
		INode sharedAttribute = new Node(ENodeType.Content, null, null, null, 0);
		
		// add the corresponding attributes to the attribute maps
		attMap1.put(sharedAttribute, attribute_1);
		attMap2.put(sharedAttribute, attribute_2);
		attMap3.put(sharedAttribute, attribute_3);
		
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

		// get the utility of the node resulting of a merge of node 1 and node 2
		double calcCatUt = utilityCalc.getMaxCategoryUtilityMerge(openNodes)
				.getCategoryUtility();
		
		// merging node 1 and 1
		List<INode> nodesToUpdate = utilityCalc.getMaxCategoryUtilityMerge(
				openNodes).getNodes();
		
		System.out.println("	Merging first two nodes: ");
		for (INode node : nodesToUpdate) {
			System.out.println(node.getNumericalAttributesString() + node.getNominalAttributesString());
		}

		INode newNode12 = new Node(ENodeType.User,0);

		TreeBuilder tr = new TreeBuilder(
				new CobwebMaxCategoryUtilitySearcher(),
				new CobwebMaxCategoryUtilitySearcher(), 
				CobwebTreeComponentFactory.getInstance(),
				CobwebTreeComponentFactory.getInstance(),
				null);
		//Merge the nodes
		try {
        Class[] parameterTypes = {List.class, Set.class};
        Method method = TreeBuilder.class.getDeclaredMethod("mergeNodes", parameterTypes);
        method.setAccessible(true);
        newNode12 = (INode) method.invoke(tr, nodesToUpdate, openNodes);
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
    }
				
		System.out.println("New node: "+newNode12.getNumericalAttributesString() + newNode12.getNominalAttributesString());
		
		// merging resulting node with node 3
		openNodes.add(node3);
		
		INode newNode123 = new Node(ENodeType.User,0);
		
		nodesToUpdate = utilityCalc.getMaxCategoryUtilityMerge(
				openNodes).getNodes();
		
		System.out.println("	Merging remaining two nodes: ");
		for (INode node : nodesToUpdate) {
			System.out.println(node.getNumericalAttributesString() + node.getNominalAttributesString());
		}

		try {
        Class[] parameterTypes = {List.class, Set.class};
        Method method = TreeBuilder.class.getDeclaredMethod("mergeNodes", parameterTypes);
        method.setAccessible(true);
        newNode123 = (INode) method.invoke(tr, nodesToUpdate, openNodes);
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
    }
		
		System.out.println("New node: "+newNode123.getNumericalAttributesString() + newNode123.getNominalAttributesString());
	
		/*
		 * Node 1: rating = 1, p = 0.5
		 * Node 2: rating = 1, p = 0.5
		 * Node 3: rating = 1, p = 0.8
		 * 
		 * This test merges node 1 and 2. Then merges resulting node with node 3.
		 */
		
		CobwebMergeTest tester = new CobwebMergeTest();
		
		//Values for successful first merge
		double numberOfRatingsAtt12 = tester.getNumberOfValues(newNode12,sharedAttribute);
		double pNode12Value1 = tester.getProbability(1.0,newNode12,sharedAttribute);
		
		//Values for successful second merge
		double numberOfRatingsAtt123 = tester.getNumberOfValues(newNode123, sharedAttribute);
		double pNode123Value1 = tester.getProbability(1.0, newNode123, sharedAttribute);
		
		//Assertions for first merge
		assertEquals("Number of distinct attribute values first merge", 1.0, numberOfRatingsAtt12, 0.000001);
		assertEquals("Probability value 1 after first merge", 0.5, pNode12Value1 , 0.00001);
		
		//Assertions for second merge
		assertEquals("Number of distinct attribute values second merge",1.0,numberOfRatingsAtt123,0.00001);
		assertEquals("Probability value 1 after second merge", ((0.5*2)+0.8)/3,pNode123Value1,0.00001);
		
		System.out.println("Done");
	}
 
	/*
	 * Node 1: rating = 3, p = 0.8
	 * Node 2: rating = 3, p = 0.2
	 * Node 3: rating = 3, p = 0.1
	 * 
	 * This test merges node 1 and 2. Then merges resulting node with node 3.
	 */
	
	/*
	public void twoLevelTestIII() {
		
		System.out.println(" ");
		System.out
				.println("----------------------Starting Cobweb two level merge test 3..----------------------");
		// create attributes
		// ratings are integers
		Map<Integer, Double> attMap = new HashMap<Integer, Double>();
		attMap.put(3, 0.8);
		IAttribute attribute_1 = new CobwebAttribute(attMap);
		
		attMap = new HashMap<Integer, Double>();
		attMap.put(3, 0.2);
		IAttribute attribute_2 = new CobwebAttribute(attMap);
		
		attMap = new HashMap<Integer, Double>();
		attMap.put(3, 0.1);
		IAttribute attribute_3 = new CobwebAttribute(attMap);
		
		// attribute maps
		Map<INode, IAttribute> attMap1 = new HashMap<INode, IAttribute>();
		Map<INode, IAttribute> attMap2 = new HashMap<INode, IAttribute>();
		Map<INode, IAttribute> attMap3 = new HashMap<INode, IAttribute>();
		
		// one common attribute
		INode sharedAttribute = new Node(ENodeType.Content, null, null);
		
		// add the corresponding attributes to the attribute maps
		attMap1.put(sharedAttribute, attribute_1);
		attMap2.put(sharedAttribute, attribute_2);
		attMap3.put(sharedAttribute, attribute_3);
		
		// create nodes
		INode node1 = new Node(ENodeType.User, null, null);
		node1.setAttributes(attMap1);
		INode node2 = new Node(ENodeType.User, null, null);
		node2.setAttributes(attMap2);
		INode node3 = new Node(ENodeType.User, null, null);
		node3.setAttributes(attMap3);

		// create the utility calcultaor
		IMaxCategoryUtilitySearcher utilityCalc = new CobwebMaxCategoryUtilitySearcher();

		// add the  created user nodes to a set (set of open nodes)
		Set<INode> openNodes = new IndexAwareSet<INode>();
		openNodes.add(node1);
		openNodes.add(node2);

		// get the utility of the node resulting of a merge of node 1 and node 2
		double calcCatUt = utilityCalc.getMaxCategoryUtilityMerge(openNodes)
				.getCategoryUtility();
		
		// merging node 1 and 1
		List<INode> nodesToUpdate = utilityCalc.getMaxCategoryUtilityMerge(
				openNodes).getNodes();
		
		System.out.println("	Merging the following nodes: ");
		for (INode node : nodesToUpdate) {
			System.out.println(node.getAttributesString());
		}

		INode newNode12 = new Node(ENodeType.User,0);

		nodesToUpdate = utilityCalc.getMaxCategoryUtilityMerge(
				openNodes).getNodes();
		
		TreeBuilder tr = new TreeBuilder(
				new CobwebMaxCategoryUtilitySearcher(),
				new CobwebMaxCategoryUtilitySearcher(), 
				CobwebTreeComponentFactory.getInstance(),
				CobwebTreeComponentFactory.getInstance(),
				null);
		//Merge the nodes
		try {
        Class[] parameterTypes = {List.class, Set.class, Counter.class};
        Method method = TreeBuilder.class.getDeclaredMethod("mergeNodes", parameterTypes);
        method.setAccessible(true);
        newNode12 = (INode) method.invoke(tr, nodesToUpdate, openNodes, Counter.getInstance());
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
    }
		
		// merging resulting node with node 3
		openNodes.add(node3);
		
		INode newNode123 = new Node(ENodeType.User,0);

		//Merge the nodes
		try {
        Class[] parameterTypes = {List.class, Set.class, Counter.class};
        Method method = TreeBuilder.class.getDeclaredMethod("mergeNodes", parameterTypes);
        method.setAccessible(true);
        newNode123 = (INode) method.invoke(tr, nodesToUpdate, openNodes, Counter.getInstance());
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
    }
		
		CobwebMergeTest tester = new CobwebMergeTest();
		
		double rating = tester.getValue(newNode123);
		double support = tester.getProbability(newNode123);
		
	//TODO: Check rating & support of newNode123
		//The following line alerts that getter have not yet been implemented. To be deleted when done.
		System.out.println("****** NO RESULTS AVAILABLE ******");
	}
	*/
}
