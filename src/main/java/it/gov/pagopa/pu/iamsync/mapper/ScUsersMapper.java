package it.gov.pagopa.pu.iamsync.mapper;

import it.gov.pagopa.pu.auth.dto.generated.CreateOperatorRequest;
import it.gov.pagopa.pu.iamsync.event.users.dto.ScUsersNotificationDTO;
import it.gov.pagopa.pu.iamsync.event.users.dto.ScUsersNotificationDTO.ScUsersDTO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ScUsersMapper {

  public CreateOperatorRequest mapToCreateOperatorRequest(
    ScUsersNotificationDTO scUsersEvent) {
    if (scUsersEvent == null) {
      return null;
    }

    ScUsersDTO user = scUsersEvent.getUser();
    CreateOperatorRequest createOperatorRequest = new CreateOperatorRequest();

    createOperatorRequest.setExternalUserId(user.getUserId());
    createOperatorRequest.setRoles(List.of(user.getProductRole()));
    createOperatorRequest.setFirstName(user.getName());
    createOperatorRequest.setLastName(user.getFamilyName());
    createOperatorRequest.setEmail(user.getEmail());

    return createOperatorRequest;
  }
}
