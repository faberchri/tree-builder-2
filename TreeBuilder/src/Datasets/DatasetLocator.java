package Datasets;

import java.io.InputStream;

/**
 * Locates a resource to load within this project.
 *
 */
public class DatasetLocator {
	
	/**
	 * Finds a resource with a given name within
	 * this project and returns an {@code InputStream} of it.
	 * @param dataSetDescriptor name of the resource
	 * @return  A {@link java.io.InputStream} object
	 * of the resource or {@code null} if no
	 * resource with this name is found.
	 */
	public static InputStream getDataset(String dataSetDescriptor) {
		return DatasetLocator.class.getResourceAsStream(dataSetDescriptor);
	}
	
	/**
	 * Must not be instantiated.
	 */
	private DatasetLocator() { }
}
