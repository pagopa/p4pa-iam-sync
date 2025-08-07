package it.gov.pagopa.pu.iamsync.event.organizations;

import it.gov.pagopa.pu.iamsync.event.organizations.dto.ScContractDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class IamOrganizationsConsumer implements Consumer<ScContractDTO> {

  @Override
  public void accept(ScContractDTO scContractEvent) {
    log.info("Received event on organization {} (originId {}) (institutionId {}) and product {} of type {}",
      scContractEvent.getInstitution().getTaxCode(),
      scContractEvent.getInstitution().getOriginId(),
      scContractEvent.getInstitutionId(),
      scContractEvent.getProduct(),
      scContractEvent.getType()
    );
  }

}
