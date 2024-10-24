package com.example.demo.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Product;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

@Service
public class PDFGeneratorService {
    @Autowired
    private ProductService productService;

    public ByteArrayInputStream exportProductsToPDF() {
        List<Product> products = productService.findAll();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (PdfWriter writer = new PdfWriter(out);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc)) {

            for (Product product : products) {
                document.add(new Paragraph("ID: " + product.getId()));
                document.add(new Paragraph("Name: " + product.getName()));
                document.add(new Paragraph("Price: " + product.getPrice()));
                document.add(new Paragraph(" "));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}