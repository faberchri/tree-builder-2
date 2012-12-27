package client;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.net.URLClassLoader;

import clusterer.TreeBuilder;

import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.tools.plugin.Plugin;

public class SerializableRMOperatorDescription implements Serializable {
	
	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String rapidminerOperatorJarFileName = "treebuilder.jar";

	private static String fullyQualifiedGroupKey;
	private static String key;
	private static String iconName;

//	public SerializableRMOperatorDescription(
//			String fullyQualifiedGroupKey, 
//			String key,
//			String iconName) {
//		
//		fullyQualifiedGroupKey = fullyQualifiedGroupKey;
//		key = key;
//		iconName = iconName;
//	}
	
	public static void setOperatorDescription(
			String fullyQualifiedGroupKey, 
			String key,
			String iconName){
		
		SerializableRMOperatorDescription.fullyQualifiedGroupKey = fullyQualifiedGroupKey;
		SerializableRMOperatorDescription.key = key;
		SerializableRMOperatorDescription.iconName = iconName;
	}
	
	public static OperatorDescription getOperatorDescription() {
		
		// get URL of treebuilder.jar from classpath -> needed for call to operator constructor of rapidminer
		URL[] urls = ((URLClassLoader) TestDriver.class.getClassLoader()).getURLs();
		URL myURL = null;
		for (URL url : urls) {
			if (url.getPath().endsWith(rapidminerOperatorJarFileName)) {
				myURL = url;
			}   
		}
		
		// Build Operator Descriptor for Rapidminer
		OperatorDescription rapidminerOperatorDescription = null;
		try {
			rapidminerOperatorDescription = new OperatorDescription(
					fullyQualifiedGroupKey,
					key,
					TreeBuilder.class,
					TreeBuilder.class.getClassLoader(),
					iconName,
					new Plugin(new File(myURL.getFile())));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return rapidminerOperatorDescription;
	}
}
