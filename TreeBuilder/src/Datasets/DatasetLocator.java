package Datasets;

import java.io.InputStream;

public class DatasetLocator {
	public static InputStream getDataset(String dataSetDescriptor) {
		return DatasetLocator.class.getResourceAsStream(dataSetDescriptor);
	}
}
