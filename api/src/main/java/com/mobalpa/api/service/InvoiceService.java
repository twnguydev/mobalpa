package com.mobalpa.api.service;

import com.mobalpa.api.model.Invoice;
import com.mobalpa.api.model.Order;
import com.mobalpa.api.model.Person;
import com.mobalpa.api.model.User;
import com.mobalpa.api.model.Visitor;
import com.mobalpa.api.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class InvoiceService {

  @Autowired
  private InvoiceRepository invoiceRepository;

  public Invoice createInvoice(Order order, Person person) {
    Invoice invoice = new Invoice();
    invoice.setInvoiceNumber(generateInvoiceNumber());
    invoice.setIssueDate(LocalDateTime.now());
    invoice.setTotalAmount(order.getTotalTtc());
    invoice.setVatAmount(order.getVat());
    invoice.setAmountExcludingTax(order.getTotalHt());
    invoice.setOrder(order);

    if (person instanceof User) {
      invoice.setUser((User) person);
    } else if (person instanceof Visitor) {
      invoice.setVisitor((Visitor) person);
    }

    Invoice createdInvoice = invoiceRepository.save(invoice);

    if (createdInvoice == null) {
      throw new RuntimeException("Invoice creation failed");
    }

    return createdInvoice;
  }

  private String generateInvoiceNumber() {
    return "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
  }

  public Invoice getInvoiceById(UUID id) {
    return invoiceRepository.findByUuid(id).orElseThrow(() -> new RuntimeException("Invoice not found"));
  }

  public Invoice getInvoiceByOrderUuid(UUID orderUuid) {
    return invoiceRepository.findByOrderUuid(orderUuid).orElseThrow(() -> new RuntimeException("Invoice not found"));
  }

  public Iterable<Invoice> getAllInvoices() {
    Iterable<Invoice> invoices = invoiceRepository.findAll();
    if (invoices == null || !invoices.iterator().hasNext()) {
      throw new RuntimeException("No invoices found");
    }
    return invoices;
  }

  public Invoice getInvoiceByInvoiceNumber(String invoiceNumber) {
    return invoiceRepository.findByInvoiceNumber(invoiceNumber)
        .orElseThrow(() -> new RuntimeException("Invoice not found"));
  }

  public byte[] generateInvoicePdf(Invoice invoice) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    String formattedIssueDate = invoice.getIssueDate().format(formatter);

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    PdfWriter writer = new PdfWriter(byteArrayOutputStream);
    PdfDocument pdfDoc = new PdfDocument(writer);
    Document document = new Document(pdfDoc);

    document.add(new Paragraph("Mobalpa"));
    document.add(new Paragraph("Facture No: " + invoice.getInvoiceNumber()));
    document.add(new Paragraph("Date d'émission: " + formattedIssueDate));
    document.add(new Paragraph("Montant Total (TTC): " + String.format("%.2f €", invoice.getTotalAmount())));
    document.add(new Paragraph("Montant HT: " + String.format("%.2f €", invoice.getAmountExcludingTax())));
    document.add(new Paragraph("TVA: " + String.format("%.2f €", invoice.getVatAmount())));

    document.close();
    return byteArrayOutputStream.toByteArray();
  }

  public void saveInvoicePdfToFile(Invoice invoice, byte[] pdfData) throws IOException {
    String directoryPath = "invoices";
    File directory = new File(directoryPath);

    if (!directory.exists()) {
      directory.mkdirs();
    }

    String filePath = directoryPath + File.separator + "invoice_" + invoice.getInvoiceNumber() + ".pdf";

    try (FileOutputStream fos = new FileOutputStream(new File(filePath))) {
      fos.write(pdfData);
    }
  }
}