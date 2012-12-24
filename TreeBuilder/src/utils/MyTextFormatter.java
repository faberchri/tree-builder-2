package utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class MyTextFormatter extends Formatter {

	@Override
	public String format(LogRecord rec) {
		StringBuilder sb = new StringBuilder();
		sb.append(calcDate(rec.getMillis()));
		sb.append(" -- ");
		sb.append(rec.getSourceClassName());
		sb.append(" -- ");
		sb.append(rec.getSourceMethodName());
		sb.append(" -- ");
		if (rec.getLevel().intValue() >= Level.WARNING.intValue()) {
			sb.append("****");
		}
		sb.append(rec.getLevel().getName());
		if (rec.getLevel().intValue() >= Level.WARNING.intValue()) {
			sb.append("****");
		}
		sb.append(": ");
		sb.append(rec.getMessage());
		sb.append("\n");
		return sb.toString();
	}
	
	private String calcDate(long millisecs) {
		SimpleDateFormat date_format = new SimpleDateFormat("EEE-dd-MMM-yyyy HH:mm:ss-SSS");
		Date resultdate = new Date(millisecs);
		return date_format.format(resultdate);
	}

}
