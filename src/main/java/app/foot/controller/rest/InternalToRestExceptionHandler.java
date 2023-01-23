package app.foot.controller.rest;

import app.foot.model.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InternalToRestExceptionHandler {

  @ExceptionHandler(value = BadRequestException.class)
  public ResponseEntity<Exception> handleBadRequest(BadRequestException e) {
    return new ResponseEntity<>(toRest(e,HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
  }

  private Exception toRest(java.lang.Exception e, HttpStatus status) {
    var restException = new Exception();
    restException.setType(status.toString());
    restException.setMessage(e.getMessage());
    return restException;
  }
}
