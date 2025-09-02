package it.gov.pagopa.pu.iamsync.connector.organization.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.iamsync.connector.organization.mapper.OrganizationRequestMapper;
import it.gov.pagopa.pu.iamsync.connector.organization.OrganizationService;
import it.gov.pagopa.pu.iamsync.connector.organization.OrganizationServiceImpl;
import it.gov.pagopa.pu.iamsync.connector.organization.client.OrganizationClient;
import it.gov.pagopa.pu.iamsync.connector.organization.client.OrganizationEntityClient;
import it.gov.pagopa.pu.iamsync.connector.organization.client.OrganizationSearchClient;
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
    organizationService = new OrganizationServiceImpl(organizationClientMock, organizationSearchClientMock, organizationEntityClientMock, organizationRequestMapperMock);
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
    String accessToken = "accessToken";

    doNothing().when(organizationClientMock).createOrganization(any(OrganizationCreateDTO.class), eq(accessToken));

    organizationService.createOrganization(new OrganizationCreateDTO(), accessToken);

    Mockito.verifyNoMoreInteractions(organizationClientMock);
    Mockito.verifyNoInteractions(organizationSearchClientMock, organizationEntityClientMock, organizationRequestMapperMock);
  }

  @Test
  void givenPresentOrganizationWhenGetOrganizationByIpaCodeThenReturnOrganization() {
    String accessToken = "accessToken";
    Organization expectedResult = new Organization();

    when(organizationSearchClientMock.findByIpaCode(anyString(), eq(accessToken)))
      .thenReturn(expectedResult);

    Optional<Organization> result = organizationService.getOrganizationByIpaCode("ipaCode", accessToken);

    assertTrue(result.isPresent());
    assertEquals(expectedResult, result.get());
  }

  @Test
  void givenMissingOrganizationWhenGetOrganizationByIpaCodeThenReturnOptionalEmpty() {
    String accessToken = "accessToken";

    when(organizationSearchClientMock.findByIpaCode(anyString(), eq(accessToken)))
      .thenReturn(null);

    Optional<Organization> result = organizationService.getOrganizationByIpaCode("ipaCode", accessToken);

    assertTrue(result.isEmpty());
  }

  @Test
  void whenUpdateOrganizationThenInvokeClient() {
    String accessToken = "accessToken";
    Organization expectedResult = new Organization();

    when(organizationRequestMapperMock.map(expectedResult)).thenReturn(new OrganizationRequestBody());
    when(organizationEntityClientMock.updateOrganization(anyString(), any(OrganizationRequestBody.class), eq(accessToken)))
      .thenReturn(expectedResult);

    Organization result = organizationService.updateOrganization(expectedResult, accessToken);

    assertEquals(expectedResult, result);
  }

}
