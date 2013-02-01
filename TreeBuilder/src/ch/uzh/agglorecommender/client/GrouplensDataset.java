package ch.uzh.agglorecommender.client;

import java.io.File;
import java.util.logging.Logger;

import ch.uzh.agglorecommender.util.TBLogger;


/**
 * 
 * Extracts {@code DatasetItems} from a Grouplens input file by parsing the 
 * file and splitting obtained strings into tokens.
 * <br>
 * For convenience reasons the data set loads a default Grouplens
 * input file if no input file is specified on instantiation.
 * The default input file is a stored together with the project.
 * <br>
 * The Grouplens data set can be obtained from
 * <a href="http://www.grouplens.org/node/12">http://www.grouplens.org/node/12</a>.
 *
 */
public class GrouplensDataset extends AbstractDataset<Integer> {
		
	/**
	 * Path to default training input file.
	 */
	private static final String pathToDefaultTrainingInputFile = "Grouplens/u1.base";
	
	/**
	 * Path to default test input file.
	 */
	private static final String pathToDefaultTestInputFile = "Grouplens/u1.test";
	
	/**
	 * Instantiates a new data set and parses the data from the specified or 
	 * the default input source.
	 * 
	 * @param datasetFile the File to load. If {@code null} the default input source is loaded.
	 */
	public GrouplensDataset(File datasetFile, DataSetSplit split) {
		//data set has rating min value = 1 and max value = 5
		super(new IntegerNormalizer(1, 5), datasetFile, split);
	}
	
	/**
	 * Splits a single line of the input stream into tokens.
	 * The tokens represent user id, content id and ratings.
	 * These values are used to create a new {@code IDatasetItem} object,
	 * which are stored in {@code datasetItems}.
	 * 
	 * @param entry A string containing a single user-content-rating information.
	 */
	@Override
	void parseLine(String entry) {
		String[] fields = entry.split("\t");
		Integer[] intFields = new Integer[fields.length];
		for (int i = 0; i < fields.length; i++) {
			if (i != fields.length - 1 ) {
				intFields[i] = Integer.parseInt(fields[i]);		
			}
		}
		addToDatasetItems(new SimpleDatasetItem<Integer>(intFields[2], intFields[0], intFields[1]));
	}
	
	@Override
	protected String getPathToDefaultInputFile(DataSetSplit split) {
		switch (split) {
		case TEST:
			return pathToDefaultTestInputFile;
		case TRAINING:
			return pathToDefaultTrainingInputFile;
		default:
			Logger log = TBLogger.getLogger(getClass().getName());
			log.severe("Unknown data set split requested.");
			System.exit(-1);
			return null;
		}
	}

}
