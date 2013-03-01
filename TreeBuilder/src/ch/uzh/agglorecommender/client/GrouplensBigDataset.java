package ch.uzh.agglorecommender.client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import ch.uzh.agglorecommender.util.TBLogger;

public class GrouplensBigDataset extends GrouplensDataset {
	
	/**
	 * Path to default training input file.
	 */
	private static final String pathToDefaultTrainingInputFile = "GrouplensBig/u1.base";
	
	/**
	 * Path to default test input file.
	 */
	private static final String pathToDefaultTestInputFile = "GrouplensBig/u1.test";
	
	/**
	 * Path to default meta input files.
	 */
	private static final List<String> pathToDefaultMetaInputFile = new ArrayList<String>();
	static {
		// The ordering of this files in the list need to be preserved! 
		pathToDefaultMetaInputFile.add("GrouplensBig/movies.dat");
		pathToDefaultMetaInputFile.add("GrouplensBig/users.dat");
	}

	/**
	 * Instantiates a new data set and parses the data from the specified or 
	 * the default input source.
	 * 
	 * @param datasetFile the File to load. If {@code null} the default input source is loaded.
	 */
	public GrouplensBigDataset(File datasetFile, DataSetSplit split) {
		super(datasetFile, split);
	}
		
	@Override
	protected String getPathToDefaultRatingsFile(DataSetSplit split) {
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

	@Override
	protected List<String> getPathToMetaFiles() {
		// TODO 
		return null;
	}
}
