package client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RandomDataset implements Dataset<Double> {

	private static final int NUM_OF_USERS = 15;
	private static final int NUM_OF_MOVIES = 10;

	private Double[][] randomMatrix = new Double[NUM_OF_MOVIES][NUM_OF_USERS];
	
	private List<DatasetItem<Double>> datasetItems = new ArrayList<DatasetItem<Double>>();

	public RandomDataset() {
		initRandomMatrix();
		initDatasetItems();
	}

	@Override
	public Iterator<DatasetItem<Double>> iterateOverDatasetItems() {
		return datasetItems.listIterator();
	}
	
	@Override
	public Normalizer<Double> getNormalizer() {
		// No normalizer needed
		return null;
	}
	
	private void initRandomMatrix() {
		Random randomGenerator = new Random();
		for (int i = 0; i < randomMatrix.length; i++) {
			for (int j = 0; j < randomMatrix[i].length; j++) {
				// high chance for null values
				if (randomGenerator.nextInt() % 4 == 0) {
					// we want to have rating values in the interval [0.0, 1.0] 
					// rounded to one decimal digit.
					randomMatrix[i][j] = ((double)randomGenerator.nextInt(11) / 10.0);

				} else {
					randomMatrix[i][j] = null;
				}
			}
		}
	}
	
	private void initDatasetItems() {
		for (int i = 0; i < randomMatrix[0].length; i++) {
			for (int j = 0; j < randomMatrix.length; j++) {
				if (randomMatrix[j][i] != null) {
					datasetItems.add(new SimpleDatasetItem<Double>(randomMatrix[j][i], i, j));	
				}
			}
		}
	}


	public void printRandomMatrix() {
		for (int i = 0; i < randomMatrix.length; i++) {
			for (int j = 0; j < randomMatrix[i].length; j++) {
				System.out.print(randomMatrix[i][j] +  "\t| ");
			}
			System.out.println();
		}
	}
	
	
	// for testing
	private static void main(String[] args) {
		RandomDataset ds = new RandomDataset();
		ds.printRandomMatrix();
		System.out.println("hello");
	}

}
