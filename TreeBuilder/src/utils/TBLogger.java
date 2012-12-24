package utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TBLogger {
	
	private static final Level defaultLogLevel = Level.ALL;

	private static final Map<String, Level> logLevelMap = new HashMap<String, Level>();
	static {
		// add log levels for any loggers, alternatively you can specify
		// log level of a logger which has the same name as a class
		// directly in a field of type Level in the class
		logLevelMap.put("exampleLogger", Level.ALL);
	}

	private static final String logDirName = "TreeBuilder_log_dir";
	private static final boolean appendToLog = true;
	private static File loggDir;
	
	private static boolean initialized = false;
			
	static private Formatter formatterTxt;

	static private Formatter formatterHTML;
	
	static private ConsoleHandler consoleHandler = null;
	static private Formatter formatterConsole;

	static private void setup(String pattern) throws IOException {
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


		// Create logger for passed class
		Logger logger = Logger.getLogger(pattern);
		String logFilePath = (new File(loggDir, pattern)).getAbsolutePath();
		FileHandler fileTxt = new FileHandler(logFilePath.concat(".log"), appendToLog);
		FileHandler fileHTML = new FileHandler(logFilePath.concat(".html"), appendToLog);
		logger.setLevel(logLevel);

		// add txt Formatter
		fileTxt.setFormatter(formatterTxt);
		logger.addHandler(fileTxt);

		// add HTML Formatter
		fileHTML.setFormatter(formatterHTML);
		logger.addHandler(fileHTML);
		
		// add Console Formatter
//		formatterConsole = new SimpleFormatter();
		logger.addHandler(consoleHandler);
	
		logger.info("logger with name "+ logger.getName() +" initialized.");
	}
	
	static public Logger getLogger(String pattern) {
		// check if a logger for logName is already initialized
		if (LogManager.getLogManager().getLogger(pattern) == null) {
			try {
				setup(pattern);
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Error on initializing logging facilities!");
				System.exit(-1);
			}
		}
		return Logger.getLogger(pattern);
	}

	static private File initLogging() {
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
}
