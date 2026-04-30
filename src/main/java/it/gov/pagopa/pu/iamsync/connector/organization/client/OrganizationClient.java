package it.gov.pagopa.pu.iamsync.connector.organization.client;

import it.gov.pagopa.pu.iamsync.connector.auth.AuthnService;
import it.gov.pagopa.pu.iamsync.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationCreateDTO;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationStatus;
import org.springframework.stereotype.Service;

@Service
public class OrganizationClient {

  private final OrganizationApisHolder organizationApisHolder;
  private final AuthnService authnService;

  public OrganizationClient(OrganizationApisHolder organizationApisHolder,
    AuthnService authnService) {
    this.organizationApisHolder = organizationApisHolder;
    this.authnService = authnService;
  }

  public void createOrganization(OrganizationCreateDTO organizationCreateDTO) {
    organizationApisHolder.getOrganizationApi(authnService.getAccessToken())
      .createOrganization(organizationCreateDTO);
  }

  public void updateOrganizationStatus(Long organizationId, OrganizationStatus newStatus) {
    organizationApisHolder.getOrganizationApi(authnService.getAccessToken())
      .updateOrganizationStatus(organizationId, newStatus);
  }
}
