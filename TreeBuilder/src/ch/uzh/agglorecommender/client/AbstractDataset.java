package ch.uzh.agglorecommender.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.uzh.agglorecommender.client.TestDriver.DataSetSplit;
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
	 * The input stream to parse.
	 */
	private InputStream input;

	/**
	 * Instantiates a new data set and parses the data from the specified or 
	 * the default input source.
	 * 
	 * @param datasetFile the File to load. If {@code null} the default input source is loaded.
	 */
	public AbstractDataset(INormalizer<T> normalizer, File datasetFile, DataSetSplit split) {
		this.normalizer = normalizer;
		try {
			if (datasetFile == null) {
				this.input = DatasetLocator.getDataset(getPathToDefaultInputFile(split));
			} else {
				this.input = new FileInputStream(datasetFile);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			this.input = null;
			TBLogger.getLogger(getClass().getName()).severe("Input file was not found");
			System.exit(-1);
		}
		parseDataset(getInput());
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
	 * Gets the current input stream to parse.
	 *  
	 * @return the stream to parse.
	 */
	InputStream getInput() {
		return input;
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
	 */
	void parseDataset(InputStream input) {
		try {
			int in = input.read();
			List<Character> charLi = new ArrayList<Character>();
			while (in != -1) {
				charLi.add((char)in);
				if (in == (int)'\n') {
					StringBuilder builder = new StringBuilder(charLi.size());
					for(Character ch: charLi)
					{
						builder.append(ch);
					}
					parseLine(builder.toString());
					charLi.clear();
				}
				in = input.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Extracts {@code IDataSetItem}s from a single
	 * line of the input stream and adds the items
	 * to the list of data set items.
	 * 
	 * @param line the line to parse
	 */
	abstract void parseLine(String line);
	
	
	abstract protected String getPathToDefaultInputFile(DataSetSplit split);
	
	/**
	 * Adds a data item to the list of data
	 * items generated from this data set. 
	 */
	void addToDatasetItems(IDatasetItem<T> item) {
		datasetItems.add(item);
	}
}
