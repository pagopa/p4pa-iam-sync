package it.gov.pagopa.pu.iamsync.connector.organization.client;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.iamsync.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.organization.client.generated.OrganizationApi;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationCreateDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrganizationClientTest {

  @InjectMocks
  private OrganizationClient organizationClient;

  @Mock
  private OrganizationApisHolder organizationApisHolderMock;

  @Mock
  private OrganizationApi organizationApiMock;

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      organizationApisHolderMock,
      organizationApiMock
    );
  }

  @Test
  void whenCreateOrganizationThenInvokeWithAccessToken() {
    String accessToken = "accessToken";

    when(organizationApisHolderMock.getOrganizationApi(accessToken))
      .thenReturn(organizationApiMock);
    doNothing().when(organizationApiMock).createOrganization(any(OrganizationCreateDTO.class));

    organizationClient.createOrganization(new OrganizationCreateDTO(), accessToken);

    Mockito.verifyNoMoreInteractions(organizationApisHolderMock, organizationApiMock);
  }
}
