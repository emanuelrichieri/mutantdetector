package emanuelrichieri.mutantdetector.io;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityBuilder {

	public static <T> ResponseEntity<T> build(T body, HttpStatus status) {
		return new ResponseEntity<T>(body, status);
	}
	
	public static <T> ResponseEntity<T> ok(T body) {
		return new ResponseEntity<T>(body, HttpStatus.OK);
	}
}
