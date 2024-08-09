package com.mobalpa.api.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class OrderTests {

  @Test
  public void testWarrantyDateNotSetInitially() {
    Order order = new Order();
    assertNull(order.getWarranty());
  }

  @Test
  public void testWarrantyDateSetOnCompletedStatus() {
    Order order = new Order();
    order.setStatus("PROCESSED");
    assertNotNull(order.getWarranty());

    LocalDate expectedWarranty = LocalDate.now().plusYears(7);

    assertTrue(order.getWarranty().isAfter(expectedWarranty.minusDays(1)));
  }
}
