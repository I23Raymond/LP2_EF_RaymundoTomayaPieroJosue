package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import com.example.demo.service.PDFGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private PDFGeneratorService pdfGeneratorService;

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "product-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        return "product-form";
    }

    @PostMapping
    public String createProduct(Product product) {
        productService.save(product);
        return "redirect:/products";
    }

    @GetMapping("/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        return "product-form";
    }

    @PostMapping("/{id}")
    public String updateProduct(@PathVariable Long id, Product product) {
        product.setId(id);
        productService.save(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return "redirect:/products";
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<InputStreamResource> exportProductsToPDF() {
        ByteArrayInputStream bis = pdfGeneratorService.exportProductsToPDF();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=products.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}