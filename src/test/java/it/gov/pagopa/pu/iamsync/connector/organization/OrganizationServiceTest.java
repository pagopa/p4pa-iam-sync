package it.gov.pagopa.pu.iamsync.connector.organization;

import it.gov.pagopa.pu.iamsync.connector.organization.client.OrganizationClient;
import it.gov.pagopa.pu.iamsync.connector.organization.client.OrganizationSearchClient;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationCreateDTO;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {

  @Mock
  private OrganizationClient organizationClientMock;
  @Mock
  private OrganizationSearchClient organizationSearchClientMock;

  private OrganizationService organizationService;

  @BeforeEach
  void setup() {
    organizationService = new OrganizationServiceImpl(organizationClientMock, organizationSearchClientMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      organizationClientMock,
      organizationSearchClientMock
    );
  }

  @Test
  void whenCreateOrganizationThenInvokeClient() {

    OrganizationCreateDTO organizationCreateDTO = new OrganizationCreateDTO();

    organizationService.createOrganization(organizationCreateDTO);

    verify(organizationClientMock)
      .createOrganization(same(organizationCreateDTO));
  }

  @Test
  void givenPresentOrganizationWhenGetOrganizationByIpaCodeThenReturnOrganization() {
    String ipaCode = "ipaCode";
    Organization expectedResult = new Organization();

    when(organizationSearchClientMock.findByIpaCode(ipaCode))
      .thenReturn(expectedResult);

    Optional<Organization> result = organizationService.getOrganizationByIpaCode(ipaCode);

    assertTrue(result.isPresent());
    assertEquals(expectedResult, result.get());
  }

  @Test
  void givenMissingOrganizationWhenGetOrganizationByIpaCodeThenReturnOptionalEmpty() {
    when(organizationSearchClientMock.findByIpaCode(anyString()))
      .thenReturn(null);

    Optional<Organization> result = organizationService.getOrganizationByIpaCode("ipaCode");

    assertTrue(result.isEmpty());
  }

  @Test
  void whenUpdateOrganizationStatusThenInvokeClient() {
    Long organizationId = 1L;
    OrganizationStatus newStatus = OrganizationStatus.ACTIVE;


    organizationService.updateOrganizationStatus(organizationId, newStatus);

    verify(organizationClientMock).updateOrganizationStatus(organizationId, newStatus);
  }

  @Test
  void givenPresentOrganizationWhenGetOrganizationByExternalOrganizationIdThenReturnOrganization() {
    String externalOrganizationId = "externalOrganizationId";
    Organization expectedResult = new Organization();

    when(organizationSearchClientMock.findByExternalOrganizationId(externalOrganizationId))
      .thenReturn(expectedResult);

    Optional<Organization> result = organizationService.getOrganizationByExternalOrganizationId(externalOrganizationId);

    assertTrue(result.isPresent());
    assertEquals(expectedResult, result.get());
  }

  @Test
  void givenMissingOrganizationWhenGetOrganizationByExternalOrganizationIdThenReturnOptionalEmpty() {
    String externalOrganizationId = "externalOrganizationId";

    when(organizationSearchClientMock.findByExternalOrganizationId(externalOrganizationId))
      .thenReturn(null);

    Optional<Organization> result = organizationService.getOrganizationByExternalOrganizationId(externalOrganizationId);

    assertTrue(result.isEmpty());
  }

}
