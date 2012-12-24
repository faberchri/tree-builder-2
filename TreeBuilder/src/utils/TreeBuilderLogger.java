package utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TreeBuilderLogger {

	private static final String logDirName = "TreeBuilder_log_dir";
	private static final boolean appendToLog = true;
	private static File loggDir;
	
	private static boolean initialized = false;
			
	static private FileHandler fileTxt;
	static private Formatter formatterTxt;

	static private FileHandler fileHTML;
	static private Formatter formatterHTML;
	
	static private ConsoleHandler consoleHandler;
	static private Formatter formatterConsole;

	static public void setup(Class logName, Level loggLevel) throws IOException {
		// perform basic initialization of logging facility
		if (! initialized) {
			loggDir = initLogging();
			initialized = true;
		}
		
		// check if a logger for logName is already initialized
		if (LogManager.getLogManager().getLogger(logName.getName()) != null) {
			return;
		}
		
		// Create logger for passed class
		Logger treeBuilderLogger = Logger.getLogger(logName.getName());
		String logFilePath = (new File(loggDir, logName.getName())).getAbsolutePath();
		treeBuilderLogger.setLevel(loggLevel);
		fileTxt = new FileHandler(logFilePath.concat(".log"), appendToLog);
		fileHTML = new FileHandler(logFilePath.concat(".html"), appendToLog);
		consoleHandler = new  ConsoleHandler(){
			@Override
			protected synchronized void setOutputStream(OutputStream out)
					throws SecurityException {
				super.setOutputStream(System.out);
			}
		};

		consoleHandler.setLevel(Level.ALL);
		

		// Create txt Formatter
		formatterTxt = new MyTextFormatter();
		fileTxt.setFormatter(formatterTxt);
		treeBuilderLogger.addHandler(fileTxt);
		
		

		// Create HTML Formatter
		formatterHTML = new MyHtmlFormatter();
		fileHTML.setFormatter(formatterHTML);
		treeBuilderLogger.addHandler(fileHTML);
		
		// Create Console Formatter
//		formatterConsole = new SimpleFormatter();
		consoleHandler.setFormatter(formatterTxt);
		treeBuilderLogger.addHandler(consoleHandler);
	}

	static private File initLogging() {
		// remove all default handlers
		Enumeration<String> en = LogManager.getLogManager().getLoggerNames();
		while (en.hasMoreElements()) {
			String string = (String) en.nextElement();
			Logger log = LogManager.getLogManager().getLogger(string);
			for(Handler h:log.getHandlers()) {
				log.removeHandler(h);
			}	
		}
		
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
