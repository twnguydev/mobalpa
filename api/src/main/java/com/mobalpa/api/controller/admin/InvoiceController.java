package com.mobalpa.api.controller.admin;

import com.mobalpa.api.model.Invoice;
import com.mobalpa.api.model.Order;
import com.mobalpa.api.model.User;
import com.mobalpa.api.service.InvoiceService;
import com.mobalpa.api.service.OrderService;
import com.mobalpa.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin/invoices")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_MANAGER')")
@Tag(name = "Invoice management for admin users", description = "APIs for managing invoices for admin users")
public class InvoiceController {

  @Autowired
  private InvoiceService invoiceService;

  @Autowired
  private OrderService orderService;

  @Autowired
  private UserService userService;

  @PostMapping("/create")
  @Operation(summary = "Create invoice", description = "Creates a new invoice.")
  public ResponseEntity<?> createInvoice(@RequestParam UUID orderUuid, @RequestParam UUID userUuid) {
    Order order = orderService.getOrderByUuid(orderUuid);
    User user = userService.getUserByUuid(userUuid);
    Invoice invoice = invoiceService.createInvoice(order, user);
    return ResponseEntity.status(HttpStatus.CREATED).body(invoice);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get invoice by ID", description = "Fetches an invoice by its unique identifier.")
  public ResponseEntity<?> getInvoice(@PathVariable String invoiceNumber) {
    return ResponseEntity.ok(invoiceService.getInvoiceByInvoiceNumber(invoiceNumber));
  }

  @GetMapping
  @Operation(summary = "Get all invoices", description = "Fetches all invoices.")
  public ResponseEntity<?> getAllInvoices() {
    return ResponseEntity.ok(invoiceService.getAllInvoices());
  }
}