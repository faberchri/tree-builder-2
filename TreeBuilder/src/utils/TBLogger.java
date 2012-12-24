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
	
	// set this to false to turn off logging
	private static final boolean doLogging = true;
	
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
	
	private static final Map<String, Logger> loggerMap = new HashMap<String, Logger>();
	
	private static boolean initialized = false;
			
	private static Formatter formatterTxt;

	private static Formatter formatterHTML;
	
	private static ConsoleHandler consoleHandler = null;
	private static Formatter formatterConsole;

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
	
		logger.info("logger with name "+ logger.getName() +" initialized.");
		
		return logger;
	}
	
	public static Logger getLogger(String pattern) {
		
		// check if a logger for logName is already initialized
		if (! loggerMap.containsKey(pattern)) {
			try {
				loggerMap.put(pattern, setup(pattern));
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Error on initializing logging facilities!");
				System.exit(-1);
			}
		}
		return loggerMap.get(pattern);
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
}
