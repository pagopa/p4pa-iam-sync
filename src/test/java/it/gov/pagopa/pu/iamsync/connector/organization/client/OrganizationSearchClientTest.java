package it.gov.pagopa.pu.iamsync.connector.organization.client;

import it.gov.pagopa.pu.iamsync.connector.auth.AuthnService;
import it.gov.pagopa.pu.iamsync.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.iamsync.exception.common.RestInvokeNotFoundException;
import it.gov.pagopa.pu.organization.client.generated.OrganizationSearchControllerApi;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationSearchClientTest {

  private static final String TOKEN = "TOKEN";

  @InjectMocks
  private OrganizationSearchClient organizationSearchClient;

  @Mock
  private AuthnService authnServiceMock;
  @Mock
  private OrganizationApisHolder organizationApisHolderMock;
  @Mock
  private OrganizationSearchControllerApi organizationSearchControllerApiMock;

  @BeforeEach
  void setup() {
    when(authnServiceMock.getAccessToken()).thenReturn(TOKEN);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      organizationApisHolderMock,
      organizationSearchControllerApiMock,
      authnServiceMock
    );
  }

  @Test
  void whenFindByIpaCodeThenInvokeWithAccessToken() {
    String orgIpaCode = "ipaCode";
    Organization expectedResult = new Organization();

    when(organizationApisHolderMock.getOrganizationSearchControllerApi(
      TOKEN))
      .thenReturn(organizationSearchControllerApiMock);
    when(
      organizationSearchControllerApiMock.crudOrganizationsFindByIpaCode(
        orgIpaCode))
      .thenReturn(expectedResult);

    Organization result = organizationSearchClient.findByIpaCode(orgIpaCode);

    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void givenNotExistentIpaCodeWhenFindByIpaCodeThenNull() {
    String orgIpaCode = "ipaCode";

    when(organizationApisHolderMock.getOrganizationSearchControllerApi(
      TOKEN))
      .thenReturn(organizationSearchControllerApiMock);
    when(
      organizationSearchControllerApiMock.crudOrganizationsFindByIpaCode(
        orgIpaCode))
      .thenThrow(new RestInvokeNotFoundException("APPNAME", HttpStatus.NOT_FOUND, "ERROR", "ERRORCODE", "ERRORMESSAGE"));

    // When
    Organization result = organizationSearchClient.findByIpaCode(orgIpaCode);

    // Then
    Assertions.assertNull(result);
  }


  @Test
  void whenFindByExternalOrganizationIdThenInvokeWithAccessToken() {
    String externalOrganizationId = "externalOrganizationId";
    Organization expectedResult = new Organization();

    when(organizationApisHolderMock.getOrganizationSearchControllerApi(
      TOKEN))
      .thenReturn(organizationSearchControllerApiMock);
    when(
      organizationSearchControllerApiMock.crudOrganizationsFindByExternalOrganizationId(
        externalOrganizationId))
      .thenReturn(expectedResult);

    Organization result = organizationSearchClient.findByExternalOrganizationId(
      externalOrganizationId);

    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void givenNotExistentExternalOrganizationIdWhenFindByExternalOrganizationIdThenNull() {
    String externalOrganizationId = "externalOrganizationId";

    when(organizationApisHolderMock.getOrganizationSearchControllerApi(
      TOKEN))
      .thenReturn(organizationSearchControllerApiMock);
    when(
      organizationSearchControllerApiMock.crudOrganizationsFindByExternalOrganizationId(
        externalOrganizationId))
      .thenThrow(new RestInvokeNotFoundException("APPNAME", HttpStatus.NOT_FOUND, "ERROR", "ERRORCODE", "ERRORMESSAGE"));

    // When
    Organization result = organizationSearchClient.findByExternalOrganizationId(
      externalOrganizationId);

    // Then
    Assertions.assertNull(result);
  }

}
