package it.gov.pagopa.pu.iamsync.connector.auth.client;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import it.gov.pagopa.pu.auth.controller.generated.AuthzApi;
import it.gov.pagopa.pu.auth.dto.generated.CreateOperatorRequest;
import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.iamsync.connector.auth.config.AuthApisHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@ExtendWith(MockitoExtension.class)
class AuthzClientTest {

  @Mock
  private AuthApisHolder authApisHolderMock;
  @Mock
  private AuthzApi authzApiMock;

  private AuthzClient authzClient;

  @BeforeEach
  void setUp() {
    authzClient = new AuthzClient(authApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      authApisHolderMock
    );
  }

  @Test
  void whenGetOperatorInfoThenInvokeWithAccessToken() {
    // Given
    UserInfo expectedResult = new UserInfo();
    String accessToken = "accessToken";
    String externalUserId = "externalUserId";

    Mockito.when(authApisHolderMock.getAuthzApi(accessToken))
      .thenReturn(authzApiMock);
    Mockito.when(
        authzApiMock.getUserInfoFromMappedExternaUserId(externalUserId))
      .thenReturn(expectedResult);

    // When
    UserInfo result = authzClient.getOperatorInfo(externalUserId, accessToken);

    // Then
    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void givenNotExistentUsedWhenGetOperatorInfoThenNull() {
    // Given
    String accessToken = "accessToken";
    String externalUserId = "externalUserId";

    Mockito.when(authApisHolderMock.getAuthzApi(accessToken))
      .thenReturn(authzApiMock);
    Mockito.when(
        authzApiMock.getUserInfoFromMappedExternaUserId(externalUserId))
      .thenThrow(
        HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null,
          null, null));

    // When
    UserInfo result = authzClient.getOperatorInfo(externalUserId, accessToken);

    // Then
    Assertions.assertNull(result);
  }

  @Test
  void whenCreateOrganizationOperatorThenInvokeWithAccessToken() {
    String accessToken = "accessToken";
    OperatorDTO expected = new OperatorDTO();

    Mockito.when(authApisHolderMock.getAuthzApi(accessToken))
      .thenReturn(authzApiMock);
    Mockito.when(authzApiMock.createOrganizationOperator(anyString(),
      any(CreateOperatorRequest.class))).thenReturn(expected);

    OperatorDTO result = authzClient.createOrganizationOperator("ipaCode",
      new CreateOperatorRequest(), accessToken);

    Assertions.assertSame(expected, result);
  }

  @Test
  void whenDeleteOrganizationOperatorByExternalUserIdThenInvokeWithAccessToken() {
    String accessToken = "accessToken";
    String organizationIpaCode = "ipaCode";
    String externalUserId = "externalUserId";

    Mockito.when(authApisHolderMock.getAuthzApi(accessToken))
      .thenReturn(authzApiMock);

    authzClient.deleteOrganizationOperatorByExternalUserId(organizationIpaCode, externalUserId, accessToken);

    Mockito.verify(authzApiMock)
      .deleteOrganizationOperatorByExternalUserId(organizationIpaCode, externalUserId);
  }
}
