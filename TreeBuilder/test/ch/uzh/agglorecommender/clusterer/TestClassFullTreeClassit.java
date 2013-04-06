package ch.uzh.agglorecommender.clusterer;

import static org.junit.Assert.assertEquals;

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
	public void runTest(){
		
		System.out.println("------------Starting Full Classit Test (10 nodes)------------");
		
		TreeBuilder treeBuilder = new TreeBuilder();
		InitialNodesCreator inc = new InitialNodesCreator(new TestDataset(), TreeComponentFactory.getInstance());
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
		assertEquals("Root node utility",0.0565685,rootNode.getCategoryUtility(),0.0000001);
		
		System.out.println("Test done");

	
	}
}
