package it.gov.pagopa.pu.iamsync.connector.organization.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.iamsync.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.organization.client.generated.OrganizationEntityControllerApi;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationRequestBody;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrganizationEntityClientTest {

  @InjectMocks
  private OrganizationEntityClient organizationEntityClient;

  @Mock
  private OrganizationApisHolder organizationApisHolderMock;

  @Mock
  private OrganizationEntityControllerApi organizationEntityControllerApiMock;

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      organizationApisHolderMock,
      organizationEntityControllerApiMock
    );
  }

  @Test
  void whenUpdateOrganizationThenInvokeWithAccessToken() {
    String accessToken = "accessToken";
    Organization expectedResult = new Organization();

    when(organizationApisHolderMock.getOrganizationEntityControllerApi(accessToken))
      .thenReturn(organizationEntityControllerApiMock);
    when(organizationEntityControllerApiMock.crudUpdateOrganization(anyString(), any(OrganizationRequestBody.class)))
      .thenReturn(expectedResult);

    Organization result = organizationEntityClient.updateOrganization("1", new OrganizationRequestBody(), accessToken);

    assertEquals(expectedResult, result);
  }
}
