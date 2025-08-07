package it.gov.pagopa.pu.iamsync.event.organizations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScContractDTO {
  private String id;
  private String institutionId;
  private String product;
  private String state;
  private String onboardingTokenId;
  private String contentType;
  private String fileName;
  private String filePath;
  private OffsetDateTime createdAt;
  private OffsetDateTime closedAt;
  private OffsetDateTime updatedAt;
  private String notificationType;
  private String pricingPlan;
  private ScBillingDTO billing;
  private ScInstitutionDTO institution;
  private Boolean isAggregator;
  private ScRootAggregatorDTO rootAggregator;
  private Boolean testInstitution;
  private String type;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ScBillingDTO {
    private String recipientCode;
    private String vatNumber;
    private Boolean publicServices;
    private String taxCodeInvoicing;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ScRootAggregatorDTO {
    private String institutionId;
    private String originId;
    private String description;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ScInstitutionDTO {
    private String address;
    private String description;
    private String digitalAddress;
    private String institutionType;
    private String origin;
    private String originId;
    private String zipCode;
    private String taxCode;
    private String istatCode;
    private String subUnitCode;
    private String subUnitType;
    private String county;
    private String country;
    private String city;
    private String category;
    private ScInstitutionParentDTO rootParent;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ScInstitutionParentDTO {
    private String id;
    private String originId;
    private String description;
  }

}
