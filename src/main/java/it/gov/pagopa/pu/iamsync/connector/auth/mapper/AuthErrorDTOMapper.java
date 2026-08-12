package it.gov.pagopa.pu.iamsync.connector.auth.mapper;

import it.gov.pagopa.pu.auth.dto.generated.AuthErrorDTO;
import it.gov.pagopa.pu.iamsync.config.rest.PuErrorDTO;
import it.gov.pagopa.pu.iamsync.dto.generated.ErrorFieldDTO;

public class AuthErrorDTOMapper {

  private AuthErrorDTOMapper() {
    /* This utility class should not be instantiated */
  }


  public static PuErrorDTO map(AuthErrorDTO errorDTO) {
    return new PuErrorDTO(
      errorDTO.getError().getValue(),
      errorDTO.getCode(),
      errorDTO.getErrorDescription(),
      errorDTO.getFields() != null
        ? errorDTO.getFields().stream()
        .map(field -> new ErrorFieldDTO(
          field.getField(),
          field.getError(),
          field.getMessage()
        ))
        .toList()
        : null
    );
  }
}
