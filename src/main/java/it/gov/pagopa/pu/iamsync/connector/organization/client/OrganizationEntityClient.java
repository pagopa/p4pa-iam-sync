package it.gov.pagopa.pu.iamsync.connector.organization.client;

import it.gov.pagopa.pu.iamsync.connector.auth.AuthnService;
import it.gov.pagopa.pu.iamsync.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationRequestBody;
import org.springframework.stereotype.Service;

@Service
public class OrganizationEntityClient {

  private final OrganizationApisHolder organizationApisHolder;
  private final AuthnService authnService;

  public OrganizationEntityClient(OrganizationApisHolder organizationApisHolder,
    AuthnService authnService) {
    this.organizationApisHolder = organizationApisHolder;
    this.authnService = authnService;
  }

  public Organization updateOrganization(String organizationId, OrganizationRequestBody organizationRequestBody) {
    return organizationApisHolder.getOrganizationEntityControllerApi(authnService.getAccessToken())
      .crudUpdateOrganization(organizationId, organizationRequestBody);
  }
}
