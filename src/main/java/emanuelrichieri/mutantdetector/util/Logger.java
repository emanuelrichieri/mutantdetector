package emanuelrichieri.mutantdetector.util;

import java.util.logging.Level;

public class Logger {
	
	private java.util.logging.Logger logger;

	public Logger(String name) {
		this.logger = java.util.logging.Logger.getLogger(name);
	}

	public static Logger getLogger(String name) {
		return new Logger(name);
	}
	
	public void error(String message) {
		this.logger.log(Level.SEVERE, message);
	}

	public void error(Exception ex) {
		this.logger.log(Level.SEVERE, null, ex);
	}
	
	public void error(String message, Exception ex) {
		this.logger.log(Level.SEVERE, message, ex);
	}
	
	public void info(String message) {
		this.logger.log(Level.INFO, message);
	}
	
	public void info (String message, Object object) {
		this.logger.log(Level.INFO, message, object);
	}
}
