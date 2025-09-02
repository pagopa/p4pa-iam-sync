package it.gov.pagopa.pu.iamsync.service.organizations;

import it.gov.pagopa.pu.iamsync.connector.organization.OrganizationService;
import it.gov.pagopa.pu.iamsync.event.organizations.dto.ScContractDTO;
import it.gov.pagopa.pu.iamsync.mapper.ScContractMapper;
import it.gov.pagopa.pu.iamsync.utils.SecurityUtils;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationCreationHandlerServiceImpl implements OrganizationCreationHandlerService {

  private final OrganizationService organizationService;
  private final ScContractMapper scContractMapper;

  @Override
  public void createOrganization(ScContractDTO scContractEvent) {
    String accessToken = SecurityUtils.getAccessToken();
    if (OrganizationStatus.ACTIVE.getValue()
      .equals(scContractEvent.getState())) {
      organizationService.getOrganizationByIpaCode(
          scContractEvent.getInstitution().getOriginId(), accessToken)
        .ifPresentOrElse(
          organization -> {
            if (OrganizationStatus.CANCELLED.equals(organization.getStatus())) {
              organization.setStatus(OrganizationStatus.DRAFT);
              organizationService.updateOrganization(organization, accessToken);
            }
          },
          () -> organizationService.createOrganization(
            scContractMapper.mapToOrganizationCreateDTO(scContractEvent,
              OrganizationStatus.DRAFT), accessToken)
        );
    }

    if (OrganizationStatus.CANCELLED.getValue().equals(scContractEvent.getState())) {
      organizationService.getOrganizationByIpaCode(scContractEvent.getInstitution().getOriginId(), accessToken)
        .ifPresent(organization -> {
              organization.setStatus(OrganizationStatus.CANCELLED);
              organizationService.updateOrganization(organization, accessToken);
        });
    }
  }
}
