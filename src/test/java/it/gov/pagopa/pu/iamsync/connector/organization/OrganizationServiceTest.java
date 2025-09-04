package it.gov.pagopa.pu.iamsync.connector.organization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.iamsync.connector.organization.client.OrganizationClient;
import it.gov.pagopa.pu.iamsync.connector.organization.client.OrganizationEntityClient;
import it.gov.pagopa.pu.iamsync.connector.organization.client.OrganizationSearchClient;
import it.gov.pagopa.pu.iamsync.connector.organization.mapper.OrganizationRequestMapper;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationCreateDTO;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationRequestBody;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {

  @Mock
  private OrganizationClient organizationClientMock;
  @Mock
  private OrganizationSearchClient organizationSearchClientMock;
  @Mock
  private OrganizationEntityClient organizationEntityClientMock;
  @Mock
  private OrganizationRequestMapper organizationRequestMapperMock;

  private OrganizationService organizationService;

  @BeforeEach
  void setup() {
    organizationService = new OrganizationServiceImpl(organizationClientMock,
      organizationSearchClientMock, organizationEntityClientMock,
      organizationRequestMapperMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      organizationClientMock,
      organizationSearchClientMock,
      organizationEntityClientMock,
      organizationRequestMapperMock
    );
  }

  @Test
  void whenCreateOrganizationThenInvokeClient() {
    doNothing().when(organizationClientMock)
      .createOrganization(any(OrganizationCreateDTO.class));

    organizationService.createOrganization(new OrganizationCreateDTO());

    Mockito.verifyNoMoreInteractions(organizationClientMock);
    Mockito.verifyNoInteractions(organizationSearchClientMock,
      organizationEntityClientMock, organizationRequestMapperMock);
  }

  @Test
  void givenPresentOrganizationWhenGetOrganizationByIpaCodeThenReturnOrganization() {
    Organization expectedResult = new Organization();

    when(organizationSearchClientMock.findByIpaCode(anyString()))
      .thenReturn(expectedResult);

    Optional<Organization> result = organizationService.getOrganizationByIpaCode(
      "ipaCode");

    assertTrue(result.isPresent());
    assertEquals(expectedResult, result.get());
  }

  @Test
  void givenMissingOrganizationWhenGetOrganizationByIpaCodeThenReturnOptionalEmpty() {
    when(organizationSearchClientMock.findByIpaCode(anyString()))
      .thenReturn(null);

    Optional<Organization> result = organizationService.getOrganizationByIpaCode(
      "ipaCode");

    assertTrue(result.isEmpty());
  }

  @Test
  void whenUpdateOrganizationThenInvokeClient() {
    Organization expectedResult = new Organization();

    when(organizationRequestMapperMock.map(expectedResult)).thenReturn(
      new OrganizationRequestBody());
    when(organizationEntityClientMock.updateOrganization(anyString(),
      any(OrganizationRequestBody.class)))
      .thenReturn(expectedResult);

    Organization result = organizationService.updateOrganization(
      expectedResult);

    assertEquals(expectedResult, result);
  }

  @Test
  void givenPresentOrganizationWhenGetOrganizationByExternalOrganizationIdThenReturnOrganization() {
    Organization expectedResult = new Organization();

    when(organizationSearchClientMock.findByExternalOrganizationId(anyString()))
      .thenReturn(expectedResult);

    Optional<Organization> result = organizationService.getOrganizationByExternalOrganizationId(
      "externalOrganizationId");

    assertTrue(result.isPresent());
    assertEquals(expectedResult, result.get());
  }

  @Test
  void givenMissingOrganizationWhenGetOrganizationByExternalOrganizationIdThenReturnOptionalEmpty() {
    when(organizationSearchClientMock.findByExternalOrganizationId(anyString()))
      .thenReturn(null);

    Optional<Organization> result = organizationService.getOrganizationByExternalOrganizationId(
      "externalOrganizationId");

    assertTrue(result.isEmpty());
  }

}
