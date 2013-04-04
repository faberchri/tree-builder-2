package ch.uzh.agglorecommender.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class TBLogger {
	
	/**
	 * set doLogging to false to turn off logging completely.
	 */
	private static final boolean doLogging = true;
	
	/**
	 * You can add here names of loggers which shall be enabled.
	 * Loggers which are not listed are disabled.
	 * BUT: If the array is empty all loggers are enabled.
	 */ 
	private static Set<String> selectiveLogging = new HashSet<String>();
	static {
		// Add names of loggers that shall be enabled here ...
		// e.g.: selectiveLogging.add("exampleLogger");
	};
	
	/**
	 * set the log level for loggers that have no specific log level defined
	 */
	private static final Level defaultLogLevel = Level.ALL;
	
	/**
	 * Here you can specify the log level of a logger.
	 * add log levels for any loggers, alternatively you can specify
	 *log level of a logger which has the same name as a class
	 *directly in a field of type Level in the class
	 */
	private static final Map<String, Level> logLevelMap = new HashMap<String, Level>();
	static {
		// Add map entries here ...
		// e.g.: logLevelMap.put("exampleLogger", Level.ALL);
		
		logLevelMap.put("ch.uzh.agglorecommender.clusterer.Counter", Level.INFO);
		logLevelMap.put("ch.uzh.agglorecommender.clusterer.treesearch.CobwebMaxCategoryUtilitySearcher", Level.FINER);
		logLevelMap.put("ch.uzh.agglorecommender.clusterer.treesearch.ClassitMaxCategoryUtilitySearcher", Level.FINER);
		logLevelMap.put("ch.uzh.agglorecommender.clusterer.treeupdate.SimpleNodeUpdater", Level.FINER);
		logLevelMap.put("ch.uzh.agglorecommender.clusterer.treeupdate.ExtendedNodeUpdater", Level.FINER);
		logLevelMap.put("ch.uzh.agglorecommender.clusterer.treeupdate.SaveAttributeNodeUpdater", Level.FINER);
	}

	private static final String logDirName = "TreeBuilder_logs";
	private static final boolean appendToLog = true;
	private static File loggDir;
	
	private static final Map<String, Logger> loggerMap = new HashMap<String, Logger>();
	
	private static boolean initialized = false;
			
	private static Formatter formatterTxt;

	private static Formatter formatterHTML;
	
	private static ConsoleHandler consoleHandler = null;

	private static Logger setup(String pattern) throws IOException {
		// perform basic initialization of logging facility
		if (! initialized) {
			loggDir = initLogging();
			initialized = true;
		}
		
		// get Log Level of class:
		// first we check if the pattern has an 
		// entry in the logLevel map.
		// If not:
		// We look for a class with same name as "pattern".
		// If a class is found searches for a field of type
		// java.util.logging.Level. If this is found
		// the fields value is used as logging level.
		// Otherwise the defaultLogLevel is used.
		Level logLevel = defaultLogLevel;
		if (logLevelMap.containsKey(pattern)) {
			logLevel = logLevelMap.get(pattern);
		} else {
			try {
				Field[] fields = Class.forName(pattern).getDeclaredFields();
				for (Field field : fields) {
					if (field.getType().equals(Level.class)) {
						field.setAccessible(true);
						logLevel = (Level) field.get(new Object());
						break;
					}
				}
			} catch (ClassNotFoundException e) {
				// Nothing to do here
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		// Get a JAVA API logger 
		Logger logger = Logger.getLogger(pattern);
				
		// we check if we don't want to log at all
		if (! doLogging) {
			logLevel = Level.OFF;
			logger.setLevel(logLevel);
			return logger;
		}
		
		if (! selectiveLogging.isEmpty()) {
			if (! selectiveLogging.contains(pattern)) {
				logLevel = Level.OFF;
				logger.setLevel(logLevel);
				return logger;
			}
		}
		
		// Set the log level
		logger.setLevel(logLevel);
		
		// create file handlers for logger
		String logFilePath = (new File(loggDir, pattern)).getAbsolutePath();
		FileHandler fileTxt = new FileHandler(logFilePath.concat(".log"), appendToLog);
		FileHandler fileHTML = new FileHandler(logFilePath.concat(".html"), appendToLog);

		// add txt Formatter
		fileTxt.setFormatter(formatterTxt);
		logger.addHandler(fileTxt);

		// add HTML Formatter
		fileHTML.setFormatter(formatterHTML);
		logger.addHandler(fileHTML);
		
		// add Console Formatter
//		formatterConsole = new SimpleFormatter();
		logger.addHandler(consoleHandler);
		logger.info("***************************************************************");
		logger.info("logger with name "+ logger.getName() +" initialized.");
		logger.info("***************************************************************");
		
		return logger;
	}
	
    /**
     * Find or create a logger for a named subsystem.  If a logger has
     * already been created with the given name it is returned.  Otherwise
     * a new logger is created.
     * 
     *
     * @param   name            A name for the logger.  This should
     *                          be a dot-separated name and should normally
     *                          be based on the package name or class name
     *                          of the subsystem, such as java.net
     *                          or javax.swing
     * @return a suitable Logger
     */
	public static Logger getLogger(String name) {
		
		// check if a logger for passed name is already initialized
		if (! loggerMap.containsKey(name)) {
			try {
				loggerMap.put(name, setup(name));
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Error on initializing logging facilities!");
				System.exit(-1);
			}
		}
		return loggerMap.get(name);
	}

	private static File initLogging() {
		if (! doLogging) return null;
		
		formatterTxt = new MyTextFormatter();
		formatterHTML = new MyHtmlFormatter();
		
		// remove all default handlers
		Enumeration<String> en = LogManager.getLogManager().getLoggerNames();
		while (en.hasMoreElements()) {
			String string = (String) en.nextElement();
			Logger log = LogManager.getLogManager().getLogger(string);
			for(Handler h:log.getHandlers()) {
				log.removeHandler(h);
			}	
		}
		
		consoleHandler = new  ConsoleHandler(){
			@Override
			protected synchronized void setOutputStream(OutputStream out)
					throws SecurityException {
				super.setOutputStream(System.out);
			}
		};
		consoleHandler.setLevel(Level.ALL);
		consoleHandler.setFormatter(formatterTxt);
		
			
		File homePath = new File(System.getProperty("user.home"));
		if (homePath.canWrite()) {
			homePath = new File(homePath, logDirName);
		} else {
			homePath = new File(System.getProperty("java.io.tmpdir"), logDirName);			  
		}
		if (! homePath.exists()) {
			homePath.mkdir();			
		}
		return homePath;
	}
	
	/**
	 * Singleton: must not be instantiated.
	 */
	private TBLogger() {}
}
