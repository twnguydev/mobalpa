package com.mobalpa.api.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
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
    order.setStatus("COMPLETED");
    assertNotNull(order.getWarranty());

    LocalDateTime expectedWarrantyDate = LocalDateTime.now().plusYears(7);
    Date expectedWarranty = Date.from(expectedWarrantyDate.atZone(ZoneId.systemDefault()).toInstant());

    long diff = Math.abs(expectedWarranty.getTime() - order.getWarranty().getTime());
    long oneDayInMillis = 1000 * 60 * 60 * 24;

    assertTrue(diff < oneDayInMillis);
  }
}
