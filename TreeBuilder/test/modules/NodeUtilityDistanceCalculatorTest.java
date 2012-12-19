package modules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import clusterer.Counter;
import clusterer.ENodeType;
import clusterer.IAttribute;
import clusterer.INode;
import clusterer.INodeDistanceCalculator;

public class NodeUtilityDistanceCalculatorTest {

	@Test
	public void testCalculateDistanceINodeINodeCounterSetOfINode() {
		
		// create attributes
		// interval of ratings: [1, 5]
		// ratings are integers		
		IAttribute attribute_1A = new SimpleAttribute(4.0, 0.0, 1, new ArrayList<Double>());
		IAttribute attribute_1B = new SimpleAttribute(3.0, 0.0, 1, new ArrayList<Double>());	
		IAttribute attribute_2B = new SimpleAttribute(5.0, 0.0, 1, new ArrayList<Double>());
		IAttribute attribute_2C = new SimpleAttribute(5.0, 0.0, 1, new ArrayList<Double>());
		
		// we want to calc the category utility of node 1 merged with node 2
		// attribute map of node 1
		Map<INode, IAttribute> attMap1 = new HashMap<INode, IAttribute>();
		
		// this node is an attribute of node 1 and node 2
		INode sharedAttribute = new UtilityNode(ENodeType.Content, null, null, null, null);
		
		// add the corresponding attributes to the attribute map of node 1
		attMap1.put(new UtilityNode(ENodeType.Content, null, null, null, null), attribute_1A);
		attMap1.put(sharedAttribute, attribute_1B);
		
		// create node 1
		INode node1 = new UtilityNode(ENodeType.User, null, null, null, null);
		node1.setAttributes(attMap1);
		
		// attribute map of node 2
		Map<INode, IAttribute> attMap2 = new HashMap<INode, IAttribute>();
		
		// add the corresponding attributes to the attribute map of node 2
		attMap2.put(sharedAttribute, attribute_2B);
		attMap2.put(new UtilityNode(ENodeType.Content, null, null, null, null), attribute_2C);
		
		// create node 2
		INode node2 = new UtilityNode(ENodeType.User, null, null, attMap2, null);
		node2.setAttributes(attMap2);
		
		// create the utility calcultaor
		INodeDistanceCalculator utilityCalc = new NodeUtilityDistanceCalculator();
		
		// get the utility of the node resulting of a merge of node 1 and node 2
		double calcCatUt = utilityCalc.calculateDistance(node1, node2, new Counter(1, 1), null);
		
		// evaluate the category utility result
		assertEquals("category utility", 1.0/3.0, calcCatUt, 0.000001);

	}

	@Test
	public void testCalculateDistanceINodeINode() {
		fail("Not yet implemented");
	}

}
