package client;

import com.beust.jcommander.Parameter;

public class CommandLineArgs {
	
	@Parameter(names = { "-d", "-dataset" }, description = "Fully qualified class name of data set to process. Example: client.GrouplensDataset", arity = 1)
	protected String nameOfDataset = "client.GrouplensDataset";
	
	@Parameter(names = { "-f", "-inputDataFile" }, description = "Path to input file of data set. Example: /path/to/input/file", arity = 1)
	protected String datasetFile = null;
	
	@Parameter(names = { "-mcsCont", "-mcsC" }, description = "Fully qualified class name of IMaxCategoryUtilitySearcher for contents to use. Example: modules.CobwebMaxCategoryUtilitySearcher", arity = 1)
	protected String nameOfmaxCategoryUtilitySearcherContents = "modules.CobwebMaxCategoryUtilitySearcher";
	
	@Parameter(names = { "-mcsUser", "-mcsU" }, description = "Fully qualified class name of IMaxCategoryUtilitySearcher for users to use. Example: modules.CobwebMaxCategoryUtilitySearcher", arity = 1)
	protected String nameOfmaxCategoryUtilitySearcherUsers = "modules.CobwebMaxCategoryUtilitySearcher";
	
	@Parameter(names = { "-nodeUpdater", "-nU", "-updater" }, description = "Fully qualified class name of INodeUpdater to use. Example: modules.SimpleNodeUpdater", arity = 1)
	protected String nameOfNodeUpdater = "modules.SimpleNodeUpdater";
	
	@Parameter(names = { "-resume", "-r"}, description = "Path to file of serialized run to resume. Example: /path/to/input/of/type.ser", arity = 1)
	protected String resumePrevRun = null;
	
	@Parameter(names = { "-serialize", "-s"}, description = "Path where to store output file. Example: /location/to/store/output.ser", arity = 1)
	protected String serializeRun = null;

}