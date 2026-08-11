package it.gov.pagopa.pu.iamsync.exception.common;

import org.springframework.http.HttpStatus;

public interface RestInvokeException {
  String getApplicationName();
  HttpStatus getHttpStatus();
  String getCategory();
}
