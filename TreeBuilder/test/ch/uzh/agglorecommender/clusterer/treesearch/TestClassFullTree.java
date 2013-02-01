package ch.uzh.agglorecommender.clusterer.treesearch;



import org.junit.Test;

import ch.uzh.agglorecommender.client.TestDriver;

public class TestClassFullTree {


  /*
	 * Tests Cobweb implementation using a test set containing 10 nodes (testData.cobweb)
	 */
	@Test
	public void runTest(){
		TestDriver.main(new String[] {"tr", "/Datasets/grouplens/testData.cobweb", "-c", "Cobweb", "-u"," Cobweb"});
	}
}
