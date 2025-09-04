package it.gov.pagopa.pu.iamsync.service.users;

import it.gov.pagopa.pu.auth.dto.generated.CreateOperatorRequest;
import it.gov.pagopa.pu.iamsync.connector.auth.AuthnService;
import it.gov.pagopa.pu.iamsync.connector.auth.AuthzService;
import it.gov.pagopa.pu.iamsync.connector.organization.OrganizationService;
import it.gov.pagopa.pu.iamsync.event.users.dto.ScUsersNotificationDTO;
import it.gov.pagopa.pu.iamsync.mapper.ScUsersMapper;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OperatorCreationHandlerServiceImpl implements
  OperatorCreationHandlerService {

  private final AuthnService authnService;
  private final AuthzService authzService;
  private final OrganizationService organizationService;
  private final ScUsersMapper scUsersMapper;

  public OperatorCreationHandlerServiceImpl(
    AuthnService authnService, AuthzService authzService, OrganizationService organizationService,
    ScUsersMapper scUsersMapper) {
    this.authnService = authnService;
    this.authzService = authzService;
    this.organizationService = organizationService;
    this.scUsersMapper = scUsersMapper;
  }

  @Override
  public void createOrganizationOperator(ScUsersNotificationDTO scUsersEvent) {
    Organization organization = organizationService.getOrganizationByExternalOrganizationId(
        scUsersEvent.getInstitutionId(), authnService.getAccessToken())
      .orElseThrow(() -> new ResourceNotFoundException(
        "Organization with externalOrganizationId " + scUsersEvent.getInstitutionId() + " not found."));

    CreateOperatorRequest createOperatorRequest = scUsersMapper.mapToCreateOperatorRequest(
      scUsersEvent);

    authzService.createOrganizationOperator(organization.getIpaCode(),
      createOperatorRequest);
  }
}
