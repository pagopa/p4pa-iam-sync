package it.gov.pagopa.pu.iamsync.connector.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import it.gov.pagopa.pu.auth.dto.generated.CreateOperatorRequest;
import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.iamsync.connector.auth.client.AuthzClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthzServiceTest {

  @Mock
  private AuthzClient authzClientMock;
  @Mock
  private AuthnService authnServiceMock;

  private AuthzService authzService;

  @BeforeEach
  void init() {
    authzService = new AuthzServiceImpl(authzClientMock, authnServiceMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      authzClientMock,
      authnServiceMock
    );
  }

  @Test
  void whenGetOperatorInfoThenObtainTokenAndCallAuthzClient() {
    String externalUserId = "externalUserId";
    String token = "TOKEN";
    UserInfo expectedResult = new UserInfo();

    Mockito.when(authnServiceMock.getAccessToken()).thenReturn(token);
    Mockito.when(authzClientMock.getOperatorInfo(externalUserId, token))
      .thenReturn(expectedResult);

    // When
    UserInfo result = authzService.getOperatorInfo(externalUserId);

    // Then
    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void whenCreateOrganizationOperatorThenCallAuthzClient() {
    OperatorDTO expected = new OperatorDTO();
    String accessToken = "accessToken";
    String organizationIpaCode = "ipaCode";

    Mockito.when(authnServiceMock.getAccessToken(organizationIpaCode)).thenReturn(accessToken);
    Mockito.when(authzClientMock.createOrganizationOperator(eq(organizationIpaCode), any(
        CreateOperatorRequest.class), eq(accessToken)))
      .thenReturn(expected);

    OperatorDTO result = authzService.createOrganizationOperator(organizationIpaCode,
      new CreateOperatorRequest());

    Assertions.assertSame(expected, result);
  }
}
