package ch.uzh.agglorecommender.client;



import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import ch.uzh.agglorecommender.clusterer.treecomponent.CobwebTreeComponentFactory;

public class TestClassFullTree {


  /*
	 * Tests Cobweb implementation using a test set containing 10 nodes (testData.cobweb)
	 */
	@Test
	public void runTest(){
		
		TestDriver.cla.contentTreeComponentFactory = CobwebTreeComponentFactory.getInstance();
		TestDriver.cla.userTreeComponentFactory = CobwebTreeComponentFactory.getInstance();
		TestDriver.cla.trainingFile = new File("C:\\Users\\IBM_ADMIN\\Documents\\Eclipse\\Workstation\\tree-builder-2\\tree-builder-2\\TreeBuilder\\test\\ch\\uzh\\agglorecommender\\client\\u1.base");
		
		
		TestDriver.main(new String[] {});
//		TestDriver.main(new String[] {"-tr", "C:/Users/IBM_ADMIN/Documents/Eclipse/Workstation/tree-builder-2/tree-builder-2/TreeBuilder/test/ch/uzh/agglorecommender/clusterer/treesearch/u1.base", "-c", "Cobweb", "-u","Cobweb"});
	
		ClusterResult trainingOutput;
		
		try {
	        Class[] parameterTypes = {};
	        Method method = TestDriver.class.getDeclaredMethod("training", parameterTypes);
	        method.setAccessible(true);
	        trainingOutput = (ClusterResult) method.invoke(null);
	    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
	        e.printStackTrace();
	    }
	}
}
