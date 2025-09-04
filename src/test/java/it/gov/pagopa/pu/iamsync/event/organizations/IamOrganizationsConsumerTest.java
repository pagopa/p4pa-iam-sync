package it.gov.pagopa.pu.iamsync.event.organizations;

import static it.gov.pagopa.pu.iamsync.utils.Constants.PIATTAFORMA_UNITARIA_PRODUCT;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import it.gov.pagopa.pu.iamsync.event.organizations.dto.ScContractDTO;
import it.gov.pagopa.pu.iamsync.event.organizations.dto.ScContractDTO.ScInstitutionDTO;
import it.gov.pagopa.pu.iamsync.event.organizations.dto.ScContractDTO.ScRootAggregatorDTO;
import it.gov.pagopa.pu.iamsync.service.organizations.OrganizationCreationHandlerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IamOrganizationsConsumerTest {

  @Mock
  private OrganizationCreationHandlerService organizationCreationHandlerServiceMock;

  private IamOrganizationsConsumer iamOrganizationsConsumer;

  @BeforeEach
  void setup() {
    iamOrganizationsConsumer = new IamOrganizationsConsumer(
      organizationCreationHandlerServiceMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      organizationCreationHandlerServiceMock
    );
  }

  @Test
  void givenProductIsNotPiattaformaUnitariaWhenAcceptThenDiscardMessage() {
    ScContractDTO scContractEvent = buildBaseScContractEvent();
    scContractEvent.setProduct("product");

    iamOrganizationsConsumer.accept(scContractEvent);

    verifyNoInteractions(organizationCreationHandlerServiceMock);
  }

  @Test
  void givenMissingBrokerIdWhenAcceptThenDiscardMessage() {
    ScContractDTO scContractEvent = buildBaseScContractEvent();
    scContractEvent.setRootAggregator(new ScRootAggregatorDTO());

    iamOrganizationsConsumer.accept(scContractEvent);

    verifyNoInteractions(organizationCreationHandlerServiceMock);
  }

  @Test
  void givenValidScContractEventWhenAcceptThenHandleCreateOrganization() {
    ScContractDTO scContractEvent = buildBaseScContractEvent();
    ScRootAggregatorDTO rootAggregator = new ScRootAggregatorDTO();
    rootAggregator.setInstitutionId("brokerId");
    scContractEvent.setRootAggregator(rootAggregator);

    doNothing().when(organizationCreationHandlerServiceMock)
      .createOrganization(scContractEvent);

    iamOrganizationsConsumer.accept(scContractEvent);

    verify(organizationCreationHandlerServiceMock).createOrganization(scContractEvent);
  }

  private ScContractDTO buildBaseScContractEvent() {
    ScContractDTO scContractEvent = new ScContractDTO();
    scContractEvent.setProduct(PIATTAFORMA_UNITARIA_PRODUCT);
    scContractEvent.setType("type");
    scContractEvent.setInstitutionId("institutionId");
    ScInstitutionDTO institution = new ScInstitutionDTO();
    institution.setTaxCode("12345678903");
    institution.setOriginId("originId");
    scContractEvent.setInstitution(institution);

    return scContractEvent;
  }


}
