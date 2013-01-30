package ch.uzh.agglorecommender.client;

import org.junit.Test;

public class TestClassFullTree {


  /*
	 * Tests Cobweb implementation using a test set containing 10 nodes (testData.cobweb)
	 */
	@Test
	public void runTest(){
		TestDriver.main(new String[] {"tr", "/Datasets/grouplens/testData.cobweb", "-c", "Cobweb", "-u"," Cobweb"});
	}
}
