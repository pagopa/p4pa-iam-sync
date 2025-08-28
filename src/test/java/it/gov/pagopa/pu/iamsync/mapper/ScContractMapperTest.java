package it.gov.pagopa.pu.iamsync.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import it.gov.pagopa.pu.iamsync.event.organizations.dto.ScContractDTO;
import it.gov.pagopa.pu.iamsync.event.organizations.dto.ScContractDTO.ScInstitutionDTO;
import it.gov.pagopa.pu.iamsync.event.organizations.dto.ScContractDTO.ScRootAggregatorDTO;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationCreateDTO;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ScContractMapperTest {

  private final ScContractMapper scContractMapper = new ScContractMapper();

  @Test
  void whenMapToOrganizationCreateDTOThenReturnCorrectMapping() {
    ScContractDTO scContractEvent = new ScContractDTO();
    scContractEvent.setInstitutionId("institutionId");
    scContractEvent.setCreatedAt(OffsetDateTime.now());
    scContractEvent.setState("ACTIVE");

    ScInstitutionDTO institution = new ScInstitutionDTO();
    institution.setOriginId("ipaCode");
    institution.setTaxCode("12345678903");
    institution.setDescription("Comune di Test");
    institution.setInstitutionType("PA");
    institution.setDigitalAddress("pec@comune.test.it");
    scContractEvent.setInstitution(institution);

    ScRootAggregatorDTO rootAggregator = new ScRootAggregatorDTO();
    rootAggregator.setInstitutionId("1");
    scContractEvent.setRootAggregator(rootAggregator);

    OrganizationCreateDTO result = scContractMapper.mapToOrganizationCreateDTO(scContractEvent);

    assertEquals(scContractEvent.getInstitutionId(), result.getExternalOrganizationId());
    assertEquals(scContractEvent.getCreatedAt().toLocalDate(), result.getStartDate());
    assertEquals(scContractEvent.getState(), result.getStatus().getValue());
    assertEquals(scContractEvent.getInstitution().getOriginId(), result.getIpaCode());
    assertEquals(scContractEvent.getInstitution().getTaxCode(), result.getOrgFiscalCode());
    assertEquals(scContractEvent.getInstitution().getDescription(), result.getOrgName());
    assertEquals(scContractEvent.getInstitution().getInstitutionType(), result.getOrgTypeCode());
    assertEquals(scContractEvent.getInstitution().getDigitalAddress(), result.getOrgEmail());
    assertFalse(result.getFlagNotifyIo());
    assertFalse(result.getFlagNotifyOutcomePush());
    assertFalse(result.getFlagPaymentNotification());
    assertFalse(result.getPdndEnabled());
  }

}
