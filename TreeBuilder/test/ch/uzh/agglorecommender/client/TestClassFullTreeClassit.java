package ch.uzh.agglorecommender.client;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import ch.uzh.agglorecommender.clusterer.treecomponent.ClassitTreeComponentFactory;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treeupdate.NullUpdater;

import com.google.common.collect.ImmutableMap;

public class TestClassFullTreeClassit {
	static String fileLocation = "test/ch/uzh/agglorecommender/client/testNodes.base";

	  /*
		 * Tests Classit implementation using a test set containing 10 nodes 
		 */
		@Test
		public void runTest(){
			
			System.out.println("------------Starting Full Classit Test (10 nodes)------------");
			
			//Create tree
			TestDriver.cla.contentTreeComponentFactory = ClassitTreeComponentFactory.getInstance();
			TestDriver.cla.userTreeComponentFactory = ClassitTreeComponentFactory.getInstance();
			TestDriver.cla.trainingFile = new File(fileLocation);
			TestDriver.cla.nodeUpdater = new NullUpdater();
			
			//TestDriver.main(new String[] {});
//			TestDriver.main(new String[] {"-tr", "C:/Users/IBM_ADMIN/Documents/Eclipse/Workstation/tree-builder-2/tree-builder-2/TreeBuilder/test/ch/uzh/agglorecommender/clusterer/treesearch/u1.base", "-c", "Cobweb", "-u","Cobweb"});
		
			ClusterResult trainingOutput = new ClusterResult(null, null, null, null, null);
			
			try {
		        Class[] parameterTypes = {};
		        Method method = TestDriver.class.getDeclaredMethod("training", parameterTypes);
		        method.setAccessible(true);
		        trainingOutput = (ClusterResult) method.invoke(null);
		    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
		        e.printStackTrace();
		    }
			
					
			System.out.println();
			System.out.println("------------Retrieving values...------------");
			
			INode rootNode = trainingOutput.getUserTreeRoot();
	
			//Get values from tree
			INode tempNode;
			
			rootNode.getChildrenCount();
			
			//Checkk the leafes
			ImmutableMap<Integer, INode> leafNodes = trainingOutput.getUserTreeLeavesMap();
			int numberOfLeafes = leafNodes.size();
			assertEquals("Number of leafes", 10, numberOfLeafes, 0.1);
			
			//Check root node
			//INode rootNode = trainingOutput.getUserTreeRoot();
			//assertEquals("Root node utility",0.02125,rootNode.getCategoryUtility(),0.0000000001);
			
			try {
				Thread.sleep(50000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

}
