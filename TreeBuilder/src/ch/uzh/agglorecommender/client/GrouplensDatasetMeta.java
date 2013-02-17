package ch.uzh.agglorecommender.client;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import ch.uzh.agglorecommender.util.TBLogger;

public class GrouplensDatasetMeta extends AbstractDataset<Integer>{
	
	/**
	 * Path to default content meta data input file.
	 */
	private static final String pathToDefaultContentMetaInputFile = "Grouplens/u.item";
	
	/**
	 * Path to default user meta data input file.
	 */
	private static final String pathToDefaultUserMetaInputFile = "Grouplens/u.user";

	public GrouplensDatasetMeta(File datasetFile,DataSetSplit split) {
		super(new IntegerNormalizer(1, 5),datasetFile, split);
	}

	@Override
	void parseLine(String line) {
		
		String[] fields = line.split("\\|");
		int id = Integer.parseInt(fields[0]);
		List<String> stringFields = new LinkedList<String>();
		for (int i = 0; i < fields.length; i++) {
			if (i != fields.length - 1 ) {
				stringFields.add(fields[i]);
			}
		}
		addToDatasetItems(new MetaDatasetItem(stringFields, id, id));	// FIXME Typ Unterscheidung fehlt
	}

	@Override
	protected String getPathToDefaultInputFile(DataSetSplit split) {
		switch (split) {
			case CONTENTMETA:
				return pathToDefaultContentMetaInputFile;
			case USERMETA:
				return pathToDefaultUserMetaInputFile;
			default:
				Logger log = TBLogger.getLogger(getClass().getName());
				log.severe("Unknown data set split requested.");
				System.exit(-1);
				return null;
		}
	}

}
