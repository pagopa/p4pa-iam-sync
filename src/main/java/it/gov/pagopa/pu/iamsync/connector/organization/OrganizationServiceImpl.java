package it.gov.pagopa.pu.iamsync.connector.organization;

import it.gov.pagopa.pu.iamsync.connector.organization.client.OrganizationClient;
import it.gov.pagopa.pu.iamsync.connector.organization.client.OrganizationEntityClient;
import it.gov.pagopa.pu.iamsync.connector.organization.client.OrganizationSearchClient;
import it.gov.pagopa.pu.iamsync.connector.organization.mapper.OrganizationRequestMapper;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationCreateDTO;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class OrganizationServiceImpl implements OrganizationService {

  private final OrganizationClient organizationClient;
  private final OrganizationSearchClient organizationSearchClient;
  private final OrganizationEntityClient organizationEntityClient;
  private final OrganizationRequestMapper organizationRequestMapper;

  public OrganizationServiceImpl(OrganizationClient organizationClient,
    OrganizationSearchClient organizationSearchClient,
    OrganizationEntityClient organizationEntityClient,
    OrganizationRequestMapper organizationRequestMapper) {
    this.organizationClient = organizationClient;
    this.organizationSearchClient = organizationSearchClient;
    this.organizationEntityClient = organizationEntityClient;
    this.organizationRequestMapper = organizationRequestMapper;
  }

  @Override
  public void createOrganization(OrganizationCreateDTO organizationCreateDTO,
    String accessToken) {
    organizationClient.createOrganization(organizationCreateDTO, accessToken);
  }

  @Override
  public Optional<Organization> getOrganizationByIpaCode(String ipaCode,
    String accessToken) {
    return Optional.ofNullable(
      organizationSearchClient.findByIpaCode(ipaCode, accessToken)
    );
  }

  @Override
  public Organization updateOrganization(Organization organization,
    String accessToken) {
    return organizationEntityClient.updateOrganization(
      String.valueOf(organization.getOrganizationId()),
      organizationRequestMapper.map(organization),
      accessToken);
  }

  @Override
  public Optional<Organization> getOrganizationByExternalOrganizationId(
    String externalOrganizationId, String accessToken) {
    return Optional.ofNullable(
      organizationSearchClient.findByExternalOrganizationId(
        externalOrganizationId, accessToken));
  }
}
