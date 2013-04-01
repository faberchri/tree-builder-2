package ch.uzh.agglorecommender.clusterer.treeupdate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import ch.uzh.agglorecommender.clusterer.TreeBuilder;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.Node;
import ch.uzh.agglorecommender.clusterer.treecomponent.TreeComponentFactory;
import ch.uzh.agglorecommender.clusterer.treesearch.ClusterSet;
import ch.uzh.agglorecommender.clusterer.treesearch.IClusterSet;
import ch.uzh.agglorecommender.clusterer.treesearch.IMergeResult;
import ch.uzh.agglorecommender.clusterer.treesearch.IndexAwareSet;
import ch.uzh.agglorecommender.clusterer.treesearch.MergeResult;

import com.google.common.collect.ImmutableSet;

public class UpdaterTester {
//public void updateNodes(INode newNode, Collection<INode> nodesToUpdate)
	
	
	/** Generates nodes used in the tests
	 * @return Set of three nodes where first (user-) node is used to update the second and third (content-) nodes
	 */
	public IndexAwareSet<INode> createTestNodes(){
		//Create two user nodes to be merged
		IAttribute N1A1 = TreeComponentFactory.getInstance()
				.createNumericalLeafAttribute(3.0);
		IAttribute N1A2 = TreeComponentFactory.getInstance()
				.createNumericalLeafAttribute(5.0);
		IAttribute N1A3 = TreeComponentFactory.getInstance()
				.createNumericalLeafAttribute(7.0);
		IAttribute N2A1 = TreeComponentFactory.getInstance()
				.createNumericalLeafAttribute(2.0);
		IAttribute N2A2 = TreeComponentFactory.getInstance()
				.createNumericalLeafAttribute(6.0);
		IAttribute N2A4 = TreeComponentFactory.getInstance()
				.createNumericalLeafAttribute(1.0);
		
		IAttribute attNodeToUpdate1 = TreeComponentFactory.getInstance()
				.createNumericalLeafAttribute(7.0);
		
		INode nodeToUpdate1 = new Node(ENodeType.Content, null, null);
		INode nodeToUpdate2 = new Node(ENodeType.Content, null, null);
			
		Map<INode, IAttribute> attMap1 = new HashMap<INode, IAttribute>();
		attMap1.put(nodeToUpdate1, N1A1);
		attMap1.put(nodeToUpdate2, N1A2);
		attMap1.put(new Node(ENodeType.Content, null, null), N1A3);
		
		Map<INode, IAttribute> attMap2 = new HashMap<INode, IAttribute>();
		attMap2.put(nodeToUpdate1, N2A1);
		attMap2.put(nodeToUpdate2, N2A2);
		attMap2.put(new Node(ENodeType.Content, null, null), N2A4);
		
		INode node1 = new Node(ENodeType.User,null,null);
		node1.setRatingAttributes(attMap1);
		INode node2 = new Node(ENodeType.User,null,null);
		node2.setRatingAttributes(attMap2);
		
		//Add attributes to Content nodes which will be updated
		Map<INode, IAttribute> attMapNodeToUpdate1 = new HashMap<INode, IAttribute>();
		attMapNodeToUpdate1.put(node1, N1A1);
		attMapNodeToUpdate1.put(node2, N2A1);
		attMapNodeToUpdate1.put(new Node(ENodeType.User,null,null), attNodeToUpdate1);
		Map<INode, IAttribute> attMapNodeToUpdate2 = new HashMap<INode, IAttribute>();
		attMapNodeToUpdate2.put(node1, N1A2);
		attMapNodeToUpdate2.put(node2, N2A2);
		
		//Merge user nodes to crate parent node
		TreeBuilder tr = new TreeBuilder();
		
		INode[] nodesOfThisMerge = new INode[2];
		nodesOfThisMerge[0] = node1;
		nodesOfThisMerge[1] = node2;
		IMergeResult res = new MergeResult(1.0, nodesOfThisMerge);
		
		Set<INode> nodeSet = new HashSet<INode>();
		nodeSet.add(node1);
		nodeSet.add(node2);
		
		IClusterSet<INode> openSet = new ClusterSet<INode>(ImmutableSet.copyOf(nodeSet));
		INode mergedNode = new Node(ENodeType.User,null,null);
		
		try {
            Class[] parameterTypes = {IMergeResult.class, IClusterSet.class};
            //private INode merge(IMergeResult mergeResult, IClusterSet<INode> openSet)
            Method method = TreeBuilder.class.getDeclaredMethod("merge", parameterTypes);
            method.setAccessible(true);
          
            mergedNode = (INode) method.invoke(tr,res,openSet);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
		
		IndexAwareSet<INode> nodes = new IndexAwareSet<INode>();
		nodes.add(mergedNode);
		nodes.add(nodeToUpdate1);
		nodes.add(nodeToUpdate2);
		
		return nodes;
	}
	
	public void testNullUpdater(){
		INodeUpdater updater = new NullUpdater();
		
		UpdaterTester ts = new UpdaterTester();
		IndexAwareSet<INode> set = ts.createTestNodes();
		
		INode userNode = set.getByIndex(0);
		Collection<INode> nodesToUpdate = new ArrayList<INode>();
		nodesToUpdate.add(set.getByIndex(1));
		nodesToUpdate.add(set.getByIndex(2));
		
		//Update the nodes
		updater.updateNodes(userNode, nodesToUpdate);
		
		Iterator<INode> i = nodesToUpdate.iterator();
		while(i.hasNext()){
			System.out.println(i.next().getNumericalAttributesString());
			i.next();
		}
		
	}
	
	/*
	 * Tests the SimpleNodeUpdater
	 */
	@Test
	public void testSimpleNodeUpdater(){
		INodeUpdater updater = new SimpleNodeUpdater();
		
		UpdaterTester ts = new UpdaterTester();
		IndexAwareSet<INode> set = ts.createTestNodes();
		
		INode userNode = set.getByIndex(0);
		Collection<INode> nodesToUpdate = new ArrayList<INode>();
		nodesToUpdate.add(set.getByIndex(1));
		nodesToUpdate.add(set.getByIndex(2));
		
		//Update the nodes
		updater.updateNodes(userNode, nodesToUpdate);
		
		Iterator<INode> i = nodesToUpdate.iterator();
		while(i.hasNext()){
			System.out.println(i.next().getNumericalAttributesString());
			i.next();
		}
	}
	
	public void testExtendedNodeUpdater(){
		INodeUpdater updater = new ExtendedNodeUpdater();
		
		UpdaterTester ts = new UpdaterTester();
		IndexAwareSet<INode> set = ts.createTestNodes();
		
		INode userNode = set.getByIndex(0);
		Collection<INode> nodesToUpdate = new ArrayList<INode>();
		nodesToUpdate.add(set.getByIndex(1));
		nodesToUpdate.add(set.getByIndex(2));
		
		//Update the nodes
		updater.updateNodes(userNode, nodesToUpdate);
		
		Iterator<INode> i = nodesToUpdate.iterator();
		while(i.hasNext()){
			System.out.println(i.next().getNumericalAttributesString());
			i.next();
		}
	}
	
	
}
