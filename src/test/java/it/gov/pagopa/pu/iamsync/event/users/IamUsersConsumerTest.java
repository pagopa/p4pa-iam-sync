package it.gov.pagopa.pu.iamsync.event.users;

import static it.gov.pagopa.pu.iamsync.utils.Constants.PIATTAFORMA_UNITARIA_PRODUCT;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import it.gov.pagopa.pu.iamsync.connector.auth.AuthzServiceImpl;
import it.gov.pagopa.pu.iamsync.event.users.dto.ScUsersNotificationDTO;
import it.gov.pagopa.pu.iamsync.event.users.dto.ScUsersNotificationDTO.ScUsersDTO;
import it.gov.pagopa.pu.iamsync.service.users.OperatorCreationHandlerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IamUsersConsumerTest {

  @Mock
  private OperatorCreationHandlerService operatorCreationHandlerServiceMock;
  @Mock
  private AuthzServiceImpl authzServiceImplMock;

  private IamUsersConsumer iamUsersConsumer;

  @BeforeEach
  void setup() {
    iamUsersConsumer = new IamUsersConsumer(operatorCreationHandlerServiceMock, authzServiceImplMock);
  }

  @AfterEach
  void verifyNoMoreInteraction() {
    Mockito.verifyNoMoreInteractions(
      operatorCreationHandlerServiceMock
    );
  }

  @Test
  void givenProductIsNotPiattaformaUnitariaWhenAcceptThenDiscardMessage() {
    ScUsersNotificationDTO scUsersEvent = buildBaseScUsersEvent();
    scUsersEvent.setProductId("product");

    iamUsersConsumer.accept(scUsersEvent);

    verifyNoInteractions(operatorCreationHandlerServiceMock);
  }

  @Test
  void givenEventTypeUpdateWhenAcceptThenUpdateOperator() {
    ScUsersNotificationDTO scUsersEvent = buildBaseScUsersEvent();
    scUsersEvent.setEventType("UPDATE");

    iamUsersConsumer.accept(scUsersEvent);

    verifyNoInteractions(operatorCreationHandlerServiceMock);
  }

  @Test
  void givenValidScUsersEventWhenAcceptThenHandleCreateOperator() {
    ScUsersNotificationDTO scUsersEvent = buildBaseScUsersEvent();

    doNothing().when(operatorCreationHandlerServiceMock)
      .createOrganizationOperator(scUsersEvent);

    iamUsersConsumer.accept(scUsersEvent);

    verify(operatorCreationHandlerServiceMock).createOrganizationOperator(scUsersEvent);
  }

  private ScUsersNotificationDTO buildBaseScUsersEvent() {
    ScUsersNotificationDTO scUsersEvent = new ScUsersNotificationDTO();
    scUsersEvent.setProductId(PIATTAFORMA_UNITARIA_PRODUCT);
    scUsersEvent.setEventType("ADD");
    scUsersEvent.setInstitutionId("institutionId");
    ScUsersDTO user = new ScUsersDTO();
    user.setUserId("userId");
    user.setName("name");
    user.setFamilyName("surname");
    user.setEmail("test@email.it");
    user.setProductRole("ROLE_ADMIN");
    scUsersEvent.setUser(user);

    return scUsersEvent;
  }


}
