package it.gov.pagopa.pu.iamsync.event.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScUsersNotificationDTO {

  private String id;
  private String institutionId;
  private String productId;
  private String onboardingTokenId;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
  private String eventType;
  private ScUsersDTO user;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ScUsersDTO {
    private String userId;
    private String name;
    private String familyName;
    private String email;
    private String role;
    private String productRole;
    private String relationshipStatus;
  }
}
