package it.gov.pagopa.pu.iamsync.connector.organization.client;

import it.gov.pagopa.pu.iamsync.connector.auth.AuthnService;
import it.gov.pagopa.pu.iamsync.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@Service
public class OrganizationSearchClient {

  private final OrganizationApisHolder organizationApisHolder;
  private final AuthnService authnService;

  public OrganizationSearchClient(OrganizationApisHolder organizationApisHolder,
    AuthnService authnService) {
    this.organizationApisHolder = organizationApisHolder;
    this.authnService = authnService;
  }

  public Organization findByIpaCode(String ipaCode) {
    try {
      return organizationApisHolder.getOrganizationSearchControllerApi(
          authnService.getAccessToken())
        .crudOrganizationsFindByIpaCode(ipaCode);
    } catch (HttpClientErrorException.NotFound e) {
      log.info("Cannot find organization having ipaCode {}", ipaCode);
      return null;
    }
  }

  public Organization findByExternalOrganizationId(
    String externalOrganizationId) {
    try {
      return organizationApisHolder.getOrganizationSearchControllerApi(
          authnService.getAccessToken())
        .crudOrganizationsFindByExternalOrganizationId(externalOrganizationId);
    } catch (HttpClientErrorException.NotFound e) {
      log.info("Cannot find organization having externalOrganizationId {}",
        externalOrganizationId);
      return null;
    }
  }
}
