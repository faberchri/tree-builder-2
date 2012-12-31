package modules;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import clusterer.ENodeType;
import clusterer.IAttribute;
import clusterer.IMaxCategoryUtilitySearcher;
import clusterer.INode;

public class ClassitMaxCategoryUtilitySearcherTest {
	
	public static void main(String[] args) {
		
		ClassitMaxCategoryUtilitySearcherTest tester = new ClassitMaxCategoryUtilitySearcherTest();
		
		for(int i = 0; i<args.length;i++){
			String arg = args[i];
			switch(arg){
				case "1": tester.testGetMaxCategoryUtilityMergeSetOfINode();
					break;
				default: System.out.println("Invalid argument: "+arg);
					break;
			}
		}
		
	}

		public void testGetMaxCategoryUtilityMergeSetOfINode() {
			
			// Ratings are Integers
			
			// Based on example in Google Docs
			// https://docs.google.com/spreadsheet/ccc?key=0AnvRo1G6q1ffdEJLWjJiX2QtX2hza1l4WG5Sclp4WEE#gid=0
			
			//Creating the attributes

			IAttribute A1 = ClassitAttributeFactory.getInstance().createAttribute(4);
			IAttribute A2 = ClassitAttributeFactory.getInstance().createAttribute(3);
			IAttribute A3 = ClassitAttributeFactory.getInstance().createAttribute(5);
			IAttribute A4 = ClassitAttributeFactory.getInstance().createAttribute(5);
			
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
			attMap2.put(sharedAttribute, A4);
			attMap2.put(new Node(ENodeType.Content, null, null), A3);
			
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
			double calcCatUt = utilityCalc.getMaxCategoryUtilityMerge(openNodes).getCategoryUtility();
			
			//Print standard deviations
			System.out.println("Node 1: "+node1.getAttributesString());
			System.out.println("Node 2: "+node2.getAttributesString());
			
			/*
			 * ArrayList<INode> nodesToUpdate = new ArrayList<INode>();
			 * nodesToUpdate.add(node1);
			 * nodesToUpdate.add(node2);
			 */

			// evaluate the category utility result
			assertEquals("category utility", 1.0/3.0, calcCatUt, 0.000001);


		}

}
