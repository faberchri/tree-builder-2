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

public class GrouplensDataset implements Dataset<Integer> {
	
	private List<DatasetItem<Integer>> datasetItems = new ArrayList<DatasetItem<Integer>>();
	
	private Normalizer<Integer> normalizer = new IntegerNormalizer(1, 5);
	
	public GrouplensDataset(File datasetFile) {
		try {
			InputStream input;
			if (datasetFile == null) {
				input = DatasetLocator.getDataset("Grouplens/u1.base");// this.getClass().getResourceAsStream("u1.base");
			} else {
				input = new FileInputStream(datasetFile);
			}
			parseDataset(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

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
	public Iterator<DatasetItem<Integer>> iterateOverDatasetItems() {
		return datasetItems.iterator();
	}

	@Override
	public Normalizer<Integer> getNormalizer() {
		return normalizer;
	}
}
