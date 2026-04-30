package it.gov.pagopa.pu.iamsync.connector.auth;

import it.gov.pagopa.pu.auth.dto.generated.CreateOperatorRequest;
import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;

public interface AuthzService {
    UserInfo getOperatorInfo(String mappedExternalUserId);

    OperatorDTO createOrganizationOperator(String organizationIpaCode, CreateOperatorRequest createOperatorRequest);

    void deleteOrganizationOperatorByExternalUserId(String organizationIpaCode, String externalUserId);
}
