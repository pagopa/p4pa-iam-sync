package it.gov.pagopa.pu.iamsync.connector.auth;

public interface AuthnService {
    String getAccessToken();
    String getAccessToken(String orgIpaCode);
}
