package it.gov.pagopa.pu.iamsync.connector.organization;

import it.gov.pagopa.pu.iamsync.connector.organization.client.OrganizationClient;
import it.gov.pagopa.pu.iamsync.connector.organization.client.OrganizationSearchClient;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationCreateDTO;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrganizationServiceImpl implements OrganizationService {

  private final OrganizationClient organizationClient;
  private final OrganizationSearchClient organizationSearchClient;

  public OrganizationServiceImpl(OrganizationClient organizationClient,
    OrganizationSearchClient organizationSearchClient) {
    this.organizationClient = organizationClient;
    this.organizationSearchClient = organizationSearchClient;
  }

  @Override
  public void createOrganization(OrganizationCreateDTO organizationCreateDTO) {
    organizationClient.createOrganization(organizationCreateDTO);
  }

  @Override
  public Optional<Organization> getOrganizationByIpaCode(String ipaCode) {
    return Optional.ofNullable(
      organizationSearchClient.findByIpaCode(ipaCode)
    );
  }

  @Override
  public void updateOrganizationStatus(Long organizationId, OrganizationStatus newStatus) {
    organizationClient.updateOrganizationStatus(organizationId, newStatus);
  }

  @Override
  public Optional<Organization> getOrganizationByExternalOrganizationId(
    String externalOrganizationId) {
    return Optional.ofNullable(
      organizationSearchClient.findByExternalOrganizationId(
        externalOrganizationId));
  }
}
