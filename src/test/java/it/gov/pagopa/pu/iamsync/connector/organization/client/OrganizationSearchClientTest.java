package it.gov.pagopa.pu.iamsync.connector.organization.client;

import it.gov.pagopa.pu.iamsync.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.organization.client.generated.OrganizationSearchControllerApi;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@ExtendWith(MockitoExtension.class)
class OrganizationSearchClientTest {

  @InjectMocks
  private OrganizationSearchClient organizationSearchClient;

  @Mock
  private OrganizationApisHolder organizationApisHolderMock;

  @Mock
  private OrganizationSearchControllerApi organizationSearchControllerApiMock;

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      organizationApisHolderMock,
      organizationSearchControllerApiMock
    );
  }

  @Test
  void whenFindByIpaCodeThenInvokeWithAccessToken() {
    String accessToken = "accessToken";
    String orgIpaCode = "ipaCode";
    Organization expectedResult = new Organization();

    Mockito.when(organizationApisHolderMock.getOrganizationSearchControllerApi(accessToken))
      .thenReturn(organizationSearchControllerApiMock);
    Mockito.when(organizationSearchControllerApiMock.crudOrganizationsFindByIpaCode(orgIpaCode))
      .thenReturn(expectedResult);

    Organization result = organizationSearchClient.findByIpaCode(orgIpaCode, accessToken);

    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void givenNotExistentIpaCodeWhenFindByIpaCodeThenNull() {
    String accessToken = "accessToken";
    String orgIpaCode = "ipaCode";

    Mockito.when(organizationApisHolderMock.getOrganizationSearchControllerApi(accessToken))
      .thenReturn(organizationSearchControllerApiMock);
    Mockito.when(organizationSearchControllerApiMock.crudOrganizationsFindByIpaCode(orgIpaCode))
      .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    // When
    Organization result = organizationSearchClient.findByIpaCode(orgIpaCode, accessToken);

    // Then
    Assertions.assertNull(result);
  }

}
