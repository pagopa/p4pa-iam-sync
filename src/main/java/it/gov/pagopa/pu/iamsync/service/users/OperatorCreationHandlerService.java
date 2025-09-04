package it.gov.pagopa.pu.iamsync.service.users;

import it.gov.pagopa.pu.iamsync.event.users.dto.ScUsersNotificationDTO;

public interface OperatorCreationHandlerService {
  void createOrganizationOperator(ScUsersNotificationDTO scUsersEvent);
}
