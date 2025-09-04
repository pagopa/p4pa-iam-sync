package it.gov.pagopa.pu.iamsync.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import it.gov.pagopa.pu.auth.dto.generated.CreateOperatorRequest;
import it.gov.pagopa.pu.iamsync.event.users.dto.ScUsersNotificationDTO;
import it.gov.pagopa.pu.iamsync.event.users.dto.ScUsersNotificationDTO.ScUsersDTO;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ScUsersMapperTest {

  private final ScUsersMapper scUsersMapper = new ScUsersMapper();

  @Test
  void whenMapToCreateOperatorRequestThenReturnCorrectMapping() {
    ScUsersNotificationDTO scUsersEvent = new ScUsersNotificationDTO();
    ScUsersDTO user = new ScUsersDTO();
    user.setUserId(UUID.randomUUID().toString());
    user.setName("Name");
    user.setFamilyName("Surname");
    user.setEmail("test@email.it");
    user.setProductRole("ROLE_ADMIN");
    scUsersEvent.setUser(user);

    CreateOperatorRequest result = scUsersMapper.mapToCreateOperatorRequest(scUsersEvent);

    assertEquals(user.getUserId(), result.getExternalUserId());
    assertEquals(user.getName(), result.getFirstName());
    assertEquals(user.getFamilyName(), result.getLastName());
    assertEquals(user.getEmail(), result.getEmail());
    assertEquals(user.getProductRole(), result.getRoles().getFirst());
  }

  @Test
  void givenNullEventWhenMapToCreateOperatorRequestThenReturnNull() {
    assertNull(scUsersMapper.mapToCreateOperatorRequest(null));
  }
}
