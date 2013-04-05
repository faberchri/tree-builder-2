package ch.uzh.agglorecommender.client;



import static org.junit.Assert.assertEquals;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treeupdate.NullUpdater;

import com.google.common.collect.ImmutableMap;

public class TestClassFullTreeCobweb {
	static String fileLocation = "test/ch/uzh/agglorecommender/client/testNodes.base";

  /*
	 * Tests Cobweb implementation using a test set containing 10 nodes 
	 */
	@Test
	public void runTest(){
		
		System.out.println("------------Starting Full Cobweb Test (10 nodes)------------");
		Field cla = Field.class.getDeclaredField("cla");
	    cla.setAccessible(true);
		//Create tree
		TestDriver.cla.contentTreeComponentFactory = CobwebTreeComponentFactory.getInstance();
		TestDriver.cla.userTreeComponentFactory = CobwebTreeComponentFactory.getInstance();
		TestDriver.cla.trainingFile = new File(fileLocation);
		TestDriver.cla.nodeUpdater = new NullUpdater();
		
		//TestDriver.main(new String[] {});
//		TestDriver.main(new String[] {"-tr", "C:/Users/IBM_ADMIN/Documents/Eclipse/Workstation/tree-builder-2/tree-builder-2/TreeBuilder/test/ch/uzh/agglorecommender/clusterer/treesearch/u1.base", "-c", "Cobweb", "-u","Cobweb"});
	
		ClusterResult trainingOutput = new ClusterResult(null, null, null, null, null);
		
		try {
	        Class[] parameterTypes = {};
	        Method method = TestDriver.class.getDeclaredMethod("training", parameterTypes);
	        method.setAccessible(true);
	        trainingOutput = (ClusterResult) method.invoke(null);
	    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
	        e.printStackTrace();
	    }
		
		//Sleep test to allow to see visualization

		System.out.println();
		System.out.println("------------Retrieving values...------------");
		
		//Checkk the leafes
		ImmutableMap<String, INode> leafNodes = trainingOutput.getUserTreeLeavesMap();
		int numberOfLeafes = leafNodes.size();
		assertEquals("Number of leafes", 10, numberOfLeafes, 0.1);
		
		//Check root node
		INode rootNode = trainingOutput.getUserTreeRoot();
		assertEquals("Root node utility",0.02125,rootNode.getCategoryUtility(),0.0000000001);
		
		//Get values from tree
		//INode tempNode1;
		//INode tempNode2;
		
		System.out.println("\n Test done.. sleeping");
		try {
			Thread.sleep(500000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
