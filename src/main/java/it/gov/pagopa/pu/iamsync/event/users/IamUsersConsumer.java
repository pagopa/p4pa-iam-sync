package it.gov.pagopa.pu.iamsync.event.users;

import static it.gov.pagopa.pu.iamsync.utils.Constants.PIATTAFORMA_UNITARIA_PRODUCT;

import it.gov.pagopa.pu.iamsync.enums.EventType;
import it.gov.pagopa.pu.iamsync.event.users.dto.ScUsersNotificationDTO;
import it.gov.pagopa.pu.iamsync.service.users.OperatorCreationHandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class IamUsersConsumer implements Consumer<ScUsersNotificationDTO> {

  private final OperatorCreationHandlerService operatorCreationHandlerService;

  @Override
  public void accept(ScUsersNotificationDTO scUsersNotificationEvent) {
    log.info("Received event on user {} of institutionId {}) and product {} of type {}",
      scUsersNotificationEvent.getUser().getUserId(),
      scUsersNotificationEvent.getInstitutionId(),
      scUsersNotificationEvent.getProductId(),
      scUsersNotificationEvent.getEventType()
    );

    log.info("relStatus: {}", scUsersNotificationEvent.getUser().getRelationshipStatus());

    if (!PIATTAFORMA_UNITARIA_PRODUCT.equals(scUsersNotificationEvent.getProductId())) {
      log.info("Discarding event due to not matching product");
      return;
    }

    if (EventType.ADD.name().equals(scUsersNotificationEvent.getEventType())) {
      operatorCreationHandlerService.createOrganizationOperator(scUsersNotificationEvent);
    }
  }

}
