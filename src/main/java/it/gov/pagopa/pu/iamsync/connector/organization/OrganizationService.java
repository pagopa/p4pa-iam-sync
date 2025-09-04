package it.gov.pagopa.pu.iamsync.connector.organization;

import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationCreateDTO;
import java.util.Optional;

public interface OrganizationService {

  void createOrganization(OrganizationCreateDTO organizationCreateDTO);

  Optional<Organization> getOrganizationByIpaCode(String ipaCode);

  Organization updateOrganization(Organization organization);

  Optional<Organization> getOrganizationByExternalOrganizationId(String externalOrganizationId);
}
