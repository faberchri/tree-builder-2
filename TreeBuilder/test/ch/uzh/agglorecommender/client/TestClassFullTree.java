package ch.uzh.agglorecommender.client;



import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map.Entry;

import org.junit.Test;

import ch.uzh.agglorecommender.clusterer.treecomponent.CobwebTreeComponentFactory;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treeupdate.NullUpdater;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.UnmodifiableIterator;

public class TestClassFullTree {
	static String fileLocation = "test/ch/uzh/agglorecommender/client/testNodes.base";

  /*
	 * Tests Cobweb implementation using a test set containing 10 nodes 
	 */
	@Test
	public void runTest(){
		
		System.out.println("------------Starting Full Cobweb Test (10 nodes)------------");
		
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
		
		System.out.println();
		System.out.println("------------Retrieving values...------------");
		
		INode rootNode = trainingOutput.getUserTreeRoot();
		ImmutableMap<Integer, INode> leafNodes = trainingOutput.getUserTreeLeavesMap();
		int numberOfLeafes = leafNodes.size();
		
		System.out.println("Number of leafes: "+numberOfLeafes);
		
		//Get values from tree
		INode tempNode;
		
		rootNode.getChildrenCount();
		
		
		//Temporary solution
		UnmodifiableIterator<Entry<Integer, INode>> entries = leafNodes.entrySet().iterator();
		while (entries.hasNext()) {
		  Entry thisEntry = (Entry) entries.next();
		  Object key = thisEntry.getKey();
		  System.out.println("Entry "+key+":");
		  Object value = thisEntry.getValue();
		  System.out.println("Is node: "+((INode) value).getAttributesString());
		}
		
		//Temporary solution: Print 1st level merges
		//System.out.println("Node: "+leafNodes.get(1).getAttributesString());
		//for(int i=0; i < numberOfLeafes;i++){
			//System.out.println("Node: "+leafNodes.get("1"));
		//}
		
	}
}
