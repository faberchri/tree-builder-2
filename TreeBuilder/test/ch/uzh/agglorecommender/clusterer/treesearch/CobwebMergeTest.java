package ch.uzh.agglorecommender.clusterer.treesearch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import ch.uzh.agglorecommender.clusterer.Counter;
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

	
	/*
	 * This method is used in Cobweb tests to get the support for node 
	 * !!Not implemented yet!!
	 */
	private double getSupport(INode node){
		/*
		try{
			Class nodeClass = newNode.getClass();
			Field attributes = nodeClass.getDeclaredField("attributes");
			attributes.setAccessible(true);
			
			//Method getP = CobwebAttribute.class.getDeclaredMethod("getProbabilities");
			//getP.setAccessible(true);
			
			//TODO: Fix the following statement
			CobwebAttribute newAttributes = (CobwebAttribute)attributes.getP(nodeClass);
			
			rating = newAttributes.getSumOfRatings();
			support = newAttributes.getSupport();
			
		}
		catch(NoSuchFieldException | //NoSuchMethodException | 
				IllegalAccessException e){
			e.printStackTrace();
		}
		*/
		return 0;
	}
	
	/*
	 * This method is used in Cobweb tests to get the value (rating) for node 
	 * !!Not implemented yet!!
	 */
	private double getValue(INode node){
		return 0;
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
		INode sharedAttribute = new Node(ENodeType.Content, null, null);
		
		// add the corresponding attributes to the attribute maps
		attMap1.put(sharedAttribute, attribute_1);
		attMap2.put(sharedAttribute, attribute_2);
		
		// create nodes
		INode node1 = new Node(ENodeType.User, null, null);
		node1.setAttributes(attMap1);
		INode node2 = new Node(ENodeType.User, null, null);
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
			System.out.println(node.getAttributesString());
		}

		INode newNode = new Node(ENodeType.User);

		TreeBuilder tr = new TreeBuilder(null,
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
        newNode = (INode) method.invoke(tr, nodesToUpdate, openNodes, Counter.getInstance());
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
    }
		
		CobwebMergeTest tester = new CobwebMergeTest();
		
		double rating = tester.getValue(newNode);
		double support = tester.getSupport(newNode);
		
		
	//TODO: Check rating & support	
	//The following line alerts that getter have not yet been implemented. To be deleted when done.
		System.out.println("****** NO RESULTS AVAILABLE ******");
	}
	
	/*
	 * Node 1: rating = 1, p = 0.00000000000001
	 * Node 2: rating = 5, p = 0.9865435678653654584
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
		attMap.put(1, 0.00000000000001);
		IAttribute attribute_1 = new CobwebAttribute(attMap);
		
		attMap = new HashMap<Integer, Double>();
		attMap.put(5, 0.9865435678653654584);
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
		INode node1 = new Node(ENodeType.User, null, null);
		node1.setAttributes(attMap1);
		INode node2 = new Node(ENodeType.User, null, null);
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
			System.out.println(node.getAttributesString());
		}

		INode newNode = new Node(ENodeType.User);

		TreeBuilder tr = new TreeBuilder(null,
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
        newNode = (INode) method.invoke(tr, nodesToUpdate, openNodes, Counter.getInstance());
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
    }
		
		CobwebMergeTest tester = new CobwebMergeTest();
		
		double rating = tester.getValue(newNode);
		double support = tester.getSupport(newNode);
		
		
	//TODO: Check rating & support	
		//The following line alerts that getter have not yet been implemented. To be deleted when done.
		System.out.println("****** NO RESULTS AVAILABLE ******");
	}
	
	/*
	 * Node 1: rating = 1, p = 0.0001
	 * Node 2: rating = 1, p = 0.98
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
		attMap.put(1, 0.0001);
		IAttribute attribute_1 = new CobwebAttribute(attMap);
		
		attMap = new HashMap<Integer, Double>();
		attMap.put(1, 0.98);
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
		INode node1 = new Node(ENodeType.User, null, null);
		node1.setAttributes(attMap1);
		INode node2 = new Node(ENodeType.User, null, null);
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
			System.out.println(node.getAttributesString());
		}

		INode newNode = new Node(ENodeType.User);

		TreeBuilder tr = new TreeBuilder(null,
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
        newNode = (INode) method.invoke(tr, nodesToUpdate, openNodes, Counter.getInstance());
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
    }
		
		CobwebMergeTest tester = new CobwebMergeTest();
		
		double rating = tester.getValue(newNode);
		double support = tester.getSupport(newNode);
		
		
	//TODO: Check rating & support	
		//The following line alerts that getter have not yet been implemented. To be deleted when done.
		System.out.println("****** NO RESULTS AVAILABLE ******");
	}

	/*
	 * Node 1: rating = 1, p = 0.5
	 * Node 2: rating = 1, p = 0.5
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
		attMap.put(1, 0.5);
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
		INode node1 = new Node(ENodeType.User, null, null);
		node1.setAttributes(attMap1);
		INode node2 = new Node(ENodeType.User, null, null);
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
			System.out.println(node.getAttributesString());
		}

		INode newNode = new Node(ENodeType.User);

		TreeBuilder tr = new TreeBuilder(null,
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
        newNode = (INode) method.invoke(tr, nodesToUpdate, openNodes, Counter.getInstance());
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
    }
		
		CobwebMergeTest tester = new CobwebMergeTest();
		
		double rating = tester.getValue(newNode);
		double support = tester.getSupport(newNode);
		
		
	//TODO: Check rating & support	
		//The following line alerts that getter have not yet been implemented. To be deleted when done.
		System.out.println("****** NO RESULTS AVAILABLE ******");
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
		INode sharedAttribute = new Node(ENodeType.Content, null, null);
		
		// add the corresponding attributes to the attribute maps
		attMap1.put(sharedAttribute, attribute_1);
		attMap2.put(sharedAttribute, attribute_2);
		
		// create nodes
		INode node1 = new Node(ENodeType.User, null, null);
		node1.setAttributes(attMap1);
		INode node2 = new Node(ENodeType.User, null, null);
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
			System.out.println(node.getAttributesString());
		}

		INode newNode = new Node(ENodeType.User);

		TreeBuilder tr = new TreeBuilder(null,
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
        newNode = (INode) method.invoke(tr, nodesToUpdate, openNodes, Counter.getInstance());
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
    }
		
		CobwebMergeTest tester = new CobwebMergeTest();
		
		double rating = tester.getValue(newNode);
		double support = tester.getSupport(newNode);
		
		
	//TODO: Check rating & support	
		//The following line alerts that getter have not yet been implemented. To be deleted when done.
		System.out.println("****** NO RESULTS AVAILABLE ******");
	}
 
	/*
	 * Node 1: rating = 1, p = 1
	 * Node 2: rating = 2, p = 1
	 * Node 3: rating = 5, p = 1
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
		attMap.put(5, 1.0);
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

		INode newNode12 = new Node(ENodeType.User);

		TreeBuilder tr = new TreeBuilder(null,
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
		
		INode newNode123 = new Node(ENodeType.User);
		
		nodesToUpdate = utilityCalc.getMaxCategoryUtilityMerge(
				openNodes).getNodes();

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
		double support = tester.getSupport(newNode123);
		
	//TODO: Check rating & support of newNode123
		//The following line alerts that getter have not yet been implemented. To be deleted when done.
		System.out.println("****** NO RESULTS AVAILABLE ******");
	}
 
	/*
	 * Node 1: rating = 1, p = 0.8
	 * Node 2: rating = 2, p = 0.2
	 * Node 3: rating = 5, p = 0.1
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
		attMap.put(1, 0.8);
		IAttribute attribute_1 = new CobwebAttribute(attMap);
		
		attMap = new HashMap<Integer, Double>();
		attMap.put(2, 0.2);
		IAttribute attribute_2 = new CobwebAttribute(attMap);
		
		attMap = new HashMap<Integer, Double>();
		attMap.put(5, 0.1);
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

		INode newNode12 = new Node(ENodeType.User);

		TreeBuilder tr = new TreeBuilder(null,
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
		
		INode newNode123 = new Node(ENodeType.User);

		nodesToUpdate = utilityCalc.getMaxCategoryUtilityMerge(
				openNodes).getNodes();
		
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
		double support = tester.getSupport(newNode123);
		
	//TODO: Check rating & support of newNode123
		//The following line alerts that getter have not yet been implemented. To be deleted when done.
		System.out.println("****** NO RESULTS AVAILABLE ******");
	}
 
	/*
	 * Node 1: rating = 3, p = 0.8
	 * Node 2: rating = 3, p = 0.2
	 * Node 3: rating = 3, p = 0.1
	 * 
	 * This test merges node 1 and 2. Then merges resulting node with node 3.
	 */
	@Test
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

		INode newNode12 = new Node(ENodeType.User);

		nodesToUpdate = utilityCalc.getMaxCategoryUtilityMerge(
				openNodes).getNodes();
		
		TreeBuilder tr = new TreeBuilder(null,
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
		
		INode newNode123 = new Node(ENodeType.User);

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
		double support = tester.getSupport(newNode123);
		
	//TODO: Check rating & support of newNode123
		//The following line alerts that getter have not yet been implemented. To be deleted when done.
		System.out.println("****** NO RESULTS AVAILABLE ******");
	}
}
