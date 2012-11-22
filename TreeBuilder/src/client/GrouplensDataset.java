package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Datasets.DatasetLocator;


/**
 * 
 * Extracts {@code DatasetItems} from a Grouplens input file by parsing the 
 * file and splitting obtained strings into tokens.
 * <br>
 * For convenience reasons the data set loads a default grouplens
 * input file if no input file is specified on instantiation.
 * The default input file is a stored together with the project.
 * <br>
 * The Groublens data set can be obtained from
 * <a href="http://www.grouplens.org/node/12">http://www.grouplens.org/node/12</a>.
 *
 */
public class GrouplensDataset implements IDataset<Integer> {
	
	/**
	 * A list of all user-content-rating combinations obtained from the input set.
	 */
	private List<IDatasetItem<Integer>> datasetItems = new ArrayList<IDatasetItem<Integer>>();
	
	/**
	 * The normalizer of this data set.
	 */
	private INormalizer<Integer> normalizer = new IntegerNormalizer(1, 5);
	
	/**
	 * Path to default input file.
	 */
	private static final String pathToDefaultInputFile = "Grouplens/u1.base";
	
	/**
	 * Instantiates a new data set and loads the data from the specified input file.
	 * 
	 * @param datasetFile the File to load. If {@code null} the default input file is loaded.
	 */
	public GrouplensDataset(File datasetFile) {
		try {
			InputStream input;
			if (datasetFile == null) {
				input = DatasetLocator.getDataset(pathToDefaultInputFile);
			} else {
				input = new FileInputStream(datasetFile);
			}
			parseDataset(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Parses the input stream created from the input file.
	 * 
	 * @param input the stream of the input source.
	 */
	private void parseDataset(InputStream input) {
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
					extractData(builder.toString());
					charLi.clear();
				}
				in = input.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Splits a single line of the input stream into tokens.
	 * The tokens represent user id, content id and ratings.
	 * These values are used to create a new {@code IDatasetItem} object,
	 * which are stored in {@code datasetItems}.
	 * 
	 * @param entry A string containing a single user-content-rating information.
	 */
	private void extractData(String entry) {
		String[] fields = entry.split("\t");
		Integer[] intFields = new Integer[fields.length];
		for (int i = 0; i < fields.length; i++) {
			if (i != fields.length - 1 ) {
				intFields[i] = Integer.parseInt(fields[i]);		
			}
		}
		datasetItems.add(new SimpleDatasetItem<Integer>(intFields[2], intFields[0], intFields[1]));
	}

	@Override
	public Iterator<IDatasetItem<Integer>> iterateOverDatasetItems() {
		return datasetItems.iterator();
	}

	@Override
	public INormalizer<Integer> getNormalizer() {
		return normalizer;
	}
}
