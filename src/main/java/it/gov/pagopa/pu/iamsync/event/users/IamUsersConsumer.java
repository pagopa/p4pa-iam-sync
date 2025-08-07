package it.gov.pagopa.pu.iamsync.event.users;

import it.gov.pagopa.pu.iamsync.event.users.dto.ScUsersNotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class IamUsersConsumer implements Consumer<ScUsersNotificationDTO> {

  @Override
  public void accept(ScUsersNotificationDTO scUsersNotificationEvent) {
    log.info("Received event on user {} of institutionId {}) and product {} of type {}",
      scUsersNotificationEvent.getUser().getUserId(),
      scUsersNotificationEvent.getInstitutionId(),
      scUsersNotificationEvent.getProductId(),
      scUsersNotificationEvent.getEventType()
    );
  }

}
