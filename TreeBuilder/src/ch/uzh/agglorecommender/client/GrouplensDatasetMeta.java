package ch.uzh.agglorecommender.client;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
		
		// Need indication which dataset is processed -> FIXME there has to be a nicer way!
		DataSetSplit split = null;
		if(fields.length == 24) {
			split = DataSetSplit.CONTENTMETA;
		}
		else if(fields.length == 5) {
			split = DataSetSplit.USERMETA;
		}
		List<String> infoList = getInfoList(split);
		
		Map<String,String> infoMap = new HashMap<String,String>();
		for (int i = 0; i < fields.length; i++) {
			if (i != fields.length - 1 ) {
				if(infoList.size() >= i){
					if(infoList.get(i) != "-IGNORE-"){
						infoMap.put(fields[i], infoList.get(i));
					}
				}
			}
		}
		
		if(split == DataSetSplit.CONTENTMETA) {
			addToDatasetItems(new MetaDatasetItem(infoMap, id, id)); // FIXME Sollte nur eine ID sein
		}
		else if(split == DataSetSplit.USERMETA){
			addToDatasetItems(new MetaDatasetItem(infoMap, id, id)); // FIXME Sollte nur eine ID sein
		}	
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
	
	private List<String> getInfoList(ch.uzh.agglorecommender.client.IDataset.DataSetSplit split){
		List<String> infoList = new LinkedList<String>();
		if(split == DataSetSplit.CONTENTMETA) {
			infoList.add("-IGNORE-"); //infoList.add("ID");
			infoList.add("-IGNORE-"); //infoList.add("Title");
			infoList.add("Date");
			infoList.add("-IGNORE-"); //infoList.add("?");
			infoList.add("-IGNORE-"); //infoList.add("URL");
			infoList.add("-IGNORE-"); //infoList.add("unknown");
			infoList.add("-IGNORE-"); //infoList.add("Action");
			infoList.add("-IGNORE-"); //infoList.add("Adventure");
			infoList.add("-IGNORE-"); //infoList.add("Animation");
			infoList.add("-IGNORE-"); //infoList.add("Children's");
			infoList.add("-IGNORE-"); //infoList.add("Comedy");
			infoList.add("-IGNORE-"); //infoList.add("Crime");
			infoList.add("-IGNORE-"); //infoList.add("Documentary");
			infoList.add("-IGNORE-"); //infoList.add("Drama");
			infoList.add("-IGNORE-"); //infoList.add("Fantasy");
			infoList.add("-IGNORE-"); //infoList.add("Film-Noir");
			infoList.add("-IGNORE-"); //infoList.add("Horror");
			infoList.add("-IGNORE-"); //infoList.add("Musical");
			infoList.add("-IGNORE-"); //infoList.add("Mystery");
			infoList.add("-IGNORE-"); //infoList.add("Romance");
			infoList.add("-IGNORE-"); //infoList.add("Sci-Fi");
			infoList.add("-IGNORE-"); //infoList.add("Thriller");
			infoList.add("-IGNORE-"); //infoList.add("War");
			infoList.add("-IGNORE-"); //infoList.add("Western");
		}
		else if(split == DataSetSplit.USERMETA){
			infoList.add("-IGNORE-"); //infoList.add("ID");
			infoList.add("Age");
			infoList.add("Gender");
			infoList.add("-IGNORE-"); //infoList.add("Occupation");
			infoList.add("-IGNORE-"); //infoList.add("?");
		}
		return infoList; 
	}

}
