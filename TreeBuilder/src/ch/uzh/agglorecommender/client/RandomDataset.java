package ch.uzh.agglorecommender.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import ch.uzh.agglorecommender.util.TBLogger;

import com.google.common.collect.ImmutableMap;

/**
 * 
 * A data set with random ratings.
 * The number of users and content items
 * and the percentage of null values
 * in the data set are passed to the constructor.
 *
 */
class RandomDataset implements IDataset {
	
	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The default number of users in the data set.
	 */
	private static final int defaultNumOfUsers = 70;
	
	/**
	 * The default number of movies in the data set.
	 */
	private static final int defaultNumOfMovies = 65;
	
	/**
	 * The default percentage of null entries in the data set.
	 */
	private static final int defaultPercentageOfNulls = 60;

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
	private final double percentageOfNulls;

	/**
	 * The ratings matrix.
	 */
	private final  Double[][] randomMatrix;
	
	/**
	 * The {@code DatasetItems} generated from the ratings matrix.
	 */
	private List<IDatasetItem> datasetItems = new ArrayList<IDatasetItem>();


	public RandomDataset() throws IllegalArgumentException {
		this(defaultNumOfUsers, defaultNumOfMovies, defaultPercentageOfNulls);
	}
	
	/**
	 * Instantiates a new random data set.
	 * 
	 * @param numberOfUsers the number of user in the data set.
	 * @param numberOfMovies the number of movies in the data set.
	 * @param percentageOfNulls the percentage of null values in the data set.
	 * @throws IllegalArgumentException if numberOfUsers < 0 OR numberOfMovies < 0
	 * OR percentageOfNulls < 0 OR percentageOfNulls > 100
	 */
	RandomDataset(int numberOfUsers, int numberOfMovies,
			double percentageOfNulls) throws IllegalArgumentException
			{
		if (numberOfUsers < 0) {
			throw new IllegalArgumentException("Negative number of users: " + numberOfUsers);
		}
		if (numberOfMovies < 0) {
			throw new IllegalArgumentException("Negative number of movies: " + numberOfMovies);
		}
		if (percentageOfNulls < 0.0 || percentageOfNulls > 100.0) {
			throw new IllegalArgumentException("percentage of null values is not in the range [0.0, 100.0]: " + percentageOfNulls);
		}
		this.numberOfUsers = numberOfUsers;
		this.numberOfMovies = numberOfMovies;
		this.percentageOfNulls = percentageOfNulls;
		randomMatrix = new Double[numberOfMovies][numberOfUsers];
		
		initRandomMatrix();
		initDatasetItems();
	}

	@Override
	public Iterator<IDatasetItem> iterateOverDatasetItems() {
		return datasetItems.listIterator();
	}
	
	@Override
	public INormalizer getNormalizer() {
		
		return new INormalizer() {
			
			@Override
			public double normalizeRating(double rating) {
				// data set is in range [0.0, 1.0]
				// we want to have it in the range [0.0, 10.0]
				
				return rating * 10.0;
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
					datasetItems.add(new SimpleDatasetItem(randomMatrix[j][i], String.valueOf(i), String.valueOf(j)));	
				}
			}
		}
	}
	
	@Override
	public ImmutableMap<String, Boolean> getAttributeClusteringConfig() {
		// not applicable since no meta data available
		return null;
	}

	@Override
	public double denormalize(double value, String attributeTag) {
		// TODO Auto-generated method stub
		return value;
	}
	
	
	
	// for testing
	private static void main(String[] args) {
		RandomDataset ds = new RandomDataset(10, 10, 60);
		ds.printRandomMatrix();
	}

	/**
	 * Print the {@code randomMatrix}.
	 */
	public void printRandomMatrix() {
		for (int i = 0; i < randomMatrix.length; i++) {
			for (int j = 0; j < randomMatrix[i].length; j++) {
				TBLogger.getLogger(getClass().getName()).info(randomMatrix[i][j] +  "\t| ");
			}
			TBLogger.getLogger(getClass().getName()).info("\n");
		}
	}
}
