package com.mobalpa.api.repository;

import com.mobalpa.api.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
  Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
  Optional<Invoice> findByUuid(UUID id);
}