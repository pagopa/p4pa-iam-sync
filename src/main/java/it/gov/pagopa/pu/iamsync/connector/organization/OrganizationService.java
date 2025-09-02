package it.gov.pagopa.pu.iamsync.connector.organization;

import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationCreateDTO;
import java.util.Optional;

public interface OrganizationService {

  void createOrganization(OrganizationCreateDTO organizationCreateDTO, String accessToken);

  Optional<Organization> getOrganizationByIpaCode(String ipaCode, String accessToken);

  Organization updateOrganization(Organization organization, String accessToken);

  Organization getOrganizationByExternalOrganizationId(String externalOrganizationId, String accessToken);
}
