package it.gov.pagopa.pu.iamsync.event.organizations;

import static it.gov.pagopa.pu.iamsync.utils.Constants.PIATTAFORMA_UNITARIA_PRODUCT;

import it.gov.pagopa.pu.iamsync.event.organizations.dto.ScContractDTO;
import it.gov.pagopa.pu.iamsync.service.OrganizationCreationHandlerService;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class IamOrganizationsConsumer implements Consumer<ScContractDTO> {

  private final OrganizationCreationHandlerService organizationCreationHandlerService;

  @Override
  public void accept(ScContractDTO scContractEvent) {
    log.info("Received event on organization {} (originId {}) (institutionId {}) and product {} of type {}",
      scContractEvent.getInstitution().getTaxCode(),
      scContractEvent.getInstitution().getOriginId(),
      scContractEvent.getInstitutionId(),
      scContractEvent.getProduct(),
      scContractEvent.getType()
    );

    if (PIATTAFORMA_UNITARIA_PRODUCT.equals(scContractEvent.getProduct())) {
      if (scContractEvent.getRootAggregator().getInstitutionId() == null) {
        log.info("Missing brokerId, event will be discarded");
      }

      organizationCreationHandlerService.createOrganization(scContractEvent);
    }
  }

}
