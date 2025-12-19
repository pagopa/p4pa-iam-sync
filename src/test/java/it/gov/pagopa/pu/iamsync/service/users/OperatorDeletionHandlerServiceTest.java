package it.gov.pagopa.pu.iamsync.service.users;


import it.gov.pagopa.pu.iamsync.connector.auth.AuthzService;
import it.gov.pagopa.pu.iamsync.connector.organization.OrganizationService;
import it.gov.pagopa.pu.iamsync.event.users.dto.ScUsersNotificationDTO;
import it.gov.pagopa.pu.iamsync.event.users.dto.ScUsersNotificationDTO.ScUsersDTO;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperatorDeletionHandlerServiceTest {
  @Mock
  private AuthzService authzServiceMock;
  @Mock
  private OrganizationService organizationServiceMock;

  private OperatorDeletionHandlerService operatorDeletionHandlerService;

  @BeforeEach
  void setup() {
    operatorDeletionHandlerService = new OperatorDeletionHandlerServiceImpl(
      authzServiceMock,
      organizationServiceMock
    );
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(authzServiceMock, organizationServiceMock);
  }

  @Test
  void whenDeleteOrganizationOperatorThenOk() {
    ScUsersNotificationDTO scUsersEvent = new ScUsersNotificationDTO();
    scUsersEvent.setInstitutionId(UUID.randomUUID().toString());
    ScUsersDTO user = new ScUsersDTO();
    user.setUserId("userId");
    scUsersEvent.setUser(user);
    Organization organization = new Organization();
    organization.setIpaCode("ipaCode");

    when(organizationServiceMock.getOrganizationByExternalOrganizationId(
      scUsersEvent.getInstitutionId())).thenReturn(Optional.of(organization));

    operatorDeletionHandlerService.deleteOrganizationOperator(scUsersEvent);

    Mockito.verify(authzServiceMock).deleteOrganizationOperatorByExternalUserId(
      "ipaCode",
      user.getUserId()
    );

    Mockito.verifyNoMoreInteractions(organizationServiceMock);
  }

  @Test
  void givenMissingOrganizationWhenDeleteOrganizationOperatorThenThrowException() {
    ScUsersNotificationDTO scUsersEvent = new ScUsersNotificationDTO();
    scUsersEvent.setInstitutionId(UUID.randomUUID().toString());

    when(organizationServiceMock.getOrganizationByExternalOrganizationId(
      scUsersEvent.getInstitutionId())).thenReturn(Optional.empty());

    Executable exec = () -> operatorDeletionHandlerService.deleteOrganizationOperator(scUsersEvent);

    assertThrows(ResourceNotFoundException.class, exec);
    Mockito.verifyNoInteractions(authzServiceMock);
  }
}
