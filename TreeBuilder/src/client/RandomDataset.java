package client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * 
 * A data set with random ratings.
 * The number of users and content items
 * and the percentage of null values
 * in the data set are passed to the constructor.
 *
 */
public class RandomDataset implements IDataset<Double> {

	/**
	 * The number of users in the data set.
	 */
	private final int numberOfUsers;
	
	/**
	 * The number of movies in the data set.
	 */
	private final int numberOfMovies;
	
	/**
	 * The percentage of null entries in the data set.
	 */
	private final int percentageOfNulls;

	/**
	 * The ratings matrix.
	 */
	private final  Double[][] randomMatrix;
	
	/**
	 * The {@code DatasetItems} generated from the ratings matrix.
	 */
	private List<IDatasetItem<Double>> datasetItems = new ArrayList<IDatasetItem<Double>>();


	/**
	 * Instantiates a new random data set.
	 * 
	 * @param numberOfUsers the number of user in the data set.
	 * @param numberOfMovies the number of movies in the data set.
	 * @param percentageOfNulls the percentage of null values in the data set.
	 * @throws IllegalArgumentException if numberOfUsers < 0 OR numberOfMovies < 0
	 * OR percentageOfNulls < 0 OR percentageOfNulls > 100
	 */
	public RandomDataset(int numberOfUsers, int numberOfMovies,
			int percentageOfNulls) throws IllegalArgumentException
			{
		if (numberOfUsers < 0) {
			throw new IllegalArgumentException("Negative number of users: " + numberOfUsers);
		}
		if (numberOfMovies < 0) {
			throw new IllegalArgumentException("Negative number of movies: " + numberOfMovies);
		}
		if (percentageOfNulls < 0 || percentageOfNulls > 100) {
			throw new IllegalArgumentException("percentage of null values is not in the range [0, 100]: " + percentageOfNulls);
		}
		this.numberOfUsers = numberOfUsers;
		this.numberOfMovies = numberOfMovies;
		this.percentageOfNulls = percentageOfNulls;
		randomMatrix = new Double[numberOfMovies][numberOfUsers];
		
		initRandomMatrix();
		initDatasetItems();
	}



	@Override
	public Iterator<IDatasetItem<Double>> iterateOverDatasetItems() {
		return datasetItems.listIterator();
	}
	
	@Override
	public INormalizer<Double> getNormalizer() {
		
		return new INormalizer<Double>() {
			
			@Override
			public double normalizeRating(Double rating) {
				// data set is in range [0.0, 1.0]
				// we want to have it in the range [0.0, 10.0]
				
				return rating.doubleValue() * 10.0;
			}
		};
		
	}
	
	/**
	 * Initializes the rating matrix {@code randomMatrix}
	 * with random values in the range of [0.0, 1.0] rounded to 
	 * one decimal digit or null.
	 */
	private void initRandomMatrix() {
		Random randomGenerator = new Random();
		for (int i = 0; i < randomMatrix.length; i++) {
			for (int j = 0; j < randomMatrix[i].length; j++) {
				// chance for null values
				if (randomGenerator.nextInt(101) <= percentageOfNulls) {
					randomMatrix[i][j] = null;
				} else {
					// we want to have rating values in the interval [0.0, 1.0] 
					// rounded to one decimal digit.
					randomMatrix[i][j] = ((double)randomGenerator.nextInt(11) / 10.0);
				}
			}
		}
	}
	
	/**
	 * Extract the {@code DatasetItems} from the
	 * {@code randomMatrix} and store them in {@code datasetItems}.
	 */
	private void initDatasetItems() {
		for (int i = 0; i < randomMatrix[0].length; i++) {
			for (int j = 0; j < randomMatrix.length; j++) {
				if (randomMatrix[j][i] != null) {
					datasetItems.add(new SimpleDatasetItem<Double>(randomMatrix[j][i], i, j));	
				}
			}
		}
	}

	/**
	 * Print the {@code randomMatrix}.
	 */
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
		RandomDataset ds = new RandomDataset(10, 10, 60);
		ds.printRandomMatrix();
		System.out.println("hello");
	}

}
