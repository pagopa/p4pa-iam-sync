package it.gov.pagopa.pu.iamsync.event.organizations;

import static it.gov.pagopa.pu.iamsync.utils.Constants.PIATTAFORMA_UNITARIA_PRODUCT;

import it.gov.pagopa.pu.iamsync.event.organizations.dto.ScContractDTO;
import it.gov.pagopa.pu.iamsync.service.organizations.OrganizationCreationHandlerService;
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
    String ipaCode = scContractEvent.getInstitution().getOriginId();

    log.info("Received event on organization {} (originId {}) (institutionId {}) and product {} of type {}",
      scContractEvent.getInstitution().getTaxCode(),
      ipaCode,
      scContractEvent.getInstitutionId(),
      scContractEvent.getProduct(),
      scContractEvent.getType()
    );

    if (!PIATTAFORMA_UNITARIA_PRODUCT.equals(scContractEvent.getProduct())) {
      log.info("Discarding event due to not matching product");
      return;
    }

    if (scContractEvent.getRootAggregator() == null) {
      log.info("Creating org with ipaCode {} without broker", ipaCode);
    }

    organizationCreationHandlerService.createOrganization(scContractEvent);
  }

}
