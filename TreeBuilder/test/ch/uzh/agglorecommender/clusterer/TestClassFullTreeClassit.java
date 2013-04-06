package ch.uzh.agglorecommender.clusterer;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Test;

import ch.uzh.agglorecommender.client.ClusterResult;
import ch.uzh.agglorecommender.client.TestDataset;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.TreeComponentFactory;
import ch.uzh.agglorecommender.clusterer.treeupdate.SaveAttributeNodeUpdater;

import com.google.common.collect.ImmutableMap;

public class TestClassFullTreeClassit {

  /*
	 * Tests Classit implementation using a test set containing 10 nodes 
	 */
	@Test
	public void runTest1(){
		
		System.out.println("------------Starting Full Classit Test I (10 nodes)------------");
		
		TreeBuilder treeBuilder = new TreeBuilder();
		InitialNodesCreator inc = new InitialNodesCreator(new TestDataset("testNodes.base"), TreeComponentFactory.getInstance());
		treeBuilder.configTreeBuilderForNewRun(
				new SaveAttributeNodeUpdater(),
				inc,
				null);
		treeBuilder.run();
		ClusterResult trainingOutput = treeBuilder.getResult();
						
		//Checkk the leafes
		ImmutableMap<String, INode> leafNodes = trainingOutput.getUserTreeLeavesMap();
		int numberOfLeafes = leafNodes.size();
		assertEquals("Number of leafes", 10, numberOfLeafes, 0.1);
		
		//Check root node
		INode rootNode = trainingOutput.getUserTreeRoot();
		assertEquals("Root node utility",0.070710678,rootNode.getCategoryUtility(),0.0000001);
		
		System.out.println("Test done");
	}
	
	@Test
	public void runTest2(){
		
		System.out.println("------------Starting Full Classit Test II (10 nodes)------------");
		
		TreeBuilder treeBuilder = new TreeBuilder();
		InitialNodesCreator inc = new InitialNodesCreator(new TestDataset("testNodes2.base"), TreeComponentFactory.getInstance());
		treeBuilder.configTreeBuilderForNewRun(
				new SaveAttributeNodeUpdater(),
				inc,
				null);
		treeBuilder.run();
		ClusterResult trainingOutput = treeBuilder.getResult();
						
		//Checkk the leafes
		ImmutableMap<String, INode> leafNodes = trainingOutput.getUserTreeLeavesMap();
		int numberOfLeafes = leafNodes.size();
		assertEquals("Number of leafes", 10, numberOfLeafes, 0.1);
		
		//Check root node
		INode rootNode = trainingOutput.getUserTreeRoot();
		assertEquals("Root node utility",0.000000,rootNode.getCategoryUtility(),0.0000001);
		
		//Check that one of children is Node with ID 5 (no common attributes with other nodes)
		Iterator rootChildrenIterator = rootNode.getChildren();
		String id = "";
		while(rootChildrenIterator.hasNext()){
			INode child = (INode) rootChildrenIterator.next();
			if(child.getNumberOfNodesInSubtree()==0){
				id = child.getDatasetId();
			}
		}
		assertEquals("Id of node 5","5",id);
		System.out.println("Test done");

	}
}
