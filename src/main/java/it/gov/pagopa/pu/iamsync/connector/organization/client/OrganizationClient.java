package it.gov.pagopa.pu.iamsync.connector.organization.client;

import it.gov.pagopa.pu.iamsync.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationCreateDTO;
import org.springframework.stereotype.Service;

@Service
public class OrganizationClient {

  private final OrganizationApisHolder organizationApisHolder;

  public OrganizationClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }

  public void createOrganization(OrganizationCreateDTO organizationCreateDTO, String accessToken) {
    organizationApisHolder.getOrganizationApi(accessToken)
      .createOrganization(organizationCreateDTO);
  }
}
