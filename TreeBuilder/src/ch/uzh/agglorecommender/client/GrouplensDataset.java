package ch.uzh.agglorecommender.client;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import ch.uzh.agglorecommender.util.TBLogger;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;


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
	 * Path to default meta input files.
	 */
	private static final List<String> pathToDefaultMetaInputFile = new ArrayList<String>();
	static {
		// The ordering of this files in the list need to be preserved! 
		pathToDefaultMetaInputFile.add("Grouplens/u.genre");
		pathToDefaultMetaInputFile.add("Grouplens/u.item");
		pathToDefaultMetaInputFile.add("Grouplens/u.occupation");
		pathToDefaultMetaInputFile.add("Grouplens/u.user");
	}
	
	/**
	 * Instantiates a new data set and parses the data from the specified or 
	 * the default input source.
	 * 
	 * @param datasetFile the File to load. If {@code null} the default input source is loaded.
	 */
	public GrouplensDataset(File datasetFile, DataSetSplit split) {
		
		// Data set has rating min value = 1 and max value = 5
		super(new IntegerNormalizer(1, 5), datasetFile, split);
		
	}
	
	/**
	 * Splits a single line of the input stream into tokens.
	 * The tokens represent user id, content id and ratings.
	 * These values are used to create a new {@code IDatasetItem} object,
	 * which are stored in {@code datasetItems}.
	 * 
	 * @param line A string containing a single user-content-rating information.
	 */
	@Override
	protected void parseRatings(List<String> lines) {
		for (String line : lines) {
			String[] fields = line.split("\t");
			Integer[] intFields = new Integer[fields.length];
			for (int i = 0; i < fields.length; i++) {
				if (i != fields.length - 1 ) {
					intFields[i] = Integer.parseInt(fields[i].trim());
				}
			}
			addToDatasetItems(new SimpleDatasetItem<Integer>(intFields[2], intFields[0], intFields[1]));			
		}
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
		return pathToDefaultMetaInputFile;
	}
	
	@Override
	protected void parseMeta(List<InputStream> metaInfos) {
		Map<Integer, String> genres = parseGenres(metaInfos.get(0));
		ListMultimap<Integer, String> contents = parseContents(metaInfos.get(1), genres);
		ListMultimap<Integer, String> users = parseUsers(metaInfos.get(3));
		
		String[] cAtts = {"title","release date","IMDb URL","genre"};
		String[] uAtts = {"age","sex","occupation","zip-code"};
		Iterator<IDatasetItem<Integer>> it = iterateOverDatasetItems();
		while (it.hasNext()) {
			IDatasetItem<Integer> dI = it.next();
			addContentMetaToDatasetItem(dI, contents, cAtts);
			addUserMetaToDatasetItem(dI, users, uAtts);
		}
	}
	
	private void addContentMetaToDatasetItem(IDatasetItem<Integer> dI, ListMultimap<Integer, String> contents, String[] cAtts) {		
		int cId = dI.getContentId();
		List<String> cValues = contents.get(cId);
		for (int i = 0; i < cValues.size(); i++) {
			if (i > 2) {
				dI.addContentMetaData(cAtts[3], cValues.get(i).trim());
			} else {
				dI.addContentMetaData(cAtts[i], cValues.get(i).trim());
			}
			
		}
	}
	
	private void addUserMetaToDatasetItem(IDatasetItem<Integer> dI, ListMultimap<Integer, String> users, String[] uAtts) {
		int uId = dI.getUserId();
		List<String> uValues = users.get(uId);
		if (uValues.size() != 4) {
			Logger log = TBLogger.getLogger(getClass().getName());
			log.severe("Err.: Number of user meta data values != 4: " + uValues);
			System.exit(-1);
		}
		for (int i = 0; i < uValues.size(); i++) {
			dI.addUserMetaData(uAtts[i], uValues.get(i).trim());
		}

	}
	
	private Map<Integer, String> parseGenres(InputStream genreStream) {
		Map<Integer, String> genres = new HashMap<Integer, String>();
		List<String> lines = getStreamLineByLine(genreStream);
		for (String line : lines) {
			String[] fields = line.split("\\|");
			genres.put(Integer.parseInt(fields[1].trim()), fields[0].trim());			
		}
		return genres;
	}
	
	private ListMultimap<Integer, String> parseContents(InputStream itemStream, Map<Integer, String> genres) {
		ListMultimap<Integer, String> r = ArrayListMultimap.create();
		List<String> lines = getStreamLineByLine(itemStream);
		for (String line : lines) {
			
			// dealing with missing values in data set
			if (line.startsWith("267|unknown")) {
				line = "267|unknown|unknown||unknown|1|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0";
			}
			line = line.replace("|||", "||unknown|");
			line = line.replace("||", "|");
			//////////////////////////////////////////////
			
			String[] fields = line.split("\\|");
			int itemId = Integer.parseInt(fields[0].trim());
//			System.out.println(line);
			for (int i = 1; i < fields.length; i++) {	
//				System.out.println(fields[i]);
				if (i < 4) {
					r.put(itemId, fields[i].trim());					
				} else {
					int gI = Integer.parseInt(fields[i].trim());
					if (gI == 1) {
						r.put(itemId, genres.get(i - 4));
					}
				}
			}
		}
		return r;
	}
	
	private ListMultimap<Integer, String> parseUsers(InputStream userStream) {
		ListMultimap<Integer, String> r = ArrayListMultimap.create();
		List<String> lines = getStreamLineByLine(userStream);
		for (String line : lines) {
			String[] fields = line.split("\\|");
			int userId = Integer.parseInt(fields[0].trim());
			for (int i = 1; i < fields.length; i++) {
				r.put(userId, fields[i].trim());
			}
		}
		return r;
	}
}
