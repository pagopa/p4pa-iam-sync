package it.gov.pagopa.pu.iamsync.mapper;

import it.gov.pagopa.pu.iamsync.event.organizations.dto.ScContractDTO;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationCreateDTO;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationStatus;
import org.springframework.stereotype.Service;

@Service
public class ScContractMapper {

  public OrganizationCreateDTO mapToOrganizationCreateDTO(ScContractDTO scContractEvent) {
    if (scContractEvent == null) {
      return null;
    }

    return mapToOrganizationCreateDTO(scContractEvent, OrganizationStatus.valueOf(scContractEvent.getState()));
  }

  public OrganizationCreateDTO mapToOrganizationCreateDTO(ScContractDTO scContractEvent, OrganizationStatus status) {
    if (scContractEvent == null) {
      return null;
    }

    OrganizationCreateDTO organizationCreateDTO = new OrganizationCreateDTO();

    organizationCreateDTO.setExternalOrganizationId(scContractEvent.getInstitutionId());
    organizationCreateDTO.setStartDate(scContractEvent.getCreatedAt().toLocalDate());
    organizationCreateDTO.setStatus(status);

    organizationCreateDTO.setIpaCode(scContractEvent.getInstitution().getOriginId());
    organizationCreateDTO.setOrgFiscalCode(scContractEvent.getInstitution().getTaxCode());
    organizationCreateDTO.setOrgName(scContractEvent.getInstitution().getDescription());
    organizationCreateDTO.setOrgTypeCode(scContractEvent.getInstitution().getInstitutionType());
    organizationCreateDTO.setOrgEmail(scContractEvent.getInstitution().getDigitalAddress());

    organizationCreateDTO.setFlagNotifyIo(false);
    organizationCreateDTO.setFlagNotifyOutcomePush(false);
    organizationCreateDTO.setFlagPaymentNotification(false);
    organizationCreateDTO.setPdndEnabled(false);

    return organizationCreateDTO;
  }
}
