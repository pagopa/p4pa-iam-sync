package it.gov.pagopa.pu.iamsync.service.organizations;

import it.gov.pagopa.pu.iamsync.connector.organization.OrganizationService;
import it.gov.pagopa.pu.iamsync.event.organizations.dto.ScContractDTO;
import it.gov.pagopa.pu.iamsync.event.organizations.dto.ScContractDTO.ScInstitutionDTO;
import it.gov.pagopa.pu.iamsync.mapper.ScContractMapper;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationCreateDTO;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationCreationHandlerServiceTest {

  @Mock
  private OrganizationService organizationServiceMock;

  @Mock
  private ScContractMapper scContractMapperMock;

  private OrganizationCreationHandlerService organizationCreationHandlerService;

  @BeforeEach
  void setup() {
    organizationCreationHandlerService = new OrganizationCreationHandlerServiceImpl(
      organizationServiceMock, scContractMapperMock);

  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      organizationServiceMock,
      scContractMapperMock
    );
  }

  /**
   * Given an event with getState() == ACTIVE, if the Organization is already
   * present and its status is not CANCELLED, then do nothing
   */
  @Test
  void givenEventStatusActiveAndPresentActiveOrganizationWhenCreateOrganizationThenOk() {
    ScContractDTO scContractEvent = new ScContractDTO();
    scContractEvent.setState("ACTIVE");

    ScInstitutionDTO institution = new ScInstitutionDTO();
    institution.setOriginId("ipaCode");
    scContractEvent.setInstitution(institution);

    Organization organization = new Organization();
    organization.setStatus(OrganizationStatus.ACTIVE);

    when(organizationServiceMock.getOrganizationByIpaCode(
      scContractEvent.getInstitution().getOriginId()))
      .thenReturn(Optional.of(organization));

    organizationCreationHandlerService.createOrganization(scContractEvent);
  }

  /**
   * Given an event with getState() == ACTIVE, if the Organization is already
   * present and its status is CANCELLED, then the Organization status is
   * updated to be DRAFT
   */
  @Test
  void givenEventStatusActiveAndPresentCancelledOrganizationWhenCreateOrganizationThenUpdateOrganizationStatusToDraft() {
    ScContractDTO scContractEvent = new ScContractDTO();
    scContractEvent.setState("ACTIVE");

    ScInstitutionDTO institution = new ScInstitutionDTO();
    institution.setOriginId("ipaCode");
    scContractEvent.setInstitution(institution);

    Organization organization = new Organization();
    organization.setOrganizationId(1L);
    organization.setStatus(OrganizationStatus.CANCELLED);

    when(organizationServiceMock.getOrganizationByIpaCode(
      scContractEvent.getInstitution().getOriginId()))
      .thenReturn(Optional.of(organization));

    organizationCreationHandlerService.createOrganization(scContractEvent);

    verify(organizationServiceMock)
      .updateOrganizationStatus(organization.getOrganizationId(), organization.getStatus());
  }

  /**
   * Given an event with getState() == ACTIVE, if the Organization is not
   * already present, then it is created via API
   */
  @Test
  void givenEventStatusActiveAndMissingOrganizationWhenCreateOrganizationThenCreateOrganization() {
    ScContractDTO scContractEvent = new ScContractDTO();
    scContractEvent.setState("ACTIVE");

    ScInstitutionDTO institution = new ScInstitutionDTO();
    institution.setOriginId("ipaCode");
    scContractEvent.setInstitution(institution);

    when(organizationServiceMock.getOrganizationByIpaCode(
      scContractEvent.getInstitution().getOriginId()))
      .thenReturn(Optional.empty());

    when(scContractMapperMock.mapToOrganizationCreateDTO(scContractEvent,
      OrganizationStatus.DRAFT))
      .thenReturn(new OrganizationCreateDTO());

    doNothing().when(organizationServiceMock)
      .createOrganization(any(OrganizationCreateDTO.class));

    organizationCreationHandlerService.createOrganization(scContractEvent);
  }

  /**
   * Given an event with getState() == CANCELLED, if the Organization is already
   * present, then its status is updated to be CANCELLED
   */
  @Test
  void givenEventStatusCancelledAndPresentOrganizationWhenCreateOrganizationThenUpdateOrganizationStatusToCancelled() {
    ScContractDTO scContractEvent = new ScContractDTO();
    scContractEvent.setState("CLOSED");

    ScInstitutionDTO institution = new ScInstitutionDTO();
    institution.setOriginId("ipaCode");
    scContractEvent.setInstitution(institution);

    Organization organization = new Organization();
    organization.setOrganizationId(1L);
    organization.setStatus(OrganizationStatus.ACTIVE);

    when(organizationServiceMock.getOrganizationByIpaCode(
      scContractEvent.getInstitution().getOriginId()))
      .thenReturn(Optional.of(organization));

    organization.setStatus(OrganizationStatus.CANCELLED);

    organizationCreationHandlerService.createOrganization(scContractEvent);

    verify(organizationServiceMock)
      .updateOrganizationStatus(organization.getOrganizationId(), organization.getStatus());
  }

  /**
   * Given an event with getState() == CANCELLED, if the Organization is not
   * present, then do nothing
   */
  @Test
  void givenEventStatusCancelledAndMissingOrganizationWhenCreateOrganizationThenDoNothing() {
    ScContractDTO scContractEvent = new ScContractDTO();
    scContractEvent.setState("CLOSED");

    ScInstitutionDTO institution = new ScInstitutionDTO();
    institution.setOriginId("ipaCode");
    scContractEvent.setInstitution(institution);

    when(organizationServiceMock.getOrganizationByIpaCode(
      scContractEvent.getInstitution().getOriginId()))
      .thenReturn(Optional.empty());

    organizationCreationHandlerService.createOrganization(scContractEvent);
  }

}
