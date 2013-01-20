package ch.uzh.agglorecommender.clusterer.treesearch;

import java.lang.reflect.Field;
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

	@Test
	public void test() {
		
		System.out.println(" ");
		System.out
				.println("----------------------Starting Cobweb merge test 1..----------------------");
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
		double rating;
		double support;
		
		//Get the attribute values
		try{
			Class nodeClass = newNode.getClass();
			Field attributes = nodeClass.getDeclaredField("attributes");
			attributes.setAccessible(true);
			
			//Method getP = CobwebAttribute.class.getDeclaredMethod("getProbabilities");
			//getP.setAccessible(true);
			
			//TODO: Fix the following statement
			CobwebAttribute newAttributes = (CobwebAttribute)attributes.get(nodeClass);
			
			rating = newAttributes.getSumOfRatings();
			support = newAttributes.getSupport();
			
		}
		catch(NoSuchFieldException | //NoSuchMethodException | 
				IllegalAccessException e){
			e.printStackTrace();
		}
	//TODO: Check rating & support	
	}

}
