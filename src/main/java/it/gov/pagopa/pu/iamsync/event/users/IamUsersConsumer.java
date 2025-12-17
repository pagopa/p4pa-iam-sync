package it.gov.pagopa.pu.iamsync.event.users;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.iamsync.connector.auth.AuthzServiceImpl;
import it.gov.pagopa.pu.iamsync.enums.EventType;
import it.gov.pagopa.pu.iamsync.event.users.dto.ScUsersNotificationDTO;
import it.gov.pagopa.pu.iamsync.service.users.OperatorCreationHandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

import static it.gov.pagopa.pu.iamsync.utils.Constants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class IamUsersConsumer implements Consumer<ScUsersNotificationDTO> {

  private final OperatorCreationHandlerService operatorCreationHandlerService;
  private final AuthzServiceImpl authzServiceImpl;

  @Override
  public void accept(ScUsersNotificationDTO scUsersNotificationEvent) {
    log.info("Received event on user {} of institutionId {}, product {} of type {} and relationshipStatus {}",
      scUsersNotificationEvent.getUser().getUserId(),
      scUsersNotificationEvent.getInstitutionId(),
      scUsersNotificationEvent.getProductId(),
      scUsersNotificationEvent.getEventType(),
      scUsersNotificationEvent.getUser().getRelationshipStatus()
    );

    if (!PIATTAFORMA_UNITARIA_PRODUCT.equals(scUsersNotificationEvent.getProductId())) {
      log.info("Discarding event due to not matching product");
      return;
    }

    String eventType = scUsersNotificationEvent.getEventType();
    String relationshipStatus = scUsersNotificationEvent.getUser().getRelationshipStatus();

    UserInfo userInfo = authzServiceImpl.getOperatorInfo(scUsersNotificationEvent.getUser().getUserId());

    // TODO: remove following log, it's only for test purpose
    log.info("userId: {}", userInfo != null ? userInfo.getUserId() : null);

    if (userInfo == null && (
      EventType.ADD.name().equals(eventType) || (EventType.UPDATE.name().equals(eventType) && SC_USER_ACTIVE_RELATIONSHIP_STATUS.equals(relationshipStatus)))) {
      operatorCreationHandlerService.createOrganizationOperator(scUsersNotificationEvent);
      return;
    }

    if (EventType.UPDATE.name().equals(eventType) &&
      SC_USER_DELETED_RELATIONSHIP_STATUS.equals(relationshipStatus)) {
      // TODO: delete operator
    }
  }

}
