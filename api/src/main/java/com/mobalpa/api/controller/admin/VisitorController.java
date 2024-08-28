package com.mobalpa.api.controller.admin;

import com.mobalpa.api.service.VisitorService;
import com.mobalpa.api.model.Payment;
import com.mobalpa.api.model.Visitor;
import com.mobalpa.api.repository.VisitorRepository;
import com.mobalpa.api.dto.PaymentRequestDTO;
import com.mobalpa.api.repository.PaymentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;

import java.util.UUID;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/visitors")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_MANAGER')")
public class VisitorController {

  @Autowired
  private VisitorService visitorService;

  @Autowired
  private VisitorRepository visitorRepository;

  @Autowired
  private PaymentRepository paymentRepository;

  @GetMapping("/{uuid}")
  public ResponseEntity<?> getVisitorByUuid(@PathVariable UUID uuid) {
    try {
      Visitor visitor = visitorService.getVisitorByUuid(uuid);
      return ResponseEntity.ok(visitor);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @GetMapping
  public ResponseEntity<?> getAllVisitors() {
    return ResponseEntity.ok(visitorService.getAllVisitors());
  }

  @PostMapping
  public ResponseEntity<?> createVisitor(@RequestBody Visitor visitor) {
    try {
      Visitor createdVisitor = visitorService.registerVisitor(visitor);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdVisitor);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @DeleteMapping("/{uuid}")
  public ResponseEntity<?> deleteVisitor(@PathVariable UUID uuid) {
    visitorService.deleteVisitor(uuid);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Visitor deleted");
  }

  @GetMapping("/{id}/orders")
  public ResponseEntity<?> getOrders(@PathVariable UUID id) {
    Visitor visitor = visitorService.getVisitorByUuid(id);
    if (visitor == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Visitor not found");
    }

    if (visitor.getOrders().isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This visitor has no orders");
    }

    return ResponseEntity.ok(visitor.getOrders());
  }

  @GetMapping("/{id}/payments")
  public ResponseEntity<?> getPayments(@PathVariable UUID id) {
    Visitor visitor = visitorService.getVisitorByUuid(id);
    if (visitor == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Visitor not found");
    }

    if (visitor.getPayments().isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This visitor has no payments");
    }

    return ResponseEntity.ok(visitor.getPayments());
  }

  @PostMapping("/{id}/payments")
  public ResponseEntity<?> addPaymentToVisitor(@PathVariable UUID id, @RequestBody PaymentRequestDTO paymentDTO) {
    Optional<Visitor> visitorOptional = visitorRepository.findById(id);
    if (visitorOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Visitor not found");
    }

    Visitor visitor = visitorOptional.get();

    Payment payment = new Payment();
    payment.setVisitor(visitor);
    payment.setCardNumber(paymentDTO.getCardNumber());
    payment.setExpirationDate(paymentDTO.getExpirationDate());
    payment.setCvv(paymentDTO.getCvv());
    payment.setCardHolder(paymentDTO.getCardHolder());
    payment.setPaypalEmail(paymentDTO.getPaypalEmail());
    payment.setPaymentMethod(paymentDTO.getPaymentMethod());

    Payment savedPayment = paymentRepository.save(payment);

    return ResponseEntity.status(HttpStatus.CREATED).body(savedPayment);
  }

  @DeleteMapping("/{id}/payments/{paymentId}")
  public ResponseEntity<?> deletePayment(@PathVariable UUID id, @PathVariable UUID paymentId) {
    Optional<Visitor> visitorOptional = visitorRepository.findById(id);
    if (visitorOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Visitor not found");
    }

    Visitor visitor = visitorOptional.get();
    Optional<Payment> paymentOptional = visitor.getPayments().stream().filter(p -> p.getUuid().equals(paymentId)).findFirst();
    if (paymentOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found");
    }

    Payment payment = paymentOptional.get();
    paymentRepository.delete(payment);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Payment deleted");
  }
}
