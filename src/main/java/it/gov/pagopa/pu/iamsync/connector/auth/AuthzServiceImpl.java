package it.gov.pagopa.pu.iamsync.connector.auth;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.iamsync.connector.auth.client.AuthzClient;
import org.springframework.stereotype.Service;

@Service
public class AuthzServiceImpl implements AuthzService {

    private final AuthzClient authzClient;
    private final AuthnService authnService;

    public AuthzServiceImpl(AuthzClient authzClient, AuthnService authnService) {
        this.authzClient = authzClient;
        this.authnService = authnService;
    }

    @Override
    public UserInfo getOperatorInfo(String mappedExternalUserId) {
        return authzClient.getOperatorInfo(mappedExternalUserId, authnService.getAccessToken());
    }
}
