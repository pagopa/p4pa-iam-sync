package it.gov.pagopa.pu.iamsync.service.users;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.auth.dto.generated.CreateOperatorRequest;
import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.iamsync.connector.auth.AuthzService;
import it.gov.pagopa.pu.iamsync.connector.organization.OrganizationService;
import it.gov.pagopa.pu.iamsync.event.users.dto.ScUsersNotificationDTO;
import it.gov.pagopa.pu.iamsync.mapper.ScUsersMapper;
import it.gov.pagopa.pu.iamsync.utils.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import java.util.Optional;
import java.util.UUID;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OperatorCreationHandlerServiceTest {

  @Mock
  private AuthzService authzServiceMock;
  @Mock
  private OrganizationService organizationServiceMock;
  @Mock
  private ScUsersMapper scUsersMapperMock;

  private OperatorCreationHandlerService operatorCreationHandlerService;

  @BeforeEach
  void setup() {
    operatorCreationHandlerService = new OperatorCreationHandlerServiceImpl(
      authzServiceMock, organizationServiceMock, scUsersMapperMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(authzServiceMock, organizationServiceMock,
      scUsersMapperMock);
  }

  @Test
  void whenCreateOrganizationOperatorThenOk() {
    TestUtils.setFakeAccessTokenInContext();

    ScUsersNotificationDTO scUsersEvent = new ScUsersNotificationDTO();
    scUsersEvent.setInstitutionId(UUID.randomUUID().toString());

    CreateOperatorRequest createOperatorRequest = new CreateOperatorRequest();

    Organization organization = new Organization();
    organization.setIpaCode("ipaCode");

    when(organizationServiceMock.getOrganizationByExternalOrganizationId(
      scUsersEvent.getInstitutionId(),
      TestUtils.getFakeAccessToken())).thenReturn(Optional.of(organization));
    when(scUsersMapperMock.mapToCreateOperatorRequest(scUsersEvent)).thenReturn(
      createOperatorRequest);
    when(authzServiceMock.createOrganizationOperator(organization.getIpaCode(),
      createOperatorRequest)).thenReturn(new OperatorDTO());

    operatorCreationHandlerService.createOrganizationOperator(scUsersEvent);

    Mockito.verifyNoMoreInteractions(authzServiceMock, organizationServiceMock,
      scUsersMapperMock);
  }


  @Test
  void givenMissingOrganizationWhenCreateOrganizationOperatorThenThrowException() {
    TestUtils.setFakeAccessTokenInContext();

    ScUsersNotificationDTO scUsersEvent = new ScUsersNotificationDTO();
    scUsersEvent.setInstitutionId(UUID.randomUUID().toString());

    when(organizationServiceMock.getOrganizationByExternalOrganizationId(
      scUsersEvent.getInstitutionId(),
      TestUtils.getFakeAccessToken())).thenReturn(Optional.empty());

    Executable exec = () -> operatorCreationHandlerService.createOrganizationOperator(scUsersEvent);

    assertThrows(ResourceNotFoundException.class, exec);
    Mockito.verifyNoInteractions(scUsersMapperMock, authzServiceMock);
  }

}
