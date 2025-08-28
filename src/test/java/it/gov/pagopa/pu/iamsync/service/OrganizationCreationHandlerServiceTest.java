package it.gov.pagopa.pu.iamsync.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.iamsync.connector.organization.service.OrganizationService;
import it.gov.pagopa.pu.iamsync.event.organizations.dto.ScContractDTO;
import it.gov.pagopa.pu.iamsync.event.organizations.dto.ScContractDTO.ScInstitutionDTO;
import it.gov.pagopa.pu.iamsync.mapper.ScContractMapper;
import it.gov.pagopa.pu.iamsync.utils.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationCreateDTO;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationStatus;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
    TestUtils.setFakeAccessTokenInContext();

    ScContractDTO scContractEvent = new ScContractDTO();
    scContractEvent.setState("ACTIVE");

    ScInstitutionDTO institution = new ScInstitutionDTO();
    institution.setOriginId("ipaCode");
    scContractEvent.setInstitution(institution);

    Organization organization = new Organization();
    organization.setStatus(OrganizationStatus.ACTIVE);

    when(organizationServiceMock.getOrganizationByIpaCode(
      scContractEvent.getInstitution().getOriginId(),
      TestUtils.getFakeAccessToken()))
      .thenReturn(Optional.of(organization));

    organizationCreationHandlerService.createOrganization(scContractEvent);

    verify(organizationServiceMock, Mockito.never()).updateOrganization(any(),
      anyString());
    verify(organizationServiceMock, Mockito.never()).createOrganization(any(),
      anyString());
  }

  /**
   * Given an event with getState() == ACTIVE, if the Organization is already
   * present and its status is CANCELLED, then the Organization status is
   * updated to be DRAFT
   */
  @Test
  void givenEventStatusActiveAndPresentCancelledOrganizationWhenCreateOrganizationThenUpdateOrganizationStatusToDraft() {
    TestUtils.setFakeAccessTokenInContext();

    ScContractDTO scContractEvent = new ScContractDTO();
    scContractEvent.setState("ACTIVE");

    ScInstitutionDTO institution = new ScInstitutionDTO();
    institution.setOriginId("ipaCode");
    scContractEvent.setInstitution(institution);

    Organization organization = new Organization();
    organization.setStatus(OrganizationStatus.CANCELLED);

    when(organizationServiceMock.getOrganizationByIpaCode(
      scContractEvent.getInstitution().getOriginId(),
      TestUtils.getFakeAccessToken()))
      .thenReturn(Optional.of(organization));

    when(organizationServiceMock.updateOrganization(organization,
      TestUtils.getFakeAccessToken()))
      .thenReturn(organization);

    organizationCreationHandlerService.createOrganization(scContractEvent);

    verify(organizationServiceMock, Mockito.never()).createOrganization(any(),
      anyString());
  }

  /**
   * Given an event with getState() == ACTIVE, if the Organization is not
   * already present, then it is created via API
   */
  @Test
  void givenEventStatusActiveAndMissingOrganizationWhenCreateOrganizationThenCreateOrganization() {
    TestUtils.setFakeAccessTokenInContext();

    ScContractDTO scContractEvent = new ScContractDTO();
    scContractEvent.setState("ACTIVE");

    ScInstitutionDTO institution = new ScInstitutionDTO();
    institution.setOriginId("ipaCode");
    scContractEvent.setInstitution(institution);

    when(organizationServiceMock.getOrganizationByIpaCode(
      scContractEvent.getInstitution().getOriginId(),
      TestUtils.getFakeAccessToken()))
      .thenReturn(Optional.empty());

    when(scContractMapperMock.mapToOrganizationCreateDTO(scContractEvent,
      OrganizationStatus.DRAFT))
      .thenReturn(new OrganizationCreateDTO());

    doNothing().when(organizationServiceMock)
      .createOrganization(any(OrganizationCreateDTO.class),
        eq(TestUtils.getFakeAccessToken()));

    organizationCreationHandlerService.createOrganization(scContractEvent);

    verify(organizationServiceMock, Mockito.never()).updateOrganization(any(),
      anyString());
  }

  /**
   * Given an event with getState() == CANCELLED, if the Organization is already
   * present, then its status is updated to be CANCELLED
   */
  @Test
  void givenEventStatusCancelledAndPresentOrganizationWhenCreateOrganizationThenUpdateOrganizationStatusToCancelled() {
    TestUtils.setFakeAccessTokenInContext();

    ScContractDTO scContractEvent = new ScContractDTO();
    scContractEvent.setState("CANCELLED");

    ScInstitutionDTO institution = new ScInstitutionDTO();
    institution.setOriginId("ipaCode");
    scContractEvent.setInstitution(institution);

    Organization organization = new Organization();
    organization.setStatus(OrganizationStatus.ACTIVE);

    when(organizationServiceMock.getOrganizationByIpaCode(
      scContractEvent.getInstitution().getOriginId(),
      TestUtils.getFakeAccessToken()))
      .thenReturn(Optional.of(organization));

    organization.setStatus(OrganizationStatus.CANCELLED);
    when(organizationServiceMock.updateOrganization(organization,
      TestUtils.getFakeAccessToken()))
      .thenReturn(organization);

    organizationCreationHandlerService.createOrganization(scContractEvent);

    Mockito.verifyNoMoreInteractions(organizationServiceMock);
    Mockito.verifyNoInteractions(scContractMapperMock);
  }

  /**
   * Given an event with getState() == CANCELLED, if the Organization is not
   * present, then do nothing
   */
  @Test
  void givenEventStatusCancelledAndMissingOrganizationWhenCreateOrganizationThenDoNothing() {
    TestUtils.setFakeAccessTokenInContext();

    ScContractDTO scContractEvent = new ScContractDTO();
    scContractEvent.setState("CANCELLED");

    ScInstitutionDTO institution = new ScInstitutionDTO();
    institution.setOriginId("ipaCode");
    scContractEvent.setInstitution(institution);

    when(organizationServiceMock.getOrganizationByIpaCode(
      scContractEvent.getInstitution().getOriginId(),
      TestUtils.getFakeAccessToken()))
      .thenReturn(Optional.empty());

    organizationCreationHandlerService.createOrganization(scContractEvent);

    verify(organizationServiceMock, Mockito.never()).updateOrganization(any(),
      anyString());
    verify(organizationServiceMock, Mockito.never()).createOrganization(any(),
      anyString());
  }

}
