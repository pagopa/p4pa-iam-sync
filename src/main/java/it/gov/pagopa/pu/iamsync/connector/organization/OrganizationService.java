package it.gov.pagopa.pu.iamsync.connector.organization;

import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationCreateDTO;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationStatus;

import java.util.Optional;

public interface OrganizationService {

  void createOrganization(OrganizationCreateDTO organizationCreateDTO);

  Optional<Organization> getOrganizationByIpaCode(String ipaCode);

  void updateOrganizationStatus(Long organizationId, OrganizationStatus newStatus);

  Optional<Organization> getOrganizationByExternalOrganizationId(String externalOrganizationId);
}
