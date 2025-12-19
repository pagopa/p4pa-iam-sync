package it.gov.pagopa.pu.iamsync.utils;

import java.time.ZoneId;
import java.util.TimeZone;

public class Constants {

  private Constants(){}

  public static final ZoneId ZONEID = ZoneId.of("Europe/Rome");
  public static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone(ZONEID);

  public static final String PIATTAFORMA_UNITARIA_PRODUCT = "prod-piattaforma-unitaria";

  public static final String SC_CONTRACT_ACTIVE_STATE = "ACTIVE";

  public static final String SC_CONTRACT_CLOSED_STATE = "CLOSED";
}

