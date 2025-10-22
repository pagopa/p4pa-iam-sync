package it.gov.pagopa.pu.iamsync.connector.organization.client;

import it.gov.pagopa.pu.iamsync.connector.auth.AuthnService;
import it.gov.pagopa.pu.iamsync.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.organization.client.generated.OrganizationApi;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationCreateDTO;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationClientTest {

  @InjectMocks
  private OrganizationClient organizationClient;

  @Mock
  private OrganizationApisHolder organizationApisHolderMock;
  @Mock
  private OrganizationApi organizationApiMock;
  @Mock
  private AuthnService authnServiceMock;

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      organizationApisHolderMock,
      organizationApiMock,
      authnServiceMock
    );
  }

  @Test
  void whenCreateOrganizationThenInvokeWithAccessToken() {
    String accessToken = "accessToken";
    OrganizationCreateDTO organizationCreateDTO = new OrganizationCreateDTO();

    when(authnServiceMock.getAccessToken())
      .thenReturn(accessToken);
    when(organizationApisHolderMock.getOrganizationApi(accessToken))
      .thenReturn(organizationApiMock);

    organizationClient.createOrganization(organizationCreateDTO);

    Mockito.verify(organizationApiMock).createOrganization(same(organizationCreateDTO));
  }

  @Test
  void whenUpdateOrganizationStatusThenInvokeWithAccessToken() {
    String accessToken = "accessToken";
    Long organizationId = 1L;
    OrganizationStatus newStatus = OrganizationStatus.ACTIVE;

    when(authnServiceMock.getAccessToken())
      .thenReturn(accessToken);
    when(organizationApisHolderMock.getOrganizationApi(accessToken))
      .thenReturn(organizationApiMock);

    organizationClient.updateOrganizationStatus(organizationId, newStatus);

    Mockito.verify(organizationApiMock).updateOrganizationStatus(organizationId, newStatus);
  }
}
