package client;

import java.io.File;

/**
 * The data set of Floraea. 
 */
public class FloareaDataset extends AbstractDataset<Double> {
	
	/**
	 * Path to default input file.
	 */
	private static final String pathToDefaultInputFile = "Floarea/classification_May8AllTesting.txt";
	
	/**
	 * Instantiates a new data set and parses the data from the specified or 
	 * the default input source.
	 * 
	 * @param datasetFile the File to load. If {@code null} the default input source is loaded.
	 */
	public FloareaDataset(File datasetFile) {
		super(new INormalizer<Double>() {
			
			@Override
			public double normalizeRating(Double rating) {
				// Nothing to do here. Ratings of this data set
				// are already normalized (in the range [0,1]).
				return rating.doubleValue();
			}
		},
		 datasetFile, FloareaDataset.pathToDefaultInputFile);

	}
	
	/**
	 * Splits a single line of the input stream into tokens.
	 * The tokens represent id_dataset id_workflow rating.
	 * These values are used to create a new {@code IDatasetItem} object,
	 * which are stored in {@code datasetItems}.
	 * 
	 * @param entry A string containing a single user-content-rating information.
	 */
	@Override
	void parseLine(String line) {
		String[] sAr = line.split("\\s+");
		IDatasetItem<Double> di = 
				new SimpleDatasetItem<Double>(
						Double.parseDouble(sAr[2]),
						Integer.parseInt(sAr[0]),
						Integer.parseInt(sAr[1]));
		addToDatasetItems(di);
	}


}
