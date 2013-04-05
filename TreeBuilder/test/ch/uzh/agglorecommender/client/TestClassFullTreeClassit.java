package ch.uzh.agglorecommender.client;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;

import com.google.common.collect.ImmutableMap;

public class TestClassFullTreeClassit {
	static String fileLocation = "test/ch/uzh/agglorecommender/client/testNodes.base";

	  /*
		 * Tests Classit implementation using a test set containing 10 nodes 
		 */
		@Test
		public void runTest() throws NoSuchFieldException, SecurityException{
			
			System.out.println("------------Starting Full Classit Test (10 nodes)------------");
			
			Field cla = TestDriver.class.getDeclaredField("cla");
		    cla.setAccessible(true);
			//Create tree
			/*cla.TreeComponentFactory = ClassitTreeComponentFactory.getInstance();
			cla.userTreeComponentFactory = ClassitTreeComponentFactory.getInstance();
			cla.trainingFile = new File(fileLocation);
			cla.
			nodeUpdater = new NullUpdater();*/
			
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
			ImmutableMap<String, INode> leafNodes = trainingOutput.getUserTreeLeavesMap();
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
