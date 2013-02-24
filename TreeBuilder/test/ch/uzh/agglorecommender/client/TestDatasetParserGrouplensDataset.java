package ch.uzh.agglorecommender.client;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Test;

import ch.uzh.agglorecommender.client.IDataset.DataSetSplit;

/* Tests whether all data is loaded from Grouplens dataset*/
public class TestDatasetParserGrouplensDataset {
	
	@Test
	public void runTest(){

	//Test dataset	
	int countTestDataset = 0;
	GrouplensDataset testDataset = new GrouplensDataset(null,DataSetSplit.TEST);
	Iterator<IDatasetItem<Integer>> testIterator = testDataset.iterateOverDatasetItems();
	 while (testIterator.hasNext()){
		 countTestDataset++;
		 testIterator.next();
	 }
	 
	 //Training dataset
		int countTrainingDataset = 0;
		GrouplensDataset trainingDataset = new GrouplensDataset(null,DataSetSplit.TRAINING);
		Iterator<IDatasetItem<Integer>> trainingIterator = trainingDataset.iterateOverDatasetItems();
		 while (trainingIterator.hasNext()){
			 countTrainingDataset++;
			 trainingIterator.next();
		 }
	
	 assertEquals("Size of Grouplens test dataset",20000,countTestDataset,0.1);
	 assertEquals("Size of Grouplens training dataset",80000,countTrainingDataset,0.1);
	 
}}
