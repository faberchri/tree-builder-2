package ch.uzh.agglorecommender.client;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Test;

import ch.uzh.agglorecommender.client.IDataset.DataSetSplit;

/* Tests whether all data is loaded from Grouplens dataset*/
public class TestDatasetParserGrouplensDataset {
	
	/*Test for the small dataset*/
	@Test
	public void grouplensSmall(){

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
	
	 assertEquals("Size of small Grouplens test dataset",20000,countTestDataset,0.1);
	 assertEquals("Size of small Grouplens training dataset",80000,countTrainingDataset,0.1);
	 
}
	/*Test for the big dataset*/
	@Test
public void grouplensBig(){
		
		//Test dataset
	FloareaDataset testDataset = new FloareaDataset(null,DataSetSplit.TEST);
	int countTestDataset = 0;
	Iterator<IDatasetItem<Double>> testIterator = testDataset.iterateOverDatasetItems();
	 while (testIterator.hasNext()){
		 countTestDataset++;
		 testIterator.next();
	 }
	 
	 //Training dataset
		int countTrainingDataset = 0;
		FloareaDataset trainingDataset = new FloareaDataset(null,DataSetSplit.TRAINING);
		Iterator<IDatasetItem<Double>> trainingIterator = trainingDataset.iterateOverDatasetItems();
		 while (trainingIterator.hasNext()){
			 countTrainingDataset++;
			 trainingIterator.next();
		 }
	 //TODO: What is the correct size?
		 assertEquals("Size of big Grouplens test dataset",20000,countTestDataset,0.1);
		 assertEquals("Size of big Grouplens training dataset",80000,countTrainingDataset,0.1);
}
	
}
