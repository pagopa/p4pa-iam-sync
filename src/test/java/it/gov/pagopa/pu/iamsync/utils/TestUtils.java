package it.gov.pagopa.pu.iamsync.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.TimeZone;

public class TestUtils {

  static {
    clearDefaultTimezone();
  }

  public static void clearDefaultTimezone() {
    TimeZone.setDefault(Constants.DEFAULT_TIMEZONE);
  }

  private static final String ACCESS_TOKEN = "TOKENHEADER.TOKENPAYLOAD.TOKENDIGEST";

  public static String getFakeAccessToken() {
    return ACCESS_TOKEN;
  }

  public static void setFakeAccessTokenInContext() {
    SecurityContextHolder.setContext(new SecurityContextImpl(new JwtAuthenticationToken(
      Jwt
        .withTokenValue(ACCESS_TOKEN)
        .header("", "")
        .claim("", "")
        .build())));
  }

}
