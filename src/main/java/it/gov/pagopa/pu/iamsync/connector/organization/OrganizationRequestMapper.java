package it.gov.pagopa.pu.iamsync.connector.organization;

import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationRequestBody;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrganizationRequestMapper {
  OrganizationRequestBody map(Organization organization);
}
