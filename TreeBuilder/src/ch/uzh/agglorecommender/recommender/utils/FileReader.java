package ch.uzh.agglorecommender.recommender.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ch.uzh.agglorecommender.util.TBLogger;

public class FileReader {
	
	public InputStream getCustomFileStream(File file) {
		InputStream input = null;
		try {
			input = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			TBLogger.getLogger(getClass().getName()).severe("Input file was not found: "+ file.getPath());
			System.exit(-1);
		}
		return input;
	}
	
	public List<String> getStreamLineByLine(InputStream input) {
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
					charLi.clear();
				}
				in = input.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;				
	}

}
