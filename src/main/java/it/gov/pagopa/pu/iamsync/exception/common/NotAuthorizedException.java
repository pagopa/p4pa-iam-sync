package it.gov.pagopa.pu.iamsync.exception.common;

public class NotAuthorizedException extends BaseBusinessException {
  public NotAuthorizedException(String code, String message) {
    super(code, message);
  }
}
