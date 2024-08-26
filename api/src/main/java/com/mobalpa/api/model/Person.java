package com.mobalpa.api.model;

import java.util.UUID;

public interface Person {
  UUID getUuid();
  String getEmail();
  String getFirstname();
  String getLastname();
  String getPhoneNumber();
  String getAddress();
  String getCity();
  String getZipcode();
}
