package it.gov.pagopa.pu.iamsync.service.organizations;

import it.gov.pagopa.pu.iamsync.connector.organization.OrganizationService;
import it.gov.pagopa.pu.iamsync.event.organizations.dto.ScContractDTO;
import it.gov.pagopa.pu.iamsync.mapper.ScContractMapper;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static it.gov.pagopa.pu.iamsync.utils.Constants.SC_CONTRACT_ACTIVE_STATE;
import static it.gov.pagopa.pu.iamsync.utils.Constants.SC_CONTRACT_CLOSED_STATE;

@Service
@RequiredArgsConstructor
public class OrganizationCreationHandlerServiceImpl implements OrganizationCreationHandlerService {

  private final OrganizationService organizationService;
  private final ScContractMapper scContractMapper;

  @Override
  public void createOrganization(ScContractDTO scContractEvent) {
    if (SC_CONTRACT_ACTIVE_STATE.equals(scContractEvent.getState())) {
      organizationService.getOrganizationByIpaCode(
          scContractEvent.getInstitution().getOriginId())
        .ifPresentOrElse(
          organization -> {
            if (OrganizationStatus.CANCELLED.equals(organization.getStatus())) {
              organization.setStatus(OrganizationStatus.DRAFT);
              organizationService.updateOrganizationStatus(organization.getOrganizationId(), OrganizationStatus.DRAFT);
            }
          },
          () -> organizationService.createOrganization(
            scContractMapper.mapToOrganizationCreateDTO(scContractEvent,
              OrganizationStatus.DRAFT))
        );
    }

    if (SC_CONTRACT_CLOSED_STATE.equals(scContractEvent.getState())) {
      organizationService.getOrganizationByIpaCode(scContractEvent.getInstitution().getOriginId())
        .ifPresent(organization -> {
          organization.setStatus(OrganizationStatus.CANCELLED);
          organizationService.updateOrganizationStatus(organization.getOrganizationId(), OrganizationStatus.CANCELLED);
        });
    }
  }
}
