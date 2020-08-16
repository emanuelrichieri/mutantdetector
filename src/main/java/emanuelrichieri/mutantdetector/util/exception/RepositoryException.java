package emanuelrichieri.mutantdetector.util.exception;

public class RepositoryException extends Exception {

	private Exception exception;
	
	public RepositoryException(String message, Exception ex) {
		super(message);
		this.exception = ex;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}
}
