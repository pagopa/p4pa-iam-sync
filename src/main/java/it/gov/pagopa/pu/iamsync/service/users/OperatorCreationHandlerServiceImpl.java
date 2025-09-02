package it.gov.pagopa.pu.iamsync.service.users;

import it.gov.pagopa.pu.auth.dto.generated.CreateOperatorRequest;
import it.gov.pagopa.pu.iamsync.connector.auth.AuthzService;
import it.gov.pagopa.pu.iamsync.connector.organization.OrganizationService;
import it.gov.pagopa.pu.iamsync.event.users.dto.ScUsersNotificationDTO;
import it.gov.pagopa.pu.iamsync.mapper.ScUsersMapper;
import it.gov.pagopa.pu.iamsync.utils.SecurityUtils;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import org.springframework.stereotype.Service;

@Service
public class OperatorCreationHandlerServiceImpl implements OperatorCreationHandlerService {

  private final AuthzService authzService;
  private final OrganizationService organizationService;
  private final ScUsersMapper scUsersMapper;

  public OperatorCreationHandlerServiceImpl(
    AuthzService authzService, OrganizationService organizationService, ScUsersMapper scUsersMapper) {
    this.authzService = authzService;
    this.organizationService = organizationService;
    this.scUsersMapper = scUsersMapper;
  }

  @Override
  public void createOrganizationOperator(ScUsersNotificationDTO scUsersEvent) {
    CreateOperatorRequest createOperatorRequest = scUsersMapper.mapToCreateOperatorRequest(scUsersEvent);

    Organization organization = organizationService.getOrganizationByExternalOrganizationId(
      scUsersEvent.getInstitutionId(), SecurityUtils.getAccessToken());

    authzService.createOrganizationOperator(organization.getIpaCode(), createOperatorRequest);
  }
}
