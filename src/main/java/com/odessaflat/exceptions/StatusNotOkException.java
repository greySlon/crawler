package com.odessaflat.exceptions;


public class StatusNotOkException extends RuntimeException {

  public StatusNotOkException() {
    super("ResponseCode not  OK");
  }
}
