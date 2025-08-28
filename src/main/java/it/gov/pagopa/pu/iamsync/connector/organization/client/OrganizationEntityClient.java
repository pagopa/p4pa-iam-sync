package it.gov.pagopa.pu.iamsync.connector.organization.client;

import it.gov.pagopa.pu.iamsync.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationRequestBody;
import org.springframework.stereotype.Service;

@Service
public class OrganizationEntityClient {

  private final OrganizationApisHolder organizationApisHolder;

  public OrganizationEntityClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }

  public Organization updateOrganization(String organizationId, OrganizationRequestBody organizationRequestBody, String accessToken) {
    return organizationApisHolder.getOrganizationEntityControllerApi(accessToken)
      .crudUpdateOrganization(organizationId, organizationRequestBody);
  }
}
