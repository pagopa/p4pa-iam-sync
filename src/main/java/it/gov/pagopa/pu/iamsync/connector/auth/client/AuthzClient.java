package it.gov.pagopa.pu.iamsync.connector.auth.client;

import it.gov.pagopa.pu.auth.dto.generated.CreateOperatorRequest;
import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.iamsync.connector.auth.config.AuthApisHolder;
import it.gov.pagopa.pu.iamsync.exception.common.RestInvokeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Slf4j
@Service
public class AuthzClient {

    private final AuthApisHolder authApisHolder;

    public AuthzClient(AuthApisHolder authApisHolder) {
        this.authApisHolder = authApisHolder;
    }

    public UserInfo getOperatorInfo(String mappedExternalUserId, String accessToken) {
        try {
            return authApisHolder.getAuthzApi(accessToken)
                    .getUserInfoFromMappedExternaUserId(mappedExternalUserId);
        } catch (RestInvokeNotFoundException e) {
            log.info("Cannot find User having externalUserId {}", mappedExternalUserId);
            return null;
        }
    }

    public OperatorDTO createOrganizationOperator(String organizationIpaCode, CreateOperatorRequest createOperatorRequest, String accessToken) {
      return authApisHolder.getAuthzApi(accessToken)
        .createOrganizationOperator(organizationIpaCode, createOperatorRequest);
    }

    public void deleteOrganizationOperatorByExternalUserId(String organizationIpaCode, String externalUserId, String accessToken) {
      authApisHolder.getAuthzApi(accessToken)
        .deleteOrganizationOperatorByExternalUserId(organizationIpaCode, externalUserId);
    }
}
