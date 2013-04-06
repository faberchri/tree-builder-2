package ch.uzh.agglorecommender.client;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ImmutableMap;


public class TestDataset implements IDataset {
	
	List<IDatasetItem> datasetItems = new ArrayList<>();
	

	public TestDataset(String datasetname) {
		InputStream stream = getClass().getResourceAsStream(datasetname);
		java.util.Scanner s = new java.util.Scanner(stream).useDelimiter("\\A");
	    String string = s.hasNext() ? s.next() : "";		
		String[] sAr = string.split("\n");
		for (String token : sAr) {
			String[] items = token.split("\t");
			System.out.println(items[0] + "\t" + items[1] + "\t" + items[2]);
			datasetItems.add(new SimpleDatasetItem((Double.parseDouble(items[2]) - 1) * 2.5, items[0], items[1]));			
		}
	}

	@Override
	public Iterator<IDatasetItem> iterateOverDatasetItems() {
		return datasetItems.iterator();
	}

	@Override
	public INormalizer getNormalizer() {
		return new INormalizer() {	
			
			@Override
			public double normalizeRating(double rating) {
				// nothing to do
				return rating;
			}
		};
	}

	@Override
	public ImmutableMap<String, Boolean> getAttributeClusteringConfig() {
		// nothing to do
		return null;
	}

	@Override
	public double denormalize(double value, String attributeTag) {
		// nothing to do 
		return 0;
	}
	
}
