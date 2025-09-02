package it.gov.pagopa.pu.iamsync.service.organization;

import it.gov.pagopa.pu.iamsync.event.organizations.dto.ScContractDTO;

public interface OrganizationCreationHandlerService {
  void createOrganization(ScContractDTO scContractDTO);
}
