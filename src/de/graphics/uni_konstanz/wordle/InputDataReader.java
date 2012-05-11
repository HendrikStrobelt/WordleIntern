package de.graphics.uni_konstanz.wordle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class InputDataReader {

	public static List<TextItem> loadCSV(File file, String splitChar) {
		List<TextItem> res = new ArrayList<TextItem>();

		if (file.exists()) {

			// Read File Line By Line
			float max = Float.MIN_VALUE;
			float min = Float.MAX_VALUE;

			try {
				CSVReader reader = new CSVReader(new FileReader(file));
				String[] nextLine;
				while ((nextLine = reader.readNext()) != null) {
					// nextLine[] is an array of values from the line

					System.out.println(nextLine[0] + nextLine[1] + "etc...");

					if (nextLine.length > 1) {
						String term = nextLine[0];
						Float weight = new Float(nextLine[1]);
						if (weight > max)
							max = weight;
						if (weight < min)
							min = weight;

						res.add(new TextItem(term, weight));
					}

				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (TextItem textItem : res) {
				textItem.setSize((textItem.getSize() - min) / (max-min));
			}

			Collections.sort(res);
			
			
		}

		return res;
	}

}
