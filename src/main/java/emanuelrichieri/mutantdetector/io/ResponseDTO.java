package emanuelrichieri.mutantdetector.io;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(content = Include.NON_NULL)
public class ResponseDTO {
	
	private Boolean success;
	private String message;
	private Object data;
	private HttpStatus status;
	
	public ResponseDTO(Boolean success, String message, Object data, HttpStatus status) {
		this.success = success;
		this.message = message;
		this.data = data;
		this.status = status;
	}
	
	public static ResponseDTO ok(String message) {
		return ok(message, null);
	}
	
	public static ResponseDTO ok(String message, Object data) {
		return new ResponseDTO(true, message, data, HttpStatus.OK);
	}
	
	public static ResponseDTO internalServerError(String message) {
		return internalServerError(message, null);
	}
	
	public static ResponseDTO internalServerError(Exception ex) {
		return internalServerError(null, ex);
	}
	
	public static ResponseDTO badRequest(String message) {
		return badRequest(message, null);
	}
	
	public static ResponseDTO badRequest(Exception ex) {
		return badRequest(null, ex);
	}
		
	public static ResponseDTO badRequest(String message, Exception ex) {
		return error(message, ex, HttpStatus.BAD_REQUEST);
	}
	
	public static ResponseDTO internalServerError(String message, Exception ex) {
		return error(message, ex, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	public static ResponseDTO forbidden(String message, Object data) {
		return error(message, data, null, HttpStatus.FORBIDDEN);
	}
	
	public static ResponseDTO error(String message, Exception ex, HttpStatus status) {
		return error(message, null, ex, status);
	}
	
	public static ResponseDTO error(String message, Object data, Exception ex, HttpStatus status) {
		if (Objects.nonNull(ex) && Objects.nonNull(ex.getMessage())) {
			if (Objects.nonNull(message)) {
				message += " :: ";
			}
			message += ex.getMessage();
		}
		return new ResponseDTO(false, message, data, status);
	}
	
	public ResponseEntity<ResponseDTO> build() {
		return ResponseEntityBuilder.build(this, this.status);
	}
	
	public Boolean getSuccess() {
		return success;
	}
	public String getMessage() {
		return message;
	}
	public Object getData() {
		return data;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setData(Object data) {
		this.data = data;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

}
