package ch.uzh.agglorecommender.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.uzh.agglorecommender.datasets.DatasetLocator;
import ch.uzh.agglorecommender.util.TBLogger;


/**
 * A basic implementation of the {@code IDataset} interface.
 * <br>
 * Allows easier addition of new data sets by creating a
 * subclass of this class and overwriting {@code parseDataset}
 * and {@code parseLine} to adapt to the particular formatting of the
 * data set. 
 *
 * @param <T> The data type of the ratings in the data set.
 */
public abstract class AbstractDataset<T extends Number> implements IDataset<T> {
	
	/**
	 * A list of all user-content-rating combinations obtained from the input set.
	 */
	private List<IDatasetItem<T>> datasetItems = new ArrayList<IDatasetItem<T>>();
	
	/**
	 * The normalizer of this data set.
	 */
	private INormalizer<T> normalizer;
	
	/**
	 * The ratings input stream to parse.
	 */
	private InputStream ratingsInput;
	
	/**
	 * The meta data input streams to parse.
	 */
	private List<InputStream> metaInputs = new ArrayList<InputStream>();

	/**
	 * Instantiates a new data set and parses the data from the specified or 
	 * the default input source.
	 * 
	 * @param datasetFile the File to load. If {@code null} the default input source is loaded.
	 */
	public AbstractDataset(INormalizer<T> normalizer, File datasetFile, DataSetSplit split) {

		this.normalizer = normalizer;


		if (datasetFile == null) {
			this.ratingsInput = getDefaultFileStream((getPathToDefaultRatingsFile(split)));
		} else {
			this.ratingsInput = getCustomFileStream(datasetFile);
		}

		List<String> metaDataFileStrings = getPathToMetaFiles();
		if (metaDataFileStrings != null) {
			for (String mS : metaDataFileStrings) {
				metaInputs.add(getDefaultFileStream(mS));
			}
		}

		parseRatings(getStreamLineByLine(ratingsInput));
		
		if (metaInputs.size() > 0) {
			parseMeta(metaInputs);	
		}
	}
	
	private InputStream getDefaultFileStream(String ressourceDescriptor) {
		InputStream input = null;
		try {
			input = DatasetLocator.getDataset(ressourceDescriptor);
			if (input == null) throw new FileNotFoundException();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			this.ratingsInput = null;
			TBLogger.getLogger(getClass().getName()).severe("InputParser file was not found: " + ressourceDescriptor);
			// System.exit(-1);
		}
		return input;
	}

	private InputStream getCustomFileStream(File file) {
		InputStream input = null;
		try {
			input = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			this.ratingsInput = null;
			TBLogger.getLogger(getClass().getName()).severe("InputParser file was not found: "+ file.getPath());
			System.exit(-1);
		}
		return input;
	}

	@Override
	public Iterator<IDatasetItem<T>> iterateOverDatasetItems(){
		return datasetItems.iterator();
	}

	@Override
	public INormalizer<T> getNormalizer() {
		return normalizer;
	}
	
	/**
	 * Parses the input stream according to its format.
	 * <br>
	 * <br>
	 * This default implementation assumes that each line of the
	 * parsed file contains one or multiple userId-contentId-rating item(s).
	 * If this assumption is wrong you need to <b>overwrite</b> this method.
	 * 
	 * @param input the stream of the input source.
	 * @param type 
	 */
	protected List<String> getStreamLineByLine(InputStream input) {
		List<String> lines = new ArrayList<String>();
		try {
			int in = input.read();
			List<Character> charLi = new ArrayList<Character>();
			while (in != -1) {
				charLi.add((char)in);
				if (in == (int)'\n') {
					StringBuilder builder = new StringBuilder(charLi.size());
					for(Character ch: charLi) {
						builder.append(ch);
					}
					lines.add(builder.toString());
//					if (input.equals(ratingsInput)) {
//						parseRatingsLine();
//					} else if (metaInputs.contains(input)) {
//						parseMetaLine(builder.toString());
//					} else {
//						TBLogger.getLogger(getClass().getName()).severe("Don't know how to handle input stream: "+ input.toString());
//						input.close();
//						System.exit(-1);
//					}					
					charLi.clear();
				}
				in = input.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;				
	}
	
	/**
	 * Extracts {@code IDataSetItem}s from a single
	 * line of the input stream and adds the items
	 * to the list of data set items.
	 * 
	 * @param line the line to parse
	 */
	abstract protected void parseRatings(List<String> lines);
	
	abstract protected void parseMeta(List<InputStream> metaInfos);
	
	
	abstract protected String getPathToDefaultRatingsFile(DataSetSplit split);
	
	abstract protected List<String> getPathToMetaFiles();
	
	/**
	 * Adds a data item to the list of data
	 * items generated from this data set. 
	 */
	void addToDatasetItems(IDatasetItem<T> item) {
		datasetItems.add(item);
	}
}
