package it.gov.pagopa.pu.iamsync.service.users;

import it.gov.pagopa.pu.iamsync.connector.auth.AuthzService;
import it.gov.pagopa.pu.iamsync.connector.organization.OrganizationService;
import it.gov.pagopa.pu.iamsync.event.users.dto.ScUsersNotificationDTO;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OperatorDeletionHandlerServiceImpl implements OperatorDeletionHandlerService {
  private final AuthzService authzService;
  private final OrganizationService organizationService;

  public OperatorDeletionHandlerServiceImpl(AuthzService authzService, OrganizationService organizationService) {
    this.authzService = authzService;
    this.organizationService = organizationService;
  }

  @Override
  public void deleteOrganizationOperatorByExternalUserId(ScUsersNotificationDTO scUsersEvent) {
    Organization organization = organizationService.getOrganizationByExternalOrganizationId(
        scUsersEvent.getInstitutionId())
      .orElseThrow(() -> new ResourceNotFoundException(
        "Organization with externalOrganizationId "
          + scUsersEvent.getInstitutionId() + " not found."));

    authzService.deleteOrganizationOperatorByExternalUserId(
      organization.getIpaCode(),
      scUsersEvent.getUser().getUserId()
    );
  }
}
