package it.gov.pagopa.pu.iamsync.exception.transcoder.handler;

import it.gov.pagopa.pu.iamsync.exception.common.BaseBusinessException;
import it.gov.pagopa.pu.iamsync.exception.transcoder.ExceptionMessageTranscoded;
import it.gov.pagopa.pu.iamsync.exception.transcoder.ExceptionMessageTranscoder;

public class BaseBusinessExceptionMessageTranscoder implements ExceptionMessageTranscoder<BaseBusinessException> {
  @Override
  public ExceptionMessageTranscoded transcode(BaseBusinessException businessException) {
    return new ExceptionMessageTranscoded(businessException.getCode(), businessException.getMessage(), businessException.getFields());
  }
}
